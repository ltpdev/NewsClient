package com.gdcp.newsclient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.ActorAdapter;
import com.gdcp.newsclient.adapter.ImgsAdapter;
import com.gdcp.newsclient.app.APPAplication;
import com.gdcp.newsclient.bean.Actor;
import com.gdcp.newsclient.bean.CommentBean;
import com.gdcp.newsclient.callback.CustomCallback;
import com.gdcp.newsclient.callback.ErrorCallback;
import com.gdcp.newsclient.callback.LoadingCallback;
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
import com.tencent.smtt.sdk.TbsVideo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FilmDetailActivity extends BaseActivity {
    @BindView(R.id.videoplayer)
    ImageView videoplayer;
    @BindView(R.id.film_name)
    TextView filmName;
    @BindView(R.id.film_pingfen)
    TextView filmPingfen;
    @BindView(R.id.film_type)
    TextView filmType;
    @BindView(R.id.film_dur)
    TextView filmDur;
    @BindView(R.id.film_time)
    TextView filmTime;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.list)
    TextView list;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.imgsRecyclerView)
    RecyclerView imgsRecyclerView;
    @BindView(R.id.tv_more_comments)
    TextView tvMoreComments;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    private String videoUrl;
    private boolean isShow = false;
    private List<Actor> actorList;
    private ActorAdapter actorAdapter;
    private List<String> imgList;
    private ImgsAdapter imgsAdapter;
    private List<CommentBean.DataBean.CommentResponseModelBean.CmtsBean> cmtsBeanList;
    private LoadService loadService;

    @Override
    protected void initData() {
        initeAdapter();
        getNetData();
    }

    private void initeAdapter() {
        actorList = new ArrayList<>();
        imgList = new ArrayList<>();
        cmtsBeanList = new ArrayList<>();
        actorAdapter = new ActorAdapter(actorList, this);
        imgsAdapter = new ImgsAdapter(imgList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(actorAdapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        imgsRecyclerView.setLayoutManager(linearLayoutManager2);
        imgsRecyclerView.setAdapter(imgsAdapter);
    }

    private void getNetData() {
        int id = getIntent().getIntExtra("id", 0);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, "http://m.maoyan.com/movie/" + id + ".json", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                PostUtil.postCodeDelayed(loadService,100,2000);
                try {
                    String json = responseInfo.result;
                    int index = json.indexOf("\"img\":\"") + 7;
                    int lastIndex = json.indexOf("\",\"isShowing\"");
                    String picUrl = json.substring(index, lastIndex);
                    index = json.indexOf("\"vd\":") + 6;
                    lastIndex = json.indexOf("\",\"ver\"");
                    videoUrl = json.substring(index, lastIndex);
                    index = json.indexOf("\"nm\":\"") + 6;
                    lastIndex = json.indexOf("\",\"photos\"");
                    String name = json.substring(index, lastIndex);
                    index = json.indexOf("\"cat\":\"") + 7;
                    lastIndex = json.indexOf("\",\"dealsum\"");
                    String type = json.substring(index, lastIndex);
                    index = json.indexOf("\"dra\":\"") + 7;
                    lastIndex = json.indexOf("\",\"dur\"");
                    String des = json.substring(index, lastIndex).trim().replace("<p>", "").replace("</p>", "");
                    index = json.indexOf("\"sc\":") + 5;
                    lastIndex = json.indexOf(",\"scm\"");
                    String sc = json.substring(index, lastIndex);
                    index = json.indexOf("\"rt\":\"") + 6;
                    lastIndex = json.indexOf("\",\"sc\"");
                    String time = json.substring(index, lastIndex).trim();
                    index = json.indexOf("\"dur\":") + 6;
                    lastIndex = json.indexOf(",\"id\"");
                    String dur = json.substring(index, lastIndex);
                    Glide.with(FilmDetailActivity.this).load(picUrl).into(videoplayer);
                    filmName.setText(name);
                    filmPingfen.setText(sc + "分");
                    filmType.setText(type);
                    filmTime.setText(time);
                    content.setText(des);
                    filmDur.setText(dur + "分钟");

                    index = json.indexOf("\"photos\":[") + 10;
                    lastIndex = json.indexOf("],\"pn\":");
                    String picUrls = json.substring(index, lastIndex);

                    index = json.indexOf("\"star\":\"") + 8;
                    lastIndex = json.indexOf("\",\"vd\":");
                    String startNames = json.substring(index, lastIndex);
                    handleString(picUrls, startNames);
                    getCommentData();
                }catch (Exception e){
                    //Toast.makeText(FilmDetailActivity.this, "出错了", Toast.LENGTH_SHORT).show();
                    PostUtil.postCodeDelayed(loadService,50,2000);
                }


            }

            @Override
            public void onFailure(HttpException error, String msg) {
                PostUtil.postCodeDelayed(loadService,0,2000);
                error.printStackTrace();

            }
        });
    }

    private void handleString(String picUrls, String startNames) {
        String pics[] = picUrls.split(",");
        String names[] = startNames.split(" ");
        for (int i = 0; i < names.length; i++) {
            Actor actor = new Actor(names[i]);
            actorList.add(actor);
        }
        actorAdapter.notifyDataSetChanged();
        for (int i = 0; i < pics.length; i++) {
            String pic = pics[i].replace("\"", "").replace("w.h", "500.200");
            imgList.add(pic);
        }
        imgsAdapter.notifyDataSetChanged();

    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_film_detail;
    }

    @Override
    protected void initView() {
         loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                getNetData();
            }}, new Convertor<Integer>() {
            @Override
            public Class<? extends Callback> map(Integer integer) {
                Class<? extends Callback> resultCode = SuccessCallback.class;
                switch (integer) {
                    case 100://成功回调
                            resultCode = SuccessCallback.class;
                        break;
                    case 50:
                        resultCode = CustomCallback.class;
                        break;
                    case 0:
                        resultCode = ErrorCallback.class;
                        break;
                }
                return resultCode;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("电影详情");
    }


    @OnClick({R.id.iv_play, R.id.list, R.id.tv_more_comments})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                TbsVideo.openVideo(FilmDetailActivity.this, videoUrl);
                break;
            case R.id.list:
                isShow = !isShow;
                if (isShow) {
                    content.setMaxLines(200);
                    list.setText("收起");
                } else {
                    content.setMaxLines(3);
                    list.setText("展开");
                }
                break;
            case R.id.tv_more_comments:
                Intent intent = new Intent(this, MoreCommentActivity.class);
                intent.putExtra("id", getIntent().getIntExtra("id", 0));
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getCommentData() {
        int id = getIntent().getIntExtra("id", 0);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, "http://m.maoyan.com/comments.json?movieid=" + id + "&limit=5&offset=0", new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                Gson gson = new Gson();
                CommentBean ommentBean = gson.fromJson(json, CommentBean.class);
                cmtsBeanList.addAll(ommentBean.getData().getCommentResponseModel().getCmts());
                for (int i = 0; i < cmtsBeanList.size(); i++) {
                    View view = LayoutInflater.from(FilmDetailActivity.this).inflate(R.layout.item_comment, null);
                    CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.head_commenter);
                    TextView nickName = (TextView) view.findViewById(R.id.tv_nickName);
                    TextView pn = (TextView) view.findViewById(R.id.tv_pn);
                    TextView content = (TextView) view.findViewById(R.id.tv_content);
                    TextView time = (TextView) view.findViewById(R.id.tv_time);
                    TextView approve = (TextView) view.findViewById(R.id.tv_approve);
                    view.findViewById(R.id.view).setVisibility(View.VISIBLE);
                    CommentBean.DataBean.CommentResponseModelBean.CmtsBean cmtsBean = cmtsBeanList.get(i);
                    nickName.setText(cmtsBean.getNickName());
                    pn.setText("星级评分:" + cmtsBean.getScore());
                    content.setText(cmtsBean.getContent());
                    time.setText(cmtsBean.getTime());
                    approve.setText(cmtsBean.getApprove() + "");
                    Glide.with(FilmDetailActivity.this).load(cmtsBean.getAvatarurl()).into(circleImageView);
                    linearLayout.addView(view);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });

    }



}
