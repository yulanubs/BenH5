package org.benmobile.protocol.serviceimpl;

import org.benmobile.protocol.bean.PushPluginAction;
import org.benmobile.protocol.config.ConstServiceType;
import org.benmobile.protocol.event.GetMsgInfoListener;
import org.benmobile.protocol.controller.QuestMsgController;
import org.benmobile.protocol.event.OnAppNetQuestListener;
import org.benmobile.protocol.utlis.ValueUtils;

import java.util.Map;

/**
 * Created by Jekshow on 2017/3/9.
 * 插件网络请求包装类
 */

public class AppNetQuestService {
    /**
     * 推送请求Action对象
     */
    private PushPluginAction mPush;
    /**
     * 回调监听
     */
    private OnAppNetQuestListener mOnAppNetQuestListener;

    /**
     * 实例化一个网络请求服务
     * @param url                域名
     * @param api                api
     * @param eventAction       事件action
     * @param data               参数
     * @param pluginId           插件id
     * @param requestCode        请求码
     * @param onAppNetQuestListener     监听
     */
    public AppNetQuestService(String url,String api,String eventAction, Map<String, String> data, String pluginId, int requestCode, OnAppNetQuestListener onAppNetQuestListener) {
        mPush = new PushPluginAction(ConstServiceType.INFRASTRUCTURE_SERVICE, ConstServiceType.Infrastructure.BENH5_APP_NET_FRAME, eventAction, data, pluginId);
        this.mOnAppNetQuestListener = onAppNetQuestListener;
        if (ValueUtils.isNotEmpty(mOnAppNetQuestListener)) {
            new QuestMsgController().pushNetQuestPluginAction(url+api,mPush, requestCode, mOnAppNetQuestListener);
        } else {
            new QuestMsgController().pushNetQuestPluginAction(url+api,mPush, requestCode);
        }
    }
}
