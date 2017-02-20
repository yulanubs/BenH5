package org.benmobile.analysis.database.obj;

import android.content.Context;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.annotation.DatabaseKey;
import org.benmobile.analysis.database.LogDatabaseHelper;
import org.benmobile.analysis.time.CurrentTimeProvider;
import org.benmobile.analysis.tools.IntenetUtil;

/**
 * Created by Jekshow on 2017/1/6.
 */

public class MBLogCrash extends  BaseDatabaseObj {

    @DatabaseKey(databaseKey = "ACCESS_TIME")
    public String accessTime;
    @DatabaseKey(databaseKey = "OS")
    public String os;
    @DatabaseKey(databaseKey = "RESOLUTION")
    public String resolution;
    @DatabaseKey(databaseKey = "OS_VERSION")
    public String osVersion;
    @DatabaseKey(databaseKey = "SDK_INT")
    public String sdkInt;
    @DatabaseKey(databaseKey = "BRAND")
    public String brand;
    @DatabaseKey(databaseKey = "MODEL")
    public String model;
    @DatabaseKey(databaseKey = "VERSION")
    public String version;
    @DatabaseKey(databaseKey = "DEVICE_TYPE")
    public String deviceType;
    @DatabaseKey(databaseKey = "USER_ID")
    public String userId;
    @DatabaseKey(databaseKey = "APPKEY")
    public String appKey;
    @DatabaseKey(databaseKey = "NETWORK")
    public String network;
    @DatabaseKey(databaseKey = "NETWORK_DETAIL")
    public String networkDetail;
    @DatabaseKey(databaseKey = "PLUGIN_ID")
    public String  pluginId;
    @DatabaseKey(databaseKey = "EXCEPTION_TYPE")
    public String  exceptionType;
    @DatabaseKey(databaseKey = "CLASS_NAME")
    public String className;
    @DatabaseKey(databaseKey = "METHOD_NAME")
    public String methodName;
    @DatabaseKey(databaseKey = "LINE_NUMBER")
    public  String lineNumber;
    /***/
    @DatabaseKey(databaseKey = "CAUSE")
    public  String cause;
    @DatabaseKey(databaseKey = "STACK_TRACE")
    public  String stackTrace;


    public MBLogCrash() {

    }
    public MBLogCrash(Context context, String user_id, String pluginId, String exceptionType, String className, String methodName, String lineNumber, String cause, String stackTrace) {
        super(user_id);
        this.os = android.os.Build.VERSION.RELEASE;
        int w = context.getResources().getDisplayMetrics().widthPixels;
        int h = context.getResources().getDisplayMetrics().heightPixels;
        String resolution = w + "*" + h;
        this.resolution=resolution;
        this.model = android.os.Build.MODEL;
        this.brand = android.os.Build.BRAND;
        this.sdkInt = String.valueOf(android.os.Build.VERSION.SDK_INT);
        this.osVersion = android.os.Build.VERSION.RELEASE;
        this.version = MobileLogConsts.version;
        this.deviceType = MobileLogConsts.deviceType;
        this.userId = user_id;
        this.network = IntenetUtil.getNetworkType();
        this.networkDetail = IntenetUtil.getNetworkDetail();
        this.accessTime = CurrentTimeProvider.getCurrentTime(context);
        this.appKey = MobileLogConsts.appkey;
        this.pluginId=pluginId;
        this.exceptionType=exceptionType;
        this.className=className;
        this.methodName=methodName;
        this.lineNumber=lineNumber;
        this.cause=cause;
        this.stackTrace=stackTrace;


    }
    @Override
    public String getDatabaseTableName() {
        return LogDatabaseHelper.TABLE_MB_LOG_CRASH;
    }
}
