/*
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.core.natvie.core.manager;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import org.benmobile.core.natvie.core.activity.ActivityStub;
import org.benmobile.core.natvie.core.bean.PluginInfo;
import org.benmobile.core.natvie.core.loader.SyknetAppClassLoader;
import org.benmobile.core.natvie.core.proxy.SyknetInstrumentation;
import org.benmobile.core.natvie.io.FileUtil;
import org.benmobile.core.natvie.log.SLogger;
import org.benmobile.core.natvie.utils.Reflection;

/*
 * 
	 * @ClassName:AxPluginManager <BR>
     * @Describe：Used to manage the plugin<BR>
     * @Author: Jekshow
     * @Version:1.0
     * @date:2016-8-3 下午4:15:19
 */
public class SyknetPluginManager {
	public static final SLogger log = SLogger.get("Syknet.plugin");
	/**The plug-in package name*/
	public final static String intentPackageName = "plugin_package";
	/**The class name of the plugin*/
	public final static String intentClassName = "plugin_class";
	private Context context;
	/***/
	private SyknetInstrumentation instrumentation;
	private String storagePath;
	private Map<String, PluginInfo> pluginInfoMap = new HashMap<String, PluginInfo>();

	private static SyknetPluginManager instance = new SyknetPluginManager();

	public static SyknetPluginManager getInstance() {
		return instance;
	}

	private SyknetAppClassLoader appClassLoader;

	public Map<String, PluginInfo> getPluginInfoMap() {
		return pluginInfoMap;
	}
	
	public ClassLoader getClassLoader() {
		return appClassLoader;
	}

	public void init(Context context) {
		try {
			this.context = context.getApplicationContext();

			instrumentation = new SyknetInstrumentation();
			instrumentation.pluginManager = this;
			injectorInstrumentation();

			storagePath = context.getDir("plugins", Context.MODE_PRIVATE)
					.getAbsolutePath();
			FileUtil.ensureDir(storagePath);
			//Initialize the plug-in class loader
			injectorClassLoader();

			loadAllFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void injectorInstrumentation() {
		Context contextImpl = ((ContextWrapper) context).getBaseContext();
		Object activityThread = Reflection.getField(contextImpl, "mMainThread");
		Object old = Reflection.getField(activityThread, "mInstrumentation");
		if (old instanceof SyknetInstrumentation) {
		} else {
			Reflection.setField(activityThread, "mInstrumentation",
					instrumentation);
			instrumentation.old = (Instrumentation) old;
		}
	}

	/**
	 * Replace the android default class loader
	 */
	void injectorClassLoader() {
		//To get the package name
		String pkgName = context.getPackageName();
		//To get the context
		Context contextImpl = ((ContextWrapper) context).getBaseContext();
		//Access to the Activity of the main thread
		Object activityThread = Reflection.getField(contextImpl, "mMainThread");
		//Get package container
		Map mPackages = (Map) Reflection.getField(activityThread, "mPackages");
		//To obtain a weak reference object, the standard reflection
		WeakReference weakReference = (WeakReference) mPackages.get(pkgName);
		if (weakReference == null) {
			log.e("loadedApk is null");
		} else {
			//Get apk need to be loaded
			Object loadedApk = weakReference.get();
			
			if (loadedApk == null) {
				log.e("loadedApk is null");
				return;
			}
			if (appClassLoader == null) {
				//Access to the original class loader
				ClassLoader old = (ClassLoader) Reflection.getField(loadedApk,
						"mClassLoader");
				//According to the default class loader instantiate a plug-in class loader
				appClassLoader = new SyknetAppClassLoader(old, this);
			}
			//Replace the new plug-in loader loader by default
			Reflection.setField(loadedApk, "mClassLoader", appClassLoader);
		}
	}

	public PluginInfo load(String apkfile, String packageName) {
		//Instantiate a plug-in class information
		PluginInfo pluginInfo = new PluginInfo();

		try {
			boolean ok = pluginInfo.load(context, storagePath, apkfile, packageName);
			
			log.d("load plugin: " + apkfile + ", name:" + packageName + ", result:" + ok);
			
			if (!ok)
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronized(this) {
			//Will object information added to the plugin's container
			pluginInfoMap.put(pluginInfo.getPackageName(), pluginInfo);
		}
		return pluginInfo;
	}

	public Intent makeLaunchIntent(String pkgName) {
		Intent launchIntent = null;
		launchIntent = new Intent(context, ActivityStub.class);
		launchIntent.putExtra(intentPackageName, pkgName);

		return launchIntent;
	}

	private boolean isRegisteredActivity(ComponentName componentName) {
		try {
			ActivityInfo activityInfo = context.getPackageManager()
					.getActivityInfo(componentName,
							PackageManager.GET_META_DATA);
			return activityInfo != null;
		} catch (NameNotFoundException e) {
		}
		return false;
	}

	public void changeIntent(Context who, Intent intent) {
		if (intent.getStringExtra(SyknetPluginManager.intentPackageName) != null) {
			return;
		}
		if (intent.getStringExtra(SyknetPluginManager.intentClassName) != null) {
			return;
		}

		ComponentName componentName = intent.getComponent();
		if (componentName != null) {
			String pkgName = componentName.getPackageName();
			String className = componentName.getClassName();

			if (pkgName != null && isRegisteredActivity(componentName) == false) {
				intent.setComponent(new ComponentName(context,
						ActivityStub.class));
				intent.putExtra(SyknetPluginManager.intentPackageName, pkgName);
				intent.putExtra(SyknetPluginManager.intentClassName, className);
			}
		}
	}

	public void launch(Context activity, String apkfile, String packageName) {
		try {
			PluginInfo pluginInfo = load(apkfile, packageName);
			Intent i = makeLaunchIntent(pluginInfo.getPackageName());
			if (i == null) {
				log.e("get launch Intent fail: "
								+ pluginInfo.getPackageName());
				return;
			}

			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.startActivity(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized PluginInfo getPlugin(String packageName) {
		return pluginInfoMap.get(packageName);
	}
	
	public synchronized void removePlugin(String packageName) {
		PluginInfo plugin = pluginInfoMap.remove(packageName);
		if (plugin != null) {
			plugin.destroy();
		}
		File file = new File(PluginInfo.getPluginDir(storagePath, packageName));
		if (file.exists()) {
			boolean ok = FileUtil.delete(file);
			if (!ok) {
				log.e("remove file fail: " + file);
				file.deleteOnExit();
			}
		}
	}
	/**
	 *
	 All files: load
	 */
	public void loadAllFiles() {
		File dir = new File(storagePath);
		File[] files = dir.listFiles();
		if (files == null) return;
		
		for (File file : files) {
			if (file.isDirectory()) {
				File apkFile = new File(file, "p.apk");
				if (apkFile.exists()) {
					load(apkFile.getAbsolutePath(), file.getName());
				}
			}
		}
	}
}
