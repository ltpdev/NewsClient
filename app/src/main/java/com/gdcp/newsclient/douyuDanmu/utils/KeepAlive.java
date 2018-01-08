package com.gdcp.newsclient.douyuDanmu.utils;

import com.gdcp.newsclient.douyuDanmu.client.DyBulletScreenClient;

/**
 * Created by asus- on 2018/1/8.
 */

public class KeepAlive extends Thread{

    @Override
    public void run() {
        super.run();
        //获取弹幕客户端
        DyBulletScreenClient dyBulletScreenClient=DyBulletScreenClient.getInstance();
        //判断客户端就绪状态
        while (dyBulletScreenClient.getReadyFlag()){
                  //发送心跳保持协议给服务器端
            dyBulletScreenClient.keepAlive();
            try {
                //设置间隔45秒再发送心跳协议
                Thread.sleep(45000);
            }catch (Exception e){
                 e.printStackTrace();
            }

        }
    }
}
