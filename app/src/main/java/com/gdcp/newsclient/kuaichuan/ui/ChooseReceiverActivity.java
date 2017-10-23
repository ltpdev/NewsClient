package com.gdcp.newsclient.kuaichuan.ui;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.AndroidCharacter;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.app.APPAplication;
import com.gdcp.newsclient.kuaichuan.BaseTransfer;
import com.gdcp.newsclient.kuaichuan.Constant;
import com.gdcp.newsclient.kuaichuan.adapter.WifiScanResultAdapter;
import com.gdcp.newsclient.kuaichuan.common.BaseActivity;
import com.gdcp.newsclient.kuaichuan.entity.FileInfo;
import com.gdcp.newsclient.kuaichuan.utils.ListUtils;
import com.gdcp.newsclient.kuaichuan.utils.NetUtils;
import com.gdcp.newsclient.kuaichuan.utils.WifiMgr;
import com.gdcp.newsclient.kuaichuan.view.RadarScanView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseReceiverActivity extends BaseActivity {

    @BindView(R.id.tv_top_tip)
    TextView tvTopTip;
    @BindView(R.id.radarView)
    RadarScanView radarView;
    @BindView(R.id.lv_result)
    ListView lvResult;
    List<ScanResult>scanResultList;
    WifiScanResultAdapter wifiScanResultAdapter;
    DatagramSocket mDatagramSocket;
    /*与文件发送方通信的线程
    * */
    Runnable updateServerRunnable;
    public static final int MSG_TO_FILE_SENDER_UI = 0X88;   //消息：跳转到文件发送列表UI
    public static final int MSG_TO_SHOW_SCAN_RESULT = 0X99; //消息：更新扫描可连接Wifi网络的列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_receiver);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        radarView.startScan();
        if (!WifiMgr.getInstance(ChooseReceiverActivity.this).isWifiEnable()){
            //wifi未打开的情况
            WifiMgr.getInstance(getContext()).openWifi();
        }
        //Android 6.0 扫描wifi 需要开启定位
        if (Build.VERSION.SDK_INT>=23){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_WIFI_STATE},REQUEST_CODE_OPEN_GPS);
            }

            return;
        }else{//Android 6.0 以下的直接开启扫描
            updateUI();
        }
        //updateUI();
    }

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==MSG_TO_FILE_SENDER_UI){
                //进入文件发送列表
                Toast.makeText(ChooseReceiverActivity.this, "进入文件发送列表", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChooseReceiverActivity.this, FileSenderActivity.class);
                startActivity(intent);
                finishNormal();
            }else if (msg.what==MSG_TO_SHOW_SCAN_RESULT){
                getOrUpdateWifiScanResult();
                handler.sendMessageDelayed(handler.obtainMessage(MSG_TO_SHOW_SCAN_RESULT), 1000);
                //handler.sendMessageDelayed(handler.obtainMessage(MSG_TO_SHOW_SCAN_RESULT), 1000);
            }
        }
    };
    /**
     * 成功进入文件发送列表UI 调用的finishNormal()
     */
    private void finishNormal() {
        closeSocket();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeSocket();
        WifiMgr.getInstance(getContext()).disconnectCurrentNetWork();
        finish();
    }

    /**
     * 关闭UDP Socket 流
     */
    private void closeSocket() {
        if (mDatagramSocket != null) {
            mDatagramSocket.disconnect();
            mDatagramSocket.close();
            mDatagramSocket = null;
        }
    }

    private void updateUI() {
        getOrUpdateWifiScanResult();
        //???????
        handler.sendMessageDelayed(handler.obtainMessage(MSG_TO_SHOW_SCAN_RESULT), 1000);
    }
    /**
     * 获取或者更新wifi扫描列表
     */
    private void getOrUpdateWifiScanResult() {
        WifiMgr.getInstance(getContext()).startScan();
        scanResultList = WifiMgr.getInstance(getContext()).getScanResultList();
        scanResultList = ListUtils.filterWithNoPassword(scanResultList);
        if (scanResultList != null) {
            wifiScanResultAdapter = new WifiScanResultAdapter(this, scanResultList);
            lvResult.setAdapter(wifiScanResultAdapter);
            lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO 进入文件传输部分
                    ScanResult scanResult = scanResultList.get(position);
                    //1.连接网络
                    String ssid = Constant.DEFAULT_SSID;
                    ssid = scanResult.SSID;
                    WifiMgr.getInstance(ChooseReceiverActivity.this).openWifi();
                    WifiMgr.getInstance(ChooseReceiverActivity.this).addNetWork(WifiMgr.createWifiCfg(ssid, null, WifiMgr.WIFICIPHER_NOPASS));
                    //2,发送udp通知信息到文件接收方，开启ServerSocketRunnable
                    updateServerRunnable = createSendMsgToServerRunnable(WifiMgr.getInstance(getContext()).getIpAddressFromHotspot());
                    APPAplication.MAIN_EXECUTOR.execute(updateServerRunnable);
                }
            });

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // 允许
                updateUI();
            } else {
                Toast.makeText(this, "用户拒绝授权", Toast.LENGTH_SHORT).show();
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
    /**
     * 创建发送UDP消息到文件接收方的服务线程
     * @param serverIP
     */
    private Runnable createSendMsgToServerRunnable(final String serverIP){

        return new Runnable() {
            @Override
            public void run() {
                try {
                    startFileSenderServer(serverIP, Constant.DEFAULT_SERVER_COM_PORT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
    /**
     * 开启 文件发送方 通信服务 (必须在子线程执行)
     * @param targetIpAddr
     * @param serverPort
     * @throws Exception
     */
    private void startFileSenderServer(String targetIpAddr, int serverPort) throws Exception {
        //确保wifi连接上之后获取得到ip地址
        int count=0;
        //、、、有问题
        while (targetIpAddr.equals(Constant.DEFAULT_UNKOWN_IP)&&count < Constant.DEFAULT_TRY_TIME){
            Thread.sleep(1000);
            targetIpAddr=WifiMgr.getInstance(ChooseReceiverActivity.this).getIpAddressFromHotspot();
            count ++;
        }
        // 即使获取到连接的热点wifi的IP地址也是无法连接网络 所以采取此策略
        count = 0;
        while(!NetUtils.pingIpAddress(targetIpAddr) && count < Constant.DEFAULT_TRY_TIME){
            Thread.sleep(500);
            count ++;
        }
        mDatagramSocket=new DatagramSocket(serverPort);
        byte[] receiveData = new byte[1024];
        byte[] sendData = null;
        InetAddress ipAddress = InetAddress.getByName(targetIpAddr);
        //1,发送，即将发送的文件列表到文件接收方
        sendFileInfoListToFileReceiverWithUdp(serverPort, ipAddress);
//2,发送文件接收方初始化
        sendData=Constant.MSG_FILE_RECEIVER_INIT.getBytes(BaseTransfer.UTF_8);
        DatagramPacket sendPacket=new DatagramPacket(sendData,sendData.length,ipAddress,serverPort);
        mDatagramSocket.send(sendPacket);
        //3.接收 文件接收方 初始化 反馈
        while (true){
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            mDatagramSocket.receive(receivePacket);
            String response = new String(receivePacket.getData(), BaseTransfer.UTF_8).trim();
            if(response != null && response.equals(Constant.MSG_FILE_RECEIVER_INIT_SUCCESS)){
                // youwenti进入文件发送列表界面 （并且通知文件接收方进入文件接收列表界面）
                handler.obtainMessage(MSG_TO_FILE_SENDER_UI).sendToTarget();
                //break;
            }
        }




    }

    /**
     * 发送即将发送的文件列表到文件接收方
     * @param serverPort
     * @param ipAddress
     *
     */


    private void sendFileInfoListToFileReceiverWithUdp(int serverPort, InetAddress ipAddress) {
        //1.1将发送的List<FileInfo> 发送给 文件接收方
        //如何将发送的数据列表封装成JSON
        Map<String,FileInfo>sendFileInfoMap=APPAplication.getAppContext().getFileInfoMap();
        List<Map.Entry<String,FileInfo>>fileInfoMapList=new ArrayList<Map.Entry<String, FileInfo>>(sendFileInfoMap.entrySet());
        List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
        //排序
        Collections.sort(fileInfoMapList,Constant.DEFAULT_COMPARATOR);
        for (Map.Entry<String,FileInfo>entry:fileInfoMapList){
            if (entry.getValue()!=null){
                FileInfo fileInfo=entry.getValue();
                String fileInfoStr=FileInfo.toJsonStr(fileInfo);
                DatagramPacket sendFileInfoListPacket=new DatagramPacket(fileInfoStr.getBytes(),fileInfoStr.getBytes().length, ipAddress, serverPort);
                try {
                    mDatagramSocket.send(sendFileInfoListPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
