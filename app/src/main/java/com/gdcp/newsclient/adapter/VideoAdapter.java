package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.VideosBean;
import com.gdcp.newsclient.listener.SharedListener;


import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

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
    public void onBindViewHolder(final VideoAdapter.ViewHolder holder, int position) {
         final DanmakuContext danmakuContext = DanmakuContext.create();;
         BaseDanmakuParser parser = new BaseDanmakuParser() {
            @Override
            protected IDanmakus parse() {
                return new Danmakus();
            }
        };
        final VideosBean.V9LG4B3A0Bean v9LG4B3A0Bean=v9LG4B3A0BeanList.get(position);
        holder.jcVideoPlayerStandard.setUp(v9LG4B3A0Bean.getMp4_url(),JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,
                v9LG4B3A0Bean.getTitle());
      /*  holder.jcVideoPlayerStandard.thumbImageView.setImageURI(Uri.parse(v9LG4B3A0Bean.getCover()));*/
        Glide.with(context).load(v9LG4B3A0Bean.getCover()).into(holder.jcVideoPlayerStandard.thumbImageView);
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
        holder.danmakuView.enableDanmakuDrawingCache(true);
        holder.danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                holder.danmakuView.start();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });

        holder.danmakuView.prepare(parser, danmakuContext);

        holder.danmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.operationLayout.getVisibility() == View.GONE) {
                    holder.operationLayout.setVisibility(View.VISIBLE);
                } else {
                    holder.operationLayout.setVisibility(View.GONE);
                }

            }
        });

        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = holder.editText.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    addDanmaku(content,holder.danmakuView,danmakuContext);
                    holder.editText.setText("");
                    holder.operationLayout.setVisibility(View.GONE);
                }
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
        DanmakuView danmakuView;
        ImageView danmu;
        LinearLayout operationLayout;
         Button send;
         EditText editText;
        public ViewHolder(View itemView) {
            super(itemView);
            jcVideoPlayerStandard= (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer);
            tvLength= (TextView) itemView.findViewById(R.id.time_video);
            tvNum= (TextView) itemView.findViewById(R.id.tv_num);
            tvTitle=(TextView) itemView.findViewById(R.id.title_video);
            ivShare= (ImageView) itemView.findViewById(R.id.share);
            danmakuView=(DanmakuView) itemView.findViewById(R.id.danmaku_view);
            danmu=(ImageView) itemView.findViewById(R.id.danmu);
            operationLayout=(LinearLayout) itemView.findViewById(R.id.operation_layout);
            send = (Button) itemView.findViewById(R.id.send);
            editText = (EditText)itemView.findViewById(R.id.edit_text);
        }
    }


    private void addDanmaku(String content, final DanmakuView danmakuView, DanmakuContext danmakuContext) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = sp2px(20);
        danmaku.textColor = Color.WHITE;
        danmaku.setTime(danmakuView.getCurrentTime());
        danmakuView.addDanmaku(danmaku);
    }

    private float sp2px(int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}
