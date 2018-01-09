package com.gdcp.newsclient.bean;

/**
 * Created by asus- on 2018/1/9.
 */

public class Msg {
    private String title;
    private String txt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public Msg(String title, String txt) {
        this.title = title;
        this.txt = txt;
    }
}
