package com.gdcp.newsclient.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.NewsBean;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class PhotoviewActivity extends BaseActivity {


    @BindView(R.id.photoView)
    PhotoView photoView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subtitle)
    TextView subtitle;

    @Override
    protected void initData() {
        NewsBean.ResultBean.AdsBean adsBean= (NewsBean.ResultBean.AdsBean) getIntent().getSerializableExtra("adBean");
        if (adsBean!=null){
            Picasso.with(this).load(adsBean.getImgsrc()).into(photoView);
            title.setText(adsBean.getTitle());
            subtitle.setText(adsBean.getTag());
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_photoview;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle("");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
