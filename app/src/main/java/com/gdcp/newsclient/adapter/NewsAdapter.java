package com.gdcp.newsclient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.NewsBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by asus- on 2017/6/28.
 */

public class NewsAdapter extends BaseAdapter {
    private static final int ITEM_TYPE_WITH_1_IMAGE = 0;
    private static final int ITEM_TYPE_WITH_3_IMAGE = 1;

    private List<NewsBean.ResultBean> resultBeanList;
    private Context context;

    public NewsAdapter(List<NewsBean.ResultBean> resultBeanList, Context context) {
        this.resultBeanList = resultBeanList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return resultBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsBean.ResultBean resultBean = resultBeanList.get(position);
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_TYPE_WITH_1_IMAGE) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_news_1, null);
            }
            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            TextView tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
            TextView tvSource = (TextView) convertView.findViewById(R.id.tv_source);
            tvTitle.setText(resultBean.getTitle());
            tvSource.setText(resultBean.getSource());
            tvComment.setText(resultBean.getReplyCount() + "跟帖");
            Picasso.with(context).load(resultBean.getImgsrc()).into(ivIcon);


        } else if (itemViewType == ITEM_TYPE_WITH_3_IMAGE) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_news_2, null);
            }
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            TextView tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
            ImageView iv01 = (ImageView) convertView.findViewById(R.id.iv_01);
            ImageView iv02 = (ImageView) convertView.findViewById(R.id.iv_02);
            ImageView iv03 = (ImageView) convertView.findViewById(R.id.iv_03);
            tvTitle.setText(resultBean.getTitle());
            tvComment.setText(resultBean.getReplyCount() + "跟帖");
            Picasso.with(context).load(resultBean.getImgsrc()).into(iv01);
            Picasso.with(context).load(resultBean.getImgextra().get(0).getImgsrc()).into(iv02);
            Picasso.with(context).load(resultBean.getImgextra().get(1).getImgsrc()).into(iv03);

        }


        return convertView;
    }


    @Override
    public int getItemViewType(int position) {
        NewsBean.ResultBean resultBean = (NewsBean.ResultBean) getItem(position);
        if (resultBean.getImgextra() == null || resultBean.getImgextra().size() == 0) {
            return ITEM_TYPE_WITH_1_IMAGE;
        } else {
            return ITEM_TYPE_WITH_3_IMAGE;
        }

    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }
}
