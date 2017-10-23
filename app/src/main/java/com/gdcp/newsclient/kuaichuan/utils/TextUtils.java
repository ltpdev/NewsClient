package com.gdcp.newsclient.kuaichuan.utils;

/**
 * Created by asus- on 2017/9/28.
 */

public class TextUtils {
    /**
     * 判断文本是否Null或者是空白
     * @param str
     * @return
     */
    public static boolean isNullOrBlank(String str){
        return str == null || str.equals("");
    }
}
