package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.CommentBean;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by asus- on 2017/8/15.
 */

public class CommentAdapter extends BaseAdapter{
    private List<CommentBean.DataBean.CommentResponseModelBean.CmtsBean>cmtsBeanList;
    private Context context;

    public CommentAdapter(List<CommentBean.DataBean.CommentResponseModelBean.CmtsBean> cmtsBeanList, Context context) {
        this.cmtsBeanList = cmtsBeanList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cmtsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return cmtsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        }
        CircleImageView circleImageView= (CircleImageView) convertView.findViewById(R.id.head_commenter);
        TextView nickName=(TextView) convertView.findViewById(R.id.tv_nickName);
        TextView pn=(TextView) convertView.findViewById(R.id.tv_pn);
        TextView content=(TextView) convertView.findViewById(R.id.tv_content);
        TextView time=(TextView) convertView.findViewById(R.id.tv_time);
        TextView approve=(TextView) convertView.findViewById(R.id.tv_approve);
        CommentBean.DataBean.CommentResponseModelBean.CmtsBean cmtsBean=cmtsBeanList.get(position);
        nickName.setText(cmtsBean.getNickName());
        pn.setText("星级评分:"+cmtsBean.getScore());
        content.setText(cmtsBean.getContent());
        time.setText(cmtsBean.getTime());
        approve.setText(cmtsBean.getApprove()+"");
        Glide.with(context).load(cmtsBean.getAvatarurl()).into(circleImageView);
        return convertView;
    }
}
