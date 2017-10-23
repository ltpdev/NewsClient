package com.gdcp.newsclient.kuaichuan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.CommentBean;

/**
 * 雷达扫描器
 */

public class RadarScanView extends View{
    private static final int MSG_RUN=1;
    /*圆圈的颜色
    * */
    private int circleColor= Color.BLACK;
    /*线条的颜色
    *
    * */
    private int lineColor=Color.BLACK;
    /*圆弧的颜色
    *
    * */
    private int arcColor=Color.WHITE;
    private int arcStartColor=Color.WHITE;
    private int arcEndColor=Color.TRANSPARENT;
    // 绘制圆形画笔
    private Paint circlePaint;
    //绘制扇形画笔
    private Paint arcPaint;
    // 绘制线条画笔
    private Paint linePaint;
    private RectF rectF;
    private int sweep=0;//扇形的角度

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_RUN) {
                sweep+=4;
                if(sweep > 360) sweep = 0;
                postInvalidate();
                sendEmptyMessageDelayed(MSG_RUN, 50);
            }
        }
    };
    public RadarScanView(Context context) {
        super(context,null);
    }

    public RadarScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, 0);
    }

    public RadarScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
          circleColor=context.getResources().getColor(R.color.transparent_white);
          arcColor=context.getResources().getColor(R.color.transparent_white);
          lineColor=context.getResources().getColor(R.color.transparent_white);

        arcStartColor=context.getResources().getColor(R.color.transparent_white);
        arcEndColor=context.getResources().getColor(android.R.color.transparent);

        circlePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(1.0f);

        arcPaint.setColor(arcColor);
        arcPaint.setStyle(Paint.Style.FILL);

        linePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1.0f);
        rectF=new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size=getMeasuredWidth();
        setMeasuredDimension(size,size);
        rectF.set(0,0,getMeasuredWidth(),getMeasuredHeight());
        arcPaint.setShader(new SweepGradient(size/2,size/2,arcStartColor,arcEndColor));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX=getMeasuredWidth()/2;
        int centerY=getMeasuredHeight()/2;
        canvas.save();
        canvas.rotate(sweep,centerX,centerY);
        canvas.drawArc(rectF,0,sweep,true,arcPaint);
        canvas.restore();
        canvas.drawLine(0,centerY,getMeasuredWidth(),centerY,linePaint);
        canvas.drawLine(centerX,0,centerX,getMeasuredHeight(),linePaint);
        canvas.drawCircle(centerX,centerY,getMeasuredWidth()/2,circlePaint);
        canvas.drawCircle(centerX,centerY,getMeasuredWidth()/4,circlePaint);
    }

    /**
     * 对外公开扫描的方法
     */
    public void startScan(){
        if(handler != null){
            handler.obtainMessage(MSG_RUN).sendToTarget();
        }
    }
}
