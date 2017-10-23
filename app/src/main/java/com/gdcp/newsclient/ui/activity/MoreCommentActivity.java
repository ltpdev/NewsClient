package com.gdcp.newsclient.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.CommentAdapter;
import com.gdcp.newsclient.adapter.FilmAdapter;
import com.gdcp.newsclient.bean.CommentBean;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreCommentActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.springView)
    SpringView springView;
    private int offset=0;
    private CommentAdapter commentAdapter;
    private List<CommentBean.DataBean.CommentResponseModelBean.CmtsBean> cmtsBeanList;

    @Override
    protected void initData() {
        cmtsBeanList=new ArrayList<>();
        commentAdapter=new CommentAdapter(cmtsBeanList,this);
        listview.setAdapter(commentAdapter);
        //获取网络评论数据
        getCommentData();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_more_comment;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle("全部评论");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initSpringView();
    }

    private void initSpringView() {
        springView.setFooter(new DefaultFooter(this));
        springView.setHeader(new DefaultHeader(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                cmtsBeanList.clear();
                getCommentData();
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                offset=offset+5;
                getCommentData();
                springView.onFinishFreshAndLoad();
            }
        });

        springView.setType(SpringView.Type.FOLLOW);
    }


    public void getCommentData() {
        int id = getIntent().getIntExtra("id", 0);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, "http://m.maoyan.com/comments.json?movieid="+id+"&limit=5&offset="+offset, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                Gson gson=new Gson();
                CommentBean ommentBean=gson.fromJson(json,CommentBean.class);
                for (int i = 0; i <ommentBean.getData().getCommentResponseModel().getCmts().size() ; i++) {
                    cmtsBeanList.add(ommentBean.getData().getCommentResponseModel().getCmts().get(i));
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

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
