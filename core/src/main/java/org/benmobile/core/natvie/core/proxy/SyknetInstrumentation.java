/*
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.core.natvie.core.proxy;

import org.benmobile.core.natvie.core.activity.ActivityStub;
import org.benmobile.core.natvie.core.bean.PluginInfo;
import org.benmobile.core.natvie.core.context.PluginContext;
import org.benmobile.core.natvie.core.loader.PluginClassLoader;
import org.benmobile.core.natvie.core.manager.SyknetPluginManager;
import org.benmobile.core.natvie.utils.Reflection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

public class SyknetInstrumentation extends InstrumentationProxy {
	/**The plug-in manager*/
	public SyknetPluginManager pluginManager;
	
	private Class<?> safeLoader(String className, ClassLoader loader) {
		try {
			return loader.loadClass(className);
		} catch (ClassNotFoundException e) {
		}
		return null;
	}
	
	@Override
	public Activity newActivity(ClassLoader cl, String className,
			Intent intent) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		
		Class<?> clazz = null;
		if (className.equals(ActivityStub.class.getName())) {
			String pkgName = intent.getStringExtra(SyknetPluginManager.intentPackageName);
			String aname = intent.getStringExtra(SyknetPluginManager.intentClassName);
			if (pkgName != null) {
				PluginInfo plugin = pluginManager.getPlugin(pkgName);
				if (plugin != null) {
					if (aname == null) {
						aname = plugin.getMainActivityName();
					}
					clazz = safeLoader(aname, plugin.getClassLoader());
				}
			}
			if (clazz == null) {
				clazz = pluginManager.getClassLoader().loadClass(aname);
			}
		}
		else {
			clazz = safeLoader(className, cl);
			if (clazz == null) {
				clazz = pluginManager.getClassLoader().loadClass(className);
			}
		}
		
		if (clazz != null) {
			Activity activity = (Activity)clazz.newInstance();
			return activity;
		}
		
		return super.newActivity(cl, className, intent);
	}
	
	@SuppressLint("NewApi")
	private void injector(Activity activity, PluginContext contextHook) {

		try {
			Reflection.setField(activity, "mBase", contextHook);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Reflection.setField(activity, "mResources", contextHook.plugin.resources);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Window window = activity.getWindow();
		LayoutInflater inflater = LayoutInflater.from(activity);
		try {
			Reflection.setField(window, "mLayoutInflater", inflater);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Reflection.setField(window, "mWindowStyle", null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ActivityInfo ai = contextHook.plugin.findAcitivityInfo(activity.getClass().getName());
		if (ai != null) {
			try {
				Reflection.setField(activity, "mActivityInfo", ai);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (Build.VERSION.SDK_INT >= 19) {
				window.setIcon(ai.getIconResource());
				window.setLogo(ai.getLogoResource());
				window.setSoftInputMode(ai.softInputMode);
				window.setUiOptions(ai.uiOptions);
				activity.setRequestedOrientation(ai.screenOrientation);
			}
			
			int theme = ai.getThemeResource();
			if (theme != 0) {
				try {
					Reflection.setField(activity, "mTheme", null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				activity.setTheme(theme);
			}
		}
	}
	
	@Override
	public void callActivityOnCreate(Activity activity, Bundle icicle) {
		try {
			pluginManager.injectorInstrumentation();
			
			Context ctx = activity.getBaseContext();
			ClassLoader cl = activity.getClass().getClassLoader();
			if (cl instanceof PluginClassLoader && !(ctx instanceof PluginContext) ) {
				PluginContext contextHook = new PluginContext(ctx);
				contextHook.plugin = ((PluginClassLoader)cl).plugin;
				injector(activity, contextHook);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			super.callActivityOnCreate(activity, icicle);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@Override
	protected void onStartActivity(Context who, Intent intent) {
		pluginManager.changeIntent(who, intent);
	}
}
