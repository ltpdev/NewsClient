package com.gdcp.newsclient.douyuDanmu.msg;

/**
 * Created by asus- on 2018/1/8.
 */

public class DyEncoder {
    private StringBuffer buffer=new StringBuffer();
    /**
     * 返回弹幕协议格式化后的结果
     * @return
     */
    public String getResult(){
        //数据包末尾必须以'\0'结尾
        buffer.append('\0');
        return buffer.toString();
    }
    /**
     * 添加协议参数项
     * @param key
     * @param value
     */
    public void addItem(String key, Object value){
        //根据斗鱼弹幕协议进行相应的编码处理
        buffer.append(key.replaceAll("/","@S").replaceAll("@", "@A"));
        buffer.append("@=");
        if (value instanceof String){
            buffer.append(((String)value).replaceAll("/", "@S").replaceAll("@", "@A"));
        }else if (value instanceof Integer){
                buffer.append(value);
        }
        buffer.append("/");
    }




}
