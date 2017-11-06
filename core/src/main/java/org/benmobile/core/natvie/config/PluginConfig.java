/*
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.core.natvie.config;
/**
 * configuration option
 *
 */
public class PluginConfig {
	public String hostUrl = null;
	
	/**
	 * Check for updates at startup
	 */
	public boolean updateWhenLaunch = false;
	
	/**
	 * Force restart after update application
	 */
	public boolean forceRestart = false;
	
	/**
	 * Update applications only under WiFi
	 */
	public boolean updateOnlyWifi = true;
	
	/**
	 * If it is debug mode, a large number of logs will be printed under debugging
	 */
	public boolean isDebug = false;
	
	/**
	 * Whether to use assets apk plugin directory preset
	 */
	public boolean copyAsset = true;
	
	/**
	 * Check the update interval
	 */
	public long checkUpdateTime = 15 * 60 * 1000;
	
	/**
	 * Class loader type. When the host is the same as the plug-in includes classes: 0 indicates preferred host, 1 said plug-in is preferred. Recommend not packaged in the plug-in and the host of the same class. There are two common class loader will question:
	 *
	 * 1. The plugin is preferred, could lead to a host and plug-in class is segregated into different space, may produce ClassCastException when calling each other.
	 *
	 * 2. The host is preferred, under Android4.4 system, if have the same kind of plug-in and the host, in some cases there may be a Class ref in the pre - verified abnormality. Solution to see https://github.com/chunquedong/axbasePlugin/wiki
	 */
	public int classLoaderType = 0;
	/**
	 * The plug-in update condition
	 * 0. All, according to the purpose of additional qualifications AppId queries all plug-ins
	 * 1. According to a plug-in id, query information
	 * 2.According to a plug-in appProject, query information
	 */
	public String flageType="2";

	/**Plug-in version with the new interfac*/
	public static  String CHECK_UPDATEAPPPLUGIN = "";
	/**AppId*/
	public  static  String APP_ID="";
}
