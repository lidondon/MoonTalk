package com.empire.vmd.client.android_lib.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;

/**
 * Created by lidondon on 2016/6/7.
 */
public class ImageUtil {
    private static final int DEFAULT_THUMBNAIL_WIDTH = 128;
    private static final int DEFAULT_THUMBNAIL_HEIGHT = 128;
    private static final int DEFAULT_CORNER_RADIUS = 100;

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
        return getThumbnailBitmap(new File(filePath), DEFAULT_THUMBNAIL_WIDTH, DEFAULT_THUMBNAIL_HEIGHT);
    }

    public static Bitmap getThumbnailBitmap(URI uri) {
        return getThumbnailBitmap(new File(uri), DEFAULT_THUMBNAIL_WIDTH, DEFAULT_THUMBNAIL_HEIGHT);
    }

    public static Bitmap getThumbnailBitmap(String filePath, int width, int height) {
        return getThumbnailBitmap(new File(filePath), DEFAULT_THUMBNAIL_WIDTH, DEFAULT_THUMBNAIL_HEIGHT);
    }

    public static Bitmap getThumbnailBitmap(File file, int width, int height) {
        Bitmap result = null;
        //File file = new File(filePath);
        String filePath = file.getPath();

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

//    public static void setImageViewCircular(Resources res, ImageView iv, int drawableResource) {
//        Bitmap bitmap = BitmapFactory.decodeResource(res, drawableResource);
//
//        if (bitmap != null) {
//            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, bitmap);
//
//            roundedBitmapDrawable.setCircular(true);
//            iv.setImageDrawable(roundedBitmapDrawable);
//        }
//    }

//    public static void setImageViewCircularCorner(Resources res, final ImageView iv, int drawableResource) {
//        final Bitmap bitmap = BitmapFactory.decodeResource(res, drawableResource);
//
//        if (bitmap != null) {
//            final RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(res, bitmap);
//            ViewTreeObserver vto = iv.getViewTreeObserver();
//
//            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    Log.e("BBB", "bitmap byte: " + bitmap.getByteCount());
//                    iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    roundedBitmapDrawable.setCornerRadius(iv.getWidth() * 2);
//                    roundedBitmapDrawable.setAntiAlias(true);
//                    iv.setImageDrawable(roundedBitmapDrawable);
//                }
//            });
//        }
//    }

    public static void setImageViewRoundedCorner(Resources res, ImageView iv, int drawableResource, int radius) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, drawableResource);

        if (bitmap != null) {
            bitmap = getRoundedCornerBitmap(bitmap, radius);
            iv.setImageBitmap(bitmap);
        }
    }

    public static void setImageViewRoundedCorner(Resources res, ImageView iv, int drawableResource) {
        setImageViewRoundedCorner(res, iv, drawableResource, DEFAULT_CORNER_RADIUS);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int radius) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = radius;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Log.e("BBB", "bitmap byte: " + bitmap.getByteCount());

        return result;
    }

    public static LruCache<String, Bitmap> getBitmapLruCache() {
        // 獲取應用程式最大可用內存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        return new LruCache<String, Bitmap>(cacheSize);
    }

    public static byte[] getBitmapBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

        return stream.toByteArray();
    }

//    private static int getAppropriateCornerRadius(ImageView iv) {
//        int result = 0;
//
//        return result;
//    }
}

