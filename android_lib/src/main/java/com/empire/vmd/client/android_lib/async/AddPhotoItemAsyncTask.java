package com.empire.vmd.client.android_lib.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.empire.vmd.client.android_lib.extendview.AutoChangeLineLayout;
import com.empire.vmd.client.android_lib.item.PhotoItem;
import com.empire.vmd.client.android_lib.util.ImageUtil;

import java.util.Set;

/**
 * Created by lidondon on 2016/6/24.
 */
public class AddPhotoItemAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private String uri;
    private PhotoItem photoItem;
    private Set<AddPhotoItemAsyncTask> taskCollection;

    public AddPhotoItemAsyncTask(PhotoItem pt,  Set<AddPhotoItemAsyncTask> taskSet) {
        photoItem = pt;
        taskCollection = taskSet;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;

        uri = params[0];
        bitmap = ImageUtil.getThumbnailBitmap(uri);

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            photoItem.getViewHolder().setContent(bitmap);
        }

        if (taskCollection != null) {
            taskCollection.remove(this);
        }
    }
}
