package com.zczy.guidupdateview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mac on 16/3/29.
 */
public class GuidUpdateView extends View {
    private static String TAG = GuidUpdateView.class.getSimpleName();
    /**
     * topview 距 左边距离
     */
    private int margLeftForTop = 100;
    /**
     * a按钮距 上面 的 topview的间距
     */
    private int margain_bt_top = 20;
    /**
     * bt 1 view 距 左边距离
     */
    private int margLeftForB1 = 100;
    private int btViewWidth=350;
    private int btViewHeight=350;

    private int margain_skip_right=100;
    private int margain_skip_bottom=100;


    private int topViewWidth=0;
    private int topViewHeight=0;

    /**
     * 文字所在区域
     */
    private Rect mbitmapTopViewDestRect;
    /**
     * bt1 所在的区域
     */
    private Rect mbitmapB1DestRect;
    /**
     * bt2 所在的区域
     */
    private Rect mbitmapB2DestRect;
    /**
     * skip 所在的区域
     */
    private Rect mbitmapSkipDestRect;


    private Resources mResources;
    private Bitmap bitmapTopView;
    private Bitmap bitmapB1;
    private Bitmap bitmapB2;
    private Bitmap bitmapSkip;

    public GuidUpdateView(Context context) {
        super(context);

        mResources = getResources();

        //实例化的时候 将图片资源加载进来,避免每次 draw时加载
        bitmapTopView =((BitmapDrawable)mResources.getDrawable(R.drawable.guid_update_text)).getBitmap();
        //BitmapUtil.scaleImageTo(bitmapTopView, topViewWidth, topViewHeight);
        bitmapB1 =((BitmapDrawable)mResources.getDrawable(R.drawable.guid_update_b1)).getBitmap();
        bitmapB2 =((BitmapDrawable)mResources.getDrawable(R.drawable.guid_update_b2)).getBitmap();
        bitmapSkip =((BitmapDrawable)mResources.getDrawable(R.drawable.guid_update_skip)).getBitmap();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(3);

        //绘制 半透明的 背景
        Rect fullRect = new Rect(0, 0, canvasWidth, canvasHeight);
        mPaint.setColor(Color.argb(220, 53, 54, 54));
        canvas.drawRect(fullRect, mPaint);

        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);

        //绘制 提示文字的 图片
        topViewWidth = canvasWidth-2*margLeftForTop;
        topViewHeight = topViewWidth;

        Rect mbitmapTopViewSrcRect = new Rect(0, 0, bitmapTopView.getWidth(), bitmapTopView.getHeight());
        mbitmapTopViewDestRect = new Rect(margLeftForTop, 0, topViewWidth+margLeftForTop, topViewHeight);
        canvas.drawBitmap(bitmapTopView, mbitmapTopViewSrcRect, mbitmapTopViewDestRect, mPaint);

        //绘制 左边按钮图片 图片
        int b1y = mbitmapTopViewDestRect.bottom+margain_bt_top;
        Rect mbitmapB1SrcRect = new Rect(0, 0, bitmapB1.getWidth(), bitmapB1.getHeight());
        mbitmapB1DestRect= new Rect(margLeftForB1,b1y,
                btViewWidth+margLeftForB1,
                btViewHeight+b1y);
        canvas.drawBitmap(bitmapB1, mbitmapB1SrcRect, mbitmapB1DestRect, mPaint);

        //绘制右边按钮图片 图片
        //计算 BT2所在区域
        int b2x = canvasWidth-(btViewWidth+margLeftForB1);
        Rect mbitmapB2SrcRect = new Rect(0, 0, bitmapB2.getWidth(), bitmapB2.getHeight());
        mbitmapB2DestRect = new Rect(b2x
                , mbitmapB1DestRect.top, canvasWidth-margLeftForB1, btViewHeight+mbitmapB1DestRect.top);
        canvas.drawBitmap(bitmapB2, mbitmapB2SrcRect, mbitmapB2DestRect, mPaint);

        Rect mbitmapSkipSrcRect = new Rect(0, 0, bitmapSkip.getWidth(), bitmapSkip.getHeight());
        mbitmapSkipDestRect = new Rect(canvasWidth-margain_skip_right-bitmapSkip.getWidth()
                , canvasHeight-margain_skip_bottom-bitmapSkip.getHeight(), canvasWidth-margain_skip_right, canvasHeight-margain_skip_bottom);
        canvas.drawBitmap(bitmapSkip, mbitmapSkipSrcRect, mbitmapSkipDestRect, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.e(TAG, ">>>>"+event.getAction());
        if (MotionEvent.ACTION_UP == event.getAction()
                &&null != this.iGuidUpdateViewClickListener){
            float clickX = event.getX();
            float clickY = event.getY();
            //Log.e(TAG, "点击的坐标范围:  [X:" + clickX + ";Y:" + clickY + "]");
            int clickXInt = Float.valueOf(clickX).intValue();
            int clickYInt = Float.valueOf(clickY).intValue();

            if (isClickedBT1(clickXInt,clickYInt)){
                this.iGuidUpdateViewClickListener.onBT1ClickListener();
            }else if (isClickedBT2(clickXInt,clickYInt)){
                this.iGuidUpdateViewClickListener.onBT2ClickListener();
            }else if (isClickedSkip(clickXInt,clickYInt)){
                this.iGuidUpdateViewClickListener.onSkipClickListener();
            }
        }
        //返回true,拦截当前View的 点击事件,避免操作到下面一层view 的事件
        return true;
    }

    private boolean isClickedBT1(int clickx,int clicky){
        return mbitmapB1DestRect.contains(clickx,clicky);
    }
    private boolean isClickedBT2(int clickx,int clicky){
        return mbitmapB2DestRect.contains(clickx,clicky);
    }
    private boolean isClickedSkip(int clickx,int clicky){
        return mbitmapSkipDestRect.contains(clickx,clicky);
    }


    public interface IGuidUpdateViewClickListener{
        public void onBT1ClickListener();
        public void onBT2ClickListener();
        public void onSkipClickListener();
    }
    private IGuidUpdateViewClickListener iGuidUpdateViewClickListener;
    public void setiGuidUpdateViewClickListener(IGuidUpdateViewClickListener iGuidUpdateViewClickListener) {
        this.iGuidUpdateViewClickListener = iGuidUpdateViewClickListener;
    }
}
