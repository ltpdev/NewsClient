package com.gdcp.newsclient.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gdcp.newsclient.R;

public class StartActivity extends BaseActivity {
    private boolean isFirstOpen;
    private SharedPreferences sharedPreferences;

    @Override
    protected void initData() {
        initSP();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);
                enterActivity();
            }
        }).start();
    }

    private void initSP() {
        sharedPreferences = getSharedPreferences("news", Activity.MODE_PRIVATE);
        isFirstOpen = sharedPreferences.getBoolean("isFirstOpen", false);
    }

    private void enterActivity() {
        if (isFirstOpen) {
            startActivity(true, MainActivity.class);
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstOpen", true);
            editor.commit();
            startActivity(true, GuideActivity.class);
        }

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_start;
    }



    @Override
    protected void initView() {

    }


}
