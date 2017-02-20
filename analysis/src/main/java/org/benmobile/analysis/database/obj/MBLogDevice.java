package org.benmobile.analysis.database.obj;

import android.content.Context;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.annotation.DatabaseKey;
import org.benmobile.analysis.time.CurrentTimeProvider;

public class MBLogDevice extends BaseDatabaseObj {

	@DatabaseKey(databaseKey = "PUSH_DEVICE_TOKEN")
	public String pushDeviceToken;

	@DatabaseKey(databaseKey = "OS")
	public String os;

	@DatabaseKey(databaseKey = "RESOLUTION")
	public String resolution;

	@DatabaseKey(databaseKey = "ACCESS_TIME")
	public String updateTime;

	@DatabaseKey(databaseKey = "OLD_VERSION")
	public String oldVersion;

	@DatabaseKey(databaseKey = "IMEI")
	public String imei;

	@DatabaseKey(databaseKey = "IMSI")
	public String imsi;

	@DatabaseKey(databaseKey = "OS_VERSION")
	public String osVersion;

	@DatabaseKey(databaseKey = "SDK_INT")
	public String sdkInt;

	@DatabaseKey(databaseKey = "BRAND")
	public String brand;

	@DatabaseKey(databaseKey = "MODEL")
	public String model;

	@DatabaseKey(databaseKey = "MAC")
	public String mac;

	@DatabaseKey(databaseKey = "VERSION")
	public String version;

	@DatabaseKey(databaseKey = "DEVICE_TYPE")
	public String deviceType;

	@DatabaseKey(databaseKey = "USER_ID")
	public String userId;

	@DatabaseKey(databaseKey = "APPKEY")
	public String appkey;

	@DatabaseKey(databaseKey = "LAT_LON")
	public String latLon;

	@DatabaseKey(databaseKey = "UUID")
	public String uuid;

	@DatabaseKey(databaseKey = "ANDROID_ID")
	public String androidId;

	@DatabaseKey(databaseKey = "FINGER_PRINT")
	public String fingerPrint;
	
	@DatabaseKey(databaseKey = "PASSIVE_TIME")
	public String passiveTime;

	public MBLogDevice() {

	}

	public MBLogDevice(String user_id, String push_device_token, String os,
			String resolution, String old_version, String imei, String imsi,
			String os_version, String sdk_int, String brand, String model,
			String mac, String lat_lon, String uuid, String android_id,
			String finger_print,boolean passive,Context context) {
		super(user_id);
		// TODO Auto-generated constructor stub
		this.pushDeviceToken = push_device_token;
		this.os = os;
		this.resolution = resolution;
		if (passive){
			this.updateTime = "";
			this.passiveTime = CurrentTimeProvider.getCurrentTime(context);
		}else{
			this.updateTime = CurrentTimeProvider.getCurrentTime(context);
			this.passiveTime = "";
		}
		
		this.oldVersion = old_version;
		this.imei = imei;
		this.imsi = imsi;
		this.osVersion = os_version;
		this.sdkInt = sdk_int;
		this.brand = brand;
		this.model = model;
		this.mac = mac;
		this.version = MobileLogConsts.version;
		this.deviceType = MobileLogConsts.deviceType;
		this.userId = user_id;
		this.appkey = MobileLogConsts.appkey;
		this.latLon = lat_lon;
		this.uuid = uuid;
		this.androidId = android_id;
		this.fingerPrint = finger_print;
	}

	@Override
	public String getDatabaseTableName() {
		// TODO Auto-generated method stub
//		return LogDatabaseHelper.TABLE_MB_LOG_DEVICE;
		return "";
	}

}
