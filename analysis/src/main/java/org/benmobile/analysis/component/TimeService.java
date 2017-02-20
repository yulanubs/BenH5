package org.benmobile.analysis.component;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import org.benmobile.analysis.task.BaseTask;
import org.benmobile.analysis.task.GetTimeTask;
import org.benmobile.analysis.task.TaskExecutor;
import org.benmobile.analysis.time.CurrentTimeProvider;
import org.benmobile.analysis.tools.Logger;

public class TimeService extends Service {

	private Handler h = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case BaseTask.TASK_GET_TIME_FAILURE:
				stopSelf();
				break;

			case BaseTask.TASK_GET_TIME_SUCCESS:
				long now = (Long) msg.obj;
				CurrentTimeProvider.updateTimeOffect(now,TimeService.this);
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
		GetTimeTask t = new GetTimeTask(h);
		TaskExecutor.INSTANCE.execute(t);
		Logger.debug("TimeService", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

}
