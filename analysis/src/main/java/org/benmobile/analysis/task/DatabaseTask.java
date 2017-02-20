package org.benmobile.analysis.task;

import android.os.Handler;

import org.benmobile.analysis.database.obj.BaseDatabaseObj;

public abstract class DatabaseTask extends BaseTask {

	protected BaseDatabaseObj data;
	
	public DatabaseTask(Handler callback,BaseDatabaseObj obj) {
		super(callback);
		// TODO Auto-generated constructor stub
		this.data = obj;
	}

}
