package org.benmobile.protocol.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Jekshow on 2016/12/6.
 * 插件通信报文封装
 */

public class PushPluginAction implements Serializable {
    /**通信类型*/
    private   String msgType;
    /**服务类型*/
    private String serviceType;
    /**事件动作*/
    private  String eventAction;
    /**请求参数*/
    private Map<String,String> param;
    /**操作者*/
    private String fromAction;
    /**目标*/
    private  String toAction;
    /**需要接收相关信息的已注册插件id*/
    private String   pluginId;
    /**Url*/
    private  String mUrl;
    /**API*/
    private  String mApi;

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmApi() {
        return mApi;
    }

    public void setmApi(String mApi) {
        this.mApi = mApi;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    public void setFromAction(String fromAction) {
        this.fromAction = fromAction;
    }

    public void setToAction(String toAction) {
        this.toAction = toAction;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public String getFromAction() {
        return fromAction;
    }

    public String getToAction() {
        return toAction;
    }

    public String getMsgType() {
        return msgType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getEventAction() {
        return eventAction;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setEventAction(String eventAction) {
        this.eventAction = eventAction;
    }


    public PushPluginAction(String msgType, String serviceType, String eventAction, Map<String, String> param, String fromAction, String toAction, String pluginId) {
        this.msgType = msgType;
        this.serviceType = serviceType;
        this.eventAction = eventAction;
        this.param = param;
        this.fromAction = fromAction;
        this.toAction = toAction;
        this.pluginId = pluginId;
    }

    public PushPluginAction(String msgType, String serviceType, String eventAction, Map<String, String> param, String fromAction, String toAction, String pluginId, String mUrl, String mApi) {
        this.msgType = msgType;
        this.serviceType = serviceType;
        this.eventAction = eventAction;
        this.param = param;
        this.fromAction = fromAction;
        this.toAction = toAction;
        this.pluginId = pluginId;
        this.mUrl = mUrl;
        this.mApi = mApi;
    }

    public PushPluginAction(String msgType, String serviceType, String eventAction, Map<String, String> param, String pluginId) {
        this.msgType = msgType;
        this.serviceType = serviceType;
        this.eventAction = eventAction;
        this.param = param;
        this.pluginId = pluginId;
    }

    public PushPluginAction() {
        super();
    }
}
