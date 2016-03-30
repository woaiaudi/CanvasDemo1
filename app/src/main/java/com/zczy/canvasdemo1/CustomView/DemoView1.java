package com.zczy.canvasdemo1.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mac on 16/3/29.
 */
public class DemoView1 extends View {
    private static String TAG = DemoView1.class.getSimpleName();

    private final float CIRCLE_BUTTON_ORGIN_X = 500;
    private final float CIRCLE_BUTTON_ORGIN_Y = 700;
    private final float CIRCLE_BUTTON_ORGIN_RADIUS = 100;


    public DemoView1(Context context) {
        super(context);
    }

    //通过重写 onDraw 获得画布对象
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*
         * 方法 说明
         * drawRect 绘制矩形
         * drawCircle 绘制圆形
         * drawOval 绘制椭圆
         * drawPath 绘制任意多边形
         * drawLine 绘制直线
         * drawPoin 绘制点
         */
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        // 创建  写标题的 画笔
        Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.MAGENTA);
        titlePaint.setTextSize(44);
        canvas.drawText("画圆",0,2,120, 100, titlePaint);// 画文本


        int targetRectX = 0;
        int targetRectY = 200;
        int targetRectWidth = canvasWidth;
        int targetRectHeight = 300;

        Rect targetRect = new Rect(targetRectX, targetRectY, targetRectX+targetRectWidth, targetRectY+targetRectHeight);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(3);
        paint.setTextSize(80);
        String testString = "要居中展示的文字";
        paint.setColor(Color.BLUE);
        canvas.drawRect(targetRect, paint);
        paint.setColor(Color.WHITE);

        Log.e(TAG,"targetRect.left:" + targetRect.left);
        Log.e(TAG,"targetRect.right:"+targetRect.right);
        Log.e(TAG,"targetRect.top:"+targetRect.top);
        Log.e(TAG,"targetRect.bottom:"+targetRect.bottom);


        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，setTextAlign(Paint.Align.CENTER);
        //标示此时画笔写出的文字的原点,都已所写出文字的中心作为原点.
        //那么此时如果想让文字水平居中,就要让文字中心原点对准目标容器的中心原点
        //所以  drawText对应传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(testString, targetRect.centerX(), baseline, paint);

        titlePaint.setColor(Color.CYAN);
        canvas.drawCircle(500,700,100,titlePaint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float clickX = event.getX();
        float clickY = event.getY();
        Log.e(TAG,"点击的坐标范围:  [X:"+clickX+";Y:"+clickY+"]");

        //计算点到圆心的距离,是否小于等于 半径

        if (isFallInCircleArea(clickX,clickY)){
            Log.e(TAG,"点击的circle");
            if (null != iClickListener){
                iClickListener.onCircleButtonClickListener();
            }
        }
        return super.onTouchEvent(event);
    }


    private boolean isFallInCircleArea(float clickX,float clickY){
        double _x = Math.abs(CIRCLE_BUTTON_ORGIN_X - clickX);
        double _y = Math.abs(CIRCLE_BUTTON_ORGIN_Y - clickY);
        double tmpLlength = Math.sqrt(_x*_x+_y*_y);
        if (tmpLlength <= CIRCLE_BUTTON_ORGIN_RADIUS){
            return true;
        }else{
            return false;
        }
    }

    public interface IDemoView1ClickListener{
        public void onCircleButtonClickListener();
    }

    private IDemoView1ClickListener iClickListener;
    public void setiClickListener(IDemoView1ClickListener iClickListener) {
        this.iClickListener = iClickListener;
    }
}
