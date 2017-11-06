/*
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.core.natvie.ui.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;
/*
 * 
	 * @ClassName: VersionInfo<BR>
     * @Describe：App plug-in version information entity class<BR>
     * @Author: Jekshow
     * @Version:1.0
     * @date:2016-8-3 下午1:32:09
 */
public  class PluginVersionInfo implements Serializable{
	private static final long serialVersionUID = 8445572940087456648L;
	public String id;				//The plug-in id
	public String fileName;		//The plugin file name
	public String whatsNew;		//Update the document
	public long fileTime;			//Upload time
	public long fileSize;			//The plugin file size
	public String appProject;		//The plug-in specific number
	public String digest;			//Plug-in check code
	public String version;			//The plug-in version number
	public String fileUrl;			//The plug-in download link
	public String appid;			//AppId
	public String pkg;				//The plug-in application package name
	

	
	public  PluginVersionInfo (JSONObject jsn) throws JSONException {


		{
			try {
				if (jsn.has("id")) {
					this.id = jsn.optString("id");
				}
				if (jsn.has("fileName")) {
					this.fileName = jsn.optString("fileName");
				}
				if (jsn.has("whatsNew")) {
					this.whatsNew = jsn.optString("whatsNew");
				}
				if (jsn.has("fileTime")) {
					this.fileTime = Long.parseLong(jsn.optString("fileTime"));
				}
				if (jsn.has("fileSize")) {
					this.fileSize = Long.parseLong(jsn.optString("fileSize"));
				}
				if (jsn.has("appProject")) {
					this.appProject = jsn.optString("appProject");
				}
				if (jsn.has("digest")) {
					this.digest = jsn.optString("digest");
				}
				if (jsn.has("version")) {
					this.version = jsn.optString("version");
				}
				if (jsn.has("fileUrl")) {
					this.fileUrl = jsn.optString("fileUrl");
				}
				if (jsn.has("appid")) {
					this.appid = jsn.optString("appid");
				}
				if (jsn.has("pkg")) {
					this.pkg = jsn.optString("pkg");
				}



			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

	public PluginVersionInfo() {
		super();
	}

	@Override
	public String toString() {
		return "VersionInfo [id=" + id + ", fileName=" + fileName
				+ ", whatsNew=" + whatsNew + ", fileTime=" + fileTime
				+ ", fileSize=" + fileSize + ", appProject=" + appProject
				+ ", digest=" + digest + ", version=" + version + ", fileUrl="
				+ fileUrl + ", appid=" + appid + ", pkg=" + pkg + "]";
	}


	
}
