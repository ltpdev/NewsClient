package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.JokeBean;

import java.util.List;

/**
 * Created by asus- on 2017/8/6.
 */

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder>{
    private Context context;
    private List<JokeBean.ResultBean>jokResultBeanList;

    public JokeAdapter(Context context, List<JokeBean.ResultBean> jokResultBeanList) {
        this.context = context;
        this.jokResultBeanList = jokResultBeanList;
    }

    @Override
    public JokeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.joke_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JokeAdapter.ViewHolder holder, int position) {
        JokeBean.ResultBean resultBean=jokResultBeanList.get(position);
        holder.tvContent.setText(resultBean.getContent());
        holder.tvUpdateTime.setText(resultBean.getUpdatetime());
    }

    @Override
    public int getItemCount() {
        return jokResultBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
       TextView tvContent;
        TextView tvUpdateTime;
        public ViewHolder(View itemView) {
            super(itemView);
            tvContent= (TextView) itemView.findViewById(R.id.content);
            tvUpdateTime= (TextView) itemView.findViewById(R.id.updatetime);
        }
    }
}
