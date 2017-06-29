package com.gdcp.newsclient.utils;

/**
 * Created by asus- on 2017/6/29.
 */

public class TimeUtil {
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }

    }
}
