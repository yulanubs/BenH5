package org.benmobile.analysis.page;

import android.content.Context;

import org.benmobile.analysis.database.obj.MBLogOperate;
import org.benmobile.analysis.task.InsertDatabaseTask;
import org.benmobile.analysis.task.TaskExecutor;
import org.benmobile.analysis.time.CurrentTimeProvider;

public enum PageMachine {
	INSTANCE;

	private String currentPage;
	private long attachTime;

	public void onNewPage(String user_id, String page, String params,
			String push_msg_id,Context context) {
		String prePage = "";
		if (currentPage != null) {
			prePage = currentPage;
		}
		long preStayTime = 0;
		long now = CurrentTimeProvider.getCurrentTimeMillis(context);
		if (attachTime != 0) {
			preStayTime = (now - attachTime)/1000;
		}
		if (preStayTime < 5 || preStayTime > 300){
			preStayTime = 5;
		}
		String p = "";
		if (params != null){
			p = params.toString();
		}
		MBLogOperate data = new MBLogOperate(user_id, push_msg_id, page,
				p, prePage, String.valueOf(preStayTime),context);
		InsertDatabaseTask task = new InsertDatabaseTask(null, data);
		TaskExecutor.INSTANCE.execute(task);
		currentPage = page;
		attachTime = now;
	}
	
	public void clear(){
		currentPage = "";
		attachTime = 0;
	}
}
