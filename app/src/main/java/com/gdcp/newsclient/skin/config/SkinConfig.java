package com.gdcp.newsclient.skin.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by asus- on 2017/6/28.
 */

public class SkinConfig {
    private static SkinConfig mInstance;
    private Context mContext;
    private SkinConfig(Context mContext) {
        this.mContext = mContext;
    }
    public static SkinConfig getInstance(Context mContext) {
        if (mInstance == null) {
            synchronized (SkinConfig.class){
                if (mInstance==null){
                    mInstance = new SkinConfig(mContext);
                }
            }

        }
        return mInstance;
    }
//保存皮肤路径
    public void setSkinResourcePath(String skinPath){
        SharedPreferences sp=mContext.getSharedPreferences("skin_sharePref", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("skinPath",skinPath);
        editor.commit();
    }

    //取出皮肤路径
    public String getSkinResourcePath() {
        SharedPreferences sp = mContext.getSharedPreferences("skin_sharePref", mContext.MODE_PRIVATE);
        return sp.getString("skinPath", "");
    }
    public void setSkinColor(String colorName){
        SharedPreferences sp=mContext.getSharedPreferences("skin_sharePref", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("colorName",colorName);
        editor.commit();
    }
    //取出皮肤路径
    public String getSkinColor() {
        SharedPreferences sp = mContext.getSharedPreferences("skin_sharePref", mContext.MODE_PRIVATE);
        return sp.getString("colorName", "");
    }

}
