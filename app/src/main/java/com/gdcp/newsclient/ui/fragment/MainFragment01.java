package com.gdcp.newsclient.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.NewsViewPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by asus- on 2017/6/27.
 */

public class MainFragment01 extends BaseFragment{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private NewsViewPagerAdapter newsViewPagerAdapter;



    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main01;
    }

    @Override
    public void initView() {
        viewPager= (ViewPager) mView.findViewById(R.id.viewPager);
        tabLayout= (TabLayout) mView.findViewById(R.id.tabLayout);
        initViewPager();

    }

    private void initViewPager() {
        final String[] titles = new String[] {
                "头条", "社会", "科技", "财经", "体育", "汽车"
        };
        final String[] channelId = new String[] {
                "T1348647909107",
                "T1348648037603",
                "T1348649580692",
                "T1348648756099",
                "T1348649079062",
                "T1348654060988",
        };
        final List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            NewsFragment fragment = new NewsFragment();
            fragment.setChannelId(channelId[i]);
            fragments.add(fragment);
        }
        newsViewPagerAdapter=new NewsViewPagerAdapter(getChildFragmentManager(),
                fragments, Arrays.asList(titles));
        viewPager.setAdapter(newsViewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void initData() {

    }
}
