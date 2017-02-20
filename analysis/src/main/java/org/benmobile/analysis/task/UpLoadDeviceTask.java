package org.benmobile.analysis.task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.SyknetMobileLog;
import org.benmobile.analysis.database.LogDatabaseHelper;
import org.benmobile.analysis.database.obj.MBLogDevice;
import org.benmobile.analysis.http.HttpEntity;
import org.benmobile.analysis.secret.DESSecret;
import org.benmobile.analysis.time.CurrentTimeProvider;
import org.benmobile.analysis.tools.Logger;

public class UpLoadDeviceTask extends BaseTask {

	private MBLogDevice device;
	private Context context;

	public UpLoadDeviceTask(Handler callback, MBLogDevice device_log,
			Context context) {
		super(callback);
		// TODO Auto-generated constructor stub
		this.device = device_log;
		this.context = context;
	}

	@Override
	public void doTask() {
		// TODO Auto-generated method stub

		HttpEntity timeEntity = new HttpEntity(
				MobileLogConsts.UPLOAD_LOG_GET_TIME);
		Map<String, String> dummy = new HashMap<String, String>();
		boolean success = timeEntity.run(dummy);
		String now = "";
		String time = timeEntity.getResult();
		try {
			JSONObject jsn = new JSONObject(time);
			if (jsn.has("sysTime")) {
				now = jsn.getString("sysTime");
				if (CurrentTimeProvider.isDateZero(now)){
					now = CurrentTimeProvider.getCurrrentSystemTime();
				}
			} else {
				success = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Logger.exception(e);
			success = false;
		}
		if (!success || now == null || now.equals("")) {
			sendResult(BaseTask.TASK_UPLOAD_DEVICE_FINISH, "");
			return;
		}

		if (device.updateTime.equals("")) {
			device.passiveTime = now;
		} else {
			device.updateTime = now;
		}

		HttpEntity entity = new HttpEntity(MobileLogConsts.UPLOAD_LOG_URL);
		Map<String, String> params = new HashMap<String, String>();
		JSONObject jsnData = device.toJSONObject();
		JSONObject main = new JSONObject();
		JSONArray ja = new JSONArray();
		ja.put(jsnData);
		try {
			main.put("MB_LOG_DEVICE", ja);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Logger.exception(e);
		}
		String data = main.toString();
		Logger.info("UpLoadDeviceTask", "data: " + data);
		if (data.equals("")) {
			sendResult(BaseTask.TASK_UPLOAD_DEVICE_FINISH, "");
			return;
		}
		/*//for test start
		data = "test";
		//for test end
*/		/*params.put("logData", data);
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
//			log = "";
//		};
		log=data;

		if (log.equals("")) {
			sendResult(BaseTask.TASK_UPLOAD_DEVICE_FINISH, "");
			return;
		}
		params.put("log", log);
		boolean ret = entity.run(params);
		Logger.info("UpLoadDeviceTask", "success: " + ret);
		sendResult(BaseTask.TASK_UPLOAD_DEVICE_FINISH, ret);

		String result = entity.getResult();
		Logger.info("UpLoadDeviceTask", "result: "+result);
		try {
			JSONObject jsonResult = new JSONObject(result);
			String code = jsonResult.getString("returnCode");
			if (code.equals("0")) {
				writeTag();

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Logger.exception(e);
		}
		if (ret){

		}
	}

	private void writeTag() {
		context.getSharedPreferences(MobileLogConsts.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE).edit()
				.putBoolean(MobileLogConsts.DEVICE_SUCCESS, true).commit();
	}

}
