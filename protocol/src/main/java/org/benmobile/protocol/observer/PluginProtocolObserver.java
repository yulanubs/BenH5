package org.benmobile.protocol.observer;


import org.benmobile.protocol.ComponentRegister;
import org.benmobile.protocol.service.OnProtocolObserverListener;

/**
 * Created by Jekshow on 2016/12/8.
 * 中间件消息观察者
 */

public class PluginProtocolObserver   implements MyObserver {
    private  int i=0;
    private  String  tag;
    //被观察者
 private ComponentRegister mComponentRegister;
    private OnProtocolObserverListener mOnProtocolObserverListener;
    public PluginProtocolObserver(String tag) {
       this. tag=tag;
    }

    public void setmOnProtocolObserverListener(OnProtocolObserverListener mOnProtocolObserverListener) {
        this.mOnProtocolObserverListener = mOnProtocolObserverListener;
    }

    public OnProtocolObserverListener getmOnProtocolObserverListener() {
        return mOnProtocolObserverListener;
    }

    public ComponentRegister getmComponentRegister() {
        return mComponentRegister;
    }

    public void setmComponentRegister(ComponentRegister mComponentRegister) {
        this.mComponentRegister = mComponentRegister;
    }

    @Override
    public void update(MyObservable observable, Object data) {
        this.mComponentRegister = (ComponentRegister) observable;
        mOnProtocolObserverListener.setPluginMsg((String) data);
        System.out.println("===="+(String)data+"编号："+(++i));
    }

    @Override
    public String getPluginId() {
        return tag;
    }
}



