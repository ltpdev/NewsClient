package com.gdcp.newsclient.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;

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

public class CategoryDetailActivity extends BaseActivity{
    private XRecyclerView xRecyclerView;
    private LiveAdapter liveAdapter;
    private List<LiveBean.DataBean> listBeans;
    private int num=0;
    private String tagid;

    @Override
    protected void initData() {
        listBeans=new ArrayList<>();
        liveAdapter=new LiveAdapter(listBeans,this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        //xRecyclerView.addItemDecoration(new GridDivider(getActivity(), 20, this.getResources().getColor(R.color.newsbg_color)));
        xRecyclerView.setLayoutManager(gridLayoutManager);
        xRecyclerView.setAdapter(liveAdapter);
        initListener();
        //获取直播数据
        getLiveDataList();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_category_detail;
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
        final String url= "http://capi.douyucdn.cn/api/v1/live/"+tagid+"?&limit=20";
        utils.send(HttpRequest.HttpMethod.GET,url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                Gson gson=new Gson();
                LiveBean liveBean=gson.fromJson(json,LiveBean.class);
                // 显示服务器数据
                showData(liveBean);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                showToast("无网络");
            }
        });
    }

    private void showData(LiveBean liveBean) {
        if (liveBean==null){
            return;
        }
        if (liveBean.getData()==null){
            return;
        }
        for (int i = 0; i < liveBean.getData().size(); i++) {
            listBeans.add(liveBean.getData().get(i));
        }
        liveAdapter.notifyDataSetChanged();

    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("tagName"));
        tagid=getIntent().getStringExtra("tagId");
        xRecyclerView= (XRecyclerView) findViewById(R.id.xRecyclerView);
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
