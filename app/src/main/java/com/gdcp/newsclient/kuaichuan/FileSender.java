package com.gdcp.newsclient.kuaichuan;

import android.content.Context;
import android.graphics.Bitmap;

import com.gdcp.newsclient.kuaichuan.entity.FileInfo;
import com.gdcp.newsclient.kuaichuan.utils.ApkUtils;
import com.gdcp.newsclient.kuaichuan.utils.FileUtils;
import com.gdcp.newsclient.kuaichuan.utils.ScreenshotUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by asus- on 2017/9/28.
 */

public class FileSender extends BaseTransfer implements Runnable{
    private static final String TAG = FileSender.class.getSimpleName();
    Context context;
    /*传送文件目标的地址及端口
    *
    * */
    private String serverIpAddress;
    private int port;
    /**
     * 传送文件的信息
     */
    private FileInfo mFileInfo;

    /**
     * Socket的输入输出流
     */
    private Socket mSocket;
    private OutputStream mOutputStream;
    /**
     * 控制线程暂停 恢复
     */
    private final Object LOCK = new Object();
    boolean mIsPaused = false;
    /**
     * 判断此线程是否完毕
     */
    boolean mIsFinished = false;
    /**
     * 设置未执行的线程不执行的标识
     */
    boolean mIsStop = false;
    OnSendListener mOnSendListener;

    public FileSender(Context context, FileInfo mFileInfo, String mServerIpAddress, int mPort){
        this.context = context;
        this.mFileInfo = mFileInfo;
        this.serverIpAddress = mServerIpAddress;
        this.port = mPort;
    }
    public void setOnSendListener(OnSendListener mOnSendListener) {
        this.mOnSendListener = mOnSendListener;
    }
    @Override
    public void init() throws Exception {
     this.mSocket=new Socket(serverIpAddress,port);
        OutputStream os=this.mSocket.getOutputStream();
        mOutputStream=new BufferedOutputStream(os);


    }

    @Override
    public void parseHeader() throws Exception {
        StringBuilder headerSb = new StringBuilder();
        String jsonStr = FileInfo.toJsonStr(mFileInfo);
        jsonStr = TYPE_FILE + SPERATOR + jsonStr;
        headerSb.append(jsonStr);
        int leftLen = BYTE_SIZE_HEADER - jsonStr.getBytes(UTF_8).length; //对于英文是一个字母对应一个字节，中文的情况下对应两个字节。剩余字节数不应该是字节数
        for(int i=0; i < leftLen; i++){
            headerSb.append(" ");
        }
        byte[] headbytes = headerSb.toString().getBytes(UTF_8);
        //写入header
        mOutputStream.write(headbytes);
        //拼接缩略图
        StringBuilder screenshotSb = new StringBuilder();
        int ssByteArraySize = 0;
        //缩略图的分类
        if (mFileInfo!=null){
            Bitmap screenshot=null;
            byte[]bytes=null;
            if (FileUtils.isApkFile(mFileInfo.getFilePath())){
                Bitmap bitmap= ApkUtils.drawableToBitmap(ApkUtils.getApkThumbnail(context, mFileInfo.getFilePath()));
                screenshot = ScreenshotUtils.extractThumbnail(bitmap, 96, 96);
            }else if (FileUtils.isJpgFile(mFileInfo.getFilePath())){
                screenshot = FileUtils.getScreenshotBitmap(context, mFileInfo.getFilePath(), FileInfo.TYPE_JPG);
                screenshot = ScreenshotUtils.extractThumbnail(screenshot, 96, 96);
            }else if (FileUtils.isMp3File(mFileInfo.getFilePath())){
                screenshot = FileUtils.getScreenshotBitmap(context, mFileInfo.getFilePath(), FileInfo.TYPE_MP3);
                screenshot = ScreenshotUtils.extractThumbnail(screenshot, 96, 96);
            }else if(FileUtils.isMp4File(mFileInfo.getFilePath())) { //mp4 缩略图处理
                screenshot = FileUtils.getScreenshotBitmap(context, mFileInfo.getFilePath(), FileInfo.TYPE_MP4);
                screenshot = ScreenshotUtils.extractThumbnail(screenshot, 96, 96);
            }
            if (screenshot!=null){
                bytes=FileUtils.bitmapToByteArray(screenshot);
                ssByteArraySize=bytes.length;
                mOutputStream.write(bytes);
            }
        }

        int ssLeftLen = BYTE_SIZE_SCREENSHOT - ssByteArraySize; //缩略图剩余的字节数
        for(int i=0; i < ssLeftLen; i++){
            screenshotSb.append(" ");
        }
        byte[] screenshotBytes = screenshotSb.toString().getBytes(UTF_8);
        //写入缩略图
        mOutputStream.write(screenshotBytes);

    }

    @Override
    public void parseBody() throws Exception {
        //写入文件
          long fileSize=mFileInfo.getSize();
        InputStream fis=new FileInputStream(new File(mFileInfo.getFilePath()));
        //记录文件开始写入时间
        long startTime=System.currentTimeMillis();
        byte[]bytes=new byte[BYTE_SIZE_DATA];
        long total=0;
        int len=0;

        long sTime=System.currentTimeMillis();
        long eTime=0;

        while ((len=fis.read(bytes))!=-1){
            synchronized (LOCK){
                if (mIsPaused){
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mOutputStream.write(bytes,0,len);
                total=total+len;
                eTime = System.currentTimeMillis();
                if (eTime-sTime>200){
                    sTime=eTime;
                    if(mOnSendListener != null) mOnSendListener.onProgress(total, fileSize);
                }
            }
        }
//gggg
        //记录文件结束写入时间
        long endTime = System.currentTimeMillis();
        mOutputStream.flush();
        mOutputStream.close();
        if (mOnSendListener!=null){
            mOnSendListener.onSuccess(mFileInfo);
        }
        mIsFinished=true;



    }

    @Override
    public void finish() throws Exception {
        if(mOutputStream != null){
            try {
                mOutputStream.close();
            } catch (IOException e) {

            }
        }

        if(mSocket != null && mSocket.isConnected()){
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void run() {
         if (mIsStop){
             return;
         }
        try {
        if (mOnSendListener!=null){
            mOnSendListener.onStart();
        }
            init();
        } catch (Exception e) {
            e.printStackTrace();
            if (mOnSendListener!=null){
                mOnSendListener.onFailure(e,mFileInfo);
            }
        }
        try {
            parseHeader();
        } catch (Exception e) {
            e.printStackTrace();
            if(mOnSendListener != null) mOnSendListener.onFailure(e, mFileInfo);
        }
        //解析主体
        try {
            parseBody();
        } catch (Exception e) {
            e.printStackTrace();
            if(mOnSendListener != null) mOnSendListener.onFailure(e, mFileInfo);
        }

        //结束
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            if(mOnSendListener != null) mOnSendListener.onFailure(e, mFileInfo);
        }

    }

    /**
     * 文件是否在传送中？
     * @return
     */
    public boolean isRunning(){
        return !mIsFinished;
    }


    /**
     * 设置当前的发送任务不执行
     */
    public void stop(){
        mIsStop = true;
    }

    /**
     * 文件传送的监听
     */
    public interface OnSendListener{
        void onStart();
        void onProgress(long progress, long total);
        void onSuccess(FileInfo fileInfo);
        void onFailure(Throwable t, FileInfo fileInfo);
    }

    /*
    * 停止线程下载
    * */
    public void pause(){
        synchronized (LOCK){
            mIsPaused=true;
            LOCK.notifyAll();
        }
    }
    /**
     * 重新开始线程下载
     */
    public void resume(){
        synchronized (LOCK){
            mIsPaused =false;
            LOCK.notifyAll();
        }
    }
}
