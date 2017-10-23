package com.gdcp.newsclient.kuaichuan.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.app.APPAplication;
import com.gdcp.newsclient.kuaichuan.adapter.FileInfoSeletedAdapter;
import com.gdcp.newsclient.kuaichuan.entity.FileInfo;
import com.gdcp.newsclient.kuaichuan.receiver.SeletedFileListChangedBroadcastReceiver;
import com.gdcp.newsclient.kuaichuan.utils.FileUtils;

import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus- on 2017/10/1.
 */

public class ShowSelectedFileInfoDialog {
    Context context;
    AlertDialog alertDialog;
    @BindView(R.id.btn_operation)
    Button btnOperation;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_result)
    ListView lvResult;
    FileInfoSeletedAdapter fileInfoSeletedAdapter;
    public ShowSelectedFileInfoDialog(Context context) {
        this.context = context;
        View contentView = View.inflate(context, R.layout.view_show_selected_file_info_dialog, null);
        ButterKnife.bind(this, contentView);
        String title = getAllSelectedFilesDes();
        tvTitle.setText(title);
        fileInfoSeletedAdapter=new FileInfoSeletedAdapter(context);
        fileInfoSeletedAdapter.setOnDataListChangedListener(new FileInfoSeletedAdapter.OnDataListChangedListener() {
            @Override
            public void onDataChanged() {
                if(fileInfoSeletedAdapter.getCount() == 0){
                    hide();
                }
                tvTitle.setText(getAllSelectedFilesDes());
                sendUpdateSeletedFilesBR();//发送更新选中文件的广播
            }
        });
        lvResult.setAdapter(fileInfoSeletedAdapter);
       this.alertDialog=new AlertDialog.Builder(context).setView(contentView).create();
    }

    private void sendUpdateSeletedFilesBR() {
         context.sendBroadcast(new Intent(SeletedFileListChangedBroadcastReceiver.ACTION_CHOOSE_FILE_LIST_CHANGED));
    }

    @OnClick({R.id.btn_operation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_operation:
                //清空操作
                clearAllSelectedFiles();
                sendUpdateSeletedFilesBR();//发送更新选中文件的广播
                break;

        }
    }

    private void clearAllSelectedFiles() {
        APPAplication.getAppContext().getFileInfoMap().clear();
        if (fileInfoSeletedAdapter!=null){
            fileInfoSeletedAdapter.notifyDataSetChanged();
        }
        this.hide();
    }

    public void show(){
        if (this.alertDialog!=null){
            notifyDataSetChanged();
            tvTitle.setText(getAllSelectedFilesDes());
            this.alertDialog.show();
        }
    }

    private String getAllSelectedFilesDes() {
       String title="";
        long totalSize=0;
        Set<Map.Entry<String,FileInfo>> entrySet = APPAplication.getAppContext().getFileInfoMap().entrySet();
        for(Map.Entry<String,FileInfo> entry : entrySet){
            FileInfo fileInfo = entry.getValue();
            totalSize = totalSize + fileInfo.getSize();
        }
        title=entrySet.size()+"个文件共"+ FileUtils.getFileSize(totalSize);
        return title;
    }
    /**
     * 通知列表发生变化
     */
    private void notifyDataSetChanged() {
        if(fileInfoSeletedAdapter != null){
            fileInfoSeletedAdapter.notifyDataSetChanged();
        }
    }
    /**
     * 隐藏
     */
    public void hide(){
        if(this.alertDialog != null){
            this.alertDialog.hide();
        }
    }

}
