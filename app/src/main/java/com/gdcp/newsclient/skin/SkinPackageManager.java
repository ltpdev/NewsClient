package com.gdcp.newsclient.skin;

/**
 * Created by asus- on 2017/6/28.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.gdcp.newsclient.skin.config.SkinConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 皮肤包管理器
 *
 *
 *
 */
public class SkinPackageManager {
    private static SkinPackageManager mInstance;
    private Context context;
    //当前资源包名
    private String packageName;
    //皮肤资源
    private Resources mResources;
    private SkinPackageManager(Context context){
        this.context=context;
    }


    public Resources getResources() {
        return mResources;
    }

    public static  SkinPackageManager getInstance(Context context){
        if (mInstance==null){
            synchronized (SkinPackageManager.class){
                if (mInstance==null){
                    mInstance=new SkinPackageManager(context);
                }
            }
        }
       return mInstance;
   }
    /*从assets中复制apk到sd卡中*/

public boolean copyApkFromAssets(Context context,String fileName,String path){
     boolean copyIsFinish=false;
    try {
        //打开assets的输入流
        InputStream is=context.getAssets().open(fileName);
        File file=new File(path);
        // 创建一个新的文件
        file.createNewFile();
        FileOutputStream fos=new FileOutputStream(file);
        byte[]temp=new byte[1024];
        int i=0;
        while ((i=is.read(temp))>0){
             fos.write(temp,0,i);
        }
        fos.close();
        is.close();
        copyIsFinish=true;


    } catch (IOException e) {
        e.printStackTrace();
    }

    return copyIsFinish;
}
/*异步加载皮肤资源
*@param dexPath  需要加载的皮肤资源
*
*
* */
    public void loadSkinAsync(final String dexPath, final LoadSkinCallBack loadSkinCallBack){
           new AsyncTask<String,Void,Resources>(){

               @Override
               protected void onPreExecute() {
                   super.onPreExecute();
                   if (loadSkinCallBack!=null){
                       loadSkinCallBack.startLoadSkin();
                   }
               }

               @Override
               protected Resources doInBackground(String... params) {
                   if (params.length==1){
                       String dexPath_tmp=params[0];
                       PackageManager packageManager=context.getPackageManager();
                       PackageInfo packageInfo=packageManager.getPackageArchiveInfo(dexPath_tmp,PackageManager.GET_ACTIVITIES);
                       packageName=packageInfo.packageName;
                       try {
                           AssetManager assetManager=AssetManager.class.newInstance();
// 通过反射调用addAssetPath方法
                           Method method=assetManager.getClass().getMethod("addAssetPath",String.class);
                           method.invoke(assetManager,dexPath_tmp);
                           // 得到资源实例
                           Resources superRes=context.getResources();
                           // 实例化皮肤资源
                           Resources skinResource =new Resources(assetManager,superRes.getDisplayMetrics(),
                                   superRes.getConfiguration());
                           // 保存资源路径
                           SkinConfig.getInstance(context).setSkinResourcePath(dexPath_tmp);
                           return skinResource;

                       } catch (InstantiationException e) {
                           e.printStackTrace();
                       } catch (IllegalAccessException e) {
                           e.printStackTrace();
                       } catch (NoSuchMethodException e) {
                           e.printStackTrace();
                       } catch (InvocationTargetException e) {
                           e.printStackTrace();
                       }


                   }



                   return null;
               }

               @Override
               protected void onPostExecute(Resources resources) {
                   super.onPostExecute(resources);
                   mResources=resources;
                   if (loadSkinCallBack!=null){
                       if (mInstance!=null){
                              loadSkinCallBack.loadSkinSuccess();
                       }else {
                           loadSkinCallBack.loadSkinFail();
                       }
                   }
               }
           }.execute(dexPath);
    }
/*
加载皮肤资源回调接口

* */
public static interface LoadSkinCallBack{
     public void startLoadSkin();
    public void loadSkinSuccess();
    public void loadSkinFail();
}

}
