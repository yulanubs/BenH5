package org.benmobile.activity;

import android.app.Activity;
import android.os.Bundle;

import org.benmobile.MainActivity;
import org.benmobile.application.BenH5Application;
import org.benmobile.core.natvie.app.PluginClient;
import org.benmobile.protocol.utlis.ValueUtils;

/**
 * Created by Jekshow on 2017/3/9.
 */

public class BaseActivity extends Activity {
   protected BenH5Application mApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ValueUtils.isEmpty(mApp)){
        mApp= (BenH5Application) getApplication();
        }
    }

    /**
     * 启动插件
     * @param pluginId
     * @param finishLaunchActivity
     */
    protected  void  launchPlugin(String pluginId,boolean finishLaunchActivity){
        PluginClient.getInstance().launch(pluginId, this, finishLaunchActivity);
    }
}
