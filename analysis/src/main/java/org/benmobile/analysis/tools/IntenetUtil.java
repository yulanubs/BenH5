package org.benmobile.analysis.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 *
 * Created by chengguo on 2016/3/17.
 */
public class IntenetUtil {
    

    public static final int NETWORN_NONE = 0;
    //wifi
    public static final int NETWORN_WIFI = 1;

    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_MOBILE = 5;
    public static final String NULL = "NULL";
    public static final String TYPE_NO = "no";
    private static String NETWORK_DETAIL = TYPE_NO;
    private static String NETWORK = "";

    public static void updateNetwork(Context context){
        int networkState = getNetworkState(context);
        if (networkState==1&&networkState!=5){
            NETWORK="WIFI";
            NETWORK_DETAIL = "WIFI";
        }else if (networkState==0){
            NETWORK=NULL;
        }else {
            NETWORK_DETAIL = "mobile";
            NETWORK=networkState+"G";
        }

    }
    /**
     *
     * @param context
     * @return
     */
    public static int getNetworkState(Context context) {
        //
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        

        if (null == connManager)
            return NETWORN_NONE;
        

        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            NETWORK = NULL;
            NETWORK_DETAIL =  TYPE_NO;
            return NETWORN_NONE;
        }

        

        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORN_WIFI;
                }
        }
        

        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        //2g
                        case TelephonyManager.NETWORK_TYPE_GPRS: //
                        case TelephonyManager.NETWORK_TYPE_CDMA: //
                        case TelephonyManager.NETWORK_TYPE_EDGE: //
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORN_2G;
                        //3g
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: //
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORN_3G;
                        //4g
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORN_4G;
                        default:
                            //
                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                return NETWORN_3G;
                            } else {
                                return NETWORN_MOBILE;
                            }
                    }
                }

        }
        return NETWORN_NONE;
    }
    public static String getNetworkDetail(){
        return NETWORK_DETAIL;
    }
    public static String getNetworkType(){
        return NETWORK;
    }
}


