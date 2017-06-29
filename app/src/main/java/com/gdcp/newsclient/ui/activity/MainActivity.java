package com.gdcp.newsclient.ui.activity;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.AndroidException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.MainViewPagerAdapter;
import com.gdcp.newsclient.skin.ISkinUpdate;
import com.gdcp.newsclient.skin.SkinPackageManager;
import com.gdcp.newsclient.skin.config.SkinConfig;
import com.gdcp.newsclient.ui.fragment.MainFragment01;
import com.gdcp.newsclient.ui.fragment.MainFragment02;
import com.gdcp.newsclient.ui.fragment.MainFragment03;
import com.gdcp.newsclient.ui.fragment.MainFragment04;
import com.gdcp.newsclient.ui.fragment.MainFragment05;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends BaseActivity implements ISkinUpdate {


    @BindView(R.id.toolbal)
    Toolbar toolbal;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tab_01)
    RadioButton tab01;
    @BindView(R.id.tab_02)
    RadioButton tab02;
    @BindView(R.id.tab_03)
    RadioButton tab03;
    @BindView(R.id.tab_04)
    RadioButton tab04;
    @BindView(R.id.tab_05)
    RadioButton tab05;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    private List<Fragment> fragmentList;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private static final String APK_NAME = "app-debug.apk";
    private static final String DEX_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/skin.apk";

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {
        initToolBar();
        initActionBarDrawerToggle();
        fragmentList = new ArrayList<>();
        fragmentList.add(new MainFragment01());
        fragmentList.add(new MainFragment02());
        fragmentList.add(new MainFragment03());
        fragmentList.add(new MainFragment04());
        fragmentList.add(new MainFragment05());
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mainViewPagerAdapter);
        initViewPagerListener();
        initRadioGroupListener();
        initNavigationViewListener();
        /*SkinPackageManager.getInstance(MainActivity.this).copyApkFromAssets(MainActivity.this,APK_NAME,DEX_PATH);*/
    }

    private void initActionBarDrawerToggle() {
         ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbal,0,0);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initToolBar() {
        setSupportActionBar(toolbal);
        toolbal.setTitleTextColor(Color.WHITE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SkinPackageManager.getInstance(this).getResources() != null) {
            updateTheme();
        }
    }

    private void initNavigationViewListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu01:
                        SkinConfig.getInstance(MainActivity.this).setSkinColor("night_btn_color");
                        loadSkin();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.menu02:
                       /* SkinPackageManager.getInstance(MainActivity.this).copyApkFromAssets(MainActivity.this, APK_NAME,
                                DEX_PATH);*/
                        SkinConfig.getInstance(MainActivity.this).setSkinColor("night_background");
                        loadSkin();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.menu03:
                        SkinConfig.getInstance(MainActivity.this).setSkinColor("colorAccent");
                        loadSkin();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.menu04:
                        SkinConfig.getInstance(MainActivity.this).setSkinColor("colorPrimary");
                        loadSkin();
                        drawerLayout.closeDrawers();
                        return true;

                }

                return false;
            }
        });
    }
    /**
     * 加载皮肤
     */
    private void loadSkin() {
        SkinPackageManager.getInstance(MainActivity.this).loadSkinAsync(DEX_PATH, new SkinPackageManager.LoadSkinCallBack() {
            @Override
            public void startLoadSkin() {
                Log.d("xiaowu", "startloadSkin");
            }

            @Override
            public void loadSkinSuccess() {
                Log.d("xiaowu", "loadSkinSuccess");
                // 然后这里更新主题
                updateTheme();
            }

            @Override
            public void loadSkinFail() {
                Log.d("xiaowu", "loadSkinFail");
            }
        });

    }


    private void initRadioGroupListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_01:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_02:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_03:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.tab_04:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.tab_05:
                        viewPager.setCurrentItem(4);
                        break;

                }
            }
        });
    }

    private void initViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.tab_01);
                        break;
                    case 1:
                        radioGroup.check(R.id.tab_02);
                        break;
                    case 2:
                        radioGroup.check(R.id.tab_03);
                        break;
                    case 3:
                        radioGroup.check(R.id.tab_04);
                        break;
                    case 4:
                        radioGroup.check(R.id.tab_05);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.toobar,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void updateTheme() {
          Resources resources=SkinPackageManager.getInstance(MainActivity.this).getResources();

             if (!SkinConfig.getInstance(MainActivity.this).getSkinColor().equals("")){
                 int background=resources.getIdentifier(SkinConfig.getInstance(MainActivity.this).getSkinColor(),"color","com.gdcp.skin");
                 drawerLayout.setBackgroundDrawable(resources.getDrawable(background));
                 if (SkinConfig.getInstance(MainActivity.this).getSkinColor().equals("night_background")){
                     int toolbalbackground=resources.getIdentifier("colorPrimary","color","com.gdcp.skin");
                     toolbal.setBackgroundDrawable(resources.getDrawable(toolbalbackground));
                 }else {
                     toolbal.setBackgroundDrawable(null);
                 }
             }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }


}
