package com.gdcp.newsclient.kuaichuan.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.kuaichuan.common.BaseActivity;
import com.gdcp.newsclient.kuaichuan.utils.FileUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.btn_receive)
    Button btnReceive;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.iv_device)
    ImageView ivDevice;
    @BindView(R.id.tv_device_desc)
    TextView tvDeviceDesc;
    @BindView(R.id.iv_file)
    ImageView ivFile;
    @BindView(R.id.tv_file_desc)
    TextView tvFileDesc;
    @BindView(R.id.rl_file)
    RelativeLayout rlFile;
    @BindView(R.id.iv_storage)
    ImageView ivStorage;
    @BindView(R.id.tv_storage_desc)
    TextView tvStorageDesc;
    @BindView(R.id.rl_storage)
    RelativeLayout rlStorage;
    @BindView(R.id.activity_home)
    RelativeLayout activityHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_FILE);
        }else{
            //初始化
            init();
        }
    }
    @Override
    protected void onResume() {
        updateBottomData();
        super.onResume();
    }
    /**
     * 更新底部 设备数，文件数，节省流量数的数据
     */
    private void updateBottomData() {
        tvFileDesc.setText(FileUtils.getReceiveFileCount()+"");
        //TODO 节省流量数的更新
        tvStorageDesc.setText(String.valueOf(FileUtils.getReceiveFileListTotalLength()));

    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("面对面快传");
        updateBottomData();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //初始化
                init();
            } else {
                // Permission Denied
                Toast.makeText(this, "拒绝授权", Toast.LENGTH_SHORT).show();
                finish();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick({R.id.btn_send, R.id.btn_receive,
             R.id.rl_file, R.id.rl_storage  })
    public void onClick(View view){
        switch (view.getId()) {

            case R.id.btn_send:
                Intent intent = new Intent(this,ChooseFileActivity.class);
                startActivity(intent);
                break;


            case R.id.btn_receive:
                Intent intent2 = new Intent(this,ReceiverWaitingActivity.class);
                startActivity(intent2);
                break;


            case R.id.rl_file:
                Intent fileIntent=new Intent(Intent.ACTION_GET_CONTENT);
                File file=new File(FileUtils.getRootDirPath());
                Uri uri=Uri.fromFile(file);
                fileIntent.addCategory(Intent.CATEGORY_DEFAULT);
                fileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                fileIntent.setDataAndType(uri,"*/*");
                startActivity(fileIntent);
                break;
            case R.id.rl_storage:
                break;


        }
    }


}
