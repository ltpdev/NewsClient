package com.gdcp.newsclient.ui.fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.NewsViewPagerAdapter;
import com.gdcp.newsclient.bean.AddItem;
import com.gdcp.newsclient.event.ItemSortEvent;
import com.gdcp.newsclient.ui.activity.AddItemActivity;
import com.gdcp.newsclient.ui.activity.CategoryActivity;
import com.gdcp.newsclient.ui.activity.SearchActivity;
import com.gdcp.newsclient.utils.XmlParseUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus- on 2017/6/27.
 */

public class MainFragment03 extends Fragment{

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private NewsViewPagerAdapter newsViewPagerAdapter;
    private ImageView add;
    private ArrayList<Fragment> fragments;
    private List<String> titleList;
    private View mView;
    private RelativeLayout rlSearch;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView==null){
            mView= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main03b,container,false);
            initView(mView);
            //EventBus.getDefault().register(this);
        }

        return mView;
    }



    public void initView(View mView) {
        viewPager= (ViewPager) this.mView.findViewById(R.id.viewPager);
        tabLayout= (TabLayout) this.mView.findViewById(R.id.tabLayout);
        add= (ImageView) this.mView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CategoryActivity.class);
                startActivity(intent);
            }
        });
        rlSearch= (RelativeLayout) this.mView.findViewById(R.id.rl_search);
        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        initViewPager();

    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        titleList= new ArrayList<>();
        titleList.add("推荐");
        titleList.add("lol");
        titleList.add("绝地求生");
        titleList.add("王者荣耀");
        titleList.add("炉石传说");
        titleList.add("穿越火线");
        titleList.add("DNF");
        titleList.add("传奇世界");
        LiveFragment fragment01 = new LiveFragment();
        LiveFragment fragment02 = new LiveFragment();
        LiveFragment fragment03 = new LiveFragment();
        LiveFragment fragment04 = new LiveFragment();
        LiveFragment fragment05 = new LiveFragment();
        LiveFragment fragment06 = new LiveFragment();
        LiveFragment fragment07 = new LiveFragment();
        LiveFragment fragment08 = new LiveFragment();
        fragment01.setUrl("http://capi.douyucdn.cn/api/v1/live?&limit=20");
        fragment02.setUrl("http://capi.douyucdn.cn/api/v1/live/1?&limit=20");
        fragment03.setUrl("http://capi.douyucdn.cn/api/v1/live/270?&limit=20");
        fragment04.setUrl("http://capi.douyucdn.cn/api/v1/live/181?&limit=20");
        fragment05.setUrl("http://capi.douyucdn.cn/api/v1/live/2?&limit=20");
        fragment06.setUrl("http://capi.douyucdn.cn/api/v1/live/33?&limit=20");
        fragment07.setUrl("http://capi.douyucdn.cn/api/v1/live/40?&limit=20");
        fragment08.setUrl("http://capi.douyucdn.cn/api/v1/live/355?&limit=20");
        fragments.add(fragment01);
        fragments.add(fragment02);
        fragments.add(fragment03);
        fragments.add(fragment04);
        fragments.add(fragment05);
        fragments.add(fragment06);
        fragments.add(fragment07);
        fragments.add(fragment08);
        newsViewPagerAdapter=new NewsViewPagerAdapter(getChildFragmentManager(),
                fragments, titleList);
        viewPager.setAdapter(newsViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


/*
* //推荐
http://capi.douyucdn.cn/api/v1/live?&limit=20
lol
http://capi.douyucdn.cn/api/v1/live/1?&limit=20
绝地求生
http://capi.douyucdn.cn/api/v1/live/270?&limit=20
//DOTA2
http://capi.douyucdn.cn/api/v1/live/3?&limit=20
//炉石传说
http://capi.douyucdn.cn/api/v1/live/2?&limit=20
//穿越火线
http://capi.douyucdn.cn/api/v1/live/33?&limit=20
//DNF
http://capi.douyucdn.cn/api/v1/live/40?&limit=20
//传奇世界
http://capi.douyucdn.cn/api/v1/live/355?&limit=20
*
*
* */




    @Override
    public void onResume() {
        super.onResume();
    }






}
