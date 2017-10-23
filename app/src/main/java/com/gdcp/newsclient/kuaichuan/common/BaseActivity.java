package com.gdcp.newsclient.kuaichuan.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gdcp.newsclient.R;

import butterknife.ButterKnife;

/**
 * Created by asus- on 2017/10/1.
 */

public class BaseActivity extends AppCompatActivity{
    /**
     * 写文件的请求码
     */
    public static final int  REQUEST_CODE_WRITE_FILE = 200;

    /**
     * 读取文件的请求码
     */
    public static final int  REQUEST_CODE_READ_FILE = 201;

    /**
     * 打开GPS的请求码
     */
    public static final int  REQUEST_CODE_OPEN_GPS = 205;


    Context context;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
    }



    /**
     * 获取上下文
     * @return
     */
    public Context getContext(){
        return context;
    }

    /**
     * 显示对话框
     */
    protected void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setMessage(getResources().getString(R.string.tip_loading));
        progressDialog.show();
    }

    /**
     * 隐藏对话框
     */
    protected void hideProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.hide();
            progressDialog = null;
        }
    }

}
