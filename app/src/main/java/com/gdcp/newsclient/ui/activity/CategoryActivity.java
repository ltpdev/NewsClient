package com.gdcp.newsclient.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.CategoryLiveAdapter;
import com.gdcp.newsclient.adapter.SearchLiveAdapter;
import com.gdcp.newsclient.bean.CategoryBean;
import com.gdcp.newsclient.bean.SearchLiveBean;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends BaseActivity {
private RecyclerView recyclerView;

   private String url="http://capi.douyucdn.cn/api/v1/getColumnDetail";

    private List<CategoryBean.DataBean> listBeans;
    private CategoryLiveAdapter liveAdapter;
    private String TAG="CategoryActivity";

    @Override
    protected void initData() {
        getCategoryData();
    }

    private void getCategoryData() {
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json=responseInfo.result;
                Gson gson = new Gson();
                Log.i(TAG, "onSuccess: "+json);
                CategoryBean categoryBean = gson.fromJson(json, CategoryBean.class);
                // 显示服务器数据
                showDatas(categoryBean);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.i(TAG, "onFailure: "+msg);
            }
        });
    }


    private void showDatas(CategoryBean categoryBean) {
        if (categoryBean==null){
            return;
        }
        if (categoryBean.getData()==null){
            return;
        }

        List<CategoryBean.DataBean> categoryBeanData=categoryBean.getData();
        for (int i = 0; i <categoryBeanData.size() ; i++) {
            listBeans.add(categoryBeanData.get(i));
        }

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
        listBeans=new ArrayList<>();
        liveAdapter=new CategoryLiveAdapter(listBeans,this);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,4);
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
