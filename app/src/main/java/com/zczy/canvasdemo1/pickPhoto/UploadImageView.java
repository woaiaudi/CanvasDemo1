package com.zczy.canvasdemo1.pickPhoto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.android.http.LoadControler;
import com.android.http.RequestManager;
import com.android.http.RequestMap;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zczy.canvasdemo1.OCRDemo.OCRResultBean;
import com.zczy.canvasdemo1.R;
import com.zczy.canvasdemo1.UpLoadModel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by mac on 16/4/11.
 */
public class UploadImageView extends ImageView {
    //http://172.168.10.168:8080
    //http://172.168.10.249:8081
    private static final String HOST_NAME = "http://172.168.10.168:8080";
    private static String URL_UPLOAD = HOST_NAME+"/mobile/app/mfileupload/uploadFile.xhtml";
    private static final String TAG = UploadImageView.class.getSimpleName();
    private Context mContext;

    private ZcZyPickPhotoDialog mZcZyPickPhotoDialog;


    /**
     * 是否显示删除按钮
     */
    private boolean canBeDeleted;

    private enum UPLOAD_STATE {
        /**
         *  初始状态
         */
        UPLOAD_STATE_DEFAULT,
        /**
         * 图片上传中
         */
        UPLOAD_STATE_UPLOADING,
        /**
         * 图片上传 完成
         */
        UPLOAD_STATE_COMPLETE,
        /**
         * 图片上传 失败
         */
        UPLOAD_STATE_ERROR
    }

    private UPLOAD_STATE mUploadState;

    private Bitmap bitmapUploadingView;
    private Bitmap bitmapUploadSuccessView;
    private Bitmap bitmapUploadErrorView;
    //绘制边框的 画笔
    private Paint mBgBodyPaint;

    private CountDownTimer mCountDownTimer;

    //记录 上传 图片 在垂直方向上的  偏移量
    private long mOffsetOnVertical;

    private void init(Context context){

        mContext = context;
        if (null == mZcZyPickPhotoDialog){
            mZcZyPickPhotoDialog = ZcZyPickPhotoDialog.newInstance(context);

            mZcZyPickPhotoDialog.setiPPDClickListener(ippdClickListener);
        }
        mUploadState = UPLOAD_STATE.UPLOAD_STATE_DEFAULT;

        //实例化的时候 将图片资源加载进来,避免每次 draw时加载
        Bitmap TMPbitmap1 =((BitmapDrawable)getResources().getDrawable(R.drawable.upload_loading)).getBitmap();
        Bitmap TMPbitmap2 =((BitmapDrawable)getResources().getDrawable(R.drawable.upload_success)).getBitmap();
        Bitmap TMPbitmap3 =((BitmapDrawable)getResources().getDrawable(R.drawable.upload_fail)).getBitmap();
        // 定义矩阵对象
        Matrix matrix=new Matrix();
        // 缩放原图
        matrix.postScale(0.5f, 0.5f);
        bitmapUploadingView=Bitmap.createBitmap(TMPbitmap1,0,0,TMPbitmap1.getWidth(),
                TMPbitmap1.getHeight(),matrix,true);

        bitmapUploadSuccessView=Bitmap.createBitmap(TMPbitmap2,0,0,TMPbitmap2.getWidth(),
                TMPbitmap2.getHeight(),matrix,true);

        bitmapUploadErrorView=Bitmap.createBitmap(TMPbitmap3,0,0,TMPbitmap3.getWidth(),
                TMPbitmap3.getHeight(),matrix,true);

        mBgBodyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgBodyPaint.setStrokeWidth(5f);
        mBgBodyPaint.setStyle(Paint.Style.STROKE); //绘制空心
        mBgBodyPaint.setColor(Color.DKGRAY);


        /**
         * 维护一个 图片上传的时钟,
         * 每次 上传开始时,时钟开启
         * 当 上传结束,时钟清零,停止
         */
        mCountDownTimer = new CountDownTimer(60*60*1000, 70) {
            @Override
            public void onTick(long l) {
                if (UploadImageView.this.mUploadState != UPLOAD_STATE.UPLOAD_STATE_UPLOADING){
                    //不在上传的时候 都调用cancel
                    UploadImageView.this.mCountDownTimer.cancel();
                }else {
                    //最大偏移量30
                    mOffsetOnVertical = mOffsetOnVertical+2;
                    if (mOffsetOnVertical > 30){
                        mOffsetOnVertical = 0;
                    }
                    //每次渲染,这样就维持了一个 固定频率的时钟,供上传动画使用
                    UploadImageView.this.invalidate();
                }
            }

            @Override
            public void onFinish() {
                UploadImageView.this.mUploadState = UPLOAD_STATE.UPLOAD_STATE_DEFAULT;
            }
        };
    }
    public UploadImageView(Context context) {
        super(context);
        init(context);
    }

    public UploadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public UploadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 设置图片
     */
    public void UIV_LoadImage(String imagePath,int placeHoldImage) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(placeHoldImage)//设置图片Uri为空或是错误的时候显示的图片
                //.showImageOnFail(R.drawable.error)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .build();//构建完成
        ImageLoader.getInstance().displayImage(imagePath, this,options);
    }

    private ZcZyPickPhotoDialog.IPPDClickListener ippdClickListener = new ZcZyPickPhotoDialog.IPPDClickListener() {
        @Override
        public void onCancelBtClicked() {

        }

        @Override
        public void onTakePhotoBtClicked() {

        }

        @Override
        public void onPickAlbumBtClicked() {

        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //这里只要处理 ,在上传 状态变价时,页面显示状态
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawRect(0,0,canvasWidth,canvasHeight,mBgBodyPaint);

        // 在画布上绘制缩放之后的baby位图
        if (this.mUploadState == UPLOAD_STATE.UPLOAD_STATE_UPLOADING){
            canvas.drawBitmap(bitmapUploadingView, canvasWidth-bitmapUploadingView.getWidth()-5, canvasHeight-bitmapUploadingView.getHeight()-5-mOffsetOnVertical, null);
        }else if (this.mUploadState == UPLOAD_STATE.UPLOAD_STATE_COMPLETE){
            canvas.drawBitmap(bitmapUploadSuccessView, canvasWidth-bitmapUploadSuccessView.getWidth()-5, canvasHeight-bitmapUploadSuccessView.getHeight()-5, null);
        }else if (this.mUploadState == UPLOAD_STATE.UPLOAD_STATE_ERROR){
            canvas.drawBitmap(bitmapUploadErrorView, canvasWidth-bitmapUploadErrorView.getWidth()-5, canvasHeight-bitmapUploadErrorView.getHeight()-5, null);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()){
            float clickX = event.getX();
            float clickY = event.getY();

            int clickXInt = Float.valueOf(clickX).intValue();
            int clickYInt = Float.valueOf(clickY).intValue();

            if (new Rect(0,0,this.getWidth(),this.getHeight()).contains(clickXInt,clickYInt)){
                //点击了图片,首先判断image 是否有图片在展示,
                if (this.getDrawable()!=null){
                    Log.e(TAG,"有图");
                    // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
                }else{
                    Log.e(TAG,"无图");
                    mZcZyPickPhotoDialog.PPD_openOnView(this);
                }
            }
        }

        return true;
    }

    public void UIV_onActivityResult(final int requestCode, int resultCode, Intent data){
        if (null != mZcZyPickPhotoDialog){
            final String imageLocalPath = mZcZyPickPhotoDialog.PPD_onActivityResult(requestCode,resultCode,data);
            if (null != imageLocalPath
                    && imageLocalPath.length()>0){
                Log.e(TAG,"要上传的图片是:"+imageLocalPath);
                // TODO: 16/4/11  此时 需要 执行 图片上传
                testOcrUpload(imageLocalPath);

//                RequestManager.getInstance().request(0,loginUrl,(Object)null, new RequestManager.RequestListener() {
//                        @Override
//                        public void onRequest() {
//
//                        }
//
//                        @Override
//                        public void onSuccess(String s, String s1, int i) {
//                            demoMap = new Gson().fromJson(s, Map.class);
//
//                            Log.e(TAG,s+"\n"+demoMap);
//                            int imageViewId = IMGIDS.getInstance().queryIMGID(requestCode);
//                            runUploadImage(imageLocalPath,imageViewId);
//                        }
//
//                        @Override
//                        public void onError(String s, String s1, int i) {
//
//                        }
//                    },false,15*60*1000,0,3);


            }

        }
    }
    /**
     * 图片上传
     * @param picFileFullName
     */
    private void runUploadImage(final String picFileFullName,int taskId){

        RequestMap params = new RequestMap();
        File uploadFile = new File(picFileFullName);
        params.put("file", uploadFile);
        params.put("share", "1");
        params.put("userId",demoMap.get("userId").toString());
        params.put("signId",demoMap.get("signId").toString());
        params.put("userName",demoMap.get("userName").toString());
        params.put("mobile",demoMap.get("mobile").toString());

        updateUploadState(UPLOAD_STATE.UPLOAD_STATE_UPLOADING);
        //开启 上传中的动画
        mCountDownTimer.start();
        LoadControler mLoadController = RequestManager.getInstance().post(URL_UPLOAD, params, new RequestManager.RequestListener() {
            @Override
            public void onRequest() {
                updateUploadState(UPLOAD_STATE.UPLOAD_STATE_UPLOADING);
            }

            @Override
            public void onSuccess(String s, String s1, int i) {
                Log.e(TAG,s);
                try {
                    UpLoadModel imageBean = new Gson().fromJson(s,UpLoadModel.class);
                    if (null != imageBean){
                        if (imageBean.getSuccess().equals("0")){
                            updateUploadState(UPLOAD_STATE.UPLOAD_STATE_COMPLETE);
                            if (null != UploadImageView.this.iUploadListener){
                                UploadImageView.this.iUploadListener.onUploadSuccess(imageBean);
                            }

                        }else{
                            updateUploadState(UPLOAD_STATE.UPLOAD_STATE_ERROR);
                            if (null != UploadImageView.this.iUploadListener) {
                                UploadImageView.this.iUploadListener.onUploadError(imageBean.getMsg());
                            }
                        }
                    }else{
                        updateUploadState(UPLOAD_STATE.UPLOAD_STATE_ERROR);
                        if (null != UploadImageView.this.iUploadListener) {
                            UploadImageView.this.iUploadListener.onUploadError(imageBean.getMsg());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    updateUploadState(UPLOAD_STATE.UPLOAD_STATE_ERROR);
                    if (null != UploadImageView.this.iUploadListener) {
                        UploadImageView.this.iUploadListener.onUploadError("图片上传失败!");
                    }
                }

            }

            @Override
            public void onError(String s, String s1, int i) {
                Log.e(TAG,s);
                updateUploadState(UPLOAD_STATE.UPLOAD_STATE_ERROR);
            }
        },false,10000,0,2);


    }

    public void updateUploadState(UPLOAD_STATE mUploadState) {
        this.mUploadState = mUploadState;
        //上传状态 更改后,要重新 绘制UI
        this.invalidate();
    }

    public interface UIV_IUploadListener{
        public void onUploadSuccess(UpLoadModel imageBean);
        public void onUploadError(String msg);




        public void onOCR(OCRResultBean ocrBean);
    }
    private UIV_IUploadListener iUploadListener;

    public void setiUploadListener(UIV_IUploadListener iUploadListener) {
        this.iUploadListener = iUploadListener;
    }





























    private static String loginUrl = HOST_NAME+"/mobile/app/mMember/doLogin.xhtml?password=DD4B21E9EF71E1291183A46B913AE6F2&userName=13000000002&mac=009acd134698b91d23bd18c01bbb866940022884506";
    private Map<String,Object> demoMap = new HashMap<String, Object>();


    private void testOcrUpload(final String picFileFullName){
        String key = "Q9cXVsRttfyGftPXC9ZWcy";				//用户ocrKey
        String secret = "814cf1f4e689405a974f0c77560d98bb";	//用户ocrSecret
        //证件类型(二代证2；行驶证6；驾照5；银行卡17；车牌19；名片 20其他详见附录)
        String typeId = "5";
        //String format = "xml";
		String format = "json"; //(返回的格式可以是xml，也可以是json)
        String url = "http://netocr.com/api/recog.do";		//http接口调用地址

        RequestMap params = new RequestMap();
        File uploadFile = new File(picFileFullName);
        params.put("file", uploadFile);
        params.put("key", key);
        params.put("secret", secret);
        params.put("typeId", typeId);
        params.put("format", format);

        //开启 上传中的动画
        mCountDownTimer.start();
        LoadControler mLoadController = RequestManager.getInstance().post(url, params, new RequestManager.RequestListener() {
            @Override
            public void onRequest() {
                updateUploadState(UPLOAD_STATE.UPLOAD_STATE_UPLOADING);
            }

            @Override
            public void onSuccess(String s, String s1, int i) {
                Log.e(TAG,s);
                try {
                    OCRResultBean ocrResultBean = new Gson().fromJson(s,OCRResultBean.class);
                    if (null != ocrResultBean){
                        if (ocrResultBean.getMessage().getStatus() > 0){
                            updateUploadState(UPLOAD_STATE.UPLOAD_STATE_COMPLETE);
                            if (null != UploadImageView.this.iUploadListener){
                                UploadImageView.this.iUploadListener.onOCR(ocrResultBean);
                            }

                        }else{
                            updateUploadState(UPLOAD_STATE.UPLOAD_STATE_ERROR);
                            if (null != UploadImageView.this.iUploadListener) {
                                UploadImageView.this.iUploadListener.onUploadError(ocrResultBean.getMessage().getValue());
                            }
                        }
                    }else{
                        updateUploadState(UPLOAD_STATE.UPLOAD_STATE_ERROR);
                        if (null != UploadImageView.this.iUploadListener) {
                            UploadImageView.this.iUploadListener.onUploadError("识别失败");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    updateUploadState(UPLOAD_STATE.UPLOAD_STATE_ERROR);
                    if (null != UploadImageView.this.iUploadListener) {
                        UploadImageView.this.iUploadListener.onUploadError("识别失败!");
                    }
                }

            }

            @Override
            public void onError(String s, String s1, int i) {
                Log.e(TAG,s);
                updateUploadState(UPLOAD_STATE.UPLOAD_STATE_ERROR);
            }
        },false,10000,0,2);
    }




}
