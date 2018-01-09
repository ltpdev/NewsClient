package com.gdcp.newsclient.ui.activity;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.AndroidException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.MainViewPagerAdapter;
import com.gdcp.newsclient.app.APPAplication;
import com.gdcp.newsclient.config.AppConfig;
import com.gdcp.newsclient.event.DayNightEvent;
import com.gdcp.newsclient.kuaichuan.ui.HomeActivity;
import com.gdcp.newsclient.ui.fragment.MainFragment01;
import com.gdcp.newsclient.ui.fragment.MainFragment02;
import com.gdcp.newsclient.ui.fragment.MainFragment03;
import com.gdcp.newsclient.ui.fragment.MainFragment04;
import com.gdcp.newsclient.ui.fragment.MainFragment05;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MainActivity extends BaseActivity {


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
    private ImageView ivcon;
    private TextView tvName;
    private Tencent mTencent;
    private LoginUiListener loginUiListener;

    @Override
    protected void initData() {
        //沉浸式
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == View.SYSTEM_UI_FLAG_VISIBLE) {
                    onWindowFocusChanged(true);
                }
            }
        });
        int index = getIntent().getIntExtra("index", 0);
        viewPager.setCurrentItem(index);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {
        checkPermissions();
        mTencent = Tencent.createInstance("1106185205", this.getApplicationContext());
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
        View view = navigationView.getHeaderView(0);
        initHeaderViewListener(view);
        checkLoginState();

    }

    private void checkLoginState() {
        if (!mTencent.isSessionValid()) {
            ivcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginQQ();
                }
            });
        } else {
            showToast("已经登录了");
            getUserinfo();

        }


    }

    private void getUserinfo() {
        QQToken qqToken = mTencent.getQQToken();
        UserInfo mUserInfo = new UserInfo(getApplicationContext(), qqToken);
        mUserInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object response) {
                JSONObject jo = (JSONObject) response;
                try {
                    String nickName = jo.getString("nickname");
                    String headUrl = jo.getString("figureurl_2");
                    Glide.with(MainActivity.this).load(headUrl).into(ivcon);
                    tvName.setText(nickName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(UiError uiError) {
                //Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                //Toast.makeText(MainActivity.this, "登录取消", Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void initHeaderViewListener(View view) {
        ivcon = (ImageView) view.findViewById(R.id.icon_user);
        tvName = (TextView) view.findViewById(R.id.name_user);
    }

    //登录qq获取个人信息
    private void LoginQQ() {
        loginUiListener = new LoginUiListener();
        //all表示获取所有权限
        mTencent.login(MainActivity.this, "all", loginUiListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class LoginUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                getUserinfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }
    }


    private void initActionBarDrawerToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbal, 0, 0);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

//檢查內存卡權限
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1002);

        }else {

        }
    }

    private void initToolBar() {
        setSupportActionBar(toolbal);
       /* toolbal.setTitleTextColor(Color.WHITE);*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initNavigationViewListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu01:
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.menu02:
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.menu03:
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.menu04:
                        drawerLayout.closeDrawers();
                        return true;
                }

                return false;
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
                if (position!=1){
                    JCVideoPlayerStandard.releaseAllVideos();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.setting01:
                startActivity(false,HomeActivity.class);
                return true;
            case R.id.setting02:
                return true;

        }

        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DayNightEvent event) {
        if (event.isNight()) {
            getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);//切换夜间模式

            reStartActivity();
        } else {
            getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//切换日间模式

            reStartActivity();
        }
    }

    ;


    private void reStartActivity() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("index", 4);
        viewPager.setCurrentItem(4);
        startActivity(intent);
        overridePendingTransition(R.anim.animo_alph_start, R.anim.animo_alph_close);
        finish();


    }


    private void refreshResources(Activity activity) {
        AppConfig appConfig = new AppConfig(getApplicationContext());
        if (appConfig.isNightTheme()) {
            updateConfig(activity, Configuration.UI_MODE_NIGHT_YES);
        } else {
            updateConfig(activity, Configuration.UI_MODE_NIGHT_NO);
        }
    }

    /**
     * google官方bug，暂时解决方案 * 手机切屏后重新设置UI_MODE
     * 模式（因为在DayNight主题下，切换横屏后UI_MODE会出错，会导致
     * 资源获取出错，需要重新设置回来）好像这个方法无效
     */
    private void updateConfig(Activity activity, int uiNightMode) {
        Configuration newConfig = new
                Configuration(activity.getResources().getConfiguration());
        newConfig.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        newConfig.uiMode |= uiNightMode;
        activity.getResources().updateConfiguration(newConfig, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
