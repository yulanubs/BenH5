package org.benmobile.analysis.database.obj;

import android.content.Context;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.annotation.DatabaseKey;
import org.benmobile.analysis.database.LogDatabaseHelper;
import org.benmobile.analysis.time.CurrentTimeProvider;
import org.benmobile.analysis.tools.IntenetUtil;

public class MBLogOperate extends BaseDatabaseObj {

	@DatabaseKey(databaseKey = "PUSH_BATCH_ID")
	public String pushBatchId;

	@DatabaseKey(databaseKey = "VIEW_PAGE")
	public String viewPage;

	@DatabaseKey(databaseKey = "PARAMS")
	public String params;

	@DatabaseKey(databaseKey = "PRE_VIEW_PAGE")
	public String preViewPage;

	@DatabaseKey(databaseKey = "PRE_VP_CONSTIME")
	public String preVpConstime;

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
	
	@DatabaseKey(databaseKey = "APPKEY")
	public String appKey;
	
	public MBLogOperate(){
		
	}
	
	public MBLogOperate(String user_id, String push_bacth_id,
			String view_page, String params, String pre_view_page,
			String pre_vp_constime,Context context) {
		super(user_id);
		// TODO Auto-generated constructor stub
		this.pushBatchId = push_bacth_id;
		this.viewPage = view_page;
		this.params = params;
		this.preViewPage = pre_view_page;
		this.preVpConstime = pre_vp_constime;
		this.version = MobileLogConsts.version;
		this.userId = user_id;
		this.network = IntenetUtil.getNetworkType();
		this.networkDetail = IntenetUtil.getNetworkDetail();
		this.deviceType = MobileLogConsts.deviceType;
		this.accessTime = CurrentTimeProvider.getCurrentTime(context);
		this.appKey = MobileLogConsts.appkey;
	}

	@Override
	public String getDatabaseTableName() {
		// TODO Auto-generated method stub
		return LogDatabaseHelper.TABLE_MB_LOG_OPERATE;
	}

}
