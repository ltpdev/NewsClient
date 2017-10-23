package com.gdcp.newsclient.callback;

import android.content.Context;
import android.view.View;

import com.gdcp.newsclient.R;
import com.kingja.loadsir.callback.Callback;

/**
 * Created by asus- on 2017/9/26.
 */

public class CustomCallback  extends Callback{

    @Override
    protected int onCreateView() {
        return R.layout.layout_parser_error;
    }


    @Override
    protected boolean onRetry(Context context, View view) {
        return true;
    }
}

