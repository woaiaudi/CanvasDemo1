package com.zczy.canvasdemo1.pickPhoto;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by mac on 16/4/11.
 */
public class MyImageView extends ImageView {

    private ZcZyPickPhotoDialog mZcZyPickPhotoDialog;

    public MyImageView(Context context) {
        super(context);
        if (null == mZcZyPickPhotoDialog){
            mZcZyPickPhotoDialog = ZcZyPickPhotoDialog.newInstance(context);
        }
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (null == mZcZyPickPhotoDialog){
            mZcZyPickPhotoDialog = ZcZyPickPhotoDialog.newInstance(context);
        }
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (null == mZcZyPickPhotoDialog){
            mZcZyPickPhotoDialog = ZcZyPickPhotoDialog.newInstance(context);
        }
    }

    public ZcZyPickPhotoDialog getmZcZyPickPhotoDialog() {
        return mZcZyPickPhotoDialog;
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

    public void MIV_onActivityResult(int requestCode, int resultCode, Intent data){
        if (null != mZcZyPickPhotoDialog){
            mZcZyPickPhotoDialog.PPD_onActivityResult(requestCode,resultCode,data);
        }
    }
}
