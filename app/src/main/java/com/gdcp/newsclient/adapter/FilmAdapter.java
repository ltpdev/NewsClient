package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.FilmBean;


import java.util.List;

/**
 * Created by asus- on 2017/8/6.
 */

public class FilmAdapter extends BaseAdapter{
    private Context context;
    private List<FilmBean.DataBean.MoviesBean>moviesBeanList;


    public FilmAdapter(Context context, List<FilmBean.DataBean.MoviesBean> moviesBeanList) {
        this.context = context;
        this.moviesBeanList = moviesBeanList;
    }

    @Override
    public int getCount() {
        return moviesBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return moviesBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView tvName;
        TextView tvType;
        TextView tvAct;
        TextView tvTime;
        TextView tvPf;
        TextView tvBuy;

        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.film_item,parent,false);
        }




        imageView= (ImageView) convertView.findViewById(R.id.iv_icon);
        tvName= (TextView) convertView.findViewById(R.id.film_name);
        tvType= (TextView) convertView.findViewById(R.id.film_type);
        tvAct= (TextView) convertView.findViewById(R.id.film_act);
        tvTime= (TextView) convertView.findViewById(R.id.film_time);
        tvPf= (TextView) convertView.findViewById(R.id.film_pingfen);
        tvBuy= (TextView) convertView.findViewById(R.id.film_buy);
        FilmBean.DataBean.MoviesBean moviesBean=moviesBeanList.get(position);
        tvName.setText(moviesBean.getNm());
        tvType.setText(moviesBean.getCat());
        tvAct.setText(moviesBean.getStar());
        tvTime.setText(moviesBean.getRt());
        tvPf.setText(moviesBean.getSc()+"");
        Glide.with(context).load(moviesBean.getImg()).into(imageView);

        return convertView;
    }
}
