package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.Actor;
import com.gdcp.newsclient.bean.LiveBean;
import com.gdcp.newsclient.ui.activity.LiveActivity;
import com.gdcp.newsclient.ui.activity.LiveRoomActivity;

import java.util.List;

/**
 * Created by asus- on 2017/8/14.
 */

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder>{
   private List<LiveBean.DataBean>listBeans;
    private Context context;

    public LiveAdapter(List<LiveBean.DataBean>listBeans, Context context) {
        this.listBeans = listBeans;
        this.context = context;
    }

    @Override
    public LiveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_live,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LiveAdapter.ViewHolder holder, int position) {
        final LiveBean.DataBean listBean=listBeans.get(position);
        holder.tvNick.setText(listBean.getNickname());
        holder.tvNum.setText(listBean.getOnline()+"");
        holder.tvTitle.setText(listBean.getRoom_name());
        Glide.with(context).load(listBean.getRoom_src()).into(holder.ivIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, LiveRoomActivity.class);
                intent.putExtra("roomid",listBean.getRoom_id());
                intent.putExtra("title",listBean.getRoom_name());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeans.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNick;
        TextView tvNum;
        TextView tvTitle;
        ImageView ivIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            tvNick=(TextView)itemView.findViewById(R.id.tv_nickName);
            ivIcon= (ImageView) itemView.findViewById(R.id.icon_live);
            tvNum=(TextView)itemView.findViewById(R.id.tv_num);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_title);
        }
    }
}
