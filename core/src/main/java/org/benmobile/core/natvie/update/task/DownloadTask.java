/*
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.core.natvie.update.task;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import org.benmobile.core.natvie.app.PluginClient;
import org.benmobile.core.natvie.config.PluginConfig;
import org.benmobile.core.natvie.net.HttpClient;
import org.benmobile.core.natvie.net.HttpClient.HttpException;
import org.benmobile.core.natvie.net.HttpClient.HttpHandler;
import org.benmobile.core.natvie.ui.bean.PluginVersionInfo;
import org.benmobile.core.natvie.ui.bean.PluginVersionInfoList;
import org.benmobile.core.natvie.update.bean.OfflineVersion;
import org.benmobile.core.natvie.update.service.UpdateService;
import org.benmobile.core.natvie.utils.StreamUtil;
import org.benmobile.core.natvie.utils.ValueUtils;

public class DownloadTask {
	private UpdateService service;

	private HttpClient httpClient = new HttpClient();
	private File downloadStoragePath;
	private int installType;

	private static String urlHost = "";
	private static String flageType="1";

	String getHost() {
		return urlHost;
	}

	public static void setHost(String url) {
		urlHost = url;
	}

	public static String getFlageType() {
		return flageType;
	}

	public static void setFlageType(String flageType) {
		DownloadTask.flageType = flageType;
	}

	String getImei() {
		String imei = null;
		try {
			imei = service.telephonyManager.getDeviceId();
			return imei;
		} catch (Exception ex) {
		}
		
		if (imei != null) {
			return imei;
		}
		return "unknow";
	}

	String getPackageName() {
		return service.packageName;
	}

	public DownloadTask(UpdateService service, int installType) {
		this.service = service;
		this.downloadStoragePath = service.downloadStoragePath;
		this.installType = installType;
	}

	public void request(String appId) throws HttpException, JSONException {
//		String urlStr = getHost() + "/AppProjects/checkUpdate/" + appId
//				+ "?did=" + getImei() + "&pkg=" + getPackageName();
//		String urlStr = getHost() + "/SykNetApi/AppPlugin/CheckUpdate.do?appkey=2xV107Po78T9fdi96Ryoq5Su2LunJ8L1&appId=1001&flageType="+getFlageType()+"&pluginId=2"+"&appProject="+appId;
		String urlStr = getHost() + PluginConfig.CHECK_UPDATEAPPPLUGIN+"?appid="+PluginConfig.APP_ID+"&flageType="+getFlageType()+"&pluginId=3&appProject="+appId;
//		String urlStr =  "http://api.syknet.cn/SykNetApi/AppPlugin/CheckUpdate.do?appkey=2xV107Po78T9fdi96Ryoq5Su2LunJ8L1&appId=1001&flageType="+getFlageType()+"&pluginId=2"+"&appProject="+appId;
		String text = httpClient.get(urlStr);
		/**版本信息*/
		PluginVersionInfo versionInfo = null;
		if (ValueUtils.isStrEmpty(text)){
			return;
		}
		PluginClient.log.d(urlStr);
		PluginClient.log.d(text);

		/**插件解析版本更新*/
		PluginVersionInfoList mPluginVersionInfoList=new PluginVersionInfoList(new JSONObject(text));

		if (mPluginVersionInfoList == null||ValueUtils.isListEmpty(mPluginVersionInfoList.mPluginVersionInfoList))
			return;
		/**默认根据插件特殊编码查询，字典只返回一条记录，所以去字典下标为0的数据*/
		versionInfo=mPluginVersionInfoList.mPluginVersionInfoList.get(0);
		if (ValueUtils.isEmpty(versionInfo)){
			//如果插件跟新数据为空，程序结束运行
			return;
		}
		PluginClient.log.d(versionInfo.toString());

		OfflineVersion ov = service.getCache(appId);
		if (ov != null) {
			if (versionInfo.version.equals(ov.version)
					&& versionInfo.fileSize == ov.file.length()) {
				PluginClient.log.d(appId + " version is ok");
				onComplete(versionInfo, true, null, true);
				return;
			}
		}

		PluginClient.log.d("doDownload " + appId);
		doDownload(versionInfo);
	}

	public class DownloadHttpHandler implements HttpHandler {
		PluginVersionInfo versionInfo;

		@Override
		public void writeReq(OutputStream out) throws IOException {
		}

		public void pipe(InputStream in, OutputStream out, int length)
				throws IOException {
			try {
				byte[] bytes = new byte[StreamUtil.bufferSize];
				int len = 0;
				int count = 0;
				int bufCount = 0;
				if (length <= 0) {
					length = (int) versionInfo.fileSize;
				}

				while ((len = in.read(bytes)) != -1) {
					out.write(bytes, 0, len);

					count += len;

					++bufCount;
					if (bufCount > 10) {
						float percent = count / (float) length;
						service.onProgress(versionInfo.appProject, percent,
								length, installType);
						bufCount = 0;

						if (PluginClient.config.isDebug) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void readRes(InputStream in, int length) throws IOException {
			File file = new File(downloadStoragePath, "temp_"
					+ versionInfo.appProject);
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream out = new FileOutputStream(file);
			pipe(in, out, length);
			out.close();

			long fileSize = file.length();
			if (versionInfo.fileSize == fileSize) {

				FileInputStream fin = new FileInputStream(file);
				String digest = "" + StreamUtil.crc32(fin);
				if (versionInfo.digest.equals(digest)) {
					File newPath = new File(downloadStoragePath,
							UpdateService.getFileName(versionInfo));
					file.renameTo(newPath);
					service.registApp(versionInfo.appProject,
							versionInfo.version, newPath);
					onComplete(versionInfo, true, null, false);
				} else {
					onComplete(versionInfo, false, "diest error", false);
				}
			} else {
				onComplete(versionInfo, false, "file size error", false);
			}
		}

		@Override
		public void onError(int code, Object msg) {
			onComplete(versionInfo, false, "net error", false);
		}

	}

	private void doDownload(PluginVersionInfo versionInfo) {
		DownloadHttpHandler handler = new DownloadHttpHandler();
		handler.versionInfo = versionInfo;
//		String url = getHost() + "/AppProjects/download/"
//				+ versionInfo.appProject + "?version=" + versionInfo.version
//				+ "&did=" + getImei() + "&pkg=" + getPackageName();
		String url = versionInfo.fileUrl;
		if (ValueUtils.isStrNotEmpty(url)) {
			//判断插件下载地址是否为空
			httpClient.doRequest(url, "GET", handler);
		}else{
			PluginClient.log.w("plugin_fileUrl" + "--error" );	
		}
	}

	private void onComplete(PluginVersionInfo versionInfo, boolean success,
			String error, boolean cache) {
		if (!success) {
			PluginClient.log.w(versionInfo.appProject + "," + error);
		}

		service.onComplete(versionInfo.appProject, versionInfo, success, error,
				installType, cache);
	}
}
