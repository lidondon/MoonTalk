package com.empire.vmd.client.android_lib.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.widget.Toast;

import java.io.File;

/**
 * Created by lidondon on 2016/6/7.
 */
public class ImageUtil {
    private static final int DEFAULT_THUMBNAIL_WIDTH = 128;
    private static final int DEFAULT_THUMBNAIL_HEIGHT = 128;

    public static Bitmap getThumbnailBitmap(Bitmap orgBitmap) {
        //以下兩種方法皆可用
        //影像處理的做法
//        Matrix scaleMatrix = new Matrix();
//        int orgWidth = orgBitmap.getWidth();
//        int orgHeight = orgBitmap.getHeight();
//
//        scaleMatrix.postScale(THUMBNAIL_WIDTH / orgWidth, THUMBNAIL_HEIGHT / orgHeight);
//        // 產生縮圖後的 bitmap
//        return Bitmap.createBitmap(orgBitmap, 0, 0, orgWidth, orgHeight, scaleMatrix, true);

        //ThumbnailＡＰＩ的做法
        return ThumbnailUtils.extractThumbnail(orgBitmap, DEFAULT_THUMBNAIL_WIDTH, DEFAULT_THUMBNAIL_HEIGHT);
    }

    public static Bitmap getThumbnailBitmap(String filePath) {
        return getThumbnailBitmap(filePath, DEFAULT_THUMBNAIL_WIDTH, DEFAULT_THUMBNAIL_HEIGHT);
    }

    public static Bitmap getThumbnailBitmap(String filePath, int width, int height) {
        Bitmap result = null;
        File file = new File(filePath);

        if (file.exists()) {
            BitmapFactory.Options option = new BitmapFactory.Options();
            int yRatio = 0;
            int xRatio = 0;

            option.inJustDecodeBounds = true;
            option.inPurgeable = true;
            option.inPreferredConfig = Bitmap.Config.RGB_565;
            result = BitmapFactory.decodeFile(filePath, option);
            yRatio = (int) Math.ceil(option.outHeight / width);
            xRatio = (int) Math.ceil(option.outWidth / height);
            if (yRatio > 1 || xRatio > 1) {
                option.inSampleSize = (yRatio > xRatio) ? yRatio : xRatio;
            }
            option.inJustDecodeBounds = false;
            result = BitmapFactory.decodeFile(filePath, option);
        }

        return result;
    }
}
