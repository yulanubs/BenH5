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

/**
 * 
	 * @ClassName:AxPluginManager <BR>
     * @Describe：用于管理插件的加载等<BR>
     * @Author: Jekshow
	 * @Extends：<BR>
     * @Version:1.0 
     * @date:2016-8-3 下午4:15:19
 */
public class SyknetPluginManager {
	public static final SLogger log = SLogger.get("Syknet.plugin");
	/**插件的包名*/
	public final static String intentPackageName = "plugin_package";
	/**插件的类名*/
	public final static String intentClassName = "plugin_class";
	/**上下文*/
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
			//初始化插件类加载器
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
	 * 
	 * 方法名：injectorClassLoader<BR>  
	 * 此方法描述的是：   替换android默认的类加载器
	 */
	void injectorClassLoader() {
		//获取包名
		String pkgName = context.getPackageName();
		//获取上下文
		Context contextImpl = ((ContextWrapper) context).getBaseContext();
		//获取Activity主线程
		Object activityThread = Reflection.getField(contextImpl, "mMainThread");
		//获得包容器
		Map mPackages = (Map) Reflection.getField(activityThread, "mPackages");
		//获取弱引用对象，规范反射
		WeakReference weakReference = (WeakReference) mPackages.get(pkgName);
		if (weakReference == null) {
			log.e("loadedApk is null");
		} else {
			//获取需要加载的apk
			Object loadedApk = weakReference.get();
			
			if (loadedApk == null) {
				log.e("loadedApk is null");
				return;
			}
			if (appClassLoader == null) {
				//获取原有的类加载器
				ClassLoader old = (ClassLoader) Reflection.getField(loadedApk,
						"mClassLoader");
				//根据默认的类加载器实例化一个插件的类加载器
				appClassLoader = new SyknetAppClassLoader(old, this);
			}
			//将新的插件加载器替换掉默认加载器
			Reflection.setField(loadedApk, "mClassLoader", appClassLoader);
		}
	}

	public PluginInfo load(String apkfile, String packageName) {
		//实例化一个插件信息类
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
			pluginInfoMap.put(pluginInfo.getPackageName(), pluginInfo);//将插件信息对象添加到插件容器中
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
	 * 方法名：loadAllFiles<BR>  
	 * 此方法描述的是：加载所有文件
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
