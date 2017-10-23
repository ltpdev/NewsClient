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
import android.widget.TextView;

import com.gdcp.newsclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus- on 2017/10/22.
 */
public class GridViewSortAdapter  extends BaseAdapter {
    private Context context;
    private List<String>typeTitle;
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
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public GridViewSortAdapter(GridView gridView, Context context, List<String> typeTitle){
        this.context=context;
        this.typeTitle=typeTitle;
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
       String value=typeTitle.get(startHideItemPosition);
        if (startHideItemPosition<currentHideItemPosition){
            typeTitle.add(currentHideItemPosition+1,value);
            typeTitle.remove(startHideItemPosition);
        }
        else if (startHideItemPosition > currentHideItemPosition)
        {
            typeTitle.add(currentHideItemPosition, value);
            typeTitle.remove(startHideItemPosition + 1);
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
        return typeTitle.size();
    }

    @Override
    public Object getItem(int position) {
        return typeTitle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.view_item_grid_view_sort,null);
             viewHolder=new ViewHolder();
            viewHolder.title=(TextView)convertView.findViewById(R.id.view_item_grid_view_sort_title);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(typeTitle.get(position));
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

    class ViewHolder
    {
        public TextView title;

    }
}
