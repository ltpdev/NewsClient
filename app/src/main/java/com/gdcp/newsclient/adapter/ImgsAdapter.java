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
import com.gdcp.newsclient.ui.activity.PhotoviewActivity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus- on 2017/8/14.
 */

public class ImgsAdapter extends RecyclerView.Adapter<ImgsAdapter.ViewHolder>{
   private List<String>imgList;
    private Context context;

    public ImgsAdapter(List<String> imgList, Context context) {
        this.imgList = imgList;
        this.context = context;
    }

    @Override
    public ImgsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_imgs,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImgsAdapter.ViewHolder holder, final int position) {
        String url=imgList.get(position);
        Glide.with(context).load(url).into(holder.ivImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoviewActivity.class);
                intent.putStringArrayListExtra("imgsList", (ArrayList<String>) imgList);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImg;
        public ViewHolder(View itemView) {
            super(itemView);
            ivImg= (ImageView) itemView.findViewById(R.id.iv_img);
        }
    }
}
