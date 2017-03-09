/*
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.core.natvie.app;


import java.io.File;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import org.benmobile.core.natvie.config.PluginConfig;
import org.benmobile.core.natvie.core.loader.PluginClassLoader;
import org.benmobile.core.natvie.log.SLogger;
import org.benmobile.core.natvie.ui.bean.PluginVersionInfo;
import org.benmobile.core.natvie.update.service.UpdateService;
import org.benmobile.core.natvie.update.task.DownloadTask;


public abstract class PluginClient {
	protected Context context;
	private MessageReceiver receiver;
	private PluginListener listener;
	Context tempContext;
	
	public static PluginConfig config = new PluginConfig();
	
	public static final SLogger log = SLogger.get("Syknet.client");
	
	public void setListener(PluginListener ls) {
		listener = ls;
	}
	
	public static void setConfig(PluginConfig c) {
		config = c;
		if (config.isDebug) {
			SLogger.setDefaultLevel(SLogger.levelDebug);
		}
		
		if (config.hostUrl != null) {
			DownloadTask.setHost(config.hostUrl);
		}
		if (config.flageType!=null) {
			DownloadTask.setFlageType(config.flageType);
		}
		UpdateService.setCheckTime(config.checkUpdateTime);
		
		PluginClassLoader.setClassLoaderType(c.classLoaderType);
	}
	
	private static PluginClient instance;
	public static void init(Context context) {
		init(context, "axplugin");
	}
	public static void init(Context context, String engine) {
		if (instance != null) {
			log.w("already inited");
			return;
		}
		
		log.d("PluginClient init");
		
		if ("axplugin".equals(engine)) {
			instance = new NativePluginClient();
		} else {
			log.i("unknow engine:" + engine);
			instance = new NativePluginClient();
		}
		
		instance.context = context.getApplicationContext();
		instance.listener = new PluginListener(instance.context);
		instance.onInit(context);
		instance.start(context);
	}
	
	public static PluginClient getInstance() {
		return instance;
	}
	
	protected abstract void onInit(Context context);
	/**
	 * 
	 * 方法名：start<BR>  
	 * 此方法描述的是：   启动插件
	 * @param context  void
	 */
	protected void start(Context context) {
		Intent i = new Intent(context, UpdateService.class);
		context.startService(i);
		registerBroadcastReceiver();//注册广播
	}
	/**
	 * 
	 * 方法名：stop<BR>  
	 * 此方法描述的是：  停止插件更新服务
	 */
	protected void stop() {
		Intent i = new Intent(context, UpdateService.class);
		context.stopService(i);
		context.unregisterReceiver(receiver);
	}

	//public abstract List<PlugInfo> installedList();
	/**
	 * 
	 * 方法名：install<BR>  
	 * 此方法描述的是： 安装插件  
	 * @param file
	 * @param id  void
	 */
	protected abstract void install(String file, String id);
	/**
	 * 
	 * 方法名：uninstall<BR>  
	 * 此方法描述的是：   卸载插件
	 * @param id  void
	 */
	protected abstract void uninstall(String id);
	/**
	 * 
	 * 方法名：isInstalled<BR>  
	 * 此方法描述的是：  验证插件是否安装 
	 * @param id
	 * @return  boolean
	 */
	protected abstract boolean isInstalled(String id);
	/**
	 * 
	 * 方法名：isLoaded<BR>  
	 * 此方法描述的是：  验证插件是否已加载 
	 * @param id
	 * @return  boolean
	 */
	public boolean isLoaded(String id) { return isInstalled(id); }
	/**
	 * 
	 * 方法名：load<BR>  
	 * 此方法描述的是：   加载插件
	 * @param id  void
	 */
	public void load(String id) {
		log.d("load: " + id);
		
		if (!isInstalled(id)) {
			//如果插件未安装，则开启插件更新服务
			downloadPlugin(id
					, UpdateService.installType_install);
		} else {
			if (listener != null) {
				//如果监听器不为空，加载插件
				listener.onLoaded(id);
			}
		}
	}

	/**
	 * load and run main Activity
	 * @param id
	 * @param ctx
	 */
	public void launch(String id, Context ctx, boolean finishLaunchActivity) {
		log.d("launch: " + id);
		
		if (listener != null) {
			listener.finishLaunchActivity = finishLaunchActivity;
		}
		
		if (!isInstalled(id)) {
			tempContext = ctx;
			downloadPlugin(id
					, UpdateService.installType_launch);
		} else {
			if (listener != null) {
				listener.onLoaded(id);
			}
			this.startMainActivity(ctx, id);
			
			if (listener != null) {
				tempContext = ctx;
				listener.onLaunch(id);
				tempContext = null;
			}
		}
	}
	/**
	 * 
	 * 方法名：downloadPlugin<BR>  
	 * 此方法描述的是：   下载插件
	 * @param appId	appid
	 * @param installType  安装类型
	 */
	private void downloadPlugin(String appId, int installType) {
		Intent i = new Intent(context, UpdateService.class);
		Bundle b = new Bundle();
		b.putString("appId", appId);
		b.putInt("installType", installType);
		i.putExtras(b);
		context.startService(i);
	}
	/**
	 * 
	 * 方法名：onDownload<BR>  
	 * 此方法描述的是：  处理下载后的插件 
	 * @param appId
	 * @param versionInfo
	 * @param installType
	 * @param cache  void
	 */
	private void onDownload(String appId, PluginVersionInfo versionInfo
			, int installType, boolean cache) {
		File downloadStoragePath = context.getDir("download",
				Context.MODE_PRIVATE);							//获取下载的插件的保存路径
		File file = new File(downloadStoragePath, UpdateService.getFileName(versionInfo));
		
		if (isInstalled(appId)) {//插件以及安装，判断是否存在缓存，如果没有缓存，先卸载插件，然后再进行安装
			if (cache == false) {
				uninstall(appId);
				install(file.getAbsolutePath(), appId);
			}
		} else {
			install(file.getAbsolutePath(), appId);
		}
		
		if (listener != null) {
			if (installType != UpdateService.installType_update) {
				listener.onLoaded(appId);
			} else {
				if (cache == false) {
					listener.onUpdate(appId, versionInfo.whatsNew);
				}
			}
		}
		
		if (installType == UpdateService.installType_launch) {
			this.startMainActivity(tempContext != null ? tempContext : context, appId);
			if (listener != null) {
				listener.onLaunch(appId);
			}
			tempContext = null;
		}
	}
	/**
	 * 
	 * 方法名：registerBroadcastReceiver<BR>  
	 * 此方法描述的是：     注册广播
	 */
	private void registerBroadcastReceiver() {
		receiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter(UpdateService.CompleteAction);
		context.registerReceiver(receiver, filter);
		IntentFilter filter2 = new IntentFilter(UpdateService.ProgressAction);
		context.registerReceiver(receiver, filter2);
	}
	/**
	 * 
		 * @ClassName:MessageReceiver <BR>
	     * @Describe：信息广播，处理插件更新动态<BR>
	     * @Author: Jekshow
		 * @Extends：<BR>
	     * @Version:1.0 
	     * @date:2016-8-3 下午6:12:34
	 */

	private class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (UpdateService.CompleteAction.equals(action)) {
				try {
					Bundle b = intent.getExtras();
					String appId = b.getString("appId");
					PluginVersionInfo versionInfo = (PluginVersionInfo) b.getSerializable("versionInfo");
					boolean success = b.getBoolean("success");
					int installType = b.getInt("installType");
					boolean cache = b.getBoolean("cache");
					String error = b.getString("error");
					
					if (success) {
						//插件更新成功，加载插件
						onDownload(appId, versionInfo, installType, cache);
					} else {
						if (listener != null) {
							listener.onError(appId, error);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (UpdateService.ProgressAction.equals(action)) {
				try {
					Bundle b = intent.getExtras();
					String appId = b.getString("appId");
					float percent = b.getFloat("percent");
					int installType = b.getInt("installType");
					int max = b.getInt("max");
					if (listener != null) {
						listener.onProgress(appId, percent, max, installType);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public abstract boolean startMainActivity(Context context, String id);

	public abstract void startActivity(Context context, Intent intent);

	public abstract void startActivityForResult(Activity activity,
			Intent intent, int requestCode);
}