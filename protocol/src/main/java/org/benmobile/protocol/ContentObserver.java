package org.benmobile.protocol;
/**
 * 
	 * @ClassName: ContentObserver<BR>
     * @Describe：消息通知类<BR>
     * @Author: Jekshow
	 * @Extends：<BR>
     * @Version:1.0 
     * @date:2016-9-7 上午8:45:43
 */
public abstract class ContentObserver {
	/**
	 * 
	 * 方法名：getMsg<BR>  
	 * 此方法描述的是：当插件发送消息时，回调该方法   
	 * @param key  
	 */
	public abstract void getMsg(String key);

}
