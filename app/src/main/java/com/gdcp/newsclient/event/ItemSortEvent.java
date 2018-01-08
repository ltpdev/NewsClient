package com.gdcp.newsclient.event;

/**
 * Created by asus- on 2017/10/23.
 */

public class ItemSortEvent {
    private boolean isUpdate;

    public ItemSortEvent(boolean isUpdate){
        this.isUpdate=isUpdate;
    }

    public boolean isUpdate() {
        return isUpdate;
    }
}
