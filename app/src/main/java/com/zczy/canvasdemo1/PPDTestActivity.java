package com.zczy.canvasdemo1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ragnarok.rxcamera.RxCamera;
import com.ragnarok.rxcamera.config.CameraUtil;
import com.ragnarok.rxcamera.config.RxCameraConfig;
import com.ragnarok.rxcamera.config.RxCameraConfigChooser;
import com.zczy.canvasdemo1.pickPhoto.UploadImageView;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;


public class PPDTestActivity extends Activity {
    private static String TAG = PPDTestActivity.class.getSimpleName();

    private UploadImageView test_imageView_1;
    private UploadImageView test_imageView_2;
    private UploadImageView test_imageView_3;
    private Button test_uploadBt_1,test_uploadBt_2,test_uploadBt_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppdtest);


        test_imageView_1 = (UploadImageView) findViewById(R.id.test_imageView_1);
        test_uploadBt_1 = (Button) findViewById(R.id.test_uploadBt_1);

        test_imageView_2 = (UploadImageView) findViewById(R.id.test_imageView_2);
        test_uploadBt_2 = (Button) findViewById(R.id.test_uploadBt_2);

        test_imageView_3 = (UploadImageView) findViewById(R.id.test_imageView_3);
        test_uploadBt_3 = (Button) findViewById(R.id.test_uploadBt_3);


        test_imageView_1.UIV_LoadImage("http://img1.gtimg.com/sports/pics/hv1/111/32/2050/133309521.jpg",R.mipmap.ic_launcher);


        test_uploadBt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        test_imageView_1.UIV_onActivityResult(requestCode,resultCode,data);
        test_imageView_2.UIV_onActivityResult(requestCode,resultCode,data);
        test_imageView_3.UIV_onActivityResult(requestCode,resultCode,data);
    }



}
