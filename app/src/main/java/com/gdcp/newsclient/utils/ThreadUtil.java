package com.gdcp.newsclient.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by asus- on 2018/1/8.
 */

public class ThreadUtil{
    public static ExecutorService executorService= Executors.newCachedThreadPool();
}
