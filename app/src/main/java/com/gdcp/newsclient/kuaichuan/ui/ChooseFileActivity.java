package com.gdcp.newsclient.kuaichuan.ui;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.AndroidCharacter;
import android.util.AndroidException;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.app.APPAplication;
import com.gdcp.newsclient.kuaichuan.common.BaseActivity;
import com.gdcp.newsclient.kuaichuan.entity.FileInfo;
import com.gdcp.newsclient.kuaichuan.fragment.FileInfoFragment;
import com.gdcp.newsclient.kuaichuan.receiver.SeletedFileListChangedBroadcastReceiver;
import com.gdcp.newsclient.kuaichuan.view.ShowSelectedFileInfoDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseFileActivity extends BaseActivity {
    /**
     * 获取文件的请求码
     */
    public static final int  REQUEST_CODE_GET_FILE_INFOS = 200;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.btn_selected)
    Button btnSelected;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.bottom_bar)
    LinearLayout bottomBar;
    @BindView(R.id.activity_choose_file)
    RelativeLayout activityChooseFile;
    /**
     * 应用，图片，音频， 视频 文件Fragment
     */
    FileInfoFragment mCurrentFragment;
    FileInfoFragment mApkInfoFragment;
    FileInfoFragment mJpgInfoFragment;
    FileInfoFragment mMp3InfoFragment;
    FileInfoFragment mMp4InfoFragment;
    /**
     * 更新文件列表的广播
     */
    SeletedFileListChangedBroadcastReceiver seletedFileListChangedBroadcastReceiver;
    /**
     * 选中文件列表的对话框
     */
    ShowSelectedFileInfoDialog mShowSelectedFileInfoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);
        ButterKnife.bind(this);
        init();
    }
    private void init(){
        getSupportActionBar().setTitle("选择文件");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GET_FILE_INFOS);
        }else{
            initData();//初始化数据
        }
    }

    private void initData() {
        mApkInfoFragment=FileInfoFragment.newInstance(FileInfo.TYPE_APK);
        mJpgInfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_JPG);
        mMp3InfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_MP3);
        mMp4InfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_MP4);
        mCurrentFragment = mApkInfoFragment;
        String[] titles = getResources().getStringArray(R.array.array_res);
        viewPager.setAdapter(new ResPagerAdapter(getSupportFragmentManager(),titles));
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        setSelectedViewStyle(false);
        mShowSelectedFileInfoDialog = new ShowSelectedFileInfoDialog(getContext());



        seletedFileListChangedBroadcastReceiver = new SeletedFileListChangedBroadcastReceiver() {
            @Override
            public void onSeletecdFileListChanged() {
                update();

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SeletedFileListChangedBroadcastReceiver.ACTION_CHOOSE_FILE_LIST_CHANGED);
        registerReceiver(seletedFileListChangedBroadcastReceiver, intentFilter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 更新选中文件列表的状态
     */
    private void update() {
        if (mApkInfoFragment!=null){
            mApkInfoFragment.updateFileInfoAdapter();
        }
        if(mJpgInfoFragment != null) {
            mJpgInfoFragment.updateFileInfoAdapter();
        }
        if(mMp3InfoFragment != null) {
            mMp3InfoFragment.updateFileInfoAdapter();
        }
        if(mMp4InfoFragment != null){
            mMp4InfoFragment.updateFileInfoAdapter();
        }
        //更新已选中Button
        getSelectedView();
    }

    @Override
    protected void onDestroy() {
        if(seletedFileListChangedBroadcastReceiver != null){
            unregisterReceiver(seletedFileListChangedBroadcastReceiver);
            seletedFileListChangedBroadcastReceiver = null;
        }
        super.onDestroy();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_GET_FILE_INFOS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initData();
            } else {


            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick({R.id.btn_selected, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_selected:
                if(mShowSelectedFileInfoDialog != null){
                    mShowSelectedFileInfoDialog.show();
                }
                break;
            case R.id.btn_next:
                if(!APPAplication.getAppContext().isFileInfoMapExist()){//不存在选中的文件
                    Toast.makeText(this, "请选择你要传输的文件", Toast.LENGTH_SHORT).show();
                    return;

                }
                //跳转到应用间传输
                startActivity(new Intent(this,ChooseReceiverActivity.class));
                break;
        }
    }

    public View getSelectedView() {
        //获取selectedView的时候，触发选择文件
        if (APPAplication.getAppContext().getFileInfoMap() != null && APPAplication.getAppContext().getFileInfoMap().size() > 0 ){
            setSelectedViewStyle(true);
            int size = APPAplication.getAppContext().getFileInfoMap().size();
            btnSelected.setText("已选("+size+")");
        }else {
            setSelectedViewStyle(false);
            btnSelected.setText("已选("+0+")");
        }
       return btnSelected;
    }
    /**
     * 设置选中View的样式
     * @param isEnable
     */
    private void setSelectedViewStyle(boolean isEnable) {
        if(isEnable){
            btnSelected.setEnabled(true);
            btnSelected.setBackgroundResource(R.drawable.selector_bottom_text_common);
            btnSelected.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else{
            btnSelected.setEnabled(false);
            btnSelected.setBackgroundResource(R.drawable.shape_bottom_text_unenable);
            btnSelected.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    class ResPagerAdapter extends FragmentPagerAdapter{
       String[]titles;
        public ResPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){ //应用
                mCurrentFragment = mApkInfoFragment;
            }else if(position == 1){ //图片
                mCurrentFragment = mJpgInfoFragment;
            }else if(position == 2){ //音乐
                mCurrentFragment = mMp3InfoFragment;
            }else if(position == 3){ //视频
                mCurrentFragment = mMp4InfoFragment;
            }
            return mCurrentFragment;
        }
        public ResPagerAdapter(FragmentManager fm, String[] titles) {
            this(fm);
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
