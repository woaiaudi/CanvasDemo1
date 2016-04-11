package com.zczy.canvasdemo1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

        test_imageView_2 = (UploadImageView) findViewById(R.id.test_imageView_2);
        test_uploadBt_2 = (Button) findViewById(R.id.test_uploadBt_2);

        test_imageView_3 = (UploadImageView) findViewById(R.id.test_imageView_3);
        test_uploadBt_3 = (Button) findViewById(R.id.test_uploadBt_3);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mZcZyPickPhotoDialog.PPD_onActivityResult(requestCode,resultCode,data);

        test_imageView_1.MIV_onActivityResult(requestCode,resultCode,data);
        test_imageView_2.MIV_onActivityResult(requestCode,resultCode,data);
        test_imageView_3.MIV_onActivityResult(requestCode,resultCode,data);
    }
}
