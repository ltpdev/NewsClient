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
import com.gdcp.newsclient.bean.CategoryBean;
import com.gdcp.newsclient.bean.SearchLiveBean;
import com.gdcp.newsclient.ui.activity.CategoryDetailActivity;
import com.gdcp.newsclient.ui.activity.LiveRoomActivity;

import java.util.List;

/**
 * Created by asus- on 2017/8/14.
 */

public class CategoryLiveAdapter extends RecyclerView.Adapter<CategoryLiveAdapter.ViewHolder>{
   private List<CategoryBean.DataBean>listBeans;
    private Context context;

    public CategoryLiveAdapter(List<CategoryBean.DataBean>listBeans, Context context) {
        this.listBeans = listBeans;
        this.context = context;
    }

    @Override
    public CategoryLiveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_category,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryLiveAdapter.ViewHolder holder, int position) {
        final CategoryBean.DataBean listBean=listBeans.get(position);
        holder.tvTitle.setText(listBean.getTag_name());
        Glide.with(context).load(listBean.getIcon_url()).into(holder.ivIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, CategoryDetailActivity.class);
                intent.putExtra("tagName",listBean.getTag_name());
                intent.putExtra("tagId",listBean.getTag_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeans.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ImageView ivIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon= (ImageView) itemView.findViewById(R.id.icon_live);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_title);
        }
    }
}
