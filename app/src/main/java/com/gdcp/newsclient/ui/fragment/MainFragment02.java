package com.gdcp.newsclient.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.EditText;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.VideoAdapter;
import com.gdcp.newsclient.bean.NewsBean;
import com.gdcp.newsclient.bean.VideosBean;
import com.gdcp.newsclient.listener.SharedListener;
import com.gdcp.newsclient.manager.URLManager;
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

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by asus- on 2017/6/27.
 */

public class MainFragment02 extends BaseFragment implements SharedListener{
    private XRecyclerView xRecyclerView;
    private List<VideosBean.V9LG4B3A0Bean>v9LG4B3A0BeanList;
    private VideoAdapter videoAdapter;
    private int num=0;
    private boolean isFirstLoad=true;

    @Override
    protected void reloadData() {
        isFirstLoad=true;
        getDataFromServer();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main02;
    }

    @Override
    public void initView() {
        v9LG4B3A0BeanList=new ArrayList<>();
        videoAdapter=new VideoAdapter(v9LG4B3A0BeanList,getActivity());
        videoAdapter.setSharedListener(this);
        xRecyclerView= (XRecyclerView) mView.findViewById(R.id.xRecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.setAdapter(videoAdapter);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        initListener();
    }

    private void initListener() {
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                v9LG4B3A0BeanList.clear();
                num=0;
                getDataFromServer();
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                num=num+20;
                getDataFromServer();
                xRecyclerView.loadMoreComplete();
            }
        });
    }

    @Override
    public void initData() {
        getDataFromServer();
    }

    private void getDataFromServer() {
        final String url = URLManager.getLoadMoreVideoURL(num);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url,new RequestCallBack<String>() {
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
                System.out.println("----服务器返回的json数据:" + json);
                Gson gson = new Gson();
                VideosBean videosBean = gson.fromJson(json, VideosBean.class);
                // 显示服务器数据
                showDatas(videosBean);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                error.printStackTrace();
                showToast("无网络");
                DiskLruCacheUtils diskLruCacheUtils=new DiskLruCacheUtils(getActivity());
                String json=diskLruCacheUtils.getJsonString(url);
                if (json!=null){
                    if (isFirstLoad){
                        PostUtil.postCodeDelayed(loadService,100,2000);
                        //loadService.showSuccess();//成功回调
                        isFirstLoad=false;
                    }
                    Gson gson = new Gson();
                    VideosBean videosBean = gson.fromJson(json, VideosBean.class);
                    // 显示服务器数据
                    showDatas(videosBean);
                }else {
                    PostUtil.postCodeDelayed(loadService,0,2000);
                }
            }
        });
    }

    private void showDatas(VideosBean videosBean) {
        if (videosBean==null){
            PostUtil.postCodeDelayed(loadService,25,2000);
            System.out.println("----没有获取到服务器的新闻数据");
            return;
        }

        if (videosBean.getV9LG4B3A0()==null){
            PostUtil.postCodeDelayed(loadService,25,2000);
            return;
        }

        for (int i = 0; i <videosBean.getV9LG4B3A0().size() ; i++) {
            v9LG4B3A0BeanList.add(videosBean.getV9LG4B3A0().get(i));
        }
        videoAdapter.notifyDataSetChanged();

    }

    @Override
    public void selected(VideosBean.V9LG4B3A0Bean v9LG4B3A0Bean) {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, "视频: "+v9LG4B3A0Bean.getTitle()+v9LG4B3A0Bean.getMp4_url());
        startActivity(Intent.createChooser(textIntent, "分享到"));

    }

  /*  @Override
    public void onBackPressed() {
        if (JCVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }*/

    @Override
    public void onPause() {
        super.onPause();
        Log.i("onPause","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("onStop","onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy","onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("onDestroyView","onDestroyView");
    }
}
