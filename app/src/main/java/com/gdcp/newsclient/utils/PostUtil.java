package com.gdcp.newsclient.utils;

import android.os.Handler;

import com.kingja.loadsir.core.LoadService;

/**
 * Created by asus- on 2017/9/26.
 */

public class PostUtil {

    public static void postCodeDelayed(final LoadService loadService, final int code, long delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadService.showWithConvertor(code);
            }
        }, delay);

    }
}
