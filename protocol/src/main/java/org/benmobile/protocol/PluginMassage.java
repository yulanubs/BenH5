package org.benmobile.protocol;

import android.os.Bundle;

import java.io.Serializable;


/**
 * 
 * @ClassName: PluginMassage<BR>
 * @Describe：查询消息传递msg<BR>
 * @Author: Jekshow
 * @Extends：<BR>
 * @Version:1.0
 * @date:2016-9-7 上午10:19:49
 */
public class PluginMassage implements Serializable {
	private static final long serialVersionUID = 1L;
	/**发送端包名（插件的id等值）*/
	public String formPackageName;
	/**发送端Action：（全类名）*/
	public String formAction;
	/**接收端包名（插件的id等值）*/
	public String toPackageName;
	/**接收端Action（全类名）*/
	public String toAction;
	/**参数*/
	public Bundle bundle;
	/**需要接收相关信息的已注册插件id*/
	public String   pluginId;
	public  int  requestcode;

	/**
	 * 
	     * 创建一个新的实例 PluginMassage.   
	     *   
	     * @param formPackageName
	     * @param formAction
	     * @param toPackageName
	     * @param toAction
	     * @param bundle
	 */
	public PluginMassage(String formPackageName, String formAction,
			String toPackageName, String toAction, Bundle bundle, String   pluginId,int requestcode) {
		super();
		this.formPackageName = formPackageName;
		this.formAction = formAction;
		this.toPackageName = toPackageName;
		this.toAction = toAction;
		this.bundle = bundle;
		this. pluginId=pluginId;
		this.requestcode=requestcode;
	}
	public PluginMassage(String formPackageName, String formAction,
						 String toPackageName, String toAction, Bundle bundle) {
		super();
		this.formPackageName = formPackageName;
		this.formAction = formAction;
		this.toPackageName = toPackageName;
		this.toAction = toAction;
		this.bundle = bundle;
	}

	/**
	 * 
	     * 创建一个新的实例 PluginMassage.   
	     *
	 */
	public PluginMassage() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
