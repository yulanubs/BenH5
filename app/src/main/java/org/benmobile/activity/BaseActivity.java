package org.benmobile.activity;

import android.app.Activity;
import android.os.Bundle;

import org.benmobile.application.BenH5Application;
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
}
