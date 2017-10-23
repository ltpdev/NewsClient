package com.gdcp.newsclient.kuaichuan.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * wifi扫描管理类
 */

public class WifiMgr {
    /**
     * 创建WifiConfiguration的类型,几种加密方式
     */
    public static final int WIFICIPHER_NOPASS = 1;
    public static final int WIFICIPHER_WEP = 2;
    public static final int WIFICIPHER_WPA = 3;
    private static WifiMgr wifiMgr;
    private Context context;
    private WifiManager wifiManager;

    //扫描的结果
    List<ScanResult>scanResultList;
    List<WifiConfiguration>wifiConfigurations;
    //当前WiFi配置的信息
    WifiInfo wifiInfo;

    private WifiMgr(Context context){
        this.context=context;
        this.wifiManager= (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }
    public static WifiMgr getInstance(Context context){
        if (wifiMgr==null){
            synchronized (WifiMgr.class){
                if (wifiMgr==null){
                    wifiMgr=new WifiMgr(context);
                }
            }
        }

        return wifiMgr;
    }

//打开wifi
    public  void openWifi(){
        if (!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
    }


    //关闭wifi
    public void closeWifi(){
        if (wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(false);
        }
    }

    /*
    * 判断WiFi是否开启的状态
    *
    * */
    public boolean isWifiEnable(){
        return wifiManager==null?false:wifiManager.isWifiEnabled();
    }

    /**
     * 开始wifi扫描
     */
    public void startScan(){
        wifiManager.startScan();
        scanResultList=wifiManager.getScanResults();
        wifiConfigurations=wifiManager.getConfiguredNetworks();
    }

    public List<ScanResult>getScanResultList(){
        return scanResultList;
    }

    public List<WifiConfiguration>getWifiConfigurations(){
        return wifiConfigurations;
    }
/*
* 添加到指定的WiFi网络,连接/切换指定的wifi网络
*
*
* */
public boolean addNetWork(WifiConfiguration wifiConfiguration){
    //断开当前的连接
    disconnectCurrentNetWork();
    //连接新的连接
    int netId=wifiManager.addNetwork(wifiConfiguration);
    boolean enable=wifiManager.enableNetwork(netId,true);
    return enable;
}


    //断开当前的连接
    public boolean  disconnectCurrentNetWork() {
      if (wifiManager!=null&&wifiManager.isWifiEnabled()){
          int netid=wifiManager.getConnectionInfo().getNetworkId();
          wifiManager.disableNetwork(netid);
          return wifiManager.disconnect();
      }
        return  false;
    }

//创建wificonfiguration
    public static WifiConfiguration createWifiCfg(String ssid,String password,int type){
       WifiConfiguration wifiConfiguration=new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.SSID="\""+ssid+"\"";
        if (type==WIFICIPHER_NOPASS){
             wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }else if (type==WIFICIPHER_WEP){
            wifiConfiguration.hiddenSSID=true;
            wifiConfiguration.wepKeys[0]="\""+password+"\"";
            wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfiguration.wepTxKeyIndex = 0;
        }else if(type==WIFICIPHER_WPA){
            wifiConfiguration.preSharedKey = "\""+password+"\"";
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
        }

        return wifiConfiguration;

    }



    //获取当前wifiinfo


    public WifiInfo getWifiInfo(){
        wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo;
    }
    //获取当前WiFi所分配的IP地址， when connect the hotspot, is still returning "0.0.0.0".
    public String getCurrentIpAddress(){
        String ipAddress="";
        int address=wifiManager.getDhcpInfo().ipAddress;
        ipAddress = ((address & 0xFF)
                + "." + ((address >> 8) & 0xFF)
                + "." + ((address >> 16) & 0xFF)
                + "." + ((address >> 24) & 0xFF));
        return ipAddress;
    }
/*设备连接WiFi热点之后，设备获取WiFi热点的ip地址
*
*
* */
public String getIpAddressFromHotspot(){
   String ipAddress="192.168.43.1";
    DhcpInfo dhcpInfo=wifiManager.getDhcpInfo();
    int address=dhcpInfo.gateway;
    ipAddress = ((address & 0xFF)
            + "." + ((address >> 8) & 0xFF)
            + "." + ((address >> 16) & 0xFF)
            + "." + ((address >> 24) & 0xFF));
    return ipAddress;
}
    /**
     * 开启热点之后，获取自身热点的IP地址
     * @return
     */
    public String getHotspotLocalIpAddress(){
        String ipAddress = "192.168.43.1";
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        int address = dhcpInfo.serverAddress;
        ipAddress=((address & 0xFF)
                + "." + ((address >> 8) & 0xFF)
                + "." + ((address >> 16) & 0xFF)
                + "." + ((address >> 24) & 0xFF));
        return ipAddress;


    }

    /**
     * 关闭Wifi
     */
    public void disableWifi(){
        if(wifiManager != null){
            wifiManager.setWifiEnabled(false);
        }
    }




}
