/**
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.core.natvie.core.bean;


import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;

import org.benmobile.core.natvie.core.context.PluginContext;
import org.benmobile.core.natvie.core.loader.PluginClassLoader;
import org.benmobile.core.natvie.core.manager.SyknetPluginManager;
import org.benmobile.core.natvie.io.FileUtil;
import org.benmobile.core.natvie.utils.Reflection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 
 * @ClassName: PluginInfo<BR>
 * @Describe：插件信息实体类<BR>
 * @Author: Jekshow
 * @Extends：<BR>
 * @Version:1.0
 * @date:2016-8-3 下午5:21:30
 */
public class PluginInfo {
	/** 包信息 */
	public PackageInfo packageInfo;
	/** 保存路径 */
	public String storagePath;
	/** 插件加载器 */
	public PluginClassLoader classLoader;
	/** 资源管理器 */
	public AssetManager assetManager;
	/** 资源引用对象 */
	public Resources resources;
	/** Application */
	public Application application;
	/** 包名 */
	public String packageName;

	public String getPackageName() {
		return packageName;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * 
	 * 方法名：makeDir<BR>
	 * 此方法描述的是： 创建文件夹
	 * 
	 * @param dirName
	 * @return String
	 */
	private String makeDir(String dirName) {
		String path = storagePath + "/" + dirName;
		FileUtil.ensureDir(path);
		return path;
	}

	/**
	 * 
	 * 方法名：getMainActivityName<BR>
	 * 此方法描述的是： 获取MainActivity名称
	 * 
	 * @return String
	 */
	public String getMainActivityName() {
		if (packageInfo.activities != null && packageInfo.activities.length > 0) {
			return packageInfo.activities[0].name;
		}
		return "";
	}

	/***
	 * 
	 * 方法名：findAcitivityInfo<BR>
	 * 此方法描述的是： 根据activity名称获取Activity信息
	 * 
	 * @param name
	 * @return ActivityInfo
	 */
	public ActivityInfo findAcitivityInfo(String name) {
		for (ActivityInfo ai : packageInfo.activities) {
			if (name.equals(ai.name)) {
				return ai;
			}
		}
		return null;
	}

	public void destroy() {
		try {
			this.application.onTerminate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 方法名：initApplication<BR>
	 * 此方法描述的是： 初始化Application
	 * 
	 * @param context
	 */
	private void initApplication(Context context) {
		String appClassName;
		if (packageInfo.applicationInfo != null
				&& packageInfo.applicationInfo.className != null) {
			appClassName = packageInfo.applicationInfo.className;
		} else {
			appClassName = Application.class.getName();
		}
		try {
			// 根据appClassName获取Application实例
			application = (Application) getClassLoader()
					.loadClass(appClassName).newInstance();

			PluginContext contextHook = new PluginContext(context);
			contextHook.plugin = this;
			Reflection.setField(application, "mBase", contextHook);

			application.onCreate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 方法名：getPluginDir<BR>
	 * 此方法描述的是： 获取插件Dir
	 * 
	 * @param storagePathBase
	 * @param packageName
	 * @return String
	 */
	public static String getPluginDir(String storagePathBase, String packageName) {
		return storagePathBase + "/" + packageName;
	}

	/**
	 * 
	 * 方法名：load<BR>
	 * 此方法描述的是：加载apk
	 * 
	 * @param context
	 *            上下文
	 * @param storagePathBase
	 *            存储路径
	 * @param apkfilePath
	 *            apk路径
	 * @param packageName
	 *            包名
	 * @return boolean
	 */
	public boolean load(Context context, String storagePathBase,
			String apkfilePath, String packageName) {

		File aokfile = new File(apkfilePath);
		if (!aokfile.exists()) { // 判断文件是否存在
			SyknetPluginManager.log.e("file not found: " + apkfilePath);
			return false;
		}

		PackageManager pm = context.getPackageManager(); // 获取包管理器
		packageInfo = pm.getPackageArchiveInfo(apkfilePath, // 获取包信息
				PackageManager.GET_ACTIVITIES | PackageManager.GET_RECEIVERS
						| PackageManager.GET_PROVIDERS
						| PackageManager.GET_META_DATA
						| PackageManager.GET_SHARED_LIBRARY_FILES
						| PackageManager.GET_SERVICES);
		if (packageInfo == null) {
			return false;
		}

		if (packageName == null) {
			this.packageName = packageInfo.packageName;
		} else {
			this.packageName = packageName;
		}

		storagePath = getPluginDir(storagePathBase, this.packageName);
		String optimizedDirectory;
		String nativeLibraryDir;
		String dexfile = storagePath + "/p.apk"; // dex路径

		File file = new File(storagePath);
		if (!file.exists()) {
			file.mkdir();
			optimizedDirectory = makeDir("olib");// 创建旧lib文件夹
			nativeLibraryDir = makeDir("lib"); // 创建lib文件夹

			try {
				FileUtil.copyFile(apkfilePath, dexfile);// 拷贝dex
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			copyNativeLibs(context, dexfile, nativeLibraryDir);// 拷贝lib
		} else {
			optimizedDirectory = storagePath + "/" + ("olib");
			nativeLibraryDir = storagePath + "/" + ("lib");
		}

		classLoader = PluginClassLoader.make(dexfile, optimizedDirectory,
				nativeLibraryDir, context.getClassLoader()); // 实例化插件加载器
		classLoader.plugin = this; // 传入插件加载器所需的插件信息对象
		loadAsset(context, dexfile);//加载资源

		initApplication(context);//初始化Application
		return true;
	}
	/**
	 * 
	 * 方法名：loadAsset<BR>  
	 * 此方法描述的是：   加载资源
	 * @param context
	 * @param dexPath  void
	 */
	private void loadAsset(Context context, String dexPath) {
		try {
			AssetManager am = (AssetManager) AssetManager.class.newInstance();//实例化一个资源管理器对象
			am.getClass().getMethod("addAssetPath", String.class)
					.invoke(am, dexPath);
			assetManager = (am);//获取插件dex资源资源管理器
			Resources ctxres = context.getResources();
			PluginContext.PluginResources res = new PluginContext.PluginResources(
					am, ctxres.getDisplayMetrics(), ctxres.getConfiguration());
			res.old = ctxres;//替换插件资源文件对象
			resources = res;//插件资源对象赋值给当前资源对象
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 这段代码来自https://github.com/DroidPluginTeam/DroidPlugin
	 * 
	 * @param context
	 * @param apkfile
	 * @param nativeLibraryDir
	 */
	private static void copyNativeLibs(Context context, String apkfile,
			String nativeLibraryDir) {
		ZipFile zipFile = null;
		try {
			try {
				zipFile = new ZipFile(apkfile);
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}

			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			Map<String, ZipEntry> libZipEntries = new HashMap<String, ZipEntry>();
			Map<String, Set<String>> soList = new HashMap<String, Set<String>>(
					1);
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String name = entry.getName();
				if (name.startsWith("/")) {
					name = name.substring(1);
				}
				if (name.contains("../")) {
					SyknetPluginManager.log.w("error so path");
					continue;
				}
				if (name.startsWith("lib/") && !entry.isDirectory()) {
					libZipEntries.put(name, entry);
					String soName = new File(name).getName();
					Set<String> fs = soList.get(soName);
					if (fs == null) {
						fs = new TreeSet<String>();
						soList.put(soName, fs);
					}
					fs.add(name);
				}
			}

			for (String soName : soList.keySet()) {
				Set<String> soPaths = soList.get(soName);
				String soPath = findSoPath(soPaths);
				if (soPath != null) {
					File file = new File(nativeLibraryDir, soName);
					if (file.exists()) {
						file.delete();
					}
					InputStream in = null;
					FileOutputStream ou = null;
					try {
						in = zipFile.getInputStream(libZipEntries.get(soPath));
						ou = new FileOutputStream(file);
						byte[] buf = new byte[8192];
						int read = 0;
						while ((read = in.read(buf)) != -1) {
							ou.write(buf, 0, read);
						}
						ou.flush();
						ou.getFD().sync();
						SyknetPluginManager.log.i("copy so(" + soName + ") for "
								+ soPath + " to " + file.getPath() + " ok!");
					} catch (Exception e) {
						if (file.exists()) {
							file.delete();
						}
						e.printStackTrace();
						return;
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (Exception e) {
							}
						}
						if (ou != null) {
							try {
								ou.close();
							} catch (Exception e) {
							}
						}
					}
				}
			}
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private static String findSoPath(Set<String> soPaths) {
		if (soPaths != null && soPaths.size() > 0) {
			for (String soPath : soPaths) {
				if (!TextUtils.isEmpty(Build.CPU_ABI)
						&& soPath.contains(Build.CPU_ABI)) {
					return soPath;
				}
			}

			for (String soPath : soPaths) {
				if (!TextUtils.isEmpty(Build.CPU_ABI2)
						&& soPath.contains(Build.CPU_ABI2)) {
					return soPath;
				}
			}
		}
		return null;
	}
}
