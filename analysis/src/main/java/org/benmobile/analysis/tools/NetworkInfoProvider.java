package org.benmobile.analysis.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkInfoProvider {

	/*public static final String TYPE_WIFI = "wifi";
	public static final String TYPE_MOBILE = "mobile";
	public static final String TYPE_MOBILE_DUN = "mobile_dun";
	public static final String TYPE_MOBILE_HIPRI = "mobile_hipri";
	public static final String TYPE_MOBILE_MMS = "mobile_mms";
	public static final String TYPE_MOBILE_SUPL = "mobile_supl";
	public static final String TYPE_WIMAX = "wimax";*/
	public static final String TYPE_NO = "no";
	
	public static final String WIFI = "WIFI";
	public static final String MOBILE = "2G/3G";
	public static final String NULL = "NULL";
	
	private static String NETWORK_DETAIL = TYPE_NO;
	private static String NETWORK = "";
	
	public static void updateNetwork(Context context){
		
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null){
			NETWORK = NULL;
			NETWORK_DETAIL =  TYPE_NO;
			return ;
		}
		if (ni.getType() == ConnectivityManager.TYPE_WIFI){
			NETWORK =  WIFI;
		}else if (ni.getType() == ConnectivityManager.TYPE_MOBILE){
			NETWORK = MOBILE;
		}else if (ni.getType() == ConnectivityManager.TYPE_WIMAX){
			NETWORK = WIFI;
		}else if(ni.getType() == ConnectivityManager.TYPE_MOBILE_DUN){
			NETWORK = MOBILE;
		}else if (ni.getType() == ConnectivityManager.TYPE_MOBILE_HIPRI){
			NETWORK = MOBILE;
		}else if (ni.getType() == ConnectivityManager.TYPE_MOBILE_MMS){
			NETWORK = MOBILE;
		}else if (ni.getType() == ConnectivityManager.TYPE_MOBILE_SUPL){
			NETWORK = MOBILE;
		}
		NETWORK_DETAIL = ni.getTypeName();
		
	}
	
	public static String getNetworkType(){
		return NETWORK;
	}
	
	public static String getNetworkDetail(){
		return NETWORK_DETAIL;
	}
}
