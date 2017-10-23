package com.gdcp.newsclient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.app.APPAplication;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        initView();
        initData();
    }

    protected abstract void initData();


    protected abstract int getLayoutRes();






    protected abstract void initView();

    private Toast mToast;
    protected void showToast(String msg){
        if (mToast==null){
            mToast= Toast.makeText(this, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

    protected void startActivity(boolean isFinish,Class activity){
        Intent intent=new Intent(this,activity);
        startActivity(intent);
        if (isFinish){
            finish();
        }
    }



}
