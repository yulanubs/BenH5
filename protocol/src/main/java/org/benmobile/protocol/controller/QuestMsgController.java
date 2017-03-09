package org.benmobile.protocol.controller;


import android.support.annotation.NonNull;
import android.util.Log;

import org.benmobile.coolhttp.http.CoolHttp;
import org.benmobile.coolhttp.http.HttpListener;
import org.benmobile.coolhttp.http.KeyValue;
import org.benmobile.coolhttp.http.Method;
import org.benmobile.coolhttp.http.Response;
import org.benmobile.coolhttp.http.StringRequest;
import org.benmobile.protocol.bean.ProtocolMsgBean;
import org.benmobile.protocol.bean.PushPluginAction;
import org.benmobile.protocol.config.ConstServiceType;
import org.benmobile.protocol.event.GetMsgInfoListener;
import org.benmobile.protocol.event.OnAppNetQuestListener;
import org.benmobile.protocol.utlis.ValueUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jekshow on 2016/12/6.
 * 通信请求控制器
 *
 */

public class QuestMsgController extends    BaseController {

    @Override
    protected void pushPluginAction(PushPluginAction mPPA, int requestCode) {

    }

    @Override
    public void pushNetQuestPluginAction( final String url,final PushPluginAction mPPA, final int requestCode) {

        if (ValueUtils.isNotEmpty(mPPA))if (mPPA.getMsgType().equals(ConstServiceType.INFRASTRUCTURE_SERVICE)){
            if (mPPA.getServiceType().equals(ConstServiceType.Infrastructure.BENH5_APP_NET_FRAME)){
                StringRequest request = getStringRequest(url, mPPA);
                if (ValueUtils.isNotEmpty(request)) {
                    //请求网络
                    CoolHttp.asyncRequest(request, new HttpListener<String>() {
                        @Override
                        public void onSucceed(Response<String> response) {
                            String result = response.getResult();
                            Log.e("CoolHttp", result);
                            resultPluginAction(mPPA.getPluginId(), requestCode, resultCode, new ProtocolMsgBean("0000", "操作成功", result, requestCode, response.getResponseCode()));
                        }

                        @Override
                        public void onFailed(Response<String> response) {
                            Log.e("CoolHttp", "", response.getException());
                            resultFailedPluginAction(mPPA.getPluginId(), requestCode, resultCode, new ProtocolMsgBean(response.getResponseCode()+"", "失败！", null, requestCode, response.getResponseCode()));
                        }
                    });
                }else {
                    //请求参数为空
                }
            }

        }else{
            //自定义控制器，暂时未实现
        }
        else{
            return;
        }

    }

    @NonNull
    private StringRequest getStringRequest(String url, PushPluginAction mPPA) {
        StringRequest request = new StringRequest(url, Method.POST);

        Map<String, String> param = mPPA.getParam();
        if (ValueUtils.isNotEmpty(param)){
            Iterator<String> iterator = param.keySet().iterator();
            try {
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = param.get(key);
                    request.addParams(new KeyValue(key, value));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return request;
    }

    @Override
    protected void pushPluginAction(PushPluginAction mPPA, int requestCode, GetMsgInfoListener msgInfoListener) {

    }

    @Override
    public void pushNetQuestPluginAction(final  String url,final PushPluginAction mPPA, final int requestCode, final OnAppNetQuestListener onAppNetQuestListener) {
        if (ValueUtils.isNotEmpty(mPPA))if (mPPA.getMsgType().equals(ConstServiceType.INFRASTRUCTURE_SERVICE)){
            if (mPPA.getServiceType().equals(ConstServiceType.Infrastructure.BENH5_APP_NET_FRAME)){
                StringRequest request = getStringRequest(url, mPPA);
                if (ValueUtils.isNotEmpty(request)){
                    //请求网络
                    CoolHttp.asyncRequest(request, new HttpListener<String>() {
                        @Override
                        public void onSucceed(Response<String> response) {
                            String result = response.getResult();
                            Log.e("CoolHttp", result);
                            resultPluginAction(mPPA.getPluginId(),requestCode, resultCode, new ProtocolMsgBean("0000", "操作成功", result,requestCode,response.getResponseCode()),onAppNetQuestListener);
                        }

                        @Override
                        public void onFailed(Response<String> response) {
                            Log.e("CoolHttp", "", response.getException());
                            resultFailedPluginAction(mPPA.getPluginId(),requestCode, resultCode, new ProtocolMsgBean(response.getResponseCode()+"", "失败！",null,requestCode,response.getResponseCode()),onAppNetQuestListener);
                        }
                    });
                }else {
                    //请求参数为空
                }


            }

        }else{
            //自定义控制器，暂时未实现
        }else{
            return;
        }

    }
}

