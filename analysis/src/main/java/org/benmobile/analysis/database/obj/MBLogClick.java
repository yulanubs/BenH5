package org.benmobile.analysis.database.obj;

import android.content.Context;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.annotation.DatabaseKey;
import org.benmobile.analysis.database.LogDatabaseHelper;
import org.benmobile.analysis.time.CurrentTimeProvider;
import org.benmobile.analysis.tools.IntenetUtil;

public class MBLogClick extends BaseDatabaseObj {

	@DatabaseKey(databaseKey = "ACCESS_TIME")
	public String createTime;

	@DatabaseKey(databaseKey = "PARAMS")
	public String params;

	@DatabaseKey(databaseKey = "MODULE_ID")
	public String moduleId;

	@DatabaseKey(databaseKey = "MODULE_DESC")
	public String moduleDesc;

	@DatabaseKey(databaseKey = "NETWORK")
	public String network;
	
	@DatabaseKey(databaseKey = "NETWORK_DETAIL")
	public String networkDetail;
	
	@DatabaseKey(databaseKey = "APPKEY")
	public String appkey;
	
	public MBLogClick(){
		
	}
	
	public MBLogClick(String user_id, String params, String module_id, String module_desc,Context context) {
		super(user_id);
		// TODO Auto-generated constructor stub
		this.createTime = CurrentTimeProvider.getCurrentTime(context);
		this.params = params;
		this.moduleId = module_id;
		this.moduleDesc = module_desc;
		this.appkey = MobileLogConsts.appkey;
		this.network = IntenetUtil.getNetworkType();
		this.networkDetail = IntenetUtil.getNetworkDetail();
	}

	@Override
	public String getDatabaseTableName() {
		// TODO Auto-generated method stub
		return LogDatabaseHelper.TABLE_MB_LOG_CLICK;
	}

}
