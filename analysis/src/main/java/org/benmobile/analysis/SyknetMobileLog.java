package org.benmobile.analysis;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.benmobile.analysis.action.ActionMachine;
import org.benmobile.analysis.component.EventReceiver;
import org.benmobile.analysis.database.LogDatabaseHelper;
import org.benmobile.analysis.database.obj.MBLogClick;
import org.benmobile.analysis.database.obj.MBLogCrash;
import org.benmobile.analysis.database.obj.MBLogDevice;
import org.benmobile.analysis.database.obj.MBLogLaunch;
import org.benmobile.analysis.page.PageMachine;
import org.benmobile.analysis.task.BaseTask;
import org.benmobile.analysis.task.InsertDatabaseTask;
import org.benmobile.analysis.task.TaskExecutor;
import org.benmobile.analysis.task.UpLoadDeviceTask;
import org.benmobile.analysis.tools.IntenetUtil;

public class SyknetMobileLog {

	private static final int REQUEST_READ_PHONE_STATE = 0x100;
	public static LogDatabaseHelper mainDatabaseHelper;

	public static boolean DEBUG = true;

	public static void init(Application context, String device_id,
			String market_id, String version, String client_type,
			String device_type, String appkey, String server,String appSecret,String password,byte[] iv) {
		MobileLogConsts.appkey = appkey;
		MobileLogConsts.appSercet = appSecret;
		MobileLogConsts.deviceId = device_id;
		MobileLogConsts.marketId = market_id;
		MobileLogConsts.version = version;
		MobileLogConsts.clientType = client_type;
		MobileLogConsts.deviceType = device_type;
		MobileLogConsts.server = server;
		MobileLogConsts.packageName = context.getPackageName();
		MobileLogConsts.password = password;
		MobileLogConsts.iv = iv;

		mainDatabaseHelper = new LogDatabaseHelper(context);

	}

	public static void onDevice(Activity context, String user_id,
			String push_device_token, String old_version, String lat_lon,
			String uuid) {
		onDevice(context, user_id, push_device_token, old_version, lat_lon,
				uuid, null, false);
	}

	public static void onDevice(Activity context, String user_id,
								String push_device_token, String old_version, String lat_lon,
								String uuid, Handler handler, boolean passive) {
		boolean deviceSuccess = context.getSharedPreferences(
				MobileLogConsts.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
				.getBoolean(MobileLogConsts.DEVICE_SUCCESS, false);
		if (deviceSuccess && passive){
			if (handler != null){
				handler.obtainMessage(BaseTask.TASK_UPLOAD_DEVICE_FINISH).sendToTarget();
			}
			return;
		}

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String	imei = tm.getDeviceId();
		String imsi = tm.getSubscriberId();
		String os = android.os.Build.VERSION.RELEASE;
		String model = android.os.Build.MODEL;
		String brand = android.os.Build.BRAND;
		String sdk_int = String.valueOf(android.os.Build.VERSION.SDK_INT);
		String os_version = android.os.Build.VERSION.RELEASE;
		String fingerPrint = android.os.Build.FINGERPRINT;

		String androidId = Settings.Secure.getString(
				context.getContentResolver(), "android_id");

		int w = context.getResources().getDisplayMetrics().widthPixels;
		int h = context.getResources().getDisplayMetrics().heightPixels;
		String resolution = w + "*" + h;

		WifiManager wm = (WifiManager) context.getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wi = wm.getConnectionInfo();
		String mac = "";
		if (wi != null) {
			mac = wi.getMacAddress();
		}
		if (mac == null) {
			mac = "";
		}

		MBLogDevice data = new MBLogDevice(user_id, push_device_token, os,
				resolution, old_version, imei, imsi, os_version, sdk_int,
				brand, model, mac, lat_lon, uuid, androidId, fingerPrint,
				passive, context);
		UpLoadDeviceTask task = new UpLoadDeviceTask(handler, data, context);
		TaskExecutor.INSTANCE.execute(task);
	}

	/**
	 * 启动页面必须调用，否则不会注册广播
	 * @param context
	 * @param user_id
	 * @param push_device_token
     * @param lat_lon
     */
	public static void onLaunch(Context context, String user_id,
			String push_device_token, String lat_lon) {
		IntenetUtil.updateNetwork(context);

		Intent getTime = new Intent(EventReceiver.GET_TIME);
		context.sendBroadcast(getTime);

		PolicyContrl.INSTANCE.registerAlarmManager(context
				.getApplicationContext());
		MBLogLaunch data = new MBLogLaunch(user_id, push_device_token, lat_lon,
				context);
		InsertDatabaseTask task = new InsertDatabaseTask(null, data);
		TaskExecutor.INSTANCE.execute(task);
		PageMachine.INSTANCE.clear();
		ActionMachine.INSTANCE.clear();

	}

	public static void onPageChange(Context context, String user_id,
			String page, String push_msg_id, String params) {
		IntenetUtil.updateNetwork(context);
		PageMachine.INSTANCE.onNewPage(user_id, page, params, push_msg_id,
				context);
	}

	public  static  void  onCrash(Context context ,String user_id,String pluginId,String exceptionType,String className,String methodName,String lineNumber,String cause,String stackTrace){
		IntenetUtil.updateNetwork(context);
		MBLogCrash data=new MBLogCrash( context , user_id, pluginId, exceptionType, className, methodName, lineNumber, cause, stackTrace);
		InsertDatabaseTask task = new InsertDatabaseTask(null, data);
		TaskExecutor.INSTANCE.execute(task);
	}
	public static void onClick(Context context, String params,
			String module_id, String module_desc, String user_id) {
		IntenetUtil.updateNetwork(context);
		String p = "";
		if (params != null) {
			p = params.toString();
		}
		MBLogClick data = new MBLogClick(user_id, p, module_id, module_desc,
				context);
			if (SyknetMobileLog.DEBUG){
				Log.e("MBLogClick",data.toString());
			}
		InsertDatabaseTask task = new InsertDatabaseTask(null, data);
		TaskExecutor.INSTANCE.execute(task);
		/*
		 * LogDatabaseHelper h = new LogDatabaseHelper(context); h.insert(data);
		 */
	}

	public static void onAction(Context context, String params,
			String action_name, String action_type) {
		IntenetUtil.updateNetwork(context);
		ActionMachine.INSTANCE.action(action_name, action_type, params, 0,
				context);
	}

	public static void onActionStart(Context context, String id, String params,
			String action_name, String action_type) {
		IntenetUtil.updateNetwork(context);
		ActionMachine.INSTANCE.actionStart(action_name, action_type, id,
				params, context);
	}

	public static void onActionEnd(Context context, String id) {
		IntenetUtil.updateNetwork(context);
		ActionMachine.INSTANCE.actionEnd(id, context);
	}

	public static void onAction(Context context, String params,
			String action_name, String action_type, long consume_time) {
		IntenetUtil.updateNetwork(context);
		ActionMachine.INSTANCE.action(action_name, action_type, params,
				consume_time, context);
	}
	
	public static void onAction(Context context,String params,String action_name,String action_type,String network_detail){
		IntenetUtil.updateNetwork(context);
		ActionMachine.INSTANCE.action(action_name, action_type, params,
				network_detail, context);
	}

	public static void onActivityResume() {

	}

	public static void onActivityPause() {

	}
}
