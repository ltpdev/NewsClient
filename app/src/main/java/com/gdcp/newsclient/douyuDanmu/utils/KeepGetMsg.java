package com.gdcp.newsclient.douyuDanmu.utils;

import com.gdcp.newsclient.douyuDanmu.client.DyBulletScreenClient;

/**
 * Created by asus- on 2018/1/8.
 */

public class KeepGetMsg extends Thread{

    @Override
    public void run() {
        super.run();
        ////获取弹幕客户端
        DyBulletScreenClient danmuClient = DyBulletScreenClient.getInstance();

        //判断客户端就绪状态
        while(danmuClient.getReadyFlag())
        {
            //获取服务器发送的弹幕信息
            danmuClient.getServerMsg();
        }
    }
}

