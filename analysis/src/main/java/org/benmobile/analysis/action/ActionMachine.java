package org.benmobile.analysis.action;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import org.benmobile.analysis.database.obj.MBLogAction;
import org.benmobile.analysis.task.InsertDatabaseTask;
import org.benmobile.analysis.task.TaskExecutor;
import org.benmobile.analysis.time.CurrentTimeProvider;

public enum ActionMachine {
	INSTANCE;
	private Map<String, Action> actions = new HashMap<String, Action>();
	
	public void actionStart(String action_name,String action_type,String id,String params,Context context){
		if (actions.containsKey(id)){
			actions.remove(id);
		}
		
		Action action = new Action();
		action.id = id;
		action.actionName = action_name;
		action.actionType = action_type;
		action.params = params;
		action.startTime = CurrentTimeProvider.getCurrentTimeMillis(context);
		action.createTime = CurrentTimeProvider.getCurrentTime(context);
		actions.put(id, action);
	}
	
	public void actionEnd(String id,Context context) {
		Action action = actions.get(id);
		if (action == null) {
			return;
		}
		action.endTime = CurrentTimeProvider.getCurrentTimeMillis(context);
		uploadAction(action);
		actions.remove(id);
	}
	
	public void actionCancel(String id){
		actions.remove(id);
	}
	
	public void action(String action_name,String action_type,String params,long consume_time,Context context){
		Action action = new Action();
		action.id = "";
		action.actionName = action_name;
		action.actionType = action_type;
		action.params = params;
		action.startTime = 0;
		action.endTime = consume_time;
		action.createTime = CurrentTimeProvider.getCurrentTime(context);
		uploadAction(action);
	}
	
	public void action(String action_name,String action_type,String params,String network_detail,Context context){
		Action action = new Action();
		action.id = "";
		action.actionName = action_name;
		action.actionType = action_type;
		action.params = params;
		action.startTime = 0;
		action.endTime = 0;
		action.createTime = CurrentTimeProvider.getCurrentTime(context);
		uploadAction(action,network_detail);
	}
	
	private void uploadAction(Action action){
		long consumeTime = (action.endTime - action.startTime) / 1000;
		String p = "";
		if (action.params != null){
			p = action.params.toString();
		}
		MBLogAction data = new MBLogAction("", action.createTime,
				p, action.actionType, action.actionName,
				String.valueOf(consumeTime));
		InsertDatabaseTask task = new InsertDatabaseTask(null, data);
		TaskExecutor.INSTANCE.execute(task);
	}
	
	private void uploadAction(Action action,String network_detail){
		long consumeTime = (action.endTime - action.startTime) / 1000;
		String p = "";
		if (action.params != null){
			p = action.params.toString();
		}
		MBLogAction data = new MBLogAction("", action.createTime,
				p, action.actionType, action.actionName,
				String.valueOf(consumeTime));
		if (network_detail != null && !network_detail.equals("")){
			data.networkDetail = network_detail;
		}
		InsertDatabaseTask task = new InsertDatabaseTask(null, data);
		TaskExecutor.INSTANCE.execute(task);
	}
	
	public void clear(){
		actions.clear();
	}
}
