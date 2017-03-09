package org.benmobile.protocol.event;

import org.benmobile.coolhttp.http.Response;
import org.benmobile.protocol.Protocol;
import org.benmobile.protocol.bean.ProtocolMsgBean;

/**
 * Created by Jekshow on 2017/3/8.
 * 消息回调接口
 */

public interface GetMsgInfoListener {
    /**
     * 消息成功
     * @param pluignId 插件id
     * @param requestCode 请求码
     * @param resultCode 响应码
     * @param msgBean  数据
     */
    public void  onMsgSucceed(String  pluignId, int requestCode, int resultCode,Protocol  msgBean);

    /**
     * 消息失败
     * @param pluignId
     * @param requestCode
     * @param resultCode
     * @param msgBean
     */
    public void  onMsgFailed(String  pluignId, int requestCode, int resultCode,Protocol  msgBean);

}
