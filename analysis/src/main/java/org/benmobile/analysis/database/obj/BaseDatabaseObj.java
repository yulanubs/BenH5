package org.benmobile.analysis.database.obj;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.annotation.DatabaseKey;

public abstract class BaseDatabaseObj {

	public int id;
	
	@DatabaseKey(databaseKey = "DEVICE_ID")
	public String deviceId;
	
	@DatabaseKey(databaseKey = "MARKET_ID")
	public String marketId;
	
	/*@DatabaseKey(databaseKey = "VERSION")
	public String version;
	
	@DatabaseKey(databaseKey = "ACCESS_TIME")
	public String accessTime;*/
	
	@DatabaseKey(databaseKey = "CLIENT_TYPE")
	public String clientType;
	
	/*@DatabaseKey(databaseKey = "DEVICE_TYPE")
	public String deviceType;*/
	
	/*@DatabaseKey(databaseKey = "NETWORK")
	public String network;
	
	@DatabaseKey(databaseKey = "NETWORK_DETAIL")
	public String networkDetail;
	
	@DatabaseKey(databaseKey = "USER_ID")
	public String userId;
	
	@DatabaseKey(databaseKey = "APPKEY")
	public String appkey;*/
	
	public BaseDatabaseObj(String user_id) {
		this.deviceId = MobileLogConsts.deviceId;
		this.marketId = MobileLogConsts.marketId;
//		this.version = MobileLogConsts.version;
		this.clientType = MobileLogConsts.clientType;
//		this.deviceType = MobileLogConsts.deviceType;
//		this.userId = user_id;
//		this.appkey = MobileLogConsts.appkey;
//		this.accessTime = CurrentTimeProvider.getCurrentTime();
//		this.network = NetworkInfoProvider.getNetworkType();
//		this.networkDetail = NetworkInfoProvider.getNetworkDetail();
	}
	
	public BaseDatabaseObj(){
		
	}
	
	public JSONObject toJSONObject(){
		JSONObject jsn = new JSONObject();
		Field[] fs = getClass().getFields();
		for (int i=0;i<fs.length;i++){
			Field f = fs[i];
			DatabaseKey a = f.getAnnotation(DatabaseKey.class);
			if (a == null){
				continue;
			}
			String key = a.databaseKey();
			try {
				jsn.put(key, f.get(this));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsn;
	}
	
	public abstract String getDatabaseTableName();
	
}
