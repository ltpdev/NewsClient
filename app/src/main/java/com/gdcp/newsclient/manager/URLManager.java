package com.gdcp.newsclient.manager;

/**
 * Created by asus- on 2017/6/28.
 */

public class URLManager {
    public static String getUrl(String channelId){
        return "http://c.m.163.com/nc/article/headline/" + channelId + "/0-20.html";
    }

    public static String getLoadMoreUrl(String channelId,int num){
        return "http://c.m.163.com/nc/article/headline/" + channelId + "/"+num+"-20.html";
    }

    public static final String VideoURL =
            "http://c.m.163.com/nc/video/list/V9LG4B3A0/y/0-20.html";

    public static String getLoadMoreVideoURL(int num){
        return "http://c.m.163.com/nc/video/list/V9LG4B3A0/y/"+num+"-20.html";
    }

}
