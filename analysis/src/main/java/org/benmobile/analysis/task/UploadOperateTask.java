package org.benmobile.analysis.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.SyknetMobileLog;
import org.benmobile.analysis.database.LogDatabaseHelper;
import org.benmobile.analysis.database.obj.BaseDatabaseObj;
import org.benmobile.analysis.database.obj.MBLogOperate;
import org.benmobile.analysis.http.HttpEntity;
import org.benmobile.analysis.tools.ValueUtils;

public class UploadOperateTask extends BaseTask {
	
	private static final String TAG = "UploadLogTask";

	private ArrayList<String> operateIds = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> ids = new HashMap<String, ArrayList<String>>();
	
	public UploadOperateTask(Handler callback) {
		super(callback);
		// TODO Auto-generated constructor stub
		ids.put(MBLogOperate.class.getName(), operateIds);
	}

	@Override
	public void doTask() {
		// TODO Auto-generated method stub
		HttpEntity entity = new HttpEntity(MobileLogConsts.UPLOAD_LOG_URL);
		Map<String, String> params = new HashMap<String, String>();
		String data = loadLogFromDatabase();
		if (SyknetMobileLog.DEBUG){
			Log.e("UploadOpertateTask", "data: "+data);
		}
//		try {
//			@SuppressWarnings("unused")
//			JSONObject temp = new JSONObject(data);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.e(TAG, e.getMessage());
//			data = "";
//		}
		if (ValueUtils.isStrEmpty(data)){
			sendResult(TASK_UPLOAD_LOG_EMPTY, null);
			return;
		}
		/**not user after 20140725 by dyj*/
		/*params.put("logData", data);
		String temp = "logData="+data;
		String sign = HttpEntity.sign(temp);
		params.put("sign", sign);
		params.put("signType", "2");*/
		
		params.put("sign", "1");
		
		String log = "";
//		try {
//			log = DESSecret.encryptDES(data, MobileLogConsts.password, MobileLogConsts.iv);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.e(TAG, e.getMessage());
//			log = "";
//
//		};
		log=data;
		if(log.equals("")){
			sendResult(TASK_UPLOAD_LOG_EMPTY, null);
			return;
		}
		params.put("log", log);
		boolean ret = entity.run(params);
		String result = entity.getResult();
		if (ValueUtils.isStrNotEmpty(result))
		{
			resultShow(result);
		}
		if (ret){
			deleteLogs();
		}
		sendResult(TASK_UPLOAD_LOG_SUCCESS, entity.getResult());
	}
	
	private String loadLogFromDatabase() {
		if (SyknetMobileLog.mainDatabaseHelper != null) {
			List<MBLogOperate> logOperate = SyknetMobileLog.mainDatabaseHelper
					.readAll(LogDatabaseHelper.TABLE_MB_LOG_OPERATE,
							MBLogOperate.class);
			JSONObject log = new JSONObject();
			try {
				if (ValueUtils.isListEmpty(logOperate))
				{return "";}
				log.put(LogDatabaseHelper.TABLE_MB_LOG_OPERATE, logToString(logOperate));
				return log.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return "";
			}
			
		}else{
			return "";
		}
	}
	
	private <T extends BaseDatabaseObj>JSONArray logToString(List<T> logs){
		JSONArray ja = new JSONArray();
		
		for(int i=0;i<logs.size();i++){
			T log = logs.get(i);
			ArrayList<String> specificIds = ids.get(log.getClass().getName());
			specificIds.add(String.valueOf(log.id));
			JSONObject jsn = log.toJSONObject();
			ja.put(jsn);
		}
		return ja;
	}
	
	private void deleteLogs(){
		if (SyknetMobileLog.mainDatabaseHelper != null){
			SyknetMobileLog.mainDatabaseHelper.delete(LogDatabaseHelper.TABLE_MB_LOG_OPERATE, operateIds);
		}
	}
	
}
