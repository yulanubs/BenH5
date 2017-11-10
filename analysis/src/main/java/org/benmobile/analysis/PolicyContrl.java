package org.benmobile.analysis;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.benmobile.analysis.component.EventReceiver;

public enum PolicyContrl {
	INSTANCE;
	public static final int POLICY_APP_LAUNCH = 0x01;
	public static final int POLICY_NETWORK_CHANGE = 0x02;
	public static final int POLICY_TIMING = 0x04;
	
	public static final int TIME_INTERVAL = 1*2*1000;
	
	public int policy;
	
	public void setPolicy(int p){
		policy = p;
	}
	
	public int getPolicy(){
		return policy;
	}
	
	public void registerAlarmManager(Context context){
		Intent wakeUp = new Intent(EventReceiver.UPLOAD_LOG);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, wakeUp, 0);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Log.e("AlarmManager","AlarmManager");
		am.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+TIME_INTERVAL, 
				TIME_INTERVAL, pi);
	}
	
	public void unregisterAlarmManager(Context context){
		Intent wakeUp = new Intent(EventReceiver.UPLOAD_LOG);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, wakeUp, 0);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(pi);
	}
}
