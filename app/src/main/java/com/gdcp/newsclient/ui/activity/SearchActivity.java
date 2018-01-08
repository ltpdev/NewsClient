package com.gdcp.newsclient.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;


import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.LiveAdapter;
import com.gdcp.newsclient.adapter.SearchLiveAdapter;
import com.gdcp.newsclient.bean.LiveBean;
import com.gdcp.newsclient.bean.NewsBean;
import com.gdcp.newsclient.bean.SearchLiveBean;
import com.gdcp.newsclient.kuaichuan.common.*;
import com.gdcp.newsclient.utils.HttpUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity{
  private ImageView back;
  private SearchView searchView;
  private RecyclerView recyclerView;
    private SearchLiveAdapter liveAdapter;
    private List<SearchLiveBean.DataBean.RoomBean> listBeans;

    @Override
    protected void initData() {
        listBeans=new ArrayList<>();
        liveAdapter=new SearchLiveAdapter(listBeans,this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        //xRecyclerView.addItemDecoration(new GridDivider(getActivity(), 20, this.getResources().getColor(R.color.newsbg_color)));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(liveAdapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        back= (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchView= (SearchView) findViewById(R.id.search);
        //searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("输入搜索内容");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!TextUtils.isEmpty(s)){
                    //mListView.setFilterText(newText);
                    searchLive(s);
                }else{
                    //mListView.clearTextFilter();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s)){
                    //mListView.setFilterText(newText);
                            //showToast(s);
                }else{
                    //mListView.clearTextFilter();
                }
                return false;
            }
        });
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void searchLive(String keyWord){
        listBeans.clear();
       String url="http://capi.douyucdn.cn/api/v1/mobileSearch/1/1?sk=" + keyWord + "&limit=20&client_sys=android";
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json=responseInfo.result;
                Gson gson = new Gson();
                SearchLiveBean searchLiveBean = gson.fromJson(json, SearchLiveBean.class);
                // 显示服务器数据
                showDatas(searchLiveBean);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    private void showDatas(SearchLiveBean searchLiveBean) {
         if (searchLiveBean==null){
             return;
         }
        if (searchLiveBean.getData()==null){
            return;
        }
        if (searchLiveBean.getData().getRoom()==null){
            return;
        }
        List<SearchLiveBean.DataBean.RoomBean> roomBeans=searchLiveBean.getData().getRoom();
        for (int i = 0; i <roomBeans.size() ; i++) {
             listBeans.add(roomBeans.get(i));
        }

        liveAdapter.notifyDataSetChanged();
    }

}
