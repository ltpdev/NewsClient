package com.gdcp.newsclient.kuaichuan.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.app.APPAplication;
import com.gdcp.newsclient.kuaichuan.Constant;
import com.gdcp.newsclient.kuaichuan.FileSender;
import com.gdcp.newsclient.kuaichuan.adapter.FileSenderAdapter;
import com.gdcp.newsclient.kuaichuan.common.BaseActivity;
import com.gdcp.newsclient.kuaichuan.entity.FileInfo;
import com.gdcp.newsclient.kuaichuan.utils.FileUtils;
import com.gdcp.newsclient.kuaichuan.utils.WifiMgr;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileSenderActivity extends BaseActivity {
    /**
     * 进度条 已传 耗时等UI组件
     */
    @BindView(R.id.pb_total)
    ProgressBar pbTotal;
    @BindView(R.id.tv_unit_storage)
    TextView tvUnitStorage;
    @BindView(R.id.tv_unit_has_send)
    TextView tvUnitHasSend;
    @BindView(R.id.tv_value_storage)
    TextView tvValueStorage;
    @BindView(R.id.tv_value_time)
    TextView tvValueTime;
    @BindView(R.id.tv_unit_time)
    TextView tvUnitTime;
    @BindView(R.id.tv_unit_has_time)
    TextView tvUnitHasTime;
    @BindView(R.id.lv_result)
    ListView lvResult;
    List<Map.Entry<String,FileInfo>>mFileInfoMapList;
    List<FileSender> mFileSenderList = new ArrayList<FileSender>();
    //所有总文件的进度
    long mTotalLen=0;
    //每次传送的偏移量
    long currentOffset=0;
    //每个文件传送onProgress() 之前的进度
    long lastUpdateLen=0;
    String[]storageArray=null;

    long totalTime=0;
    long currentTimeOffset=0;
    long lastUpdateTime=0;
    String[]timeArray=null;
    int hasSendedFileCount=0;
    FileSenderAdapter mFileSenderAdapter;
    public static final int MSG_UPDATE_FILE_INFO=0X6666;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==MSG_UPDATE_FILE_INFO){
                updateTotalProgressView();
                if (mFileSenderAdapter!=null){
                    mFileSenderAdapter.notifyDataSetChanged();
                }
            }
        }
    };
    /**
     * 更新进度 和 耗时的 View
     */
    private void updateTotalProgressView() {
         //设置传送的总容量大小
        storageArray= FileUtils.getFileSizeArrayStr(mTotalLen);
        tvValueStorage.setText(storageArray[0]);
        tvUnitStorage.setText(storageArray[1]);

        //设置传送的时间情况
        timeArray=FileUtils.getTimeByArrayStr(totalTime);
        tvValueTime.setText(timeArray[0]);
        tvUnitTime.setText(timeArray[1]);
        //设置传送的进度条情况
        if (hasSendedFileCount==APPAplication.getAppContext().getFileInfoMap().size()){
            pbTotal.setProgress(0);
            tvValueStorage.setTextColor(getResources().getColor(R.color.color_yellow));
            tvValueTime.setTextColor(getResources().getColor(R.color.color_yellow));
            return;
        }

        long total=APPAplication.getAppContext().getAllSendFileInfoSize();
        int percent= (int) (mTotalLen*100/total);
        pbTotal.setProgress(percent);
        if (total==mTotalLen){
            pbTotal.setProgress(0);
            tvValueStorage.setTextColor(getResources().getColor(R.color.color_yellow));
            tvValueTime.setTextColor(getResources().getColor(R.color.color_yellow));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_sender);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void onBackPressed() {
        //需要判断是否有文件在发送？
        if (hasFileSending()){
            showExistDialog();
            return;
        }

        finishNormal();
    }

    private void showExistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("还有任务在运行，确定要退出?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishNormal();
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }
    /**
     * 判断是否有文件在传送
     */
    private boolean hasFileSending() {
        for (FileSender fileSender:mFileSenderList){
            if (fileSender.isRunning()){
                return true;
            }
        }
        return false;
    }

    private void init(){
       getSupportActionBar().setTitle("传送门");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pbTotal.setMax(100);
        mFileSenderAdapter=new FileSenderAdapter(this);
        lvResult.setAdapter(mFileSenderAdapter);
        List<Map.Entry<String,FileInfo>>fileInfoMapList=new ArrayList<Map.Entry<String, FileInfo>>(APPAplication.getAppContext().getFileInfoMap().entrySet());
        mFileInfoMapList=fileInfoMapList;
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_FILE);
        }else{
            initSendServer(fileInfoMapList);//开启传送文件
        }


    }

    private void initSendServer(List<Map.Entry<String, FileInfo>> fileInfoMapList) {
         String serverIp= WifiMgr.getInstance(this).getIpAddressFromHotspot();
        for (Map.Entry<String,FileInfo>entry:fileInfoMapList){
            final FileInfo fileInfo=entry.getValue();
            FileSender fileSender=new FileSender(this,fileInfo,serverIp,Constant.DEFAULT_SERVER_PORT);
            fileSender.setOnSendListener(new FileSender.OnSendListener() {
                @Override
                public void onStart() {
                    lastUpdateLen=0;
                    lastUpdateTime=System.currentTimeMillis();

                }

                @Override
                public void onProgress(long progress, long total) {
                    //TODO 更新
                    //=====更新进度 流量 时间视图 start ====//
                    currentOffset=progress-lastUpdateLen>0?progress-lastUpdateLen:0;
                    mTotalLen = mTotalLen + currentOffset;
                    lastUpdateLen = progress;
                    currentTimeOffset=System.currentTimeMillis()-lastUpdateTime>0?System.currentTimeMillis() - lastUpdateTime : 0;
                    totalTime=totalTime+currentTimeOffset;
                    lastUpdateTime= System.currentTimeMillis();
                    fileInfo.setProcceed(progress);
                    APPAplication.getAppContext().updateFileInfo(fileInfo);
                    handler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);


                }

                @Override
                public void onSuccess(FileInfo fileInfo) {
                     hasSendedFileCount++;
                    mTotalLen=mTotalLen+(fileInfo.getSize()-lastUpdateLen);
                    lastUpdateLen=0;
                    lastUpdateTime=System.currentTimeMillis();
                    fileInfo.setResult(FileInfo.FLAG_SUCCESS);
                    APPAplication.getAppContext().updateFileInfo(fileInfo);
                    handler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);
                }

                @Override
                public void onFailure(Throwable t, FileInfo fileInfo) {
                    hasSendedFileCount++;
                    fileInfo.setResult(FileInfo.FLAG_FAILURE);
                    APPAplication.getAppContext().updateFileInfo(fileInfo);
                    handler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);


                }
            });
            mFileSenderList.add(fileSender);
            APPAplication.FILE_SENDER_EXECUTOR.execute(fileSender);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initSendServer(mFileInfoMapList);//开启传送文件
            } else {
                // Permission Denied
                //ToastUtils.show(this, getResources().getString(R.string.tip_permission_denied_and_not_send_file));
                finishNormal();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void finishNormal() {
        stopAllFileSendingTask();
        APPAplication.getAppContext().getFileInfoMap().clear();
        this.finish();
    }
    /**
     * 停止所有的文件发送任务
     */
    private void stopAllFileSendingTask() {
        for (FileSender fileSender:mFileSenderList){
            if (fileSender!=null){
                fileSender.stop();
        }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
