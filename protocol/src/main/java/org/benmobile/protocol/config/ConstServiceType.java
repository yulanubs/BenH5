package org.benmobile.protocol.config;

/**
 * Created by Jekshow on 2016/12/6.
 * 服务类型常量类
 */

public class ConstServiceType
{
    /**消息服务类型*/
    public  static  final  String MSG_SERVICE="msgservice";
    /**业务类型*/
    public  static  final  String BUSINESS_SERVICE="business_service";
    /**基础设施，一些基础的功能，网络、加解密，二维码、工具等*/
    public  static  final  String INFRASTRUCTURE_SERVICE="infrastructure_service";

    //消息服务，主要用于应用内部或者插件之间的传值
    public static class  Msg{

    }

    /**
     * 业务服务，主要用于相关事件和相关业务请求，链接网络，跳转页面等。
     */
    public static  class Business{



    }

    /**
     * 基础设施服务，基础的功能，网络、加解密，二维码、工具等
     */
    public static  class  Infrastructure{
        /**网络模块注册配置信息*/
        public  static  final  String BENH5_APP_NET_FRAME="BenH5AppNetFrame";

    }
}
