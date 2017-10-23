package com.gdcp.newsclient.ui.activity;

import android.view.MenuItem;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.kuaichuan.FileSender;

public class TransportActivity extends BaseActivity {


    @Override
    protected void initData() {
        FileSender fileSender=new FileSender(this,null,"",90);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_transport;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle("面对面快传");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }


}
