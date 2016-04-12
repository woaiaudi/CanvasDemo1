package com.zczy.canvasdemo1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ragnarok.rxcamera.RxCamera;
import com.ragnarok.rxcamera.RxCameraData;
import com.ragnarok.rxcamera.config.RxCameraConfig;
import com.ragnarok.rxcamera.config.RxCameraConfigChooser;
import com.ragnarok.rxcamera.request.Func;
import com.zczy.canvasdemo1.pickPhoto.IMGIDS;
import com.zczy.canvasdemo1.pickPhoto.PPDUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class PPDCameraActivity extends Activity {
    public static final String EXTRA_TARGET_IMG_ID = "EXTRA_TARGET_IMG_ID";
    private static String TAG = PPDCameraActivity.class.getSimpleName();

    private Button ppdcamera_bt_cancel;
    private Button ppdcamera_bt_ok;

    private TextureView ppdcamera_textureview;

    private RxCamera mRxCamera;

    private int currentTargetImageViewId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppdcamera);

        ppdcamera_bt_cancel = (Button) findViewById(R.id.ppdcamera_bt_cancel);
        ppdcamera_bt_ok = (Button) findViewById(R.id.ppdcamera_bt_ok);
        ppdcamera_textureview = (TextureView) findViewById(R.id.ppdcamera_textureview);

        try {
            currentTargetImageViewId = getIntent().getIntExtra(EXTRA_TARGET_IMG_ID,0);
        }catch (Exception e){
            e.printStackTrace();
        }


        ppdcamera_bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
openCamera();

            }
        });

        ppdcamera_bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestTakePicture();

//                if (mRxCamera != null) {
//                    mRxCamera.closeCameraWithResult().subscribe(new Action1<Boolean>() {
//                        @Override
//                        public void call(Boolean aBoolean) {
//                            Log.e(TAG,"close camera finished, success: " + aBoolean);
//                        }
//                    });
//                }
            }
        });
    }


    /**
     * 打开 相机
     */
    private void openCamera() {
        RxCameraConfig config = RxCameraConfigChooser.obtain().
                useBackCamera().
                setAutoFocus(true).
                setPreferPreviewFrameRate(15, 30).
                setPreferPreviewSize(new Point(640, 480)).
                setHandleSurfaceEvent(true).
                get();
        Log.d(TAG, "config: " + config);
        RxCamera.open(this, config).flatMap(new Func1<RxCamera, Observable<RxCamera>>() {
            @Override
            public Observable<RxCamera> call(RxCamera rxCamera) {
                Log.e(TAG,"isopen: " + rxCamera.isOpenCamera() + ", thread: " + Thread.currentThread());
                mRxCamera = rxCamera;
                return rxCamera.bindTexture(ppdcamera_textureview);
            }
        }).flatMap(new Func1<RxCamera, Observable<RxCamera>>() {
            @Override
            public Observable<RxCamera> call(RxCamera rxCamera) {
                Log.e(TAG,"isbindsurface: " + rxCamera.isBindSurface() + ", thread: " + Thread.currentThread());
                return rxCamera.startPreview();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<RxCamera>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"open camera error: " + e.getMessage());
            }

            @Override
            public void onNext(final RxCamera rxCamera) {
                mRxCamera = rxCamera;
                Log.e(TAG,"open camera success: " + mRxCamera);
                Toast.makeText(PPDCameraActivity.this, "Now you can tap to focus", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRxCamera != null) {
            mRxCamera.closeCamera();
        }
    }



    private void requestSuccessiveData() {
        if (!checkCamera()) {
            return;
        }
        mRxCamera.request().successiveDataRequest().subscribe(new Action1<RxCameraData>() {
            @Override
            public void call(RxCameraData rxCameraData) {
                Log.e(TAG,"successiveData, cameraData.length: " + rxCameraData.cameraData.length);
            }
        });
    }

    private void requestOneShot() {
        if (!checkCamera()) {
            return;
        }
        mRxCamera.request().oneShotRequest().subscribe(new Action1<RxCameraData>() {
            @Override
            public void call(RxCameraData rxCameraData) {
                showLog("one shot request, cameraData.length: " + rxCameraData.cameraData.length);
            }
        });
    }

    private void requestPeriodicData() {
        if (!checkCamera()) {
            return;
        }
        mRxCamera.request().periodicDataRequest(1000).subscribe(new Action1<RxCameraData>() {
            @Override
            public void call(RxCameraData rxCameraData) {
                showLog("periodic request, cameraData.length: " + rxCameraData.cameraData.length);
            }
        });
    }

    private void requestTakePicture() {
        if (!checkCamera()) {
            return;
        }
        mRxCamera.request().takePictureRequest(true, new Func() {
            @Override
            public void call() {
                showLog("Captured!");
            }
        }, 480, 640, ImageFormat.JPEG).subscribe(new Action1<RxCameraData>() {
            @Override
            public void call(RxCameraData rxCameraData) {
//                String path = Environment.getExternalStorageDirectory() + "/test.jpg";
                File file = PPDUtils.getDiskCacheFile(PPDCameraActivity.this,currentTargetImageViewId);
                Bitmap bitmap = BitmapFactory.decodeByteArray(rxCameraData.cameraData, 0, rxCameraData.cameraData.length);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                        rxCameraData.rotateMatrix, false);
                try {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showLog("Save file on " + file.getAbsolutePath());
                int resultCode= IMGIDS.getInstance().queryRequestCode(currentTargetImageViewId);
                PPDCameraActivity.this.setResult(resultCode, new Intent());
                PPDCameraActivity.this.finish();

            }
        });
    }

    private void actionZoom() {
        if (!checkCamera()) {
            return;
        }
        mRxCamera.action().zoom(10).subscribe(new Subscriber<RxCamera>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showLog("zoom error: " + e.getMessage());
            }

            @Override
            public void onNext(RxCamera rxCamera) {
                showLog("zoom success: " + rxCamera);
            }
        });
    }

    private void actionSmoothZoom() {
        if (!checkCamera()) {
            return;
        }
        mRxCamera.action().smoothZoom(10).subscribe(new Subscriber<RxCamera>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showLog("zoom error: " + e.getMessage());
            }

            @Override
            public void onNext(RxCamera rxCamera) {
                showLog("zoom success: " + rxCamera);
            }
        });
    }

//    private void actionOpenFlash() {
//        if (!checkCamera()) {
//            return;
//        }
//
//        mRxCamera.action().flashAction(true).subscribe(new Subscriber<RxCamera>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                showLog("open flash error: " + e.getMessage());
//            }
//
//            @Override
//            public void onNext(RxCamera rxCamera) {
//                showLog("open flash");
//            }
//        });
//    }

//    private void actionCloseFlash() {
//        if (!checkCamera()) {
//            return;
//        }
//        mRxCamera.action()
//        mRxCamera.action().flashAction(false).subscribe(new Subscriber<RxCamera>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                showLog("close flash error: " + e.getMessage());
//            }
//
//            @Override
//            public void onNext(RxCamera rxCamera) {
//                showLog("close flash");
//            }
//        });
//    }



    private boolean checkCamera() {
        if (mRxCamera == null || !mRxCamera.isOpenCamera()) {
            return false;
        }
        return true;
    }

    private void showLog(String logStr) {
        Log.e(TAG,logStr);
    }
}
