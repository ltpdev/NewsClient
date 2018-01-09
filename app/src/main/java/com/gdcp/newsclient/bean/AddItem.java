package com.gdcp.newsclient.bean;

import java.io.Serializable;

/**
 * Created by asus- on 2017/6/30.
 */

public class AddItem implements Serializable{
    private String title;
    private String channelId;
    private boolean isAdded=false;

   /* public AddItem(String title, String channelId,boolean isAdded) {
        this.title = title;
        this.channelId = channelId;
        this.isAdded=isAdded;
    }*/

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
