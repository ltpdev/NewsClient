package com.gdcp.newsclient.view;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.GridViewSortAdapter;

/**
 * Created by asus- on 2017/10/22.
 */

public class DragSortGridView extends GridView implements AdapterView.OnItemLongClickListener{
    private WindowManager windowManager;
    private WindowManager.LayoutParams dragItemLayoutParams;
    private ImageView dragItemView;
    private int downX;
    private int downY;
    private boolean dragStarted;
    private View mView;
    public DragSortGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public DragSortGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public DragSortGridView(Context context) {
        super(context);
        initialize();
    }
//实例化
    private void initialize() {
        dragItemView=new ImageView(getContext());
        windowManager= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        dragItemLayoutParams=new WindowManager.LayoutParams();
        setOnItemLongClickListener(this);
    }




    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getContext(), "dddd", Toast.LENGTH_SHORT).show();
        //至少有两个item时才可以排序
        if (getChildCount()>=2){
            /*ImageView imageView= (ImageView) view.findViewById(R.id.iv_del);
            imageView.setVisibility(VISIBLE);
            imageView.invalidate();*/
            mView=view;
            //在调用getDrawingCache必须先调用如下方法：
            view.setDrawingCacheEnabled(true);
            //获取截图并设置
            Bitmap bitmap=view.getDrawingCache();
            dragItemView.setImageBitmap(bitmap);
            //设置拖拽imageview的params
            dragItemLayoutParams.gravity= Gravity.TOP|Gravity.LEFT;
            dragItemLayoutParams.width=bitmap.getWidth();
            dragItemLayoutParams.height=bitmap.getHeight();
            dragItemLayoutParams.x=(downX-dragItemLayoutParams.width/2);
            dragItemLayoutParams.y=(downX-dragItemLayoutParams.height/2);
            //设置拖拽imageview的中心位于长按点击点
            dragItemLayoutParams.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //不接受按键事件
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  // 不接收触摸事件
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON   // 保持常亮
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN; // place the window within the entire screen, ignoring decorations around the border (such as the status bar)
            dragItemLayoutParams.format = PixelFormat.TRANSLUCENT;
            dragItemLayoutParams.windowAnimations = 0;
            //往windowManager中添加拖拽的view
            windowManager.addView(dragItemView,dragItemLayoutParams);
            ((GridViewSortAdapter) getAdapter()).init();
            ((GridViewSortAdapter) getAdapter()).hideView(position);
            ((GridViewSortAdapter) getAdapter()).setVisiable(true);
            dragStarted = true;
        }
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()&ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                downX=(int)ev.getRawX();
                downY= (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (dragStarted){
                    //保持中心
                    dragItemLayoutParams.x= (int) (ev.getRawX()-dragItemView.getWidth()/2);
                    dragItemLayoutParams.y= (int) (ev.getRawY()-dragItemView.getHeight()/2);
                    // 更新params
                    windowManager.updateViewLayout(dragItemView, dragItemLayoutParams);
                    //找到被拖拽到哪个item 的位置
                    int position= pointToPosition((int) ev.getX(), (int) ev.getY());
                    //互换位置eeee

                    if (position!=AdapterView.INVALID_POSITION&&!((GridViewSortAdapter) getAdapter()).isInAnimation()){
                        ((GridViewSortAdapter) getAdapter()).swap(position);

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (dragStarted){
                    windowManager.removeView(dragItemView);
                    ((GridViewSortAdapter) getAdapter()).clear();
                    dragStarted = false;
                    mView.destroyDrawingCache();
                }
                break;

        }
        return super.onTouchEvent(ev);
    }
/*内部拦截，解决滑动冲突*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                if (dragStarted){
                    //设置需要拦截此事件
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (dragStarted){
                    //设置不需要拦截此事件
                    requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
