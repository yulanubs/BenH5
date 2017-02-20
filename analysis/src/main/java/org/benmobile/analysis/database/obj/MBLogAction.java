package org.benmobile.analysis.database.obj;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.annotation.DatabaseKey;
import org.benmobile.analysis.database.LogDatabaseHelper;
import org.benmobile.analysis.tools.IntenetUtil;

public class MBLogAction extends BaseDatabaseObj {

	@DatabaseKey(databaseKey = "ACCESS_TIME")
	public String createTime;
	
	@DatabaseKey(databaseKey = "PARAMS")
	public String params;
	
	@DatabaseKey(databaseKey = "ACTION_TYPE")
	public String actionType;
	
	@DatabaseKey(databaseKey = "ACTION_NAME")
	public String actionName;
	
	@DatabaseKey(databaseKey = "CONSUME_TIME")
	public String consumeTime;
	
	@DatabaseKey(databaseKey = "APPKEY")
	public String appkey;
	
	@DatabaseKey(databaseKey = "NETWORK")
	public String network;
	
	@DatabaseKey(databaseKey = "NETWORK_DETAIL")
	public String networkDetail;

	public MBLogAction(){
		
	}
	
	public MBLogAction(String user_id, String create_time,
			String params, String action_type, String action_name,
			String consume_time) {
		super(user_id);
		// TODO Auto-generated constructor stub
		this.createTime = create_time;
		this.params = params;
		this.actionType = action_type;
		this.actionName = action_name;
		this.consumeTime = consume_time;
		this.network = IntenetUtil.getNetworkType();
		this.networkDetail = IntenetUtil.getNetworkDetail();
		this.appkey = MobileLogConsts.appkey;
	}

	@Override
	public String getDatabaseTableName() {
		// TODO Auto-generated method stub
		return LogDatabaseHelper.TABLE_MB_LOG_ACTION;
	}

}
