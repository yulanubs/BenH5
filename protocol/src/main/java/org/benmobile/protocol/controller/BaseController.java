package org.benmobile.protocol.controller;


import org.benmobile.protocol.ComponentRegister;
import org.benmobile.protocol.Protocol;
import org.benmobile.protocol.bean.ProtocolMsgBean;
import org.benmobile.protocol.bean.PushPluginAction;
import org.benmobile.protocol.event.GetMsgInfoListener;
import org.benmobile.protocol.event.OnAppNetQuestListener;

/**
 * Created by Jekshow on 2016/12/6.
 * 基础控制器类
 */

public abstract class  BaseController  {
    /**响应*/
    protected  int  resultCode =-1;

    /**
     * 网络消息请求方法，不带回调，默认支持观察者
     * @param mPPA          请求体
     * @param requestCode  请求码
     */
    protected abstract  void   pushPluginAction(PushPluginAction mPPA, int requestCode);
    /**
     * 发起请网络求方法，不带回调，默认支持观察者
     * @param mPPA          请求体
     * @param requestCode  请求码
     */
    protected abstract  void   pushNetQuestPluginAction(String url,PushPluginAction mPPA, int requestCode);

    /**
     * 发送请求，带回调
     * @param mPPA                  请求体
     * @param requestCode          请求码
     * @param msgInfoListener      回调监听
     */
    protected abstract  void   pushPluginAction(PushPluginAction mPPA, int requestCode, GetMsgInfoListener msgInfoListener);
    /**
     * 发送网络请求，带回调
     * @param mPPA                  请求体
     * @param requestCode          请求码
     * @param onAppNetQuestListener      回调监听
     */
    protected abstract  void   pushNetQuestPluginAction(String url,PushPluginAction mPPA, int requestCode,OnAppNetQuestListener onAppNetQuestListener);

    /**
     * 发送基础数据到中间件,带回调，默认支持观察者
     * @param pluignId 插件id
     * @param requestCode   请求码
     * @param resultCode    响应码
     * @param data           数据
     * @param msgInfoListener   回调接口
     */
    protected  void resultPluginAction(String  pluignId, int requestCode, int resultCode, final ProtocolMsgBean data, GetMsgInfoListener msgInfoListener){

     Protocol hostInterface = new Protocol() {
        @Override
        public Object call(Object arg) {
            return data;
        }
    };
    //发送数据到中间件，带回调
    ComponentRegister.getInstance().setComponent(pluignId,requestCode+"",resultCode+"", hostInterface,msgInfoListener);

}

    /**发送网络响应数据到中间件,带回调，默认支持观察者
     *
     * @param pluignId      插件ID
     * @param requestCode   请求码
     * @param resultCode    响应码
     * @param data          数据
     * @param onAppNetQuestListener   回调监听
     */
    protected  void resultFailedPluginAction(String  pluignId, int requestCode, int resultCode, final ProtocolMsgBean data,OnAppNetQuestListener onAppNetQuestListener){

     Protocol hostInterface = new Protocol() {
        @Override
        public Object call(Object arg) {
            return data;
        }
    };
    //发送数据到中间件，带回调
    ComponentRegister.getInstance().setFailedComponent(pluignId,requestCode+"",resultCode+"", hostInterface,onAppNetQuestListener);
}

    /**
     * 发送数据到中间件，不带回调，默认支持观察者
     * @param pluignId      插件id
     * @param requestCode   请求码
     * @param resultCode    响应码
     * @param data           数据
     */
    protected  void resultPluginAction(String  pluignId, int requestCode, int resultCode, final ProtocolMsgBean data){

        Protocol hostInterface = new Protocol() {
            @Override
            public Object call(Object arg) {
                return data;
            }
        };
        //发送数据到中间件，默认支持观察者
        ComponentRegister.getInstance().setComponent(pluignId,requestCode+"", hostInterface);
    }

    /**
     *发送网络响应数据到中间件,无回调，默认支持观察者
     * @param pluignId          插件id
     * @param requestCode       请求码
     * @param resultCode        响应码
     * @param data              数据
     */
    protected  void resultFailedPluginAction(String  pluignId, int requestCode, int resultCode, final ProtocolMsgBean data){

        Protocol hostInterface = new Protocol() {
            @Override
            public Object call(Object arg) {
                return data;
            }
        };
        //发送数据到中间件，默认支持观察者
        ComponentRegister.getInstance().setFailedComponent(pluignId,requestCode+"", hostInterface);
    }

}


