package com.gdcp.newsclient.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.NewsAdapter;
import com.gdcp.newsclient.bean.NewsBean;
import com.gdcp.newsclient.manager.URLManager;
import com.gdcp.newsclient.ui.activity.NewsDetailActivity;
import com.gdcp.newsclient.ui.activity.PhotoviewActivity;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asus- on 2017/6/27.
 */

public class NewsFragment extends BaseFragment {



    private ListView listview;
    private SpringView springView;
    private int num=0;
    private List<NewsBean.ResultBean>news=new ArrayList<>();
    private boolean isFrist=true;
    /**
     * 新闻类别id
     */
    private String channelId;
    private NewsAdapter newsAdapter;

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news;
    }

    @Override
    public void initView() {
        listview= (ListView) mView.findViewById(R.id.listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsBean.ResultBean  resultBean= (NewsBean.ResultBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("news", resultBean);
                startActivity(intent);
            }
        });
        initSpringView();
         newsAdapter = new NewsAdapter(news,getActivity());
        listview.setAdapter(newsAdapter);
    }

    private void initSpringView() {
        springView= (SpringView) mView.findViewById(R.id.springView);
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                num=0;
                news.clear();
                getDataFromServer();

            }

            @Override
            public void onLoadmore() {
                getDataFromServer();
            }
        });

        springView.setType(SpringView.Type.FOLLOW);
    }

    @Override
    public void initData() {
                getDataFromServer();
    }

    private void getDataFromServer() {
        String url = URLManager.getLoadMoreUrl(channelId,num);

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                System.out.println("----服务器返回的json数据:" + json);
                json =  json.replace(channelId, "result");
                Gson gson = new Gson();
                NewsBean newsBean = gson.fromJson(json, NewsBean.class);
                // 显示服务器数据
                showDatas(newsBean);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }
        });

    }

    private void showDatas(NewsBean newsBean) {
        if (newsBean==null||  newsBean.getResult() == null || newsBean.getResult().size() == 0){
            System.out.println("----没有获取到服务器的新闻数据");
            return;
        }

        List<NewsBean.ResultBean.AdsBean>adsBeanList=newsBean.getResult().get(0).getAds();
        if (isFrist) {
            if (adsBeanList != null && adsBeanList.size() > 0) {
                View headerView = View.inflate(getActivity(), R.layout.list_header, null);
                SliderLayout sliderLayout = (SliderLayout) headerView.findViewById(R.id.sliderlayout);
                for (int i = 0; i < adsBeanList.size(); i++) {
                    final NewsBean.ResultBean.AdsBean adBean = adsBeanList.get(i);
                    TextSliderView textSliderView = new TextSliderView(getActivity());
                    textSliderView.description(adBean.getTitle()).image(adBean.getImgsrc());
                    sliderLayout.addSlider(textSliderView);
                    textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Intent intent = new Intent(getActivity(), PhotoviewActivity.class);
                            intent.putExtra("adBean", adBean);
                            startActivity(intent);
                        }
                    });
                }

                listview.addHeaderView(headerView);
            }
            isFrist=false;
        }

        for (int i = 0; i < newsBean.getResult().size(); i++) {
            news.add(newsBean.getResult().get(i));
        }
        num=num+20;
        newsAdapter.notifyDataSetChanged();
        springView.onFinishFreshAndLoad();
    }


}
