package com.gdcp.newsclient.utils;

import android.content.Context;
import android.graphics.Color;

import com.gdcp.newsclient.douyuDanmu.client.DyBulletScreenClient;

import java.io.InputStream;
import java.util.HashMap;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;

/**
 *弹幕投影工具类
 */

public class DanmuProcess{
     private Context context;
     private IDanmakuView danmakuView;
     private DanmakuContext danmakuContext;
     private BaseDanmakuParser parser;
     private DyBulletScreenClient dyBulletScreenClient;
     private int roomId;

    public DanmuProcess(Context context, IDanmakuView danmakuView, int roomId) {
        this.context = context;
        this.danmakuView = danmakuView;
        this.roomId = roomId;
    }
    public void start() {
        initDanmaku();
        getAndAddDanmu();
    }

    //获取弹幕信息并添加到弹幕库框架里
    private void getAndAddDanmu() {
          Thread thread=new Thread(new Runnable() {
              @Override
              public void run() {
                  int groupId = -9999;
                  dyBulletScreenClient=DyBulletScreenClient.getInstance();
                  //设置需要连接和访问的房间ID，以及弹幕池分组号
                  dyBulletScreenClient.start(roomId, groupId);
                  dyBulletScreenClient.setHandMsgListener(new DyBulletScreenClient.HandMsgListener() {
                      @Override
                      public void handleDanMuMessage(String txt) {
                          addDanmaku(true, txt);
                      }

                      @Override
                      public void handleListViewMessage(String name,String txt) {
                          if (onDanmuDataComeListener!=null){
                              onDanmuDataComeListener.refreshListView(name,txt);
                          }
                      }
                  });
              }
          });
        ThreadUtil.executorService.execute(thread);
        //thread.start();
    }

    private void addDanmaku(boolean isAlive, String txt) {
        BaseDanmaku danmaku= danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);;
        if (danmaku == null || danmakuView == null) {
            return;
        }
        danmaku.text=txt;
        danmaku.padding=5;
        danmaku.priority=0;
        danmaku.isLive = isAlive;
        danmaku.textSize = 25;
        danmaku.textColor = Color.WHITE;
        danmaku.setTime(danmakuView.getCurrentTime());
        danmakuView.addDanmaku(danmaku);
    }

    public void finish() {
        //停止从服务器获取弹幕
        dyBulletScreenClient.stop();
    }

    //初始化弹幕配置
    private void initDanmaku() {
        danmakuContext=DanmakuContext.create();
        try {
            parser=createParser(null);
        }catch (Exception e){
             e.printStackTrace();
        }
        HashMap<Integer,Integer>maxLinesPair=new HashMap<>();
        //设置弹幕数据显示的最大行数
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 6);
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN,3)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f)
                .setScaleTextSize(1.2f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
        if (danmakuView!=null){
            danmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    danmakuView.start();
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void drawingFinished() {

                }
            });
            danmakuView.prepare(parser,danmakuContext);
            danmakuView.enableDanmakuDrawingCache(true);
        }

    }
//创建弹幕库解析器
    private BaseDanmakuParser createParser(InputStream stream) throws IllegalDataException {
        if (stream==null){
            return new BaseDanmakuParser() {
                @Override
                protected IDanmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader= DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
        loader.load(stream);
        BaseDanmakuParser parser=new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;
    }

    private OnDanmuDataComeListener onDanmuDataComeListener;

    public void setOnDanmuDataComeListener(OnDanmuDataComeListener onDanmuDataComeListener) {
        this.onDanmuDataComeListener = onDanmuDataComeListener;
    }

    public interface OnDanmuDataComeListener{
        void refreshListView(String name, String data);
 }

}
