package com.gdcp.newsclient.ui.activity;

import android.os.Bundle;
import android.text.AndroidCharacter;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.NewsBean;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailActivity extends BaseActivity {


    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void initData() {
        NewsBean.ResultBean resultBean= (NewsBean.ResultBean)
                getIntent().getSerializableExtra("news");
        webView.loadUrl(resultBean.getUrl());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(resultBean.getTitle());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initView() {
        initWebView();
    }

    private void initWebView() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                 if (newProgress==100){
                     progressBar.setVisibility(View.GONE);
                 }else {
                     progressBar.setVisibility(View.VISIBLE);
                     progressBar.setProgress(newProgress);
                 }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){
             finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
