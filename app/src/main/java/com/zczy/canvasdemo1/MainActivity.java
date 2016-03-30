package com.zczy.canvasdemo1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zczy.canvasdemo1.CustomView.DemoView1;
import com.zczy.guidupdateview.GuidUpdateView;

public class MainActivity extends ActionBarActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        initGuidView();
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
            }
        });

    }


    private void initGuidView(){
        GuidUpdateView guidUpdateView = new GuidUpdateView(this);
        guidUpdateView.invalidate();
        LinearLayout.LayoutParams relLayoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        this.addContentView(guidUpdateView,relLayoutParams);

        guidUpdateView.setiGuidUpdateViewClickListener(new GuidUpdateView.IGuidUpdateViewClickListener() {
            @Override
            public void onBT1ClickListener() {
                Log.e(TAG,"点击了 BT1");
            }

            @Override
            public void onBT2ClickListener() {
                Log.e(TAG,"点击了 BT2");
            }

            @Override
            public void onSkipClickListener() {
                Log.e(TAG,"点击了 Skip");
            }
        });
    }
}
