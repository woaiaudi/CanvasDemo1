package com.zczy.canvasdemo1.pickPhoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

/**
 * Created by mac on 16/4/11.
 */
public class PPDTools {
//    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
//
//    public static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;

    public static final long maxFileLength = 10 * 1024 * 1024;

    /**
     * 拍照
     * @param context
     * @return 返回 拍照成功后,保存所拍照片的绝对路径
     */
    public static void takePhoto(Context context,int viewId){
        //拍照
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File tmpDesFile = PPDUtils.getDiskCacheFile(context,viewId);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpDesFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            int tmpRCode= IMGIDS.getInstance().queryRequestCode(viewId);
            ((Activity)context).startActivityForResult(intent, tmpRCode);
        } else {
            Toast.makeText(context,"SD 卡有误！",Toast.LENGTH_SHORT);
        }
    }


    /**
     * 打开本地相册
     *
     */
    public static void openAlbum(Context context,int viewId) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        int tmpRCode= IMGIDS.getInstance().queryRequestCode(viewId);
        ((Activity)context).startActivityForResult(intent, tmpRCode);
    }

    /**
     * This method is used to get real path of file from from uri<br/>
     * http://stackoverflow.com/questions/11591825/how-to-get-image-path-just-
     * captured-from-camera
     *
     * @param contentUri
     * @return String
     */
    public static String getRealPathFromURI(Uri contentUri,Context context) {

        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            // Do not call Cursor.close() on a cursor obtained using this
            // method,
            // because the activity will do that for you at the appropriate time
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            // Cursor cursor = this.managedQuery(contentUri, proj, null, null,
            // null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }



}
