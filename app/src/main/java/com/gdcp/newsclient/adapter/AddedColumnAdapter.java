package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.AddItem;
import com.gdcp.newsclient.listener.ClickListener;

import java.util.List;

/**
 * Created by asus- on 2017/6/30.
 */

public class AddedColumnAdapter extends RecyclerView.Adapter<AddedColumnAdapter.ViewHolder>{
    private List<AddItem>nameList;
    private Context context;
    private ClickListener clickListener;


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public AddedColumnAdapter(List<AddItem> nameList, Context context) {
        this.nameList = nameList;
        this.context = context;
    }

    @Override
    public AddedColumnAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.column_added,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddedColumnAdapter.ViewHolder holder, final int position) {
              holder.btncolumn.setText(nameList.get(position).getTitle());
                holder.ivDel.setVisibility(View.GONE);
                holder.btncolumn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.ivDel.setVisibility(View.VISIBLE);
                return true;
            }
        });


        holder.btncolumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivDel.setVisibility(View.GONE);
            }
        });

        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.del(nameList.get(position),position);
            }
        });
    }

/*    public void changeState(){
        for (int i=0;i<nameList.size();i++){

        }
    }*/

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        Button btncolumn;
        ImageView ivDel;
        public ViewHolder(View itemView) {
            super(itemView);
            btncolumn=(Button)itemView.findViewById(R.id.name_column);
            ivDel= (ImageView) itemView.findViewById(R.id.iv_del);
        }
    }

}
