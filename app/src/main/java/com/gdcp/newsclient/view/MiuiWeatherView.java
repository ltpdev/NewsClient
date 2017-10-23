package com.gdcp.newsclient.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.bean.WeatherBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus- on 2017/8/25.
 */

public class MiuiWeatherView extends View {
    private static int DEFAULT_BULE = 0XFF00BFFF;
    private static int DEFAULT_GRAY = Color.GRAY;
    private int backgroundColor;
    //控件的最低高度
    private int minViewHeight;
    //折线的最低点的高度
    private int minPointHeight;
    //折线线段长度
    private int lineInterval;
    //折线点的半径
    private float pointRadius;
    //字体大小
    private float textSize;
    //折线单位高度差
    private float pointGap;
    //折线坐标图四周留出来的偏移量
    private int defaultPadding;
    //天气图标的边长
    private float iconWidth;
    //控件的高度
    private int viewHeight;
    //控件的宽度
    private int viewWidth;
    //手机屏幕的宽度
    private int screenWidth;
    //手机屏幕的高度
    private int screenHeight;
    private Paint linePaint; //线画笔
    private Paint textPaint; //文字画笔
    private Paint circlePaint; //圆点画笔
    //元数据
    private List<WeatherBean> data = new ArrayList<>();
    private List<Pair<Integer, String>> weatherDatas = new ArrayList<>();  //对元数据中天气分组后的集合
    //不同天气之间虚线的x坐标集合
    private List<Float> dashDatas = new ArrayList<>();
    //折线拐点的集合
    private List<PointF> points = new ArrayList<>();
    //天气图标集合
    private Map<String, Bitmap> icons = new HashMap<>();

    private int maxTemperature;//元数据中的最高和最低温度
    private int minTemperature;
    //速度追踪器
    private VelocityTracker velocityTracker;
    //弹性滑动对象，用于实现view的弹性滑动
    private Scroller scroller;
    //view的配置
    private ViewConfiguration viewConfiguration;


    public MiuiWeatherView(Context context) {
        this(context, null);
    }

    public MiuiWeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiuiWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
        viewConfiguration = ViewConfiguration.get(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MiuiWeatherView);
        minPointHeight = (int) ta.getDimension(R.styleable.MiuiWeatherView_min_point_height, dp2pxF(context, 60));
        lineInterval = (int) ta.getDimension(R.styleable.MiuiWeatherView_line_interval, dp2pxF(context, 60));
        backgroundColor = ta.getColor(R.styleable.MiuiWeatherView_background_color, Color.WHITE);
        ta.recycle();
        //为控件设置背景颜色
        setBackgroundColor(backgroundColor);
        //初始化默认数据
        initSize(context);
        //初始化画笔
        initPaint(context);
        //初始化图标
        initIcons();
    }

    /**
     * 初始化天气图标集合
     * （涉及解析、缩放等耗时操作，故不要在ondraw里再去获取图片，提前解析好放在集合里)
     */
    private void initIcons() {
        icons.clear();
        String[] weathers = WeatherBean.getAllWeathers();
        for (int i = 0; i < weathers.length; i++) {
            Bitmap bmp = getWeatherIcon(weathers[i], iconWidth, iconWidth);
            icons.put(weathers[i], bmp);
        }
    }

    /**
     * 根据天气获取对应的图标，并且缩放到指定大小
     *
     * @param weather
     * @param requestW
     * @param requestH
     * @return
     */
    private Bitmap getWeatherIcon(String weather, float requestW, float requestH) {
        int resId = getIconResId(weather);
        Bitmap bitmap;
        int outWidth;
        int outHeight;
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置仅仅获取图片信息，而非加载图片
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, options);
        //获取图标的原始高度和宽度
        outWidth = options.outWidth;
        outHeight = options.outHeight;
        //设置图标缩放比例
        options.inSampleSize = 1;
        if (outWidth > requestW || outHeight > requestH) {
            int radioW = Math.round(outWidth / requestW);
            int radioH = Math.round(outHeight / requestH);
            options.inSampleSize = Math.max(radioW, radioH);
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(getResources(), resId, options);
        return bitmap;
    }

    //获得对应名称图标资源的id
    private int getIconResId(String weather) {
        int resId;
        switch (weather) {
            case WeatherBean.SUN:
                resId = R.drawable.sun;
                break;
            case WeatherBean.CLOUDY:
                resId = R.drawable.cloudy;
                break;
            case WeatherBean.RAIN:
                resId = R.drawable.rain;
                break;
            case WeatherBean.SNOW:
                resId = R.drawable.snow;
                break;
            case WeatherBean.SUN_CLOUD:
                resId = R.drawable.sun_cloud;
                break;
            case WeatherBean.THUNDER:
                resId = R.drawable.thunder;
                break;
            case WeatherBean.BAO_RAIN:
                resId = R.drawable.rain;
                break;
            case WeatherBean.Middle_RAIN:
                resId = R.drawable.rain;
                break;
            case WeatherBean.BIG_RAIN:
                resId = R.drawable.rain;
                break;
            case WeatherBean.SMALL_RAIN:
                resId = R.drawable.rain;
                break;
            default:
                resId = R.drawable.rain;
                break;
        }
        return resId;
    }

    private void initPaint(Context context) {
        //抗锯齿画笔
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(dp2px(context, 1));
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStrokeWidth(dp2pxF(context, 1));
    }

    //工具类
    public static int dp2px(Context c, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSize(getContext());
        calculatePontGap();
    }

    /**
     * 计算折线单位高度差
     * <p>
     * 这个方法中，找出了元数据里最高温度和最低温度，相减，
     * 然后将折线显示范围除以差值，即可得到单位温度的高度差，
     * 注意差值可能为0（即传入的所有数据温度都相同）。
     */
    private void calculatePontGap() {
        int lastMaxTem = -100000;
        int lastMinTem = 100000;
        for (WeatherBean weatherBean : data) {
            if (weatherBean.temperature > lastMaxTem) {
                maxTemperature = weatherBean.temperature;
                lastMaxTem = weatherBean.temperature;
            }
            if (weatherBean.temperature < lastMinTem) {
                minTemperature = weatherBean.temperature;
                lastMinTem = weatherBean.temperature;
            }
        }
        float gap = (maxTemperature - minTemperature) * 1.0f;
        gap = (gap == 0.0f ? 1.0f : gap);  //保证分母不为0
        //单位温度的高度差
        pointGap = (viewHeight - minPointHeight - 2 * defaultPadding) / gap;

    }

    /**
     * 唯一公开方法，用于设置元数据
     *
     * @param data
     */
    public void setData(List<WeatherBean> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        this.data = data;
        weatherDatas.clear();
        points.clear();
        dashDatas.clear();
        initWeatherMap(); //初始化相邻的相同天气分组
        requestLayout();
        invalidate();
    }

    /**
     * 根据元数据中连续相同的天气数做分组,
     * pair中的first值为连续相同天气的数量，second值为对应天气
     */
    private void initWeatherMap() {
        weatherDatas.clear();
        String lastWeather = "";
        int count = 0;
        for (int i = 0; i < data.size(); i++) {
            WeatherBean bean = data.get(i);
            if (i == 0) {
                lastWeather = bean.weather;
            }
            if (bean.weather != lastWeather) {
                Pair<Integer, String> pair = new Pair<>(count, lastWeather);
                weatherDatas.add(pair);
                count = 1;
            } else {
                count++;
            }
            lastWeather = bean.weather;
            if (i == data.size() - 1) {
                Pair<Integer, String> pair = new Pair<>(count, lastWeather);
                weatherDatas.add(pair);
            }
        }

        for (int i = 0; i < weatherDatas.size(); i++) {
            int c = weatherDatas.get(i).first;
            String w = weatherDatas.get(i).second;
            Log.d("ccy", "weatherMap i =" + i + ";count = " + c + ";weather = " + w);
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data.isEmpty()) {
            return;
        }
        drawAxis(canvas);

        drawLinesAndPoints(canvas);

        drawTemperature(canvas);

        drawWeatherDash(canvas);

        drawWeatherIcon(canvas);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 画天气图标和它下方文字
     * 若相邻虚线都在屏幕内，图标的x位置即在两虚线的中间
     * 若有一条虚线在屏幕外，图标的x位置即在屏幕边沿到另一条虚线的中间
     * 若两条都在屏幕外，图标x位置紧贴某一条虚线或屏幕中间
     *
     * @param canvas
     */
    private void drawWeatherIcon(Canvas canvas) {
        canvas.save();
        textPaint.setTextSize(0.9f * textSize); //字体缩小一丢丢
        boolean leftUsedScreenLeft = false;
        boolean rightUsedScreenRight = false;
        //范围控制在0 ~ viewWidth-screenWidth
        //获得滚动的值
        int scrollX = getScrollX();
        float left, right;
        float iconX, iconY;
        float textY;
        //文字的x坐标跟图标是一样的，无需额外声明
        iconY = viewHeight - (defaultPadding + minPointHeight / 2.0f);
        textY = iconY + iconWidth / 2.0f + dp2pxF(getContext(), 10);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        for (int i = 0; i < dashDatas.size() - 1; i++) {
            left = dashDatas.get(i);
            right = dashDatas.get(i + 1);
            //以下校正的情况为：两条虚线都在屏幕内或只有一条在屏幕内
            //仅左虚线在屏幕外
            if (left < scrollX && right < scrollX + screenWidth) {
                left = scrollX;
                leftUsedScreenLeft = true;
            }

            if (right > scrollX + screenWidth &&  //仅右虚线在屏幕外
                    left > scrollX) {
                right = scrollX + screenWidth;
                rightUsedScreenRight = true;
            }
            if (right - left > iconWidth) {    //经过上述校正之后左右距离还大于图标宽度
                iconX = left + (right - left) / 2.0f;
            } else {                          //经过上述校正之后左右距离小于图标宽度，则贴着在屏幕内的虚线
                if (leftUsedScreenLeft) {
                    iconX = right - iconWidth / 2.0f;
                } else {
                    iconX = left + iconWidth / 2.0f;
                }
            }

            //以下校正的情况为：两条虚线都在屏幕之外

            if (right < scrollX) {  //两条都在屏幕左侧，图标紧贴右虚线
                iconX = right - iconWidth / 2.0f;
            } else if (left > scrollX + screenWidth) {   //两条都在屏幕右侧，图标紧贴左虚线
                iconX = left + iconWidth / 2.0f;
            } else if (left < scrollX && right > scrollX + screenWidth) {  //一条在屏幕左一条在屏幕右，图标居中
                iconX = scrollX + (screenWidth / 2.0f);
            }

            Bitmap icon = icons.get(weatherDatas.get(i).second);

            //经过上述校正之后可以得到图标和文字的绘制区域
            RectF iconRect = new RectF(iconX - iconWidth / 2.0f,
                    iconY - iconWidth / 2.0f,
                    iconX + iconWidth / 2.0f,
                    iconY + iconWidth / 2.0f);
            if (icon!=null){
                canvas.drawBitmap(icon, null, iconRect, null);  //画图标
            }else {
                //暂无图标
            }


            canvas.drawText(weatherDatas.get(i).second, //画图标下方文字
                    iconX,
                    textY - (metrics.ascent+metrics.descent)/2,
                    textPaint);

            leftUsedScreenLeft = rightUsedScreenRight = false; //重置标志位
        }

        textPaint.setTextSize(textSize);
        canvas.restore();
    }

    private float lastX = 0;
    private float x = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {  //fling还没结束
                    scroller.abortAnimation();
                }
                lastX = x = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                int deltaX = (int) (lastX - x);
                if (getScrollX() + deltaX < 0) {    //越界恢复
                    scrollTo(0, 0);
                    return true;
                } else if (getScrollX() + deltaX > viewWidth - screenWidth) {
                    scrollTo(viewWidth - screenWidth, 0);
                    return true;
                }
                scrollBy(deltaX, 0);
                lastX = x;
                return true;
            case MotionEvent.ACTION_UP:
                x = event.getX();
                velocityTracker.computeCurrentVelocity(1000);  //计算1秒内滑动过多少像素
                int xVelocity = (int) velocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > viewConfiguration.getScaledMinimumFlingVelocity()) {  //滑动速度可被判定为抛动
                    scroller.fling(getScrollX(), 0, -xVelocity, 0, 0, viewWidth - screenWidth, 0, 0);
                    invalidate();
                }
                return true;

        }
        return super.onTouchEvent(event);
    }

    /**
     * 画不同天气之间的虚线
     *
     * @param canvas
     */
    private void drawWeatherDash(Canvas canvas) {
        canvas.save();
        linePaint.setColor(DEFAULT_GRAY);
        linePaint.setStrokeWidth(dp2pxF(getContext(), 0.5f));
        linePaint.setAlpha(0xcc);
        //设置画笔画出虚线
        float f[] = {dp2pxF(getContext(), 5), dp2pxF(getContext(), 1)};
        //两个值分别代表为循环的实线的长度，空白的长度
        PathEffect pathEffect = new DashPathEffect(f, 0);
        linePaint.setPathEffect(pathEffect);
        dashDatas.clear();
        int interval = 0;
        float startX, startY, endX, endY;
        endY = viewHeight - defaultPadding;
        //0坐标点的虚线手动画上
        canvas.drawLine(defaultPadding,
                points.get(0).y + pointRadius + dp2pxF(getContext(), 2),
                defaultPadding,
                endY,
                linePaint);
        dashDatas.add((float) defaultPadding);
        for (int i = 0; i < weatherDatas.size(); i++) {
            interval += weatherDatas.get(i).first;
            if (interval > points.size() - 1) {
                interval = points.size() - 1;
            }
            startX = endX = defaultPadding + interval * lineInterval;
            startY = points.get(interval).y + pointRadius + dp2pxF(getContext(), 2);
            dashDatas.add(startX);
            canvas.drawLine(startX, startY, endX, endY, linePaint);
        }
//这里注意一下，当最后一组的连续天气数为1时，是不需要计入虚线集合的，否则会多画一个天气图标
        //若不理解，可尝试去掉下面这块代码并观察运行效果?????
        if (weatherDatas.get(weatherDatas.size() - 1).first == 1
                && dashDatas.size() > 1) {
            dashDatas.remove(dashDatas.get(dashDatas.size() - 1));
        }

        linePaint.setPathEffect(null);
        linePaint.setAlpha(0xff);
        canvas.restore();
    }

    /**
     * 画温度描述值
     *
     * @param canvas
     */
    private void drawTemperature(Canvas canvas) {
        canvas.save();
        //字体放大一丢丢
        textPaint.setTextSize(1.2f * textSize);
        float centerX;
        float centerY;
        String text;
        for (int i = 0; i < points.size(); i++) {
            text = data.get(i).temperatureStr;
            centerX = points.get(i).x;
            centerY = points.get(i).y - dp2pxF(getContext(), 13);
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            canvas.drawText(text, centerX, centerY - (metrics.ascent + metrics.descent) / 2,
                    textPaint);
        }

        textPaint.setTextSize(textSize);
        canvas.restore();
    }

    /**
     * 画折线和它拐点的园
     *
     * @param canvas
     */
    private void drawLinesAndPoints(Canvas canvas) {
        canvas.save();
        linePaint.setColor(DEFAULT_BULE);
        linePaint.setStrokeWidth(dp2pxF(getContext(), 1));
        linePaint.setStyle(Paint.Style.STROKE);
        Path linePath = new Path(); //用于绘制折线
        points.clear();
        int baseHeight = defaultPadding + minPointHeight;
        float centerX;
        float centerY;
        for (int i = 0; i < data.size(); i++) {
            int tem = data.get(i).temperature;
            tem = tem - minTemperature;
            centerY = (int) (viewHeight - (baseHeight + tem * pointGap));
            centerX = defaultPadding + i * lineInterval;
            points.add(new PointF(centerX, centerY));
            if (i == 0) {
                linePath.moveTo(centerX, centerY);
            } else {
                linePath.lineTo(centerX, centerY);
            }

        }
        canvas.drawPath(linePath, linePaint); //画出折线
        //接下来画折线拐点的园
        float x, y;
        for (int i = 0; i < points.size(); i++) {
            x = points.get(i).x;
            y = points.get(i).y;
            //先画一个颜色为背景颜色的实心园覆盖掉折线拐角
            circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            circlePaint.setColor(backgroundColor);
            canvas.drawCircle(x, y, pointRadius + dp2pxF(getContext(), 1),
                    circlePaint);
            //再画一个正常的空心圆
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setColor(DEFAULT_BULE);
            canvas.drawCircle(x, y,
                    pointRadius,
                    circlePaint);
        }
        canvas.restore();


    }

    /**
     * 画时间轴
     *
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        canvas.save();
        linePaint.setColor(DEFAULT_GRAY);
        linePaint.setStrokeWidth(dp2px(getContext(), 1));
        canvas.drawLine(defaultPadding, viewHeight - defaultPadding, viewWidth - defaultPadding, viewHeight - defaultPadding, linePaint);
        float centerY = viewHeight - defaultPadding + dp2pxF(getContext(), 15);
        float centerX;
        for (int i = 0; i < data.size(); i++) {
            String text = data.get(i).time;
            centerX = defaultPadding + i * lineInterval;
            Paint.FontMetrics m = textPaint.getFontMetrics();
            canvas.drawText(text, 0, text.length(), centerX, centerY - (m.ascent + m.descent) / 2, textPaint);
        }

        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            viewHeight = Math.max(heightSize, minViewHeight);
        } else {
            viewHeight = minViewHeight;
        }

        int totalWidth = 0;
        if (data.size() > 1) {
            totalWidth = 2 * defaultPadding + lineInterval * (data.size() - 1);
        }
        viewWidth = Math.max(screenWidth, totalWidth);  //默认控件最小宽度为屏幕宽度
        setMeasuredDimension(viewWidth, viewHeight);
        calculatePontGap();


    }

    /**
     * 初始化默认数据
     */
    private void initSize(Context context) {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        minViewHeight = minPointHeight * 3;//默认为最低点的3倍
        pointRadius = dp2pxF(context, 2.5f);
        textSize = sp2pxF(context, 10);
        //默认0.5倍
        defaultPadding = (int) (0.5 * minPointHeight);
        iconWidth = (1.0f / 3.0f) * lineInterval; //默认1/3倍

    }

    //sp转化为px像素
    private float sp2pxF(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    //dp转化为px像素
    private float dp2pxF(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
