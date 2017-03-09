package org.benmobile.protocol.service;


import org.benmobile.protocol.bean.ProtocolMsgBean;

/**
 * 控制器请求基础回调接口
 */
public interface PushPluginActionListener {
      /**请求回调方法*/
      public  void resultPluginAction(int requestCode, int resultCode, ProtocolMsgBean data);

}