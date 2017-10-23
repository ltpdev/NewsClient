package com.gdcp.newsclient.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.NewsViewPagerAdapter;
import com.gdcp.newsclient.bean.AddItem;
import com.gdcp.newsclient.bean.NewsBean;
import com.gdcp.newsclient.manager.URLManager;
import com.gdcp.newsclient.ui.activity.AddItemActivity;
import com.gdcp.newsclient.utils.XmlParseUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by asus- on 2017/6/27.
 */

public class MainFragment01 extends Fragment{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private NewsViewPagerAdapter newsViewPagerAdapter;
    private ImageView add;
    private ArrayList<Fragment> fragments;
    private List<String>titleList;
    private View mView;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView==null){
            mView= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main01,container,false);
            initView(mView);

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
                   Intent intent=new Intent(getActivity(), AddItemActivity.class);
                    startActivityForResult(intent,1000);
            }
        });
        initViewPager();

    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        titleList= new ArrayList<>();
        getData();
        newsViewPagerAdapter=new NewsViewPagerAdapter(getChildFragmentManager(),
                fragments, titleList);
        viewPager.setAdapter(newsViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }



   /*T1368497029546  历史
     T1348648141035  军事
     T1348648517839  娱乐
     T1348654151579  游戏
     T1351233117091  智能
T1348649654285   手机

   *
   *
   *
   *
   *
   *
   *
   * */


    public void getData(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("addItemList", Context.MODE_PRIVATE);
        String json=sharedPreferences.getString("addItemList","");
        //增加判断json是否为空
        if (!json.equals("")){
            Gson gson=new Gson();
            List<AddItem> addItems=gson.fromJson(json,new TypeToken<List<AddItem>>(){}.getType());
            fragments.clear();
            titleList.clear();
            for (int i = 0; i < addItems.size(); i++) {
                if (addItems.get(i).isAdded()){
                    NewsFragment fragment = new NewsFragment();
                    fragment.setChannelId(addItems.get(i).getChannelId());
                    fragments.add(fragment);
                    titleList.add(addItems.get(i).getTitle());
                }

            }
        }else{
            //从网络获取ChannelId，标题，解析xml,转化为json格式
            getDataFromServer("http://c.m.163.com");

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        newsViewPagerAdapter.notifyDataSetChanged();
    }


    private void getDataFromServer(String url) {

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String str = responseInfo.result;
                /*showToast(str);*/
                handData(str);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }
        });

    }

    private void handData(String str) {

        List<AddItem> addItems = XmlParseUtil.getAddItemInfo(str);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("addItemList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        List<AddItem> list = new ArrayList<>();
        for (int i = 0; i < addItems.size(); i++) {
            if (i <= 5) {
                AddItem addItem = new AddItem();
                addItem.setAdded(true);
                addItem.setChannelId(addItems.get(i).getChannelId());
                addItem.setTitle(addItems.get(i).getTitle());
                list.add(addItem);
//
              /*  NewsFragment fragment = new NewsFragment();
                fragment.setChannelId(addItems.get(i).getChannelId());
                fragments.add(fragment);
                titleList.add(addItems.get(i).getTitle());*/


            } else {
                AddItem addItem = new AddItem();
                addItem.setAdded(false);
                addItem.setChannelId(addItems.get(i).getChannelId());
                addItem.setTitle(addItems.get(i).getTitle());
                list.add(addItem);
            }

        }
        String json = gson.toJson(list);
        editor.putString("addItemList", json);
        editor.commit();


    }

}
