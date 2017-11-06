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

	/*
	 * The method name：start<BR>
	 * This method describes: start the plugin
	 * @param context
	 */
	protected void start(Context context) {
		Intent i = new Intent(context, UpdateService.class);
		context.startService(i);
		//Registered broadcasting
		registerBroadcastReceiver();//
	}
	/*
	 *
	 * The method name：stop<BR>
	 * This method describes: Stop plug - in update service
	 */
	protected void stop() {
		Intent i = new Intent(context, UpdateService.class);
		context.stopService(i);
		context.unregisterReceiver(receiver);
	}

	/*
	 * The method name：install<BR>
	 * This method describes Install plug-ins
	 * @param file
	 * @param id
	 */
	protected abstract void install(String file, String id);

	/*
	 * The method name：uninstall<BR>
	 * This method describes  Uninstall plug-in
	 * @param id
	 */
	protected abstract void uninstall(String id);

	/*
	 * The method name：isInstalled<BR>
	 * This method describes  Verify that the plug-in is installed
	 * @param id
	 * @return
	 */
	protected abstract boolean isInstalled(String id);

	/*
	 * The method name：isLoaded<BR>
	 * This method describes Verify that the plug-in is loaded
	 * @param id
	 * @return
	 */
	public boolean isLoaded(String id) { return isInstalled(id); }

	/*
	 * The method name：load<BR>
	 * This method describes  Load plug-in
	 * @param id
	 */
	public void load(String id) {
		log.d("load: " + id);
		
		if (!isInstalled(id)) {
			//If the plug-in is not installed, then the plug-in update service is opened
			downloadPlugin(id
					, UpdateService.installType_install);
		} else {
			if (listener != null) {
				//If the listener is not empty, load the plug-in
				listener.onLoaded(id);
			}
		}
	}

	/*
	 * load and run main Activity
	 * @param id
	 * @param ctx
	 * @param finishLaunchActivity
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

	/*
	 * The method name：downloadPlugin<BR>
	 * This method describes   Download Plug-in
	 * @param appId
	 * @param installType
	 */
	private void downloadPlugin(String appId, int installType) {
		Intent i = new Intent(context, UpdateService.class);
		Bundle b = new Bundle();
		b.putString("appId", appId);
		b.putInt("installType", installType);
		i.putExtras(b);
		context.startService(i);
	}

	/*
	 * The method name：onDownload<BR>
	 * This method describes  Handling downloaded plug-ins
	 * @param appId
	 * @param versionInfo
	 * @param installType
	 * @param cache
	 */
	private void onDownload(String appId, PluginVersionInfo versionInfo
			, int installType, boolean cache) {
		File downloadStoragePath = context.getDir("download",
				Context.MODE_PRIVATE);							//Gets the saved path for the downloaded plug-in
		File file = new File(downloadStoragePath, UpdateService.getFileName(versionInfo));
		
		if (isInstalled(appId)) {
			//Plug in and install to determine if there is a cache. If there is no cache, uninstall the plug-in first, and then install
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

	/*
	 * The method name：registerBroadcastReceiver<BR>
	 * This method describes    Registered broadcasting
	 */
	private void registerBroadcastReceiver() {
		receiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter(UpdateService.CompleteAction);
		context.registerReceiver(receiver, filter);
		IntentFilter filter2 = new IntentFilter(UpdateService.ProgressAction);
		context.registerReceiver(receiver, filter2);
	}
	/*
	 * 
		 * @ClassName:MessageReceiver <BR>
	     * @Describe：Information broadcasting, processing plug-in update dynamics<BR>
	     * @Author: Jekshow
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
						//The plug-in was updated successfully and loaded the plug-in
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