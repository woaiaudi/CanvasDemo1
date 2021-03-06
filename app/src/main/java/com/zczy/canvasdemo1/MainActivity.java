package com.zczy.canvasdemo1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.http.RequestManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zczy.canvasdemo1.CustomView.DemoView1;
import com.zczy.canvasdemo1.OCRDemo.OcrDemoActivity;
import com.zczy.guidupdateview.GuidUpdateView;
import com.zczy.ocryuanmai.OCRDemoMainActivity;

public class MainActivity extends Activity {
    private static String TAG = MainActivity.class.getSimpleName();

    private GuidUpdateView guidUpdateView;

    private Button bt1,bt2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initImageLoader(this);

        RequestManager.getInstance().init(this);

        guidUpdateView = GuidUpdateView.newInstance(this);
        guidUpdateView.GUV_setMarginTopForTopview(100);

        guidUpdateView.setiGuidUpdateViewClickListener(new GuidUpdateView.IGuidUpdateViewClickListener() {
            @Override
            public void onBT1ClickListener() {
                Log.e(TAG, "点击了 BT1");
            }

            @Override
            public void onBT2ClickListener() {
                Log.e(TAG, "点击了 BT2");
            }

            @Override
            public void onSkipClickListener() {
                Log.e(TAG, "点击了 Skip");
                guidUpdateView.GUV_dismiss();
            }
        });

        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///PPDTestActivity/OCRDemoMainActivity
                Intent intent = new Intent(MainActivity.this, OcrDemoActivity.class);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///PPDTestActivity/OCRDemoMainActivity
                Intent intent = new Intent(MainActivity.this, OCRDemoMainActivity.class);
                startActivity(intent);
            }
        });

    }


    private void init() {
        FrameLayout layout=(FrameLayout) findViewById(R.id.demoview_content);
        final DemoView1 view=new DemoView1(this);
        view.setMinimumHeight(500);
        view.setMinimumWidth(300);
        //通知view组件重绘
        view.invalidate();
        layout.addView(view);

        view.setiClickListener(new DemoView1.IDemoView1ClickListener() {
            @Override
            public void onCircleButtonClickListener() {
                //点击了原型按钮
                //guidUpdateView.GUV_show();
                //OcrDemoActivity/PPDTestActivity/OCRDemoMainActivity
                Intent intent = new Intent(MainActivity.this, PPDTestActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     *
     * 方法描述 :图片缓存
     *
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();

        ImageLoader.getInstance().init(config);
    }
}
