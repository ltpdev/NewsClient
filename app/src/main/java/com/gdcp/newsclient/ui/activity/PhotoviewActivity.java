package com.gdcp.newsclient.ui.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.NewsBean;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class PhotoviewActivity extends BaseActivity {


    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private List<View>viewList=new ArrayList<>();

    @Override
    protected void initData() {
        NewsBean newsBean = (NewsBean) getIntent().getSerializableExtra("newsBean");
        int postion = getIntent().getIntExtra("position", 0);
        if (newsBean!=null){
           /* Glide.with(this).load(adsBean.getImgsrc()).into(photoView);
            title.setText(adsBean.getTitle());
            subtitle.setText(adsBean.getTag());*/
            List<NewsBean.ResultBean.AdsBean> adsBeans= newsBean.getResult().get(0).getAds();
            for (int i = 0; i < adsBeans.size(); i++) {
                View view=View.inflate(this,R.layout.view_viewpager,null);
                PhotoView photoView= (PhotoView) view.findViewById(R.id.photoView);
                TextView title= (TextView) view.findViewById(R.id.title);
                TextView subtitle= (TextView) view.findViewById(R.id.subtitle);
                Glide.with(this).load(adsBeans.get(i).getImgsrc()).into(photoView);
                title.setText(adsBeans.get(i).getTitle());
                subtitle.setText(adsBeans.get(i).getTag());
                viewList.add(view);
            }

        }else {
            List<String> imgsList= getIntent().getStringArrayListExtra("imgsList");
            if (imgsList!=null){
                for (int i = 0; i < imgsList.size(); i++) {
                    View view=View.inflate(this,R.layout.view_viewpager,null);
                    PhotoView photoView= (PhotoView) view.findViewById(R.id.photoView);
                    TextView title= (TextView) view.findViewById(R.id.title);
                    TextView subtitle= (TextView) view.findViewById(R.id.subtitle);
                    Glide.with(this).load(imgsList.get(i)).into(photoView);
                    title.setText("");
                    subtitle.setText("");
                    viewList.add(view);
                }
            }
        }

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(postion);
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
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


  private PagerAdapter pagerAdapter=new PagerAdapter() {
      @Override
      public int getCount() {
          return viewList.size();
      }

      @Override
      public Object instantiateItem(ViewGroup container, int position) {
          ((ViewPager)container).addView(viewList.get(position%viewList.size()), 0);
          return viewList.get(position);
      }

      @Override
      public void destroyItem(View container, int position, Object object) {
          ((ViewPager)container).removeView(viewList.get(position%viewList.size()));

      }


      @Override
      public boolean isViewFromObject(View view, Object object) {
          return view==object;
      }
  } ;


}
