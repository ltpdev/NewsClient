package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.VideosBean;
import com.gdcp.newsclient.listener.SharedListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by asus- on 2017/6/29.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{
   private List<VideosBean.V9LG4B3A0Bean>v9LG4B3A0BeanList;
    private Context context;
    private SharedListener sharedListener;


    public void setSharedListener(SharedListener sharedListener) {
        this.sharedListener = sharedListener;
    }

    public VideoAdapter(List<VideosBean.V9LG4B3A0Bean> v9LG4B3A0BeanList, Context context) {
        this.v9LG4B3A0BeanList = v9LG4B3A0BeanList;
        this.context = context;
    }

    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_video,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapter.ViewHolder holder, int position) {
        final VideosBean.V9LG4B3A0Bean v9LG4B3A0Bean=v9LG4B3A0BeanList.get(position);
        holder.jcVideoPlayerStandard.setUp(v9LG4B3A0Bean.getMp4_url(),JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,
                v9LG4B3A0Bean.getTitle());
      /*  holder.jcVideoPlayerStandard.thumbImageView.setImageURI(Uri.parse(v9LG4B3A0Bean.getCover()));*/
        Picasso.with(context).load(v9LG4B3A0Bean.getCover()).into(holder.jcVideoPlayerStandard.thumbImageView);
        holder.tvNum.setText(v9LG4B3A0Bean.getPlayCount()+"");
        String time=DateFormat.format("mm:ss",(v9LG4B3A0Bean.getLength()*1000)).toString();
        holder.tvLength.setText(time);
        holder.tvTitle.setText(v9LG4B3A0Bean.getTitle());
        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedListener.selected(v9LG4B3A0Bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return v9LG4B3A0BeanList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        JCVideoPlayerStandard jcVideoPlayerStandard;
        TextView tvLength;
        TextView tvNum;
        TextView tvTitle;
        ImageView ivShare;
        public ViewHolder(View itemView) {
            super(itemView);
            jcVideoPlayerStandard= (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer);
            tvLength= (TextView) itemView.findViewById(R.id.time_video);
            tvNum= (TextView) itemView.findViewById(R.id.tv_num);
            tvTitle=(TextView) itemView.findViewById(R.id.title_video);
            ivShare= (ImageView) itemView.findViewById(R.id.share);
        }
    }
}
