package org.benmobile.analysis.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

import android.os.Handler;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.http.HttpEntity;
import org.benmobile.analysis.time.CurrentTimeProvider;

public class GetTimeTask extends BaseTask {

	public GetTimeTask(Handler callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doTask() {
		// TODO Auto-generated method stub
		HttpEntity entity = new HttpEntity(MobileLogConsts.UPLOAD_LOG_GET_TIME);
		Map<String, String> params = new HashMap<String, String>();
		
		boolean success = entity.run(params);
		long now = 0;
		String time = entity.getResult();
		try {
			JSONObject jsn = new JSONObject(time);
			if (jsn.has("sysTime")){
				String nowTime = jsn.getString("sysTime");
				if (CurrentTimeProvider.isDateZero(nowTime)){
					nowTime = CurrentTimeProvider.getCurrrentSystemTime();
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
				Date date = sdf.parse(nowTime);
				now = date.getTime();
			}else{
				success = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			success = false;
		}
		
		if (success) {
			sendResult(TASK_GET_TIME_SUCCESS, now);
		}else{
			sendResult(TASK_GET_TIME_FAILURE, null);
		}
	}

}
