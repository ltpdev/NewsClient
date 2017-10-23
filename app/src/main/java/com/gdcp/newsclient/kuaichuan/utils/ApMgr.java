package com.gdcp.newsclient.kuaichuan.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

/**
 * 热点管理工具类
 */



public class ApMgr {
    //检查WiFi热点是否打开
    public static boolean isApOn(Context context){
        //???
        WifiManager wifiManager= (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        try {
            //使用反射来访问WiFi热点
            Method method=wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(wifiManager);
        } catch (Throwable ignored) {

        }
        return false;
    }

    //关闭WiFi热点
    public static void  disableAp(Context context){
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try{
            Method method=wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class,boolean.class);
            method.invoke(wifimanager,null,false);
        }catch (Throwable ignored){

        }
    }
//切换WiFi热点的状态
    public static boolean configApState(Context context){
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wifiConfiguration=null;
        try {
            if (isApOn(context)){
                //关闭WiFi
                wifimanager.setWifiEnabled(false);
                disableAp(context);
            }
            Method method=wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class,boolean.class);
            method.invoke(wifimanager,wifiConfiguration,!isApOn(context));
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    //切换WiFi热点的状态,并且带上热点的名字
    public static boolean configApState(Context context,String appName){
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wifiConfiguration=null;
        try {
            wifiConfiguration=new WifiConfiguration();
            wifiConfiguration.SSID=appName;
            if (isApOn(context)){
                wifimanager.setWifiEnabled(false);
                disableAp(context);
            }
            Method method=wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class,boolean.class);
            method.invoke(wifimanager,wifiConfiguration,!isApOn(context));
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }



}
