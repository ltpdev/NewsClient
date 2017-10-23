package com.gdcp.newsclient.kuaichuan.ui;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.AndroidCharacter;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.app.APPAplication;
import com.gdcp.newsclient.kuaichuan.BaseTransfer;
import com.gdcp.newsclient.kuaichuan.Constant;
import com.gdcp.newsclient.kuaichuan.FileReceiver;
import com.gdcp.newsclient.kuaichuan.adapter.FileReceiverAdapter;
import com.gdcp.newsclient.kuaichuan.common.BaseActivity;
import com.gdcp.newsclient.kuaichuan.entity.FileInfo;
import com.gdcp.newsclient.kuaichuan.entity.IpPortInfo;
import com.gdcp.newsclient.kuaichuan.utils.ApMgr;
import com.gdcp.newsclient.kuaichuan.utils.FileUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileReceiverActivity extends BaseActivity {

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
    FileInfo mCurFileInfo;
    IpPortInfo mIpPortInfo;
    ServerRunnable mReceiverServer;
    long mTotalLen = 0;     //所有总文件的进度
    long mCurOffset = 0;    //每次传送的偏移量
    long mLastUpdateLen = 0; //每个文件传送onProgress() 之前的进度
    String[] mStorageArray = null;


    long mTotalTime = 0;
    long mCurTimeOffset = 0;
    long mLastUpdateTime = 0;
    String[] mTimeArray = null;

    int mHasSendedFileCount = 0;
    public static final int MSG_FILE_RECEIVER_INIT_SUCCESS = 0X4444;
    public static final int MSG_ADD_FILE_INFO = 0X5555;
    public static final int MSG_UPDATE_FILE_INFO = 0X6666;
    FileReceiverAdapter mFileReceiverAdapter;
    DatagramSocket mDatagramSocket;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_FILE_RECEIVER_INIT_SUCCESS){
                sendMsgToFileSender(mIpPortInfo);
            }else if(msg.what == MSG_ADD_FILE_INFO){
                //ADD FileInfo 到 Adapter
                FileInfo fileInfo = (FileInfo) msg.obj;
                Toast.makeText(FileReceiverActivity.this, "收到一个任务：" + fileInfo.getFilePath(), Toast.LENGTH_SHORT).show();
                //ToastUtils.show(getContext(), "收到一个任务：" + (fileInfo != null ? fileInfo.getFilePath() : ""));
            }else if(msg.what == MSG_UPDATE_FILE_INFO){
                //ADD FileInfo 到 Adapter
                updateTotalProgressView();
                if(mFileReceiverAdapter != null) mFileReceiverAdapter.update();
            }
        }
    };

    private void updateTotalProgressView() {
        try {
            mStorageArray = FileUtils.getFileSizeArrayStr(mTotalLen);
            tvValueStorage.setText(mStorageArray[0]);
            tvUnitStorage.setText(mStorageArray[1]);

            //设置传送的时间情况
            mTimeArray = FileUtils.getTimeByArrayStr(mTotalTime);
            tvValueTime.setText(mTimeArray[0]);
            tvUnitTime.setText(mTimeArray[1]);


            //设置传送的进度条情况
            if (mHasSendedFileCount == APPAplication.getAppContext().getReceiverFileInfoMap().size()) {
                pbTotal.setProgress(0);
                tvValueStorage.setTextColor(getResources().getColor(R.color.color_yellow));
                tvValueTime.setTextColor(getResources().getColor(R.color.color_yellow));
                return;
            }

            long total = APPAplication.getAppContext().getAllReceiverFileInfoSize();
            int percent = (int) (mTotalLen * 100 / total);
            pbTotal.setProgress(percent);

            if (total == mTotalLen) {
                pbTotal.setProgress(0);
                tvValueStorage.setTextColor(getResources().getColor(R.color.color_yellow));
                tvValueTime.setTextColor(getResources().getColor(R.color.color_yellow));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void sendMsgToFileSender(final IpPortInfo ipPortInfo) {
        new Thread(){
            @Override
            public void run() {
                try {
                    sendFileReceiverInitSuccessMsgToFileSender(ipPortInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * 通知文件发送方 ===>>> 文件接收方初始化完毕
     */
    public void sendFileReceiverInitSuccessMsgToFileSender(IpPortInfo ipPortInfo) throws Exception{
       mDatagramSocket=new DatagramSocket(ipPortInfo.getPort() +1);
        byte[] receiveData = new byte[1024];
        byte[] sendData = null;
        InetAddress ipAddress = ipPortInfo.getInetAddress();
        //1.发送 文件接收方 初始化
        sendData = Constant.MSG_FILE_RECEIVER_INIT_SUCCESS.getBytes(BaseTransfer.UTF_8);
        DatagramPacket sendPacket =
                new DatagramPacket(sendData, sendData.length, ipAddress, ipPortInfo.getPort());
        mDatagramSocket.send(sendPacket);




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_receiver);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        //界面初始化
        getSupportActionBar().setTitle("接收文件列表");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFileReceiverAdapter = new FileReceiverAdapter(getContext());
        lvResult.setAdapter(mFileReceiverAdapter);
        mIpPortInfo = (IpPortInfo) getIntent().getSerializableExtra(Constant.KEY_IP_PORT_INFO);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_FILE);
        }else{
            initServer(); //启动接收服务
        }
    }

    private void initServer() {
        //有点问题？？？
        mReceiverServer = new ServerRunnable(Constant.DEFAULT_SERVER_PORT);

        new Thread(mReceiverServer).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initServer(); //启动接收服务
            } else {
                // Permission Denied
                //ToastUtils.show(this, getResources().getString(R.string.tip_permission_denied_and_not_receive_file));
                onBackPressed();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //关闭TCP UDP 资源
        //清除选中文件的信息
        //关闭热点

        if(mReceiverServer != null){
            mReceiverServer.close();
            mReceiverServer = null;
        }
        closeSocket();

        APPAplication.getAppContext().getReceiverFileInfoMap().clear();
        ApMgr.disableAp(getContext());
        this.finish();
    }


    /**
     * 关闭UDP Socket 流
     */
    private void closeSocket(){
        if(mDatagramSocket != null){
            mDatagramSocket.disconnect();
            mDatagramSocket.close();
            mDatagramSocket = null;
        }
    }
    /**
     * ServerSocket启动线程
     */
    class ServerRunnable implements Runnable{
        ServerSocket serverSocket;
        private int port;
        public ServerRunnable(int port){
            this.port = port;
        }
        @Override
        public void run() {
            try {
                serverSocket=new ServerSocket(Constant.DEFAULT_SERVER_PORT);
                mHandler.obtainMessage(MSG_FILE_RECEIVER_INIT_SUCCESS).sendToTarget();
               while (!Thread.currentThread().isInterrupted()){
                   Socket socket=serverSocket.accept();
                   //生成缩略图
                   FileReceiver fileReceiver=new FileReceiver(socket);
                   fileReceiver.setOnReceiveListener(new FileReceiver.OnReceiveListener() {
                       @Override
                       public void onStart() {
                           mLastUpdateLen = 0;
                           mLastUpdateTime = System.currentTimeMillis();
                       }

                       @Override
                       public void onGetFileInfo(FileInfo fileInfo) {
                           mHandler.obtainMessage(MSG_ADD_FILE_INFO, fileInfo).sendToTarget();
                           mCurFileInfo = fileInfo;
                           APPAplication.getAppContext().addReceiverFileInfo(mCurFileInfo);
                           mHandler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);

                       }

                       @Override
                       public void onGetScreenshot(Bitmap bitmap) {


                       }

                       @Override
                       public void onProgress(long progress, long total) {
                           mCurOffset = progress - mLastUpdateLen > 0 ? progress - mLastUpdateLen : 0;
                           mTotalLen = mTotalLen + mCurOffset;
                           mLastUpdateLen = progress;

                           mCurTimeOffset = System.currentTimeMillis() - mLastUpdateTime > 0 ? System.currentTimeMillis() - mLastUpdateTime : 0;
                           mTotalTime = mTotalTime + mCurTimeOffset;
                           mLastUpdateTime = System.currentTimeMillis();
                           //=====更新进度 流量 时间视图 end ====//

                           mCurFileInfo.setProcceed(progress);
                           APPAplication.getAppContext().updateReceiverFileInfo(mCurFileInfo);
                           mHandler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);
                       }

                       @Override
                       public void onSuccess(FileInfo fileInfo) {
                           //=====更新进度 流量 时间视图 start ====//
                           mHasSendedFileCount ++;

                           mTotalLen = mTotalLen + (fileInfo.getSize() - mLastUpdateLen);
                           mLastUpdateLen = 0;
                           mLastUpdateTime = System.currentTimeMillis();
                           //=====更新进度 流量 时间视图 end ====//

                           fileInfo.setResult(FileInfo.FLAG_SUCCESS);
                           APPAplication.getAppContext().updateReceiverFileInfo(fileInfo);
                           mHandler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);
                       }

                       @Override
                       public void onFailure(Throwable t, FileInfo fileInfo) {
                           mHasSendedFileCount ++;//统计发送文件

                           fileInfo.setResult(FileInfo.FLAG_FAILURE);
                           APPAplication.getAppContext().updateFileInfo(fileInfo);
                           mHandler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);
                       }
                   });
                   APPAplication.getAppContext().MAIN_EXECUTOR.execute(fileReceiver);

               }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * 关闭Socket 通信 (避免端口占用)
         */
        public void close(){
            //rrr
            if(serverSocket != null){
                try {
                    serverSocket.close();
                    serverSocket = null;
                } catch (IOException e) {
                }
            }
        }

    }
}
