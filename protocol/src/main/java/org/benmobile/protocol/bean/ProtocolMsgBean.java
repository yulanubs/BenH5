package org.benmobile.protocol.bean;

import java.io.Serializable;

/**
 * Created by Jekshow on 2016/12/6.
 * 消息基本实体类
 */

public class ProtocolMsgBean  implements Serializable{
    /**结果状态码*/
    public String returnCode;
    /**结果描述*/
    public  String returnMessage;
    /**结果内容*/
    public  String  data;
    /**请求码*/
    int requestCode;
    /**响应码*/
    int resultCode;

    public ProtocolMsgBean(String returnCode, String returnMessage, String data) {

        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
        this.data = data;
    }

    public ProtocolMsgBean(String returnCode, String returnMessage, String data, int requestCode, int resultCode) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
        this.data = data;
        this.requestCode = requestCode;
        this.resultCode = resultCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public String getData() {
        return data;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ProtocolMsgBean{" +
                "returnCode='" + returnCode + '\'' +
                ", returnMessage='" + returnMessage + '\'' +
                ", data='" + data + '\'' +
                ", requestCode=" + requestCode +
                ", resultCode=" + resultCode +
                '}';
    }
}
