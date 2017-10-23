package com.gdcp.newsclient.ui.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.JokeAdapter;
import com.gdcp.newsclient.bean.JokeBean;
import com.gdcp.newsclient.bean.VideosBean;
import com.gdcp.newsclient.utils.DiskLruCacheUtils;
import com.gdcp.newsclient.utils.PostUtil;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asus- on 2017/6/27.
 */

public class MainFragment03 extends BaseFragment{
    private XRecyclerView xRecyclerView;
    private JokeAdapter jokeAdapter;
    private List<JokeBean.ResultBean>resultBeanList;
    private int num=1;
    private boolean isFirstLoad=true;

    @Override
    protected void reloadData() {
        isFirstLoad=true;
        getNetJokeDataList();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main03;
    }

    @Override
    public void initView() {
        xRecyclerView= (XRecyclerView) mView.findViewById(R.id.xRecyclerView);
    }

    @Override
    public void initData() {
        resultBeanList=new ArrayList<>();
        jokeAdapter=new JokeAdapter(getActivity(),resultBeanList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.setAdapter(jokeAdapter);
        initListener();
        //获取网络笑话数据
        getNetJokeDataList();
    }

    private void initListener() {
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                resultBeanList.clear();
                num=1;
                getNetJokeDataList();
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                num=num+1;
                getNetJokeDataList();
                xRecyclerView.loadMoreComplete();
            }
        });
    }

    private void getNetJokeDataList() {
        HttpUtils utils = new HttpUtils();
       final String url= "http://api.avatardata.cn/Joke/NewstJoke?key=740c728a0b574c" +
                "f085bf4089ef45bf96&page="+num+"&rows=20";
        utils.send(HttpRequest.HttpMethod.GET,url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (isFirstLoad){
                    PostUtil.postCodeDelayed(loadService,100,2000);
                    //loadService.showSuccess();//成功回调
                    isFirstLoad=false;
                }
                String json = responseInfo.result;
                DiskLruCacheUtils diskLruCacheUtils=new DiskLruCacheUtils(getActivity());
                diskLruCacheUtils.inputDiskCache(url,json);
                Gson gson=new Gson();
                JokeBean jokeBean=gson.fromJson(json,JokeBean.class);
                // 显示服务器数据
                showData(jokeBean);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                showToast("无网络");
                DiskLruCacheUtils diskLruCacheUtils=new DiskLruCacheUtils(getActivity());
                String json=diskLruCacheUtils.getJsonString(url);
                if (json!=null){
                    if (isFirstLoad){
                        PostUtil.postCodeDelayed(loadService,100,2000);
                        //loadService.showSuccess();//成功回调
                        isFirstLoad=false;
                    }
                    Gson gson=new Gson();
                    JokeBean jokeBean=gson.fromJson(json,JokeBean.class);
                    // 显示服务器数据
                    showData(jokeBean);
                }else {
                    PostUtil.postCodeDelayed(loadService,0,2000);
                }
            }
        });
    }

    private void showData(JokeBean jokeBean) {
        if (jokeBean==null){
            PostUtil.postCodeDelayed(loadService,25,2000);
            return;
        }
        if (jokeBean.getResult()==null){
            PostUtil.postCodeDelayed(loadService,25,2000);
            return;
        }



        for (int i = 0; i < jokeBean.getResult().size(); i++) {
            resultBeanList.add(jokeBean.getResult().get(i));
        }
        jokeAdapter.notifyDataSetChanged();

    }


}
