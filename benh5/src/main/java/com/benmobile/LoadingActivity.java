package com.benmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.benh5.R;

/**
 *LoadingActivity
 * 启动Activity
 */
public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    LoadingActivity.this.startActivity(new Intent(LoadingActivity.this,MainActivity.class));
                    LoadingActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
