package com.zczy.canvasdemo1.OCRDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zczy.canvasdemo1.R;
import com.zczy.canvasdemo1.UpLoadModel;
import com.zczy.canvasdemo1.pickPhoto.UploadImageView;

import java.util.List;

public class OcrDemoActivity extends Activity {
    private UploadImageView ocr_imageView_1;


    private TextView ocr_result_scrollview_tv_zhenghao;
    private TextView ocr_result_scrollview_tv_xingming;
    private TextView ocr_result_scrollview_tv_xingbie;
    private TextView ocr_result_scrollview_tv_guoji;
    private TextView ocr_result_scrollview_tv_zhuzhi;
    private TextView ocr_result_scrollview_tv_chushengriqi;
    private TextView ocr_result_scrollview_tv_lingzhengriqi;
    private TextView ocr_result_scrollview_tv_zhunjiachexing;
    private TextView ocr_result_scrollview_tv_youxiaoqixian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_demo);
        ocr_imageView_1 = (UploadImageView) findViewById(R.id.ocr_imageView_1);
        ocr_imageView_1.setiUploadListener(uivListener);


        initView();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ocr_imageView_1.UIV_onActivityResult(requestCode,resultCode,data);
    }




    private void initView(){

        ocr_result_scrollview_tv_zhenghao = (TextView) findViewById(R.id.ocr_result_scrollview_tv_zhenghao);
        ocr_result_scrollview_tv_xingming = (TextView) findViewById(R.id.ocr_result_scrollview_tv_xingming);
        ocr_result_scrollview_tv_xingbie = (TextView) findViewById(R.id.ocr_result_scrollview_tv_xingbie);
        ocr_result_scrollview_tv_guoji = (TextView) findViewById(R.id.ocr_result_scrollview_tv_guoji);
        ocr_result_scrollview_tv_zhuzhi = (TextView) findViewById(R.id.ocr_result_scrollview_tv_zhuzhi);
        ocr_result_scrollview_tv_chushengriqi = (TextView) findViewById(R.id.ocr_result_scrollview_tv_chushengriqi);
        ocr_result_scrollview_tv_lingzhengriqi = (TextView) findViewById(R.id.ocr_result_scrollview_tv_lingzhengriqi);
        ocr_result_scrollview_tv_zhunjiachexing = (TextView) findViewById(R.id.ocr_result_scrollview_tv_zhunjiachexing);
        ocr_result_scrollview_tv_youxiaoqixian = (TextView) findViewById(R.id.ocr_result_scrollview_tv_youxiaoqixian);
    }
    UploadImageView.UIV_IUploadListener uivListener = new UploadImageView.UIV_IUploadListener() {
        @Override
        public void onUploadSuccess(UpLoadModel imageBean) {

        }

        @Override
        public void onUploadError(String msg) {

        }

        @Override
        public void onOCR(OCRResultBean ocrBean) {
            updateUI(ocrBean);
        }
    };

    private void updateUI(OCRResultBean ocrBean){
        if (ocrBean.getMessage().getStatus() > 0){
            if (null != ocrBean.getCardsinfo()
                    && ocrBean.getCardsinfo().size() > 0){
                List<OCRCardContentItemBean> tmpList = ocrBean.getCardsinfo().get(0).getItems();
                if (null != tmpList&&tmpList.size()>0){
                    for (OCRCardContentItemBean item:tmpList) {
                        if ("证号".equals(item.getDesc())){
                            ocr_result_scrollview_tv_zhenghao.setText(item.getContent());
                        }else if ("姓名".equals(item.getDesc())){
                            ocr_result_scrollview_tv_xingming.setText(item.getContent());
                        }else if ("性别".equals(item.getDesc())){
                            ocr_result_scrollview_tv_xingbie.setText(item.getContent());
                        }else if ("国籍----".equals(item.getDesc())){
                            ocr_result_scrollview_tv_guoji.setText(item.getContent());
                        }else if ("住址".equals(item.getDesc())){
                            ocr_result_scrollview_tv_zhuzhi.setText(item.getContent());
                        }else if ("出生日期".equals(item.getDesc())){
                            ocr_result_scrollview_tv_chushengriqi.setText(item.getContent());
                        }else if ("初始领证日期".equals(item.getDesc())){
                            ocr_result_scrollview_tv_lingzhengriqi.setText(item.getContent());
                        }else if ("准驾车型".equals(item.getDesc())){
                            ocr_result_scrollview_tv_zhunjiachexing.setText(item.getContent());
                        }else if ("有效起始日期".equals(item.getDesc())){
                            ocr_result_scrollview_tv_youxiaoqixian.setText(item.getContent());
                        }
                    }
                }
            }

        }

    }
}
