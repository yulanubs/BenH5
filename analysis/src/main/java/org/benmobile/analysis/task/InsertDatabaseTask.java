package org.benmobile.analysis.task;

import android.os.Handler;

import org.benmobile.analysis.SyknetMobileLog;
import org.benmobile.analysis.database.obj.BaseDatabaseObj;

public class InsertDatabaseTask extends DatabaseTask {

	public InsertDatabaseTask(Handler callback, BaseDatabaseObj obj) {
		super(callback, obj);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doTask() {
		// TODO Auto-generated method stub
		if (SyknetMobileLog.mainDatabaseHelper != null){
			SyknetMobileLog.mainDatabaseHelper.insert(data);
		}
	}

}
