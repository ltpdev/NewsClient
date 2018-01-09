package com.gdcp.newsclient.kuaichuan.fragment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.app.APPAplication;
import com.gdcp.newsclient.kuaichuan.adapter.FileInfoAdapter;
import com.gdcp.newsclient.kuaichuan.entity.FileInfo;
import com.gdcp.newsclient.kuaichuan.ui.ChooseFileActivity;
import com.gdcp.newsclient.kuaichuan.utils.AnimationUtils;
import com.gdcp.newsclient.kuaichuan.utils.FileUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gdcp.newsclient.app.APPAplication.MAIN_EXECUTOR;

/**
 * Created by asus- on 2017/10/1.
 */

public class FileInfoFragment extends Fragment {
    @BindView(R.id.gv)
    GridView gv;
    @BindView(R.id.pb)
    ProgressBar pb;
    private int mType = FileInfo.TYPE_APK;
    private List<FileInfo> mFileInfoList;
    private FileInfoAdapter fileInfoAdapter;

    public FileInfoFragment() {

    }

    @SuppressLint("ValidFragment")
    public FileInfoFragment(int type) {
        this.mType = type;
    }

    public static FileInfoFragment newInstance(int type) {
        FileInfoFragment fragment = new FileInfoFragment(type);
        return fragment;
    }
    @Override
    public void onResume() {
        updateFileInfoAdapter();
        super.onResume();
    }

    /**
     * 更新FileInfoAdapter
     */
    public void updateFileInfoAdapter(){
        if(fileInfoAdapter != null){
            fileInfoAdapter.notifyDataSetChanged();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_apk, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, rootView);
        if(mType == FileInfo.TYPE_APK){ //应用
            gv.setNumColumns(4);
        }else if(mType == FileInfo.TYPE_JPG){ //图片
            gv.setNumColumns(3);
        }else if(mType == FileInfo.TYPE_MP3){ //音乐
            gv.setNumColumns(1);
        }else if(mType == FileInfo.TYPE_MP4){ //视频
            gv.setNumColumns(1);
        }
        init();//初始化界面
        return rootView;
    }
    private void init() {
        if (mType==FileInfo.TYPE_APK){
           new GetFileInfoListTask(getActivity(),FileInfo.TYPE_APK).executeOnExecutor(APPAplication.MAIN_EXECUTOR);
        }else if (mType==FileInfo.TYPE_JPG){
            new GetFileInfoListTask(getActivity(),FileInfo.TYPE_JPG).executeOnExecutor(APPAplication.MAIN_EXECUTOR);
        }else if (mType==FileInfo.TYPE_MP3){
            new GetFileInfoListTask(getActivity(),FileInfo.TYPE_MP3).executeOnExecutor(APPAplication.MAIN_EXECUTOR);
        }else if (mType==FileInfo.TYPE_MP4){
            new GetFileInfoListTask(getActivity(),FileInfo.TYPE_MP4).executeOnExecutor(APPAplication.MAIN_EXECUTOR);
        }
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileInfo fileInfo=mFileInfoList.get(position);
                if (APPAplication.getAppContext().isExist(fileInfo)){
                    APPAplication.getAppContext().delFileInfo(fileInfo);
                    updateSelectedView();
                }else {
                    //1.添加任务
                    APPAplication.getAppContext().addFileInfo(fileInfo);
                    //2.添加任务动画
                    View startView=null;
                    View targetView=null;
                    startView = view.findViewById(R.id.iv_shortcut);
                    if (getActivity()!=null&&(getActivity() instanceof ChooseFileActivity)){
                        ChooseFileActivity chooseFileActivity = (ChooseFileActivity) getActivity();
                        targetView = chooseFileActivity.getSelectedView();
                    }
                    AnimationUtils.setAddTaskAnimation(getActivity(), startView, targetView, null);
                }
                fileInfoAdapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * 更新ChoooseFileActivity选中View
     */
    private void updateSelectedView() {
        if(getActivity() != null && (getActivity() instanceof ChooseFileActivity)){
            ChooseFileActivity chooseFileActivity = (ChooseFileActivity) getActivity();
            chooseFileActivity.getSelectedView();
        }
    }

    /**
     * 显示进度
     */
    public void showProgressBar(){
        if(pb != null) {
            pb.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏进度
     */
    public void hideProgressBar(){
        if(pb != null && pb.isShown()) {
            pb.setVisibility(View.GONE);
        }
    }

    /**
     * 获取FileInfo列表任务
     */
    class GetFileInfoListTask extends AsyncTask<String,Integer,List<FileInfo>>{
        Context context;
        int sType=FileInfo.TYPE_APK;
        List<FileInfo>fileInfoList=null;
        public GetFileInfoListTask(Context context,int type){
            this.context=context;
            this.sType=type;
        }



        @Override
        protected void onPreExecute() {
            showProgressBar();
            super.onPreExecute();
        }

        @Override
        protected List<FileInfo> doInBackground(String... params) {
            if (sType==FileInfo.TYPE_APK){
                fileInfoList= FileUtils.getSpecificTypeFiles(context,new String[]{ FileInfo.EXTEND_APK});
                fileInfoList = FileUtils.getDetailFileInfos(context, fileInfoList, FileInfo.TYPE_APK);
            }else if (sType==FileInfo.TYPE_JPG){
                fileInfoList= FileUtils.getSpecificTypeFiles(context,new String[]{ FileInfo.EXTEND_JPEG, FileInfo.EXTEND_JPG});
                fileInfoList = FileUtils.getDetailFileInfos(context, fileInfoList, FileInfo.TYPE_JPG);
            }else if (sType==FileInfo.TYPE_MP3){
                fileInfoList= FileUtils.getSpecificTypeFiles(context,new String[]{ FileInfo.EXTEND_MP3});
                fileInfoList = FileUtils.getDetailFileInfos(context, fileInfoList, FileInfo.TYPE_MP3);
            }else if (sType==FileInfo.TYPE_MP3){
                fileInfoList= FileUtils.getSpecificTypeFiles(context,new String[]{ FileInfo.EXTEND_MP3});
                fileInfoList = FileUtils.getDetailFileInfos(context, fileInfoList, FileInfo.TYPE_MP3);
            }else if (sType==FileInfo.TYPE_MP4){
                fileInfoList= FileUtils.getSpecificTypeFiles(context,new String[]{ FileInfo.EXTEND_MP4});
                fileInfoList = FileUtils.getDetailFileInfos(context, fileInfoList, FileInfo.TYPE_MP4);
            }
            mFileInfoList=fileInfoList;
            return fileInfoList;
        }

        @Override
        protected void onPostExecute(List<FileInfo> fileInfos) {
            hideProgressBar();
            if(mFileInfoList != null && mFileInfoList.size() > 0){
                if(mType == FileInfo.TYPE_APK){ //应用
                    fileInfoAdapter = new FileInfoAdapter(context,mFileInfoList , FileInfo.TYPE_APK);
                    gv.setAdapter(fileInfoAdapter);
                }else if(mType == FileInfo.TYPE_JPG){ //图片
                    fileInfoAdapter = new FileInfoAdapter(context,mFileInfoList, FileInfo.TYPE_JPG);
                    gv.setAdapter(fileInfoAdapter);
                }else if(mType == FileInfo.TYPE_MP3){ //音乐
                    fileInfoAdapter = new FileInfoAdapter(context,mFileInfoList, FileInfo.TYPE_MP3);
                    gv.setAdapter(fileInfoAdapter);
                }else if(mType == FileInfo.TYPE_MP4){ //视频
                    fileInfoAdapter = new FileInfoAdapter(context,mFileInfoList, FileInfo.TYPE_MP4);
                    gv.setAdapter(fileInfoAdapter);
                }
            }else{

            }
            super.onPostExecute(fileInfos);
        }
    }
}
