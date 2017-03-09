/*
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.core.natvie.app;


import java.util.HashMap;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.benmobile.core.natvie.core.bean.PluginInfo;
import org.benmobile.core.natvie.core.manager.SyknetPluginManager;

public class NativePluginClient extends PluginClient {
	/**插件管理器*/
	private SyknetPluginManager plugMgr;
	/**插件容器*/
	private Map<String, String> plugMap = new HashMap<String, String>();

	@Override
	protected void onInit(Context context) {
		//实例化插件管理器
		plugMgr = SyknetPluginManager.getInstance();
		//初始化插件管理器
		plugMgr.init(context);
	}

	@Override
	protected void uninstall(String id) {
		String pkgName = plugMap.get(id);
		plugMgr.removePlugin(pkgName);
	}

	@Override
	protected void install(String file, String id) {
		try {
			PluginInfo pluginInfo = plugMgr.load(file, id);
			plugMap.put(id, pluginInfo.getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean isInstalled(String id) {
		String pkgName = plugMap.get(id);
		return plugMgr.getPlugin(pkgName) != null;
	}

	@Override
	public boolean startMainActivity(Context context, String id) {
		String pkgName = plugMap.get(id);
		Intent i = plugMgr.makeLaunchIntent(pkgName);
		context.startActivity(i);
		return true;
	}

	@Override
	public void startActivity(Context context, Intent intent) {
		plugMgr.changeIntent(context, intent);
		context.startActivity(intent);
	}

	@Override
	public void startActivityForResult(Activity activity, Intent intent,
			int requestCode) {
		plugMgr.changeIntent(context, intent);
		activity.startActivityForResult(intent, requestCode);
	}
}