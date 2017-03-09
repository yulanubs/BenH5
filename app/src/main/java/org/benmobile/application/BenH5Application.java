package org.benmobile.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.benmobile.BuildConfig;
import org.benmobile.R;
import org.benmobile.config.Configs;
import org.benmobile.config.ConstData;
import org.benmobile.coolhttp.http.CoolHttp;
import org.benmobile.coolhttp.http.execute.URLConnectionExecutor;
import org.benmobile.core.natvie.app.PluginClient;
import org.benmobile.core.natvie.config.PluginConfig;
import org.benmobile.core.natvie.log.Logcat;
import org.benmobile.core.natvie.log.SLogger;
import org.benmobile.device.MyDeviceManager;
import org.benmobile.entity.app.AppInfo;
import org.benmobile.log.BenH5Log;
import org.benmobile.protocol.ComponentRegister;
import org.benmobile.utils.DIdUtil;


/**
 * Created by Jekshow on 2017/2/27.
 * App的入口，主要作用提供公共的处理，相关组件的初始化
 */

public class BenH5Application extends Application {
    /**AppId*/
    public final String   appid="10002";
    /**日志开关*/
    private boolean debug = true;
    /**测试环境开关,false为Beta环境，true为生产环境*/
    public boolean isBetaorPrd = true;
    /**APP配置文件*/
    public Configs configs;
    /**组件配置文件*/
    public PluginConfig config;
    /**包对象*/
    public PackageInfo packageinfo = null;
    /**设备管理器*/
    public MyDeviceManager dev = null;
    /**AndroidID*/
    public static String BenH5_ANDROID_ID = "";
    /**BenH5标识*/
    public final static String BENH5_PREFERENCES = "benh5";
    /**APP信息*/
    public AppInfo appinfo = null;
    /**UUID*/
    public static final String UUID = "UUID";
    /**AppKey*/
    public   static String appkey = "2xV107Po78T9fdi96Ryoq5Su2LunJ8L2";
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化组件
        initApp();

    }

    /**
     * 初始化应用组件
     */
    private void initApp() {
        //初始化Log
        initLog();
        //初始化App配置
        initConfigs();
        //初始化Plugin配置
        initPlugin();
        //初始化包
        initPackageInfo();
        //初始化设备
        initDeviceInfo();
        //AppInfo
        initAPPInfo();
        //事件日志
       initMobileLog();
       //初始化中间件
        initProtocol();



    }

    /**
     * initProtocol
     * 初始化中间中间件
     */
    private void initProtocol() {
        ComponentRegister.getInstance().initialize(this, new CoolHttp.HttpConfig()
                .setConnectionTimeout(10 * 1000)
                .setReadTimeout(20 * 1000)
                .setHttpExecutor(URLConnectionExecutor.getInstance()));
    }

    /**
     * 初始化手机日志
     */
    private void initMobileLog() {

    }

    private void initAPPInfo() {
        appinfo = new AppInfo("apr_2017_build01",

                packageinfo.versionName, this.getSessionID(),
                getSharedPreferences(UUID), appkey, "", configs.marketId,
                "ule");

    }

    /**
     * 初始化设备信息
     */
    private void initDeviceInfo() {
        {
            dev = new MyDeviceManager();
            dev.init(getApplicationContext());
            // add by xqq
            dev.deviceInfo.setSessionID(getSessionID());
            dev.deviceInfo.setAppVersionName(packageinfo.versionName);
            dev.deviceInfo.setUUID(getcachedUUID());
            dev.refrashstoragestatic(this);
        }

    }

    /**
     * 获取UUID
     * @return
     */
    public String getcachedUUID() {
        try {
            SharedPreferences sp = getSharedPreferences(
                    BENH5_PREFERENCES, 0);
            return sp.getString("UUID", "");
        } catch (Exception e) {
            BenH5Log.error(MyDeviceManager.class.toString(), e.toString());
            return "";
        }
    }

    /**
     * 保存值到SP中
     * @param key
     * @param value
     */
    public void setSharedPreferences(String key, String value) {
        try {
            SharedPreferences sp = this.getSharedPreferences(
                    BENH5_PREFERENCES, 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, value);

            editor.commit();
        } catch (Exception e) {
            BenH5Log.error(this.toString(), e.toString());
        }
    }
    /**
     * 获取SP中的值
     * @param key key
     * @return  value
     */
    public String getSharedPreferences(String key) {
        try {
            SharedPreferences sp = this.getSharedPreferences(
                    BENH5_PREFERENCES, 0);
            return sp.getString(key, "");

        } catch (Exception e) {
            BenH5Log.error(this.toString(), e.toString());
        }
        return "";
    }

    /**
     * 获取SessionId
     *
     * @return AndroidId
     */
    public String getSessionID() {
        try {
            if (BenH5_ANDROID_ID == null) {
                BenH5_ANDROID_ID = DIdUtil.getInstance(this).getDId();

            }
        } catch (Exception e) {
            e.printStackTrace();
            BenH5Log.error("ddddddddddddddddddddddddddd", e.toString());
            return BenH5_ANDROID_ID;
        }


        return BenH5_ANDROID_ID;
    }
    /**
     * 初始化包信息
     */
    private void initPackageInfo() {
        PackageManager pm = getPackageManager();
        try {
            packageinfo = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            BenH5Log.excaption(e);
            packageinfo = null;
        }
    }

    /**
     * 初始化Log
     */
    private void initLog() {
        boolean mIsPrintLog[]={debug, debug, debug, debug, debug, debug, debug, debug, debug, debug};
        //设置TAG以及是否打印
        BenH5Log.setEnable("POST_STORE", mIsPrintLog[0], mIsPrintLog[1], mIsPrintLog[2], mIsPrintLog[3], mIsPrintLog[4], mIsPrintLog[5], mIsPrintLog[6], mIsPrintLog[7], mIsPrintLog[8], mIsPrintLog[9]);
    }

    /**
     * 初始化Plugin配置
     */
    private void initPlugin() {
       //1.初始化配置
        initPluginConfigs();
        //2.初始化PluginClient
        PluginClient.init(this);

    }
    /**初始化PluginConfig*/
    private void initPluginConfigs() {
        if (BuildConfig.DEBUG) {
            Logcat.getInstance(this).start();
            SLogger.setDefaultLevel(SLogger.levelDebug);
            config = new PluginConfig();
            config.hostUrl = configs.SERVER_BENH5_VPS;
            //是否调用预置的插件
//            config.copyAsset = true;
            config.isDebug = true;
            //0.全部，根据主项目的AppId查询所有插件,1.根据某个插件的id,查询插件信息,2.根据某个插件的appProject，查询插件信息
            config.flageType="2";
            //插件更新接口
            config.CHECK_UPDATEAPPPLUGIN= ConstData.check_updateappplugin;
            config.APP_ID=appid;
            //设置配置
            PluginClient.setConfig(config);
        }
    }

    /**
     * 初始化App配置
     */
    private void initConfigs() {
        {
            configs = new Configs();
            configs.marketId = this.getResources().getString(R.string.marketId);
            configs.SERVER_BENH5_PUSH = this.getResources()
                    .getString(R.string.SERVER_BENH5_PUSH).trim();
            if (isBetaorPrd) {
                //生产环境
                configs.SERVER_BENH5_VPS = this.getResources().getString(
                        R.string.SERVER_PRD_BENH5_VPS).trim();
                configs.SERVER_BENH5_ALTERNATE_VPS = this.getResources()
                        .getString(R.string.SERVER_PRD_BENH5_ALTERNATE_VPS).trim();

            } else {
                //Beta环境
                configs.SERVER_BENH5_VPS = this.getResources().getString(
                        R.string.SERVER_BETA_BENH5_VPS);
                configs.SERVER_BENH5_ALTERNATE_VPS = this.getResources()
                        .getString(R.string.SERVER_BETA_BENH5_ALTERNATE_VPS).trim();

            }

            configs.UPDATE_KEY = this.getResources().getString(R.string.UPDATE_KEY);


        }

    }
}
