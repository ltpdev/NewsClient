package com.gdcp.newsclient.app;
import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.gdcp.newsclient.callback.CustomCallback;
import com.gdcp.newsclient.callback.EmptyCallback;
import com.gdcp.newsclient.callback.ErrorCallback;
import com.gdcp.newsclient.callback.LoadingCallback;
import com.gdcp.newsclient.config.AppConfig;
import com.gdcp.newsclient.kuaichuan.entity.FileInfo;
import com.kingja.loadsir.core.LoadSir;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class APPAplication extends Application {
	/*主要的线程池
	*
	* */
	public static Executor MAIN_EXECUTOR= Executors.newFixedThreadPool(5);
	/**
	 * 文件发送单线程
	 */

	public static  Executor FILE_SENDER_EXECUTOR=Executors.newSingleThreadExecutor();

	public static AppConfig appConfig;

	/**
	 * 全局应用的上下文
	 */
	private static APPAplication appAplication;
	private Map<String,FileInfo>mFileInfoMap=new HashMap<String,FileInfo>();
	private Map<String,FileInfo>mReceiverFileInfoMap=new HashMap<String,FileInfo>();



	@Override
	public void onCreate() {
		super.onCreate();
		this.appAplication=this;
		//加载loadstr配置
		loadConfig();

		appConfig = new AppConfig(getApplicationContext());
		if (appConfig.isNightTheme()){
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		}else {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		}

		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
			
			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				Log.d("app", " onViewInitFinished is " + arg0);
			}
			
			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub
			}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(),  cb);
	}

	/**
	 * 获取全局的AppContext
	 * @return
	 */
	public static APPAplication getAppContext(){
		return appAplication;
	}

	/*发送方
	*
	* 添加一个fileInfo
	*
	* */

	public void addFileInfo(FileInfo fileInfo){
		if (!mFileInfoMap.containsKey(fileInfo.getFilePath())){
			mFileInfoMap.put(fileInfo.getFilePath(), fileInfo);
		}
	}

	/*更新fileInfo

	*
	* */
	public void updateFileInfo(FileInfo fileInfo){
		mFileInfoMap.put(fileInfo.getFilePath(),fileInfo);
	}
	/*
	* 删除一个fileInfo
	* */
	public void delFileInfo(FileInfo fileInfo){
		if (mFileInfoMap.containsKey(fileInfo.getFilePath())){
			mFileInfoMap.remove(fileInfo.getFilePath());
		}
	}
	/**
	 * 是否存在FileInfo
	 * @param fileInfo
	 * @return
	 */
	public boolean isExist(FileInfo fileInfo){
		if (mFileInfoMap==null){
			return false;
		}
		return  mFileInfoMap.containsKey(fileInfo.getFilePath());

	}

	/**
	 * 判断文件集合是否有元素
	 * @return 有返回true， 反之
	 */

	public boolean isFileInfoMapExist(){
		if (mFileInfoMap==null||mFileInfoMap.size()<=0){
			return false;
		}

		return true;
	}

	/*获取即将发送文件列表的总长度
	*
	*
	* */
	public long getAllSendFileInfoSize(){
		long total=0;
		for (FileInfo fileInfo:mFileInfoMap.values()){
			if (fileInfo!=null){
				total=total+fileInfo.getSize();
			}

		}
		return  total;
	}

	/**
	 * 获取全局变量中的FileInfoMap
	 * @return
	 */
	public Map<String, FileInfo> getFileInfoMap(){
		return mFileInfoMap;
	}
	/*/接收方
	  添加一个fileInfo
	*
	* */
	public void addReceiverFileInfo(FileInfo fileInfo){
		if (mReceiverFileInfoMap.containsKey(fileInfo.getFilePath())){
			mReceiverFileInfoMap.put(fileInfo.getFilePath(),fileInfo);
		}
	}

	/**
	 * 更新FileInfo
	 * @param fileInfo
	 */
	public void updateReceiverFileInfo(FileInfo fileInfo){
		mReceiverFileInfoMap.put(fileInfo.getFilePath(), fileInfo);
	}
	/**
	 * 删除一个FileInfo
	 * @param fileInfo
	 */
	public void delReceiverFileInfo(FileInfo fileInfo){
		if(mReceiverFileInfoMap.containsKey(fileInfo.getFilePath())){
			mReceiverFileInfoMap.remove(fileInfo.getFilePath());
		}
	}

	/**
	 * 是否存在FileInfo
	 * @param fileInfo
	 * @return
	 */
	public boolean isReceiverInfoExist(FileInfo fileInfo){
		if(mReceiverFileInfoMap == null) return false;
		return mReceiverFileInfoMap.containsKey(fileInfo.getFilePath());
	}

	/**
	 * 判断文件集合是否有元素
	 * @return 有返回true， 反之
	 */
	public boolean isReceiverFileInfoMapExist(){
		if(mReceiverFileInfoMap == null || mReceiverFileInfoMap.size() <= 0){
			return false;
		}
		return true;
	}
	/**
	 * 获取全局变量中的FileInfoMap
	 * @return
	 */
	public Map<String, FileInfo> getReceiverFileInfoMap(){
		return mReceiverFileInfoMap;
	}

	/**
	 * 获取即将接收文件列表的总长度
	 * @return
	 */
	public long getAllReceiverFileInfoSize(){
		long total=0;
		for (FileInfo fileInfo:mReceiverFileInfoMap.values()){
			if (fileInfo!=null){
				total=total+fileInfo.getSize();
			}
		}
		return total;
	}




	private void loadConfig() {

		LoadSir.beginBuilder()
				.addCallback(new ErrorCallback())
				.addCallback(new CustomCallback())
				.addCallback(new LoadingCallback())
				.addCallback(new EmptyCallback())
				.setDefaultCallback(LoadingCallback.class)//设置默认状态页
				.commit();



	}


	/*public static void setAppNightMode(boolean isNight){
		if (isNight){
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		}else {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		}
	}*/




}
