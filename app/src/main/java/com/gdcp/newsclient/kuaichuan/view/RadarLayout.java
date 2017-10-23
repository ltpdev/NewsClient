package com.gdcp.newsclient.kuaichuan.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus- on 2017/10/4.
 */

public class RadarLayout extends FrameLayout {
    public static final int INFINITE = 0;

    private static final int DEFAULT_COUNT = 4;
    private static final int DEFAULT_COLOR = Color.rgb(0, 116, 193);
    private static final int DEFAULT_DURATION = 7000;
    private static final int DEFAULT_REPEAT = INFINITE;
    private static final int DEFAULT_STROKE_WIDTH = 2;
    private int count;
    private int duration;
    private int repeat;
    private AnimatorSet animatorSet;
    private Paint paint;
    private int color;
    //半径
    private float radius;
    //圆心坐标
    private float centerX;
    private float centerY;
    private int strokeWidth;
    private boolean isStarted;
    private boolean useRing;
    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isStarted = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isStarted = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isStarted = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public RadarLayout(Context context) {
        super(context);
        initGlobalparams();
    }

    public RadarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGlobalparams();
    }

    public RadarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGlobalparams();
    }

    private void initGlobalparams() {
        color = DEFAULT_COLOR;
        count = DEFAULT_COUNT;
        duration = DEFAULT_DURATION;
        repeat = DEFAULT_REPEAT;
        useRing = false;
        strokeWidth = dip2px(DEFAULT_STROKE_WIDTH);
        build();

    }

    private int dip2px(int dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void build() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        int repeatCount = (repeat == INFINITE) ? ObjectAnimator.INFINITE : repeat;
        List animators = new ArrayList();
        for (int index = 0; index < count; index++) {
            RadarView radarView = new RadarView(getContext());
            radarView.setScaleX(0);
            radarView.setScaleY(0);
            radarView.setAlpha(1);
            addView(radarView, index, params);
            // 计算时间间隔
            long delay = index * duration / count;
            // 属性动画
            animators.add(create(radarView, "scaleX", repeatCount, delay, 0, 1));
            animators.add(create(radarView, "scaleY", repeatCount, delay, 0, 1));
            animators.add(create(radarView, "alpha", repeatCount, delay, 1, 0));
        }
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.setDuration(duration);
        animatorSet.addListener(mAnimatorListener);
    }

    public synchronized void start() {
        if (animatorSet == null || isStarted) {
            return;
        }
        animatorSet.start();
    }

    public synchronized void stop() {
        if (animatorSet == null || !isStarted) {
            return;
        }
        animatorSet.end();
    }

    public synchronized boolean isStarted() {
        return (animatorSet != null && isStarted);
    }

    public int getCount() {
        return count;
    }

    public int getDuration() {
        return duration;
    }

    public void setCount(int count) {
        if (count < 0) {
            return;
        }
        if (this.count != count) {
            this.count = count;
            reset();
            invalidate();
        }
    }

    public void setUseRing(boolean useRing) {
        if (this.useRing != useRing) {
            this.useRing = useRing;
            reset();
            invalidate();
        }
    }

    private void reset() {
        boolean mIsStarted = isStarted();
        clear();
        build();
        if (isStarted) {
            start();
        }
    }

    public void setColor(int color) {
        if (this.color != color) {
            this.color = color;
            reset();
            invalidate();
        }
    }

    private void clear() {
        stop();
        removeAllViews();
    }

    public void setDuration(int millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }

        if (millis != duration) {
            duration = millis;
            reset();
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        // 确定圆的圆点坐标及半径
        centerX = width * 0.5f;
        centerY = height * 0.5f;
        radius = Math.min(width, height) * 0.5f;
    }

    private ObjectAnimator create(View target, String propertyName, int repeatCount, long delay, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, propertyName, from, to);
        animator.setRepeatCount(repeatCount);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setStartDelay(delay);
        return animator;
    }


    private class RadarView extends View {

        public RadarView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (paint == null) {
                paint = new Paint();
                paint.setColor(color);
                paint.setAntiAlias(true);
                paint.setStyle(useRing ? Paint.Style.STROKE : Paint.Style.FILL);
                paint.setStrokeWidth(useRing ? strokeWidth : 0);

            }
            // 画圆或环
            canvas.drawCircle(centerX, centerY, useRing ? radius - strokeWidth : radius, paint);
        }
    }


}
