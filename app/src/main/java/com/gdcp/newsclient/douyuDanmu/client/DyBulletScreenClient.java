package com.gdcp.newsclient.douyuDanmu.client;

import android.util.Log;

import com.gdcp.newsclient.douyuDanmu.msg.DyMessage;
import com.gdcp.newsclient.douyuDanmu.msg.MsgView;
import com.gdcp.newsclient.douyuDanmu.utils.KeepAlive;
import com.gdcp.newsclient.douyuDanmu.utils.KeepGetMsg;
import com.gdcp.newsclient.utils.ThreadUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;

/**
 * Created by asus- on 2018/1/8.
 */

public class DyBulletScreenClient{
    private static final String TAG = "DyBulletScreenClient";
    private static DyBulletScreenClient instance;
    //第三方弹幕协议服务器地址
    private static final String hostName = "openbarrage.douyutv.com";
    //第三方弹幕协议服务器端口
    private static final int port = 8601;
    //设置字节获取buffer的最大值
    private static final int MAX_BUFFER_LENGTH = 4096;

    //socket配置
    private Socket socket;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;

    //获取弹幕线程及心跳线程运行和停止标记
    private boolean readyFlag=false;

    private DyBulletScreenClient(){

    }

    public static DyBulletScreenClient getInstance(){
        if (instance==null){
            instance=new DyBulletScreenClient();
        }
        return instance;
    }

    public interface HandMsgListener{
        void handleMessage(String txt);

    }
    private HandMsgListener handMsgListener;

    public void setHandMsgListener(HandMsgListener handMsgListener) {
        this.handMsgListener = handMsgListener;
    }
  private boolean isHandleMsgListenerNull(){
        return handMsgListener==null;
  }

public void start(int roomId,int groupId){
      init(roomId,groupId);
    KeepAlive keepAlive = new KeepAlive();
    //启动向弹幕服务器发送心跳请求的线程
    //keepAlive.start();
    ThreadUtil.executorService.execute(keepAlive);
//启动获取弹幕信息的线程
    KeepGetMsg keepGetMsg = new KeepGetMsg();
    ThreadUtil.executorService.execute(keepGetMsg);
    //keepGetMsg.start();
}

    public  boolean getReadyFlag(){
      return readyFlag;
    }


//客户端初始化，连接弹幕服务器并登陆房间及弹幕池
    private void init(int roomId, int groupId) {
        //连接弹幕服务器
        this.connectServer();
        //登陆指定房间
        this.loginRoom(roomId);
        //加入指定的弹幕池
        this.joinGroup(roomId, groupId);
        //设置客户端就绪标记为就绪状态
        readyFlag = true;
    }
//加入弹幕分组池
    private void joinGroup(int roomId, int groupId) {
        //获取弹幕服务器加弹幕池请求数据包
        byte[]joinGroupRequest=DyMessage.getJoinGroupRequest(roomId, groupId);
        try {
            //往弹幕服务器发送加入弹幕池请求数据
            bos.write(joinGroupRequest,0,joinGroupRequest.length);
            bos.flush();

        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "joinGroup: Send join group request failed!");
        }
    }
//登录指定房间
    private void loginRoom(int roomId) {
         //获取弹幕服务器登陆请求数据包
        byte[]loginRequestData = DyMessage.getLoginRequestData(roomId);
        try{
            //发送登陆请求数据包给弹幕服务器
            bos.write(loginRequestData,0,loginRequestData.length);
            bos.flush();
            //初始化弹幕服务器返回值读取包大小
            byte[]recvByte=new byte[MAX_BUFFER_LENGTH];
            //获取弹幕服务器返回值
            bis.read(recvByte,0,recvByte.length);

            //解析服务器返回的登录信息
            if(DyMessage.parseLoginRespond(recvByte)){
                Log.d(TAG, "loginRoom: Receive login response successfully!");
            } else {
                Log.d(TAG, "loginRoom: Receive login response failed!!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //连接弹幕服务器
    private void connectServer() {
            try{
                String host= InetAddress.getByName(hostName).getHostAddress();
                //建立socket连接
                socket=new Socket(host,port);
                //设置socket输入及输出
                bos=new BufferedOutputStream(socket.getOutputStream());
                bis=new BufferedInputStream(socket.getInputStream());
            }catch (Exception e){
                e.printStackTrace();

            }
    }
// 服务器心跳连接
    public void keepAlive(){
//获取与弹幕服务器保持心跳的请求数据包
        byte[] keepAliveRequest = DyMessage.getKeepAliveData((int)(System.currentTimeMillis() / 1000));
       try{
           //向弹幕服务器发送心跳请求数据包
           bos.write(keepAliveRequest,0,keepAliveRequest.length);
           bos.flush();
           Log.d(TAG, "keepAlive: Send keep alive request successfully!");
       }catch (Exception e){
           e.printStackTrace();
           Log.e(TAG, "keepAlive: Send keep alive request failed!");
       }

}
    public void stop() {
        unInit();
    }

    public void unInit() {
        readyFlag = false;
    }

//获取服返回务器返回的信息
    public void getServerMsg(){
        //初始化获取弹幕服务器返回的信息包大小
        byte[]recvByte=new byte[MAX_BUFFER_LENGTH];
        //定义服务器返回信息的字符串
        String dataStr;
        try{
            //读取服务器返回信息，并获取返回信息的整体字节长度
            int recvLen=bis.read(recvByte,0,recvByte.length);
            //根据实际获取的字节数初始化返回信息内容长度
            byte[]realBuf=new byte[recvLen];
            //按照实际获取的字节长度读取返回信息
            //复制数组
            System.arraycopy(recvByte,0,realBuf,0,recvLen);
            //根据tcp协议获取返回信息中的字符串信息
            dataStr=new String(realBuf,12,realBuf.length-12);
            //对单一数据包进行解析
            MsgView msgView = new MsgView(dataStr);
            //分析该包的数据类型，以及根据需要进行业务操作
            parseServerMsg(msgView.getMessageList());


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 解析从服务器接受的协议，并根据需要订制业务需求
     * @param msg
     */
    private void parseServerMsg(Map<String, Object> msg){
         if (msg.get("type")!=null){
             //服务器返回错误信息
             if (msg.get("type").equals("error")){
                 Log.d(TAG, "parseServerMsg: msg.toString()=" + msg.toString());
                 //结束心跳和获取弹幕线程
                 this.readyFlag=false;
             }
             /***@TODO 根据业务需求来处理获取到的所有弹幕及礼物信息***********/
             //判断信息类型
             if (msg.get("type").equals("chatmsg")){//弹幕消息
                 if (!isHandleMsgListenerNull()){
                     handMsgListener.handleMessage(msg.get("nn").toString()+":"+msg.get("txt").toString());
                 }
             }

             //@TODO 其他业务信息根据需要进行添加


         }
    }

}
