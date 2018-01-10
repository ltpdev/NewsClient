package com.gdcp.newsclient.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.LiveAdapter;
import com.gdcp.newsclient.bean.LiveBean;
import com.gdcp.newsclient.utils.DiskLruCacheUtils;
import com.gdcp.newsclient.utils.PostUtil;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liaoinstan.springview.container.AcFunFooter;
import com.liaoinstan.springview.container.AcFunHeader;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;
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
    private RecyclerView recyclerView;
    private LiveAdapter liveAdapter;
    private List<LiveBean.DataBean> listBeans;
    //private int num=0;
    //是否数据添加到尾部的标志
    private boolean isAddFoot=true;
    private boolean isFirstLoad=true;
    private String url;
    private SpringView springView;

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
        recyclerView= (RecyclerView) mView.findViewById(R.id.recyclerView);
        springView= (SpringView) mView.findViewById(R.id.springView);
    }

    @Override
    public void initData() {
        listBeans=new ArrayList<>();
        liveAdapter=new LiveAdapter(listBeans,getActivity());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        //xRecyclerView.addItemDecoration(new GridDivider(getActivity(), 20, this.getResources().getColor(R.color.newsbg_color)));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(liveAdapter);
        initListener();
        //获取直播数据
        getLiveDataList();
    }

    private void initListener() {
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setHeader(new MeituanHeader(getActivity()));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                //listBeans.clear();
                //num=0;
                isAddFoot=false;
                getLiveDataList();
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                //num=num+20;
                isAddFoot=true;
                getLiveDataList();
                springView.onFinishFreshAndLoad();
            }
        });
        springView.setType(SpringView.Type.FOLLOW);
    }

    private void getLiveDataList() {
        HttpUtils utils = new HttpUtils();
        //final String url= "http://capi.douyucdn.cn/api/v1/live?&limit=20";
        url=url+"&offset="+listBeans.size();
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
            if (isAddFoot){
                listBeans.add(liveBean.getData().get(i));
            }else {
                listBeans.add(0,liveBean.getData().get(i));
            }
        }
        liveAdapter.notifyDataSetChanged();

    }
}
