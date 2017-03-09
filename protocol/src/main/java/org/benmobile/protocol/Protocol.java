/**
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.protocol;

/**
 * 
	 * @ClassName:Protocol <BR>
     * @Describe：消息接口<BR>
     * @Author: Jekshow
	 * @Extends：<BR>
     * @Version:1.0 
     * @date:2016-9-7 上午10:18:15
 */
public interface Protocol {
	/**
	 * 
	 * 方法名：call<BR>  
	 * 此方法描述的是：   获取消息内容
	 * @param arg
	 * @return  Object
	 */
	Object call(Object arg);
}
