package com.gdcp.newsclient.kuaichuan.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by asus- on 2017/10/1.
 */

public abstract class CommonAdapter<T> extends BaseAdapter{
    Context context;
    List<T>dataList;
    public CommonAdapter(Context context,List<T>dataList){
        this.context=context;
        this.dataList=dataList;
    }

    public Context getContext(){
        return context;
    }

    public List<T>getDataList(){
        return dataList;
    }
    /**
     * 添加数据源
     * @param dataList
     */

    public void addDataList(List<T>dataList){
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    /**
     * 清除数据
     */
    public void clear(){
        this.dataList.clear();
    }



    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = convertView(position, convertView);
        return convertView;
    }

    /**
     * 重写convertView方法
     *
     * @param position
     * @param convertView
     * @return
     */
    public abstract View convertView(int position, View convertView);

}
