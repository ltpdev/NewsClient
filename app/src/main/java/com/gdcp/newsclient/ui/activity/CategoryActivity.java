package com.gdcp.newsclient.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.CategoryLiveAdapter;
import com.gdcp.newsclient.bean.CategoryBean;
import com.gdcp.newsclient.bean.LiveBean;
import com.gdcp.newsclient.callback.EmptyCallback;
import com.gdcp.newsclient.callback.ErrorCallback;
import com.gdcp.newsclient.callback.LoadingCallback;
import com.gdcp.newsclient.utils.DiskLruCacheUtils;
import com.gdcp.newsclient.utils.PostUtil;
import com.google.gson.Gson;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private String url = "http://capi.douyucdn.cn/api/v1/getColumnDetail";
    private List<CategoryBean.DataBean> listBeans;
    private CategoryLiveAdapter liveAdapter;
    private String TAG = "CategoryActivity";
    private LoadService loadService;

    @Override
    protected void initData() {
        getCategoryData();
    }

    private void getCategoryData() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                DiskLruCacheUtils diskLruCacheUtils=new DiskLruCacheUtils(CategoryActivity.this);
                diskLruCacheUtils.inputDiskCache(url,json);
                Gson gson = new Gson();
                Log.i(TAG, "onSuccess: " + json);
                CategoryBean categoryBean = gson.fromJson(json, CategoryBean.class);
                // 显示服务器数据
                showDatas(categoryBean);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
               /* PostUtil.postCodeDelayed(loadService, 0, 2000);*/
                Log.i(TAG, "onFailure: " + msg);
                showToast("无网络");
                DiskLruCacheUtils diskLruCacheUtils=new DiskLruCacheUtils(CategoryActivity.this);
                String json=diskLruCacheUtils.getJsonString(url);
                if (json!=null){
                    Gson gson=new Gson();
                    CategoryBean categoryBean = gson.fromJson(json, CategoryBean.class);
                    // 显示服务器数据
                    showDatas(categoryBean);
                }else {
                    PostUtil.postCodeDelayed(loadService,0,2000);
                }
            }
        });
    }


    private void showDatas(CategoryBean categoryBean) {
        if (categoryBean == null) {
            PostUtil.postCodeDelayed(loadService, 50, 2000);
            return;
        }
        if (categoryBean.getData() == null) {
            PostUtil.postCodeDelayed(loadService, 50, 2000);
            return;
        }
        List<CategoryBean.DataBean> categoryBeanData = categoryBean.getData();
        if (categoryBeanData.size() <= 0) {
            PostUtil.postCodeDelayed(loadService, 50, 2000);
            return;
        }
        for (int i = 0; i < categoryBeanData.size(); i++) {
            listBeans.add(categoryBeanData.get(i));
        }
        PostUtil.postCodeDelayed(loadService, 100, 2000);
        liveAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_category;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("全部分类");
        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                getCategoryData();
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
        initeAdapter();
    }

    private void initeAdapter() {
        listBeans = new ArrayList<>();
        liveAdapter = new CategoryLiveAdapter(listBeans, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        //xRecyclerView.addItemDecoration(new GridDivider(getActivity(), 20, this.getResources().getColor(R.color.newsbg_color)));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(liveAdapter);
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
