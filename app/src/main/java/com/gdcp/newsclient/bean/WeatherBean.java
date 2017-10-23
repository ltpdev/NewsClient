package com.gdcp.newsclient.bean;

/**
 * Created by asus- on 2017/8/25.
 */

public class WeatherBean {
    public static final String SUN="晴";
    public static final String CLOUDY="阴";
    public static final String SNOW="雪";
    public static final String RAIN="阵雨";
    public static final String SUN_CLOUD="多云";
    public static final String THUNDER = "雷阵雨";
    public static final String BIG_RAIN= "大雨";
    public static final String Middle_RAIN = "中雨";
    public static final String BAO_RAIN = "暴雨";
    public static final String SMALL_RAIN = "小雨";
    //天气，取值为上面6种
    public String weather;
    //温度值
    public int temperature;
    //温度的描述值
    public  String temperatureStr;
    //时间值
    public String time;

    public WeatherBean(String weather, int temperature,String time){
        this.weather = weather;
        this.temperature = temperature;
        this.time = time;
        this.temperatureStr = temperature + "°";
    }
    public WeatherBean(String weather, int temperature,String temperatureStr,String time){
        this.weather = weather;
        this.temperature = temperature;
        this.temperatureStr = temperatureStr;
        this.time = time;
    }

    public static String[] getAllWeathers(){
        String[] str = {SUN,RAIN,CLOUDY,SUN_CLOUD,SNOW,THUNDER,BIG_RAIN,Middle_RAIN,BAO_RAIN,SMALL_RAIN};
        return str;
    }

}
