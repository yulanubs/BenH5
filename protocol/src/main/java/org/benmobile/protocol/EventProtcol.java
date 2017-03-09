package org.benmobile.protocol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.benmobile.protocol.config.PluginConstData;


/**
 * 
	 * @ClassName: EventProtcol<BR>
     * @Describe：中间件事件处理<BR>
     * @Author: Jekshow
	 * @Extends：<BR>
     * @Version:1.0 
     * @date:2016-9-12 上午9:41:51
 */
public class EventProtcol {
	
	/**
	 * 
	 * 方法名：JumpActivityForResult<BR>
	 * 此方法描述的是： 页面跳转方法，回传值
	 * 
	 * @param pluginId
	 *            上下文
	 * @param formPackageName
	 *            发送端包名
	 * @param formClassName
	 *            发送端Activity全类名
	 * @param toPackageName
	 *            接收端包名
	 * @param toClassName
	 *            接收端全类名
	 * @param bundle
	 *            参数
	 */
	public static  void JumpActivityForResult(Activity activity, final String formPackageName,
			final String formClassName, final String toPackageName,final  String toClassName,
			final Bundle bundle,String pluginId,int resultCode) {

		System.out.println("mContext:"+activity.getClass().getName());
		// 判断是否为本插件中跳转，判断依据为发送端和接收端的包名是否相等
		if (formPackageName.equals(toPackageName))

			try {
				Class<?> acty = Class.forName(toClassName);
				Intent mIntent = new Intent(activity, acty);
				mIntent.putExtra(toClassName, bundle);
				activity.startActivityForResult(mIntent,resultCode);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		else {
			//向宿主发送页面跳转的请求消息
			Protocol pluginInterface = new Protocol() {
				@Override
				public Object call(Object arg) {
					return new PluginMassage(formPackageName, formClassName, toPackageName, toClassName, bundle);
				}
			};
			
			ComponentRegister.getInstance().setComponent(pluginId, PluginConstData.JUMP_ACTION_TYPE, pluginInterface);
		}

	}

	/**
	 * JumpActivity
	 * Activity跳转，不回传值
	 * @param mContext	上下文
	 * @param formPackageName		跳转发起者包名
	 * @param toPackageName		目标包名
	 * @param toClassName			目标全类名
	 * @param bundle				Bundle对象用于封装参数
	 * @param pluginId				发起者所属插件id
     * @param requestcode			请求码
     */
	public static  void JumpActivity(Context mContext, final String formPackageName,
									 final String formClassName, final String toPackageName, final  String toClassName,
									 final Bundle bundle, final String pluginId, final int requestcode) {

		System.out.println("mContext:"+mContext.getClass().getName());
		// 判断是否为本插件中跳转，判断依据为发送端和接收端的包名是否相等
		if (formPackageName.equals(toPackageName))

			try {
				Class<?> acty = Class.forName(toClassName);
				Intent mIntent = new Intent(mContext, acty);
				mIntent.putExtra(toClassName, bundle);
				mContext.startActivity(mIntent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		else {
			//向宿主发送页面跳转的请求消息
			Protocol pluginInterface = new Protocol() {
				@Override
				public Object call(Object arg) {
					return new PluginMassage(formPackageName, formClassName, toPackageName, toClassName, bundle,pluginId,requestcode);
				}
			};

			ComponentRegister.getInstance().setComponent(pluginId,PluginConstData.JUMP_ACTION_TYPE, pluginInterface);
		}

	}
}
