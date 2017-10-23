package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.AddItem;
import com.gdcp.newsclient.listener.ClickListener;

import java.util.List;

/**
 * Created by asus- on 2017/6/30.
 */

public class NotAddColumnAdapter extends RecyclerView.Adapter<NotAddColumnAdapter.ViewHolder>{
    private List<AddItem>nameList;
    private Context context;
    private ClickListener clickListener;

    public NotAddColumnAdapter(List<AddItem> nameList, Context context) {
        this.nameList = nameList;
        this.context = context;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public NotAddColumnAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.column_notadded,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotAddColumnAdapter.ViewHolder holder, final int position) {
              holder.btncolumn.setText(nameList.get(position).getTitle());
        holder.btncolumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.add(nameList.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
       Button btncolumn;
        public ViewHolder(View itemView) {
            super(itemView);
            btncolumn=(Button)itemView.findViewById(R.id.name_column);
        }
    }

}
