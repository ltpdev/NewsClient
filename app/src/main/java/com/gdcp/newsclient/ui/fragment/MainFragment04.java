package com.gdcp.newsclient.ui.fragment;

import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.FilmAdapter;
import com.gdcp.newsclient.bean.FilmBean;
import com.gdcp.newsclient.bean.JokeBean;
import com.gdcp.newsclient.ui.activity.FilmDetailActivity;
import com.gdcp.newsclient.utils.DiskLruCacheUtils;
import com.gdcp.newsclient.utils.PostUtil;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus- on 2017/6/27.
 */

public class MainFragment04 extends BaseFragment{
    private ListView listView;
    private FilmAdapter filmAdapter;
    private SpringView springView;
    private List<FilmBean.DataBean.MoviesBean>moviesBeanList;
    private int num=20;
    private int offset=0;
    private boolean isFirstLoad=true;

    @Override
    protected void reloadData() {
        isFirstLoad=true;
        getNetDataList();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main04;
    }

    @Override
    public void initView() {
        listView= (ListView) mView.findViewById(R.id.listview);
        springView= (SpringView) mView.findViewById(R.id.springView);
        initSpringView();
        initListViewListener();
    }

    private void initListViewListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),FilmDetailActivity.class);
                intent.putExtra("id",moviesBeanList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void initSpringView() {
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                num=20;
                offset=0;
                moviesBeanList.clear();
                getNetDataList();
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                num=num+20;
                offset=offset+20;
                getNetDataList();
                springView.onFinishFreshAndLoad();
            }
        });

        springView.setType(SpringView.Type.FOLLOW);
    }

    @Override
    public void initData() {
        moviesBeanList=new ArrayList<>();
        filmAdapter=new FilmAdapter(getActivity(),moviesBeanList);
        listView.setAdapter(filmAdapter);
        //获取网络数据
        getNetDataList();

    }

    private void getNetDataList() {
        HttpUtils utils = new HttpUtils();
        final String url="http://m.maoyan.com/movie/list.json?type=hot&offset=0&limit="+num;
        utils.send(HttpRequest.HttpMethod.GET,url ,
                new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (isFirstLoad){
                    PostUtil.postCodeDelayed(loadService,100,2000);
                    //loadService.showSuccess();//成功回调
                    isFirstLoad=false;
                }
                String json = responseInfo.result;
                Log.i("josn",json);
                DiskLruCacheUtils diskLruCacheUtils=new DiskLruCacheUtils(getActivity());
                diskLruCacheUtils.inputDiskCache(url,json);
                Gson gson=new Gson();
                try {
                    FilmBean filmBean=gson.fromJson(json,FilmBean.class);
                    // 显示服务器数据
                    showData(filmBean);
                }catch (Exception e){
                    e.printStackTrace();
                }

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
                    FilmBean filmBean=gson.fromJson(json,FilmBean.class);
                    // 显示服务器数据
                    showData(filmBean);
                }else {
                    PostUtil.postCodeDelayed(loadService,0,2000);
                }
            }
        });
    }

    private void showData(FilmBean filmBean) {
        if (filmBean==null){
            showToast("没有更多数据了");
            PostUtil.postCodeDelayed(loadService,25,2000);
            return;
        }

        if (filmBean.getData()==null||filmBean.getData().getMovies()==null){
            PostUtil.postCodeDelayed(loadService,25,2000);
            return;
        }
        if (offset>=filmBean.getData().getMovies().size()){
            showToast("没有更多数据了");
            return;
        }
        for (int i = offset; i < filmBean.getData().getMovies().size(); i++) {
            moviesBeanList.add(filmBean.getData().getMovies().get(i));
        }
        filmAdapter.notifyDataSetChanged();

    }
}
