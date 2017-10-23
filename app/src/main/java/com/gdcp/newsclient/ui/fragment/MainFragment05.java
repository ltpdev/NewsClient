package com.gdcp.newsclient.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.app.APPAplication;
import com.gdcp.newsclient.event.DayNightEvent;
import com.gdcp.newsclient.gson.Weather;
import com.gdcp.newsclient.ui.activity.WeatherActivity;
import com.gdcp.newsclient.utils.DiskLruCacheUtils;
import com.tencent.tauth.Tencent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by asus- on 2017/6/27.
 */

public class MainFragment05 extends Fragment{
    private Switch open;
    private RelativeLayout rl_weather;
    private RelativeLayout rl_clearCache;
    private LinearLayout ll_logout;
    private TextView tv_cache;
    private View mView;
    private AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView==null){
            mView= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main05,container,false);
            initView(mView);
        }
            initTvCache();


        return mView;
    }




    public void initView(View mView) {

        open= (Switch) this.mView.findViewById(R.id.open);
        if(APPAplication.appConfig.isNightTheme()){
            open.setChecked(true);
        }else{
            open.setChecked(false);
        }
        open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeTheme(isChecked);
            }
        });

        rl_weather= (RelativeLayout) this.mView.findViewById(R.id.rl_weather);
        rl_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      Intent intent=new Intent(getActivity(),WeatherActivity.class);
                      startActivity(intent);
            }
        });
        rl_clearCache=(RelativeLayout) this.mView.findViewById(R.id.rl_clearCache);
        rl_clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (builder==null){
                    builder=new AlertDialog.Builder(getActivity());
                }
                builder.setTitle("温馨提示").setMessage("确定要清除缓存吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DiskLruCacheUtils diskLruCacheUtils=new DiskLruCacheUtils(getActivity());
                        diskLruCacheUtils.deleteCache();
                        initTvCache();
                        Toast.makeText(getActivity(), "已成功清除缓存", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消",null).show();

            }
        });
        tv_cache=(TextView) this.mView.findViewById(R.id.tv_cache);
        ll_logout= (LinearLayout) mView.findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tencent mTencent = Tencent.createInstance("1106185205", getActivity().getApplicationContext());
                if (mTencent.isSessionValid()){
                    mTencent.logout(getActivity());
                    Toast.makeText(getActivity(), "成功退出登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }






    private void initTvCache() {
        DiskLruCacheUtils diskLruCacheUtils=new DiskLruCacheUtils(getActivity());
        tv_cache.setText(diskLruCacheUtils.getCacheSize()+"M");
    }

    private void changeTheme(boolean isNight) {
        APPAplication.appConfig.setNightTheme(isNight);
        //APPAplication.setAppNightMode(isNight);
        EventBus.getDefault().post(new DayNightEvent(isNight));
    }



}
