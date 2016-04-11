package com.zczy.canvasdemo1.pickPhoto;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.android.http.LoadControler;
import com.android.http.RequestManager;
import com.android.http.RequestMap;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by mac on 16/4/11.
 */
public class UploadImageView extends ImageView {

    private static final String TAG = UploadImageView.class.getSimpleName();
    private Context mContext;

    private ZcZyPickPhotoDialog mZcZyPickPhotoDialog;
    //http://172.168.10.168:8080
    //http://172.168.10.249:8081
    private static final String HOST_NAME = "http://172.168.10.168:8080";
    private static String URL_UPLOAD = HOST_NAME+"/mobile/app/mfileupload/uploadFile.xhtml";

    public UploadImageView(Context context) {
        super(context);
        mContext = context;
        if (null == mZcZyPickPhotoDialog){
            mZcZyPickPhotoDialog = ZcZyPickPhotoDialog.newInstance(context);
        }
    }

    public UploadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        if (null == mZcZyPickPhotoDialog){
            mZcZyPickPhotoDialog = ZcZyPickPhotoDialog.newInstance(context);
        }
    }

    public UploadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (null == mZcZyPickPhotoDialog){
            mZcZyPickPhotoDialog = ZcZyPickPhotoDialog.newInstance(context);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()){
            //点击了图片,首先判断image 是否有图片在展示,

            //wutup
            mZcZyPickPhotoDialog.PPD_openOnView(this);
        }

        return true;
    }

    public void MIV_onActivityResult(final int requestCode, int resultCode, Intent data){
        if (null != mZcZyPickPhotoDialog){
            final String imageLocalPath = mZcZyPickPhotoDialog.PPD_onActivityResult(requestCode,resultCode,data);
            if (null != imageLocalPath
                    && imageLocalPath.length()>0){
                Log.e(TAG,"要上传的图片是:"+imageLocalPath);
                // TODO: 16/4/11  此时 需要 执行 图片上传
                RequestManager.getInstance().request(0,loginUrl,(Object)null, new RequestManager.RequestListener() {
                        @Override
                        public void onRequest() {

                        }

                        @Override
                        public void onSuccess(String s, String s1, int i) {
                            demoMap = new Gson().fromJson(s, Map.class);

                            Log.e(TAG,s+"\n"+demoMap);
                            int imageViewId = IMGIDS.getInstance().queryIMGID(requestCode);
                            runUploadImage(imageLocalPath,imageViewId);
                        }

                        @Override
                        public void onError(String s, String s1, int i) {

                        }
                    },false,15*60*1000,0,3);


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

        LoadControler mLoadController = RequestManager.getInstance().post(URL_UPLOAD, params, new RequestManager.RequestListener() {
            @Override
            public void onRequest() {

            }

            @Override
            public void onSuccess(String s, String s1, int i) {
                Log.e(TAG,s);
            }

            @Override
            public void onError(String s, String s1, int i) {
                Log.e(TAG,s);
            }
        },false,10000,0,2);


    }




    private static String loginUrl = HOST_NAME+"/mobile/app/mMember/doLogin.xhtml?password=DD4B21E9EF71E1291183A46B913AE6F2&userName=13000000002&mac=009acd134698b91d23bd18c01bbb866940022884506";
    private Map<String,Object> demoMap = new HashMap<String, Object>();

}
