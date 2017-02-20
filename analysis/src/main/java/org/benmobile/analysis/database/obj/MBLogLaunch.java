package org.benmobile.analysis.database.obj;

import android.content.Context;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.annotation.DatabaseKey;
import org.benmobile.analysis.database.LogDatabaseHelper;
import org.benmobile.analysis.time.CurrentTimeProvider;
import org.benmobile.analysis.tools.IntenetUtil;

public class MBLogLaunch extends BaseDatabaseObj {

	@DatabaseKey(databaseKey = "PUSH_DEVICE_TOKEN")
	public String pushDeviceToken;

	@DatabaseKey(databaseKey = "VERSION")
	public String version;
	
	@DatabaseKey(databaseKey = "ACCESS_TIME")
	public String accessTime;
	
	@DatabaseKey(databaseKey = "DEVICE_TYPE")
	public String deviceType;
	
	@DatabaseKey(databaseKey = "NETWORK")
	public String network;
	
	@DatabaseKey(databaseKey = "NETWORK_DETAIL")
	public String networkDetail;
	
	@DatabaseKey(databaseKey = "USER_ID")
	public String userId;
	
	@DatabaseKey(databaseKey = "LAT_LON")
	public String latLon;
	
	@DatabaseKey(databaseKey = "APPKEY")
	public String appKey;
	
	public MBLogLaunch(){
		
	}
	
	public MBLogLaunch(String user_id, String push_device_token, String lat_lon,Context context) {
		super(user_id);
		// TODO Auto-generated constructor stub
		this.pushDeviceToken = push_device_token;
		this.accessTime = CurrentTimeProvider.getCurrentTime(context);
		this.version = MobileLogConsts.version;
		this.deviceType = MobileLogConsts.deviceType;
		this.userId = user_id;
		this.network = IntenetUtil.getNetworkType();
		this.networkDetail = IntenetUtil.getNetworkDetail();
		this.latLon = lat_lon;
		this.appKey = MobileLogConsts.appkey;
	}

	@Override
	public String getDatabaseTableName() {
		// TODO Auto-generated method stub
		return LogDatabaseHelper.TABLE_MB_LOG_LAUNCH;
	}

}
