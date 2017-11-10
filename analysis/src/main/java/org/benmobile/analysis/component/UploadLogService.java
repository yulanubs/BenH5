package org.benmobile.analysis.component;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.benmobile.analysis.PolicyContrl;
import org.benmobile.analysis.task.BaseTask;
import org.benmobile.analysis.task.TaskExecutor;
import org.benmobile.analysis.task.UploadActionTask;
import org.benmobile.analysis.task.UploadClickTask;
import org.benmobile.analysis.task.UploadCrashTask;
import org.benmobile.analysis.task.UploadLaunchTask;
import org.benmobile.analysis.task.UploadOperateTask;

public class UploadLogService extends Service {

	private boolean isActived = false;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case BaseTask.TASK_UPLOAD_LOG_SUCCESS:
				stopSelf();
				break;

			case BaseTask.TASK_UPLOAD_LOG_EMPTY:
				PolicyContrl.INSTANCE.unregisterAlarmManager(getApplicationContext());
				stopSelf();
				break;
			}
		}
		
	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e("UploadLogService", "onStartCommand isActived: "+isActived);
		if (isActived){
			
		}else{
			isActived = true;
			//Action
			UploadActionTask taskAction = new UploadActionTask(handler);
			TaskExecutor.INSTANCE.execute(taskAction);
			//Operate
			UploadOperateTask taskOperate = new UploadOperateTask(handler);
			TaskExecutor.INSTANCE.execute(taskOperate);
			//Click
			UploadClickTask taskClick = new UploadClickTask(handler);
			TaskExecutor.INSTANCE.execute(taskClick);
			//Crash
			UploadCrashTask taskCrash = new UploadCrashTask(handler);
			TaskExecutor.INSTANCE.execute(taskCrash);
			//Launch
			UploadLaunchTask taskLaunch = new UploadLaunchTask(handler);
			TaskExecutor.INSTANCE.execute(taskLaunch);




		}
		return super.onStartCommand(intent, flags, startId);
	}

	
}
