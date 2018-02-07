package com.empire.vmd.client.android_lib.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.empire.vmd.client.android_lib.R;
import com.empire.vmd.client.android_lib.util.ImageUtil;

import java.util.Set;

/**
 * Created by lidondon on 2016/6/20.
 */
public class ImageViewBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private String uri;
    private ImageView imageView;
    private LruCache<String, Bitmap> bitmapLruCache;
    private Set<ImageViewBitmapAsyncTask> taskCollection;

    public ImageViewBitmapAsyncTask(ImageView iv, LruCache<String, Bitmap> lc, Set<ImageViewBitmapAsyncTask> taskSet) {
        imageView = iv;
        bitmapLruCache = lc;
        taskCollection = taskSet;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;

        uri = params[0];
        bitmap = ImageUtil.getThumbnailBitmap(uri);
        if (bitmap != null && bitmapLruCache != null) {
            bitmapLruCache.put(uri, bitmap);
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

        if (taskCollection != null) {
            taskCollection.remove(this);
        }
    }
}
