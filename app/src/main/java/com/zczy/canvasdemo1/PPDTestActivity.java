package com.zczy.canvasdemo1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zczy.canvasdemo1.pickPhoto.ZcZyPickPhotoDialog;

public class PPDTestActivity extends ActionBarActivity {
    private static String TAG = PPDTestActivity.class.getSimpleName();

    private ZcZyPickPhotoDialog mZcZyPickPhotoDialog;
    private ImageView test_imageView_1,test_imageView_2,test_imageView_3;
    private Button test_uploadBt_1,test_uploadBt_2,test_uploadBt_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppdtest);



        test_imageView_1 = (ImageView) findViewById(R.id.test_imageView_1);
        test_uploadBt_1 = (Button) findViewById(R.id.test_uploadBt_1);

        test_imageView_2 = (ImageView) findViewById(R.id.test_imageView_2);
        test_uploadBt_2 = (Button) findViewById(R.id.test_uploadBt_2);

        test_imageView_3 = (ImageView) findViewById(R.id.test_imageView_3);
        test_uploadBt_3 = (Button) findViewById(R.id.test_uploadBt_3);

        mZcZyPickPhotoDialog = ZcZyPickPhotoDialog.newInstance(this);

        test_uploadBt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mZcZyPickPhotoDialog.PPD_openOnView(test_imageView_1);
            }
        });

        test_uploadBt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mZcZyPickPhotoDialog.PPD_openOnView(test_imageView_2);
            }
        });

        test_uploadBt_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mZcZyPickPhotoDialog.PPD_openOnView(test_imageView_3);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mZcZyPickPhotoDialog.PPD_onActivityResult(requestCode,resultCode,data);
    }
}
