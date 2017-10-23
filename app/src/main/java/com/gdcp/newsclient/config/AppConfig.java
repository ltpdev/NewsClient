package com.gdcp.newsclient.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by asus- on 2017/8/24.
 */

public class AppConfig {
    private SharedPreferences innerConfig;
    private static final String KEY_NIGHT_MODE_SWITCH="night_theme";
    public AppConfig(Context context){
        innerConfig=context.getSharedPreferences("app_config", Application.MODE_PRIVATE);

    }

    public boolean isNightTheme(){
        return innerConfig.getBoolean(KEY_NIGHT_MODE_SWITCH,false);
    }

    public void setNightTheme(boolean on){
        SharedPreferences.Editor editor = innerConfig.edit();
        editor.putBoolean(KEY_NIGHT_MODE_SWITCH, on);
        editor.commit();
    }

    public void clear() {
        SharedPreferences.Editor editor = innerConfig.edit();
        editor.clear();
        editor.commit();
    }
}
