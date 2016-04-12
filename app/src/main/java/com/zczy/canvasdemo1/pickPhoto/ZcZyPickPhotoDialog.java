package com.zczy.canvasdemo1.pickPhoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mac on 16/4/8.
 */
public class ZcZyPickPhotoDialog extends View {
    private static String TAG = ZcZyPickPhotoDialog.class.getSimpleName();

    public enum ACTION_TYPE {
        ACTION_TYPE_TAKE_PHOTO, ACTION_TYPE_PICK_IMAGE
    }

    /**
     * 是拍照还是 选相册中
     */
    private ACTION_TYPE mActionType;

    private static final String  BOTTOM_BT_1 = "拍  照";

    private static final String  BOTTOM_BT_2 = "从相册选取";

    private static final String  BOTTOM_BT_CANCEL = "取  消";


    private Context mContext;
    private Resources mResources;

    private Bitmap bitmapTopView;


    private Paint mBgPaint;
    private Paint mBottomBtPaint;

    //最后一个必须是     "取消"功能,不要修改
    private List<PPDButtonBean> bottomButtonList;


    //保存 当前照片对应的 资源ID
    private ImageView currentImageView;

    public ZcZyPickPhotoDialog(Context context) {
        super(context);
        mContext = context;
        mResources = getResources();
        bottomButtonList = new ArrayList<PPDButtonBean>();
        bottomButtonList.add(new PPDButtonBean(BOTTOM_BT_1,null));
        bottomButtonList.add(new PPDButtonBean(BOTTOM_BT_2,null));
        bottomButtonList.add(new PPDButtonBean(BOTTOM_BT_CANCEL,null));
        //为了 能从底部开始绘制,所以将数组倒序一下
        Collections.reverse(bottomButtonList);

        //实例化的时候 将图片资源加载进来,避免每次 draw时加载
        //bitmapTopView =((BitmapDrawable)mResources.getDrawable(R.drawable.guid_update_text)).getBitmap();

        initPaint();

    }

    /**
     * 初始化  画笔
     */
    private void initPaint(){
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mBottomBtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBottomBtPaint.setColor(Color.WHITE);
    }

    public static ZcZyPickPhotoDialog newInstance(Context context,float alphaValue) {
        ZcZyPickPhotoDialog instanceView = new ZcZyPickPhotoDialog(context);
        instanceView.invalidate();
        instanceView.setVisibility(View.GONE);

        final WindowManager.LayoutParams relLayoutParams = new WindowManager.LayoutParams();
        relLayoutParams.alpha = alphaValue;
        // 获取WindowManager
        WindowManager mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(instanceView, relLayoutParams);
        return instanceView;
    }
    public static ZcZyPickPhotoDialog newInstance(Context context){
        return ZcZyPickPhotoDialog.newInstance(context,0.9f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        //绘制 半透明的 背景
        Rect fullRect = new Rect(0, 0, canvasWidth, canvasHeight);
        mBgPaint.setColor(Color.argb(255, 145, 156, 159));
        canvas.drawRect(fullRect, mBgPaint);

        //画底部的按钮:拍照,从相册选择,取消
        if (null != bottomButtonList && bottomButtonList.size()>1){
            int btCount = bottomButtonList.size();
            for (int i = 0;i<btCount;i++){
                PPDButtonBean itemBean = bottomButtonList.get(i);
                Rect tmpRect = getBottomBtRectByIndex(i,canvasWidth,canvasHeight);
                drawTextOnRect(canvas,mBgPaint,Color.WHITE,Color.DKGRAY,44f,itemBean.getName(),tmpRect);
                itemBean.setContentRect(tmpRect);
                bottomButtonList.set(i,itemBean);
            }
        }
    }

    public void PPD_openOnView(ImageView view){
        this.setVisibility(VISIBLE);
        currentImageView = view;
    }
    public void PPD_dismiss(){
        this.setVisibility(GONE);
        //取消 于资源ID 的关联
        currentImageView = null;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            //用户点击了返回,相当于 点击了 "取消功能"
            PPD_dismiss();
            if (null != this.iPPDClickListener){
                iPPDClickListener.onCancelBtClicked();
            }
        }
        //返回true,拦截当前View的 点击事件,避免操作到下面一层view 的事件
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.e(TAG, ">>>>"+event.getAction());
        if (MotionEvent.ACTION_UP == event.getAction()){
            float clickX = event.getX();
            float clickY = event.getY();

            int clickXInt = Float.valueOf(clickX).intValue();
            int clickYInt = Float.valueOf(clickY).intValue();
            if (null != bottomButtonList && bottomButtonList.size()>1){
                int btCount = bottomButtonList.size();
                for (int i = 0;i<btCount;i++){
                    PPDButtonBean itemBean = bottomButtonList.get(i);
                    Rect tmpItemRect = itemBean.getContentRect();
                    if (null !=tmpItemRect && tmpItemRect.contains(clickXInt,clickYInt)){
                        //点击了一个选项,注意,在处理的时候都是倒序的,所以返回给调用者的时候,要转一下
                        String clickName = itemBean.getName();
                        if (BOTTOM_BT_1.equals(clickName)){
                            if (null != this.iPPDClickListener){
                                iPPDClickListener.onTakePhotoBtClicked();
                            }

                            if (currentImageView.getId() > 0){
                                mActionType = ACTION_TYPE.ACTION_TYPE_TAKE_PHOTO;
                                PPDTools.takePhoto(mContext,currentImageView.getId());
                            }else{
                                Log.e("error","调用PPD_openOnView时,没有指定viewId");
                            }
                        }else if (BOTTOM_BT_2.equals(clickName)){
                            if (null != this.iPPDClickListener){
                                iPPDClickListener.onPickAlbumBtClicked();
                            }
                            if (currentImageView.getId() > 0){
                                mActionType = ACTION_TYPE.ACTION_TYPE_PICK_IMAGE;
                                PPDTools.openAlbum(mContext,currentImageView.getId());
                            }else{
                                Log.e("error","调用PPD_openOnView时,没有指定viewId");
                            }
                        }else if (BOTTOM_BT_CANCEL.equals(clickName)){
                            PPD_dismiss();
                            if (null != this.iPPDClickListener){
                                iPPDClickListener.onCancelBtClicked();
                            }
                        }
                    }
                }
            }
        }
        //返回true,拦截当前View的 点击事件,避免操作到下面一层view 的事件
        return true;
    }

    private void drawTextOnRect(Canvas pCanvas,Paint pPaint,int bgColor,int textColor,float textSize,String text,Rect targetRect){
        //备份 画笔的原来属性
        float oldTextSize = pPaint.getTextSize();
        int oldPaintColor = pPaint.getColor();

        //画背景
        pPaint.setColor(bgColor);
        pCanvas.drawRect(targetRect, pPaint);

        //写文字
        pPaint.setTextSize(textSize);
        pPaint.setColor(textColor);

        Paint.FontMetricsInt fontMetrics = pPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，setTextAlign(Paint.Align.CENTER);
        //标示此时画笔写出的文字的原点,都已所写出文字的中心作为原点.
        //那么此时如果想让文字水平居中,就要让文字中心原点对准目标容器的中心原点
        //所以  drawText对应传入targetRect.centerX()
        pPaint.setTextAlign(Paint.Align.CENTER);
        pCanvas.drawText(text, targetRect.centerX(), baseline, pPaint);

        //还原 画笔的属性
        pPaint.setTextSize(oldTextSize);
        pPaint.setColor(oldPaintColor);
    }

    /**
     * 在一个容器中,从底部开始  网上 排列
     * @param index
     * @param contentWidth
     * @param canvasHeight
     * @return
     */
    private Rect getBottomBtRectByIndex(int index,int contentWidth, int canvasHeight){
        //按钮的高度
        int btHeight = canvasHeight/12;

        //取消按钮距离上面 按钮的间距
        int paddingForCancleButton = btHeight/7;

        //相邻 按钮间分割线的高度
        int btDividerHeight = 4;

        Rect returnRect = null;
        if (index == 0){
            //最底部的 "取消"功能
            returnRect = new Rect(0,canvasHeight-btHeight,contentWidth,canvasHeight);
        }else{
            returnRect = new Rect(0,
                    canvasHeight-(index+1)*btHeight-paddingForCancleButton-index*btDividerHeight,
                    contentWidth,
                    canvasHeight-(index)*btHeight-paddingForCancleButton-index*btDividerHeight);
        }
        return returnRect;
    }


    public interface IPPDClickListener{
        public void onCancelBtClicked();

        /**
         * 点击了 拍照按钮
         */
        public void onTakePhotoBtClicked();
        /**
         * 点击了 从相册选择 按钮
         */
        public void onPickAlbumBtClicked();
    }
    private IPPDClickListener iPPDClickListener;
    public void setiPPDClickListener(IPPDClickListener iPPDClickListener) {
        this.iPPDClickListener = iPPDClickListener;
    }

    public String PPD_onActivityResult(int requestCode, int resultCode, Intent data){
        if (null == currentImageView || requestCode != IMGIDS.getInstance().queryRequestCode(currentImageView.getId())){
            //使用 requestCode标示 请求是从哪个图片对象 发过来的
            return "";
        }

        this.setVisibility(GONE);
        if (mActionType == ACTION_TYPE.ACTION_TYPE_TAKE_PHOTO) {

            if (resultCode == Activity.RESULT_OK) {
                File takePhotoImageFile = PPDUtils.getDiskCacheFile(mContext,currentImageView.getId());
                if (PPDUtils.getFileSize(takePhotoImageFile) > PPDTools.maxFileLength) {
                    Toast.makeText(mContext,"上传图片不能大于10M或者图片路径包含中文特殊字符",Toast.LENGTH_SHORT);
                    return "";
                }
                Log.e(",,,,,,,,,,,,,,,,,",takePhotoImageFile.getAbsolutePath());
                ImageLoader.getInstance().displayImage("file://"+takePhotoImageFile.getAbsolutePath(), currentImageView);
//                runPublicImage(picFileFullName);

                return takePhotoImageFile.getAbsolutePath();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // 用户取消了图像捕获
                return "";
            } else {
                // 图像捕获失败，提示用户
                Toast.makeText(mContext,"拍照失败",Toast.LENGTH_SHORT);
                return "";
            }
        } else if (mActionType == ACTION_TYPE.ACTION_TYPE_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    String realPath =PPDTools.getRealPathFromURI(uri,mContext);

                    if (null == realPath||PPDUtils.getFileSize(new File(realPath)) > PPDTools.maxFileLength) {
                        Toast.makeText(mContext,"上传图片不能大于10M或者图片路径包含中文特殊字符",Toast.LENGTH_SHORT);
                        return "";
                    }
                    Log.e(",,,,,,,,,,,,,,,,,",realPath);
                    ImageLoader.getInstance().displayImage("file://"+realPath, currentImageView);

//                    runPublicImage(realPath);
                    return realPath;
                } else {
                    Toast.makeText(mContext,"从相册获取图片失败",Toast.LENGTH_SHORT);
                    return "";
                }
            }else {
                return "";
            }
        }else {
            return "";
        }
    }


}
