package com.gdcp.newsclient.kuaichuan.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AndroidException;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.app.APPAplication;
import com.gdcp.newsclient.kuaichuan.Constant;
import com.gdcp.newsclient.kuaichuan.common.BaseActivity;
import com.gdcp.newsclient.kuaichuan.entity.FileInfo;
import com.gdcp.newsclient.kuaichuan.entity.IpPortInfo;
import com.gdcp.newsclient.kuaichuan.receiver.WifiAPBroadcastReceiver;
import com.gdcp.newsclient.kuaichuan.utils.ApMgr;
import com.gdcp.newsclient.kuaichuan.utils.TextUtils;
import com.gdcp.newsclient.kuaichuan.utils.WifiMgr;
import com.gdcp.newsclient.kuaichuan.view.RadarLayout;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReceiverWaitingActivity extends BaseActivity {
    public static final int REQUEST_CODE_WRITE_SETTINGS = 7879;
    @BindView(R.id.tv_top_tip)
    TextView tvTopTip;
    @BindView(R.id.radarLayout)
    RadarLayout radarLayout;
    @BindView(R.id.iv_device)
    CircleImageView ivDevice;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.activity_receiver_waiting)
    RelativeLayout activityReceiverWaiting;
    WifiAPBroadcastReceiver wifiAPBroadcastReceiver;
    //是否初始化
    boolean isInitialized = false;
    /*
    * 与文件发送方通信的线程
    *
    * */
    Runnable mUdpServerRuannable;
    public static final int MSG_TO_FILE_RECEIVER_UI = 0X88;

   Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           if (msg.what==MSG_TO_FILE_RECEIVER_UI){
               System.out.println("debug..." + "news5");
               IpPortInfo ipPortInfo = (IpPortInfo) msg.obj;
               Bundle bundle=new Bundle();
               bundle.putSerializable(Constant.KEY_IP_PORT_INFO, ipPortInfo);
               Intent intent = new Intent(ReceiverWaitingActivity.this, FileReceiverActivity.class);
               intent.putExtras(bundle);
               startActivity(intent);
               finishNormal();
           }
       }
   };
    /**
     * 成功进入文件接收列表UI 调用的finishNormal()
     */
    private void finishNormal() {
        if(wifiAPBroadcastReceiver != null){
            unregisterReceiver(wifiAPBroadcastReceiver);
            wifiAPBroadcastReceiver = null;
        }

        closeSocket();

        this.finish();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_waiting);
        ButterKnife.bind(this);
        initWithGetPermission(this);
    }

    private void initWithGetPermission(Activity context) {
        boolean permission;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            permission= Settings.System.canWrite(context);
        }else {
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            init();
        }  else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
            } else {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_SETTINGS}, REQUEST_CODE_WRITE_SETTINGS);
            }
        }

    }


    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS && Settings.System.canWrite(this)){
            Log.d("TAG", "CODE_WRITE_SETTINGS_PERMISSION success");
            //do your code
            init();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do your code
            init();
        } else {
            // Permission Denied
            Toast.makeText(this, "用户拒绝授权", Toast.LENGTH_SHORT).show();
            //ToastUtils.show(this, getResources().getString(R.string.tip_permission_denied_and_not_modify_ap_info));
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
//初始化工作
    private void init() {
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        radarLayout.setUseRing(true);
        radarLayout.setColor(getResources().getColor(R.color.white));
        radarLayout.setCount(4);
        radarLayout.start();
        //初始化热点
        //1,先关闭wifi
        WifiMgr.getInstance(this).disableWifi();
        //2,关闭当前热点，再配置相应的热点
        if(ApMgr.isApOn(this)){
            ApMgr.disableAp(this);
        }
        wifiAPBroadcastReceiver=new WifiAPBroadcastReceiver() {
            @Override
            public void onWifiApEnabled() {
                if(!isInitialized){
                    mUdpServerRuannable = createSendMsgToFileSenderRunnable();
                    APPAplication.MAIN_EXECUTOR.execute(mUdpServerRuannable);
                    isInitialized = true;
                    tvDesc.setText("初始化完毕");
                    tvDesc.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvDesc.setText("正在等待连接....");
                        }
                    }, 2 * 1000);

                }
            }
        };
        IntentFilter filter=new IntentFilter(WifiAPBroadcastReceiver.ACTION_WIFI_AP_STATE_CHANGED);
         registerReceiver(wifiAPBroadcastReceiver,filter);
        ApMgr.isApOn(this);
        String ssid = TextUtils.isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE;
        ApMgr.configApState(this, ssid); // change Ap state :boolean
        tvDeviceName.setText(ssid);
        tvDesc.setText("正在初始化。。。");
    }
    /**
     * 创建发送UDP消息到文件发送方的服务线程
     */
    private Runnable createSendMsgToFileSenderRunnable() {

        return new Runnable() {
            @Override
            public void run() {
                try {
                    startFileReceiverServer(Constant.DEFAULT_SERVER_COM_PORT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 开启 文件接收方 通信服务 (必须在子线程执行)
     * @param serverPort
     * @throws Exception
     */
    DatagramSocket mDatagramSocket;
    private void startFileReceiverServer(int serverPort) throws Exception {
        System.out.println("debug..." + "rrrrr");
      //网络连接上，无法获取IP的问题
        int count=0;
        String localAddress = WifiMgr.getInstance(this).getHotspotLocalIpAddress();
        while(localAddress.equals(Constant.DEFAULT_UNKOWN_IP) && count <  Constant.DEFAULT_TRY_TIME){
            Thread.sleep(1000);
            localAddress = WifiMgr.getInstance(this).getHotspotLocalIpAddress();
           // Log.i(TAG, "receiver get local Ip ----->>>" + localAddress);
            count ++;
        }
        mDatagramSocket=new DatagramSocket(serverPort);
        byte[] receiveData = new byte[1024];
        byte[] sendData = null;
        while (true){
            System.out.println("debug..." + "news2");
            //1.接收文件发送方的消息
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            mDatagramSocket.receive(receivePacket);
            String msg = new String(receivePacket.getData()).trim();
            InetAddress inetAddress=receivePacket.getAddress();
            int port = receivePacket.getPort();
            if (msg!=null && msg.startsWith(Constant.MSG_FILE_RECEIVER_INIT)){
                System.out.println("debug..." + "news1");
                //Toast.makeText(ReceiverWaitingActivity.this, "准备跳跃eeee。。。", Toast.LENGTH_SHORT).show();
                // 进入文件接收列表界面 (文件接收列表界面需要 通知 文件发送方发送 文件开始传输UDP通知)
               // handler.obtainMessage(MSG_TO_FILE_RECEIVER_UI, new IpPortInfo(inetAddress, port)).sendToTarget();

                handler.obtainMessage(MSG_TO_FILE_RECEIVER_UI, new IpPortInfo(inetAddress, port)).sendToTarget();
/*
                Message message=new Message();
                message.what=MSG_TO_FILE_RECEIVER_UI;
                message.obj=new IpPortInfo(inetAddress, port);
                handler.sendMessage(message);*/
                //break;
            }else{ //接收发送方的 文件列表
                if(msg != null){
//                    FileInfo fileInfo = FileInfo.toObject(msg);
                    System.out.println("Get the FileInfo from FileReceiver######>>>" + msg);
                    parseFileInfo(msg);
                }
            }
        }
    }
    /**
     * 解析FileInfo
     * @param msg
     */
    private void parseFileInfo(String msg) {
        FileInfo fileInfo = FileInfo.toObject(msg);
        if(fileInfo != null && fileInfo.getFilePath() != null){
            APPAplication.getAppContext().addReceiverFileInfo(fileInfo);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (wifiAPBroadcastReceiver!=null){
            unregisterReceiver(wifiAPBroadcastReceiver);
            wifiAPBroadcastReceiver=null;
        }
        closeSocket();
        //关闭热点
        ApMgr.disableAp(this);
        this.finish();
    }
    /**
     * 关闭UDP Socket流
     */
    private void closeSocket() {
        if(mDatagramSocket != null){
            mDatagramSocket.disconnect();
            mDatagramSocket.close();
            mDatagramSocket = null;
        }
    }
}
