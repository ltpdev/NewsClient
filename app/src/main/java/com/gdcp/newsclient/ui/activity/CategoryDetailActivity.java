package com.gdcp.newsclient.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.LiveAdapter;
import com.gdcp.newsclient.bean.LiveBean;
import com.gdcp.newsclient.callback.EmptyCallback;
import com.gdcp.newsclient.callback.ErrorCallback;
import com.gdcp.newsclient.callback.LoadingCallback;
import com.gdcp.newsclient.utils.DiskLruCacheUtils;
import com.gdcp.newsclient.utils.PostUtil;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailActivity extends BaseActivity{
    private RecyclerView recyclerView;
    private LiveAdapter liveAdapter;
    private List<LiveBean.DataBean> listBeans;
    //private int num=0;
    private String tagid;
    private SpringView springView;
    private LoadService loadService;
    //是否数据添加到尾部的标志
    private boolean isAddFoot=true;
    //private boolean isFirstLoad=true;

    @Override
    protected void initData() {
        listBeans=new ArrayList<>();
        liveAdapter=new LiveAdapter(listBeans,this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        //xRecyclerView.addItemDecoration(new GridDivider(getActivity(), 20, this.getResources().getColor(R.color.newsbg_color)));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(liveAdapter);
        initListener();
        //获取直播数据
        getLiveDataList();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_category_detail;
    }
    private void initListener() {
        springView.setFooter(new DefaultFooter(this));
        springView.setHeader(new MeituanHeader(this));
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
                /*num=num+20;*/
                isAddFoot=true;
                getLiveDataList();
                springView.onFinishFreshAndLoad();
            }
        });
        springView.setType(SpringView.Type.FOLLOW);
    }

    private void getLiveDataList() {
        HttpUtils utils = new HttpUtils();
        final String url= "http://capi.douyucdn.cn/api/v1/live/"+tagid+"?&limit=20&offset="+listBeans.size() ;
        utils.send(HttpRequest.HttpMethod.GET,url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                DiskLruCacheUtils diskLruCacheUtils=new DiskLruCacheUtils(CategoryDetailActivity.this);
                diskLruCacheUtils.inputDiskCache(url,json);
                Gson gson=new Gson();
                LiveBean liveBean=gson.fromJson(json,LiveBean.class);
                // 显示服务器数据
                showData(liveBean);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //PostUtil.postCodeDelayed(loadService, 0, 2000);
                showToast("无网络");
                DiskLruCacheUtils diskLruCacheUtils=new DiskLruCacheUtils(CategoryDetailActivity.this);
                String json=diskLruCacheUtils.getJsonString(url);
                if (json!=null){
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
            PostUtil.postCodeDelayed(loadService, 50, 2000);
            return;
        }
        if (liveBean.getData()==null){
            PostUtil.postCodeDelayed(loadService, 50, 2000);
            return;
        }
        if (liveBean.getData().size()<=0){
            PostUtil.postCodeDelayed(loadService, 50, 2000);
            return;
        }
        for (int i = 0; i < liveBean.getData().size(); i++) {
            if (isAddFoot){
                listBeans.add(liveBean.getData().get(i));
            }else {
                listBeans.add(0,liveBean.getData().get(i));
            }
        }
        PostUtil.postCodeDelayed(loadService, 100, 2000);
        liveAdapter.notifyDataSetChanged();

    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("tagName"));
        tagid=getIntent().getStringExtra("tagId");
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        springView= (SpringView)findViewById(R.id.springView);
        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                getLiveDataList();
            }
        }, new Convertor<Integer>() {
            @Override
            public Class<? extends Callback> map(Integer integer) {
                Class<? extends Callback> resultCode = SuccessCallback.class;
                switch (integer) {
                    case 100://成功回调
                        resultCode = SuccessCallback.class;
                        break;
                    case 50:
                        resultCode = EmptyCallback.class;
                        break;
                    case 0:
                        resultCode = ErrorCallback.class;
                        break;
                }
                return resultCode;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
