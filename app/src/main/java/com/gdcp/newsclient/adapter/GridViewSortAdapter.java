package com.gdcp.newsclient.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.AddItem;
import com.gdcp.newsclient.listener.ClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus- on 2017/10/22.
 */
public class GridViewSortAdapter  extends BaseAdapter {
    private Context context;
    /*private List<String>typeTitle;*/
    private List<AddItem>addItemList;
    private List<Integer>positionList=new ArrayList<>();
    private int currentHideItemPosition= AdapterView.INVALID_POSITION;
    private int startHideItemPosition = AdapterView.INVALID_POSITION;
    private List<AnimatorSet> animatorSetList = new ArrayList<>();
    private int mHorizontalSpace;
    private int mVerticalSpace;
    private int mTranslateX;
    private int mTranslateY;
    private int mColsNum;
    private GridView mGridView;
    private boolean mInAnimation;
    private boolean isShow=false;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public GridViewSortAdapter(GridView gridView, Context context, List<AddItem>addItemList){
        this.context=context;
        this.addItemList=addItemList;
        mHorizontalSpace = gridView.getRequestedHorizontalSpacing();
        //???
        mVerticalSpace = gridView.getRequestedHorizontalSpacing();
        mGridView = gridView;
    }






    public void init() {
     View view=mGridView.getChildAt(0);
      mTranslateX=view.getWidth()+mHorizontalSpace;
      mTranslateY=view.getHeight()+mVerticalSpace;
        mColsNum=mGridView.getNumColumns();
    }
//隐藏选中的item
    public void hideView(int position) {
        resetPositionList();
        startHideItemPosition=currentHideItemPosition=position;
        notifyDataSetChanged();
    }

    private void resetPositionList() {
        positionList.clear();
        for (int i = 0; i < mGridView.getChildCount(); i++)
        {
            positionList.add(i);
        }
    }

    public boolean isInAnimation() {
        return mInAnimation;
    }
/*交换动画*/
    public void swap(int position) {
       animatorSetList.clear();
        int r_p=positionList.indexOf(position);
        if (currentHideItemPosition<r_p){
            for (int i =  currentHideItemPosition + 1; i <= r_p; i++) {
                View v = mGridView.getChildAt(positionList.get(i));
                if (i % mColsNum == 0 && i > 0)
                {
                    startMoveAnimation(v, v.getTranslationX() + mTranslateX * (mColsNum - 1), v.getTranslationY() -
                            mTranslateY);
                }
                else
                {
                    startMoveAnimation(v, v.getTranslationX() - mTranslateX, 0);
                }
            }
        }
        else if (currentHideItemPosition > r_p)
        {
            for (int i = r_p; i < currentHideItemPosition; i++)
            {
                View v = mGridView.getChildAt(positionList.get(i));
                if ((i + 1) % mColsNum == 0)
                {
                   startMoveAnimation(v, v.getTranslationX() - mTranslateX * (mColsNum - 1), v.getTranslationY() + mTranslateY);
                }
                else
                {
                    startMoveAnimation(v, v.getTranslationX() + mTranslateX, 0);
                }
            }
        }
        resetPositionList();

        int value = positionList.get(startHideItemPosition);
        if (startHideItemPosition < r_p)
        {
            positionList.add(r_p + 1, value);
            positionList.remove(startHideItemPosition);
        }
        else if (startHideItemPosition > r_p)
        {
            positionList.add(r_p, value);
            positionList.remove(startHideItemPosition + 1);
        }

        currentHideItemPosition = r_p;

    }

    private void startMoveAnimation(View myView, float x, float y) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(myView, "translationX", myView.getTranslationX(), x),
                ObjectAnimator.ofFloat(myView, "translationY", myView.getTranslationY(), y)
        );
        set.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animator)
            {
                mInAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                mInAnimation = false;
            }

            @Override
            public void onAnimationCancel(Animator animator)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animator)
            {

            }
        });
        animatorSetList.add(set);
        set.setDuration(150).start();

    }

    public void clear() {
       AddItem value=addItemList.get(startHideItemPosition);
        if (startHideItemPosition<currentHideItemPosition){
            addItemList.add(currentHideItemPosition+1,value);
            addItemList.remove(startHideItemPosition);
        }
        else if (startHideItemPosition > currentHideItemPosition)
        {
            addItemList.add(currentHideItemPosition, value);
            addItemList.remove(startHideItemPosition + 1);
        }
        startHideItemPosition = currentHideItemPosition = AdapterView.INVALID_POSITION;
        notifyDataSetChanged();
        for (AnimatorSet set : animatorSetList)
        {
            set.cancel();
        }
        animatorSetList.clear();
        for (int i = 0; i < mGridView.getChildCount(); i++) {
            mGridView.getChildAt(i).setTranslationX(0);
            mGridView.getChildAt(i).setTranslationY(0);
        }

    }

    @Override
    public int getCount() {
        return addItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return addItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.column_added,null);
            viewHolder=new ViewHolder();
            viewHolder.title=(TextView)convertView.findViewById(R.id.name_column);
            viewHolder.ivDel=(ImageView) convertView.findViewById(R.id.iv_del);
            convertView.setTag(viewHolder);
        }else {

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(addItemList.get(position).getTitle());
        if (isShow){
            viewHolder.ivDel.setVisibility(View.VISIBLE);
        }else {
            viewHolder.ivDel.setVisibility(View.GONE);
        }

        viewHolder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.del(addItemList.get(position),position);
            }
        });
        if (startHideItemPosition == position)
        {
            convertView.setVisibility(View.INVISIBLE);
        }
        else
        {
            convertView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public void setVisiable(boolean isShow) {
        this.isShow=isShow;
        notifyDataSetChanged();
    }
    private ClickListener clickListener;

    public List<AddItem> getAddItemList() {
        return addItemList;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder
    {
        public TextView title;
        public ImageView ivDel;

    }
}
