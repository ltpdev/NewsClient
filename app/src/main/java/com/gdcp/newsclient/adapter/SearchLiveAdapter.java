package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.LiveBean;
import com.gdcp.newsclient.bean.SearchLiveBean;
import com.gdcp.newsclient.ui.activity.LiveRoomActivity;
import com.gdcp.newsclient.utils.StringUtil;

import java.util.List;

/**
 * Created by asus- on 2017/8/14.
 */

public class SearchLiveAdapter extends RecyclerView.Adapter<SearchLiveAdapter.ViewHolder>{
   private List<SearchLiveBean.DataBean.RoomBean>listBeans;
    private Context context;

    public SearchLiveAdapter(List<SearchLiveBean.DataBean.RoomBean>listBeans, Context context) {
        this.listBeans = listBeans;
        this.context = context;
    }

    @Override
    public SearchLiveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_live,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchLiveAdapter.ViewHolder holder, int position) {
        final SearchLiveBean.DataBean.RoomBean listBean=listBeans.get(position);
        holder.tvNick.setText(listBean.getNickname());
        holder.tvNum.setText(listBean.getHn()+"");
        holder.tvTitle.setText(StringUtil.delHTMLTag(listBean.getRoom_name()));
        Glide.with(context).load(listBean.getRoomSrc()).into(holder.ivIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, LiveRoomActivity.class);
                intent.putExtra("roomid",listBean.getRoom_id()+"");
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
