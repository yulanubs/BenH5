package org.benmobile.analysis.task;

import android.os.Handler;

public abstract class BaseTask implements Runnable {

	public static final int TASK_GET_TIME_SUCCESS = 0x100;
	public static final int TASK_GET_TIME_FAILURE = 0x101;
	public static final int TASK_UPLOAD_LOG_SUCCESS = 0x200;
	public static final int TASK_UPLOAD_LOG_FAILURE = 0x201;
	public static final int TASK_UPLOAD_LOG_EMPTY = 0x202;
	public static final int TASK_UPLOAD_DEVICE_FINISH = 0x300;
	
//	public static final int 
	
	private Handler mh;
	
	public BaseTask(Handler callback){
		this.mh = callback;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		doTask();
	}

	public abstract void doTask();
	
	protected void sendResult(int msg_id,Object result){
		if (mh != null){
			mh.obtainMessage(msg_id,result).sendToTarget();
		}
	}
}
