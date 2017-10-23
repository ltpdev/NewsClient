package com.gdcp.newsclient.listener;

import android.widget.ImageView;

import com.gdcp.newsclient.bean.AddItem;

/**
 * Created by asus- on 2017/6/30.
 */

public interface ClickListener {
    public void del(AddItem position, int i);

    public void add(AddItem s, int position);


}
