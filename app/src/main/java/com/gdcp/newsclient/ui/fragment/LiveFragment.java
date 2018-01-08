package com.gdcp.newsclient.ui.fragment;

import android.support.v7.widget.GridLayoutManager;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.LiveAdapter;
import com.gdcp.newsclient.bean.LiveBean;
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

/**
 * Created by asus- on 2018/1/7.
 */

public class LiveFragment extends BaseFragment{
    private XRecyclerView xRecyclerView;
    private LiveAdapter liveAdapter;
    private List<LiveBean.DataBean> listBeans;
    private int num=0;
    private boolean isFirstLoad=true;
    private String url;

    @Override
    protected void reloadData() {
        isFirstLoad=true;
        getLiveDataList();
    }

    public void setUrl(String url) {
        this.url = url;
        //System.out.println("---"+channelId);
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
        listBeans=new ArrayList<>();
        liveAdapter=new LiveAdapter(listBeans,getActivity());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        //xRecyclerView.addItemDecoration(new GridDivider(getActivity(), 20, this.getResources().getColor(R.color.newsbg_color)));
        xRecyclerView.setLayoutManager(gridLayoutManager);
        xRecyclerView.setAdapter(liveAdapter);
        initListener();
        //获取直播数据
        getLiveDataList();
    }

    private void initListener() {
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listBeans.clear();
                num=0;
                getLiveDataList();
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                num=num+20;
                getLiveDataList();
                xRecyclerView.loadMoreComplete();
            }
        });
    }

    private void getLiveDataList() {
        HttpUtils utils = new HttpUtils();
        //final String url= "http://capi.douyucdn.cn/api/v1/live?&limit=20";
        url=url+"&offset="+num;
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
                LiveBean liveBean=gson.fromJson(json,LiveBean.class);
                // 显示服务器数据
                showData(liveBean);
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
                    LiveBean liveBean=gson.fromJson(json,LiveBean.class);
                    // 显示服务器数据
                    showData(liveBean);
                }else {
                    PostUtil.postCodeDelayed(loadService,0,2000);
                }
            }
        });
    }

    private void showData(LiveBean liveBean) {
        if (liveBean==null){
            PostUtil.postCodeDelayed(loadService,25,2000);
            return;
        }
        if (liveBean.getData()==null){
            PostUtil.postCodeDelayed(loadService,25,2000);
            return;
        }



        for (int i = 0; i < liveBean.getData().size(); i++) {
            listBeans.add(liveBean.getData().get(i));
        }
        liveAdapter.notifyDataSetChanged();

    }
}
