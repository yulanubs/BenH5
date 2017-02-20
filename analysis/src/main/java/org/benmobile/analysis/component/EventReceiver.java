package org.benmobile.analysis.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.benmobile.analysis.tools.Logger;

public class EventReceiver extends BroadcastReceiver {

	public static final String GET_TIME = "com.syknet.mobilelog.log.GET_TIME";
	public static final String UPLOAD_LOG = "com.syknet.mobilelog.log.UPLOAD";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(GET_TIME)){
			getTime(context);
		}else if(intent.getAction().equals(UPLOAD_LOG)){
			upload(context);
		}else if(intent.getAction().equals(Intent.ACTION_TIME_CHANGED)){
			getTime(context);
		}else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
			networkChange(context);
		}
		Logger.info("EventReceiver", intent.getAction());
	}
	
	private void getTime(Context context){
		Intent startService = new Intent(context, TimeService.class);
		context.startService(startService);
	}
	
	private void upload(Context context){
		Intent startService = new Intent(context, UploadLogService.class);
		context.startService(startService);
	}
	
	private void networkChange(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null){
			upload(context);
		}
	}

}
