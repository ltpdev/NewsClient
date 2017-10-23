package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.Actor;
import java.util.List;

/**
 * Created by asus- on 2017/8/14.
 */

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ViewHolder>{
   private List<Actor>actorList;
    private Context context;

    public ActorAdapter(List<Actor> actorList, Context context) {
        this.actorList = actorList;
        this.context = context;
    }

    @Override
    public ActorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_actor,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActorAdapter.ViewHolder holder, int position) {
        Actor actor=actorList.get(position);
        holder.tvName.setText(actor.getName());
    }

    @Override
    public int getItemCount() {
        return actorList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        ImageView ivStart;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tv_name);
            ivStart= (ImageView) itemView.findViewById(R.id.iv_start);
        }
    }
}
