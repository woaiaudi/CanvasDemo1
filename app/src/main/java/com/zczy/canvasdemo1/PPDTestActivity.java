package com.zczy.canvasdemo1;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zczy.canvasdemo1.pickPhoto.UploadImageView;

public class PPDTestActivity extends ActionBarActivity {
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
        test_uploadBt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownTimer.start();
            }
        });

        test_imageView_2 = (UploadImageView) findViewById(R.id.test_imageView_2);
        test_uploadBt_2 = (Button) findViewById(R.id.test_uploadBt_2);
        test_uploadBt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cancel后 重新 计数
                mCountDownTimer.cancel();
            }
        });

        test_imageView_3 = (UploadImageView) findViewById(R.id.test_imageView_3);
        test_uploadBt_3 = (Button) findViewById(R.id.test_uploadBt_3);


        test_imageView_1.UIV_LoadImage("http://img1.gtimg.com/sports/pics/hv1/111/32/2050/133309521.jpg",R.mipmap.ic_launcher);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mZcZyPickPhotoDialog.PPD_onActivityResult(requestCode,resultCode,data);

        test_imageView_1.UIV_onActivityResult(requestCode,resultCode,data);
        test_imageView_2.UIV_onActivityResult(requestCode,resultCode,data);
        test_imageView_3.UIV_onActivityResult(requestCode,resultCode,data);
    }


    /**
     * 维护一个 图片上传的时钟,
     * 每次 上传开始时,时钟开启
     * 当 上传结束,时钟清零,停止
     */
    CountDownTimer mCountDownTimer = new CountDownTimer(24*60*60*1000, 250) {
        @Override
        public void onTick(long l) {
            Log.e(TAG,"RUNNING:::"+l);
        }

        @Override
        public void onFinish() {
            Log.e(TAG,"onFinish:::");
        }
    };
}
