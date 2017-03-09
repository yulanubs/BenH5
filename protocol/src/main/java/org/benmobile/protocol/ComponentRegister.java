/**
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.protocol;

import android.content.Context;

import org.benmobile.coolhttp.http.CoolHttp;
import org.benmobile.coolhttp.http.CoolHttp.HttpConfig;
import org.benmobile.protocol.event.GetMsgInfoListener;
import org.benmobile.protocol.observer.MyObservable;
import org.benmobile.protocol.utlis.ValueUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ComponentRegister extends MyObservable {
	private static Context sContext;
	private static HttpConfig sHttpConfig;
	private static ComponentRegister instance = new ComponentRegister();
	/**插件推送信息缓存容器*/
	private Map<String, Protocol> map = new HashMap<String, Protocol>();
	/**单例*/
	public static ComponentRegister getInstance() {
		return instance;
	}

	/**
	 * 初始化coolHttp网络请求框架
	 * @param context
	 * @param httpConfig
     */
	public static void initialize(Context context, HttpConfig httpConfig) {
		sContext = context;
		sHttpConfig = httpConfig;
		if(ValueUtils.isNotEmpty(sContext)&&ValueUtils.isNotEmpty(sHttpConfig)){
			CoolHttp.initialize(context, httpConfig);
		}
	}

	/**
	 * 读取一条信息,意味者消费一条信息，所以移除该消费发信息
	 * @param name key
	 * @return		数据
     */
	public synchronized Protocol getComponent(String name) {
		Protocol protocol = map.get(name);
		removeComponent(name);
		return protocol;
	}

	/**
	 * 移除一条消息
	 * @param name	key
	 * @return		数据
     */
	public synchronized boolean removeComponent(String name) {
		boolean is=false;
		//创建迭代器
			Iterator<String> iterator = map.keySet().iterator();
			try {
				while (iterator.hasNext()) {
					String key = iterator.next();
					//判断key
					if (key.equals(name)) {
						//移除
						iterator.remove();

					}

				}
			is=true;
		} catch (Exception e) {
			e.printStackTrace();
			is=false;
		}
		return is;
	}

	/**
	 * 写入一条成功数据消息，支持监听回调和观察者
	 * @param pluginId 			插件id
	 * @param key					key（请求码）
	 * @param resultCode			响应码
	 * @param p						数据
	 * @param msgInfoListener		监听回调
     */
	public synchronized void setComponent(String pluginId, String key,String resultCode, Protocol p,GetMsgInfoListener msgInfoListener) {
		map.put(key, p);
		setChanged();
		//观察者
		notifyObservers(pluginId,key);
		//回调传值
		msgInfoListener.onMsgSucceed(pluginId,Integer.parseInt(key),Integer.parseInt(resultCode), p);
	}

	/**
	 *写入一条失败数据消息，支持监听回调和观察者
	 * @param pluginId				插件id
	 * @param key					key（请求码）
	 * @param resultCode			响应码
	 * @param p						数据
	 * @param msgInfoListener		监听回调
     */
	public synchronized void setFailedComponent(String pluginId, String key,String resultCode, Protocol p,GetMsgInfoListener msgInfoListener) {
		map.put(key, p);
		setChanged();
		//观察者
		notifyObservers(pluginId,key);
		//回调传值
		msgInfoListener.onMsgFailed(pluginId,Integer.parseInt(key),Integer.parseInt(resultCode), p);
	}

	/**
	 * 写入一条成功数据信息，只支持观察者，建议插件间使用
	 * @param pluginId			插件id
	 * @param key				key（请求码）
     * @param p					数据
     */
	public synchronized void setComponent(String pluginId, String key, Protocol p) {
		map.put(key, p);
		setChanged();
		//观察者
		notifyObservers(pluginId,key);
	}

	/**
	 *写入一条失败状态数据信息，只支持观察者，建议插件间使用
	 * @param pluginId		插件id
	 * @param key			key（请求码）
     * @param p				数据
     */
	public synchronized void setFailedComponent(String pluginId, String key, Protocol p) {
		map.put(key, p);
		setChanged();
		//观察者
		notifyObservers(pluginId,key);
	}



}
