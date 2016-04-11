package com.zczy.canvasdemo1.pickPhoto;

import android.content.Context;
import android.os.Environment;
import java.io.File;

/**
 * Created by mac on 16/4/11.
 */
public class PPDUtils {

    /**
     * 根据 viewId 生产 一个唯一的图片
     * @param context
     * @param viewId
     * @return
     */
    public static File getDiskCacheFile(Context context, int viewId) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        File outDir = new File(cachePath + File.separator + "take-image");
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        return new File(outDir, "IMG_" +viewId+ ".jpg");
    }

//    /**
//     * 获取文件大小
//     * @return
//     */
//    public static long getDiskCacheFileSize(Context context, int viewId) {
//        long size = 0;
//        File file = PPDUtils.getDiskCacheFile(context,viewId);
//        if (file != null && file.exists()) {
//            size = file.length();
//        }
//        return size;
//    }

    /**
     * 获取文件大小
     * @return
     */
    public static long getFileSize(File pFile) {
        long size = 0;

        if (pFile != null && pFile.exists()) {
            size = pFile.length();
        }
        return size;
    }

}
