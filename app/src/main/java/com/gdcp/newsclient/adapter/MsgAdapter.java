package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.Msg;

import java.net.ContentHandler;
import java.util.List;

/**
 * Created by asus- on 2018/1/9.
 */

public class MsgAdapter extends BaseAdapter{

    private Context context;
    private List<Msg>msgList;

    public MsgAdapter(Context context, List<Msg> msgList) {
        this.context = context;
        this.msgList = msgList;
    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int i) {
        return msgList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_msg,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.tvTitle= (TextView) view.findViewById(R.id.tv_title);
            viewHolder.tvContent= (TextView) view.findViewById(R.id.tv_content);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        Msg msg=msgList.get(i);
        viewHolder.tvTitle.setText(msg.getTitle());
        viewHolder.tvContent.setText(msg.getTxt());
        return view;
    }

    static class ViewHolder{
        TextView tvTitle;
        TextView tvContent;
    }
}
