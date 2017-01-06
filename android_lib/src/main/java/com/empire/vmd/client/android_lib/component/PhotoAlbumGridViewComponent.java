package com.empire.vmd.client.android_lib.component;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;

import com.empire.vmd.client.android_lib.R;
import com.empire.vmd.client.android_lib.adapter.BaseTypeAdapter;
import com.empire.vmd.client.android_lib.async.ImageViewBitmapAsyncTask;
import com.empire.vmd.client.android_lib.item.PhotoItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lidondon on 2016/6/7.
 */
public class PhotoAlbumGridViewComponent implements AbsListView.OnScrollListener {
    private Context context;
    private ImageView ivLoading;
    private GridView gvPhoto;
    private boolean isFirstEnter = true;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;
    private List<String> photoUriList;
    private LruCache<String, Bitmap> bitmapLruCache;
    private Set<ImageViewBitmapAsyncTask> taskCollection;

    public PhotoAlbumGridViewComponent(Context ctx) {
        context = ctx;
        initBitmapLruCache();
        taskCollection = new HashSet<ImageViewBitmapAsyncTask>();
    }

    private void initBitmapLruCache() {
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        bitmapLruCache = new LruCache<String, Bitmap>(cacheSize);
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView() {
        View resultView = getInflater().inflate(R.layout.component_photo_album_grid_view, null);

        findViews(resultView);
        initGvPhoto();
        ivLoading.setVisibility(View.GONE);
        gvPhoto.setOnScrollListener(this);

        return resultView;
    }

    private void initGvPhoto() {
        photoUriList = getAllImageUri();
        if (photoUriList != null && photoUriList.size() > 0) {
            BaseTypeAdapter.IItemView iItemView = new BaseTypeAdapter.IItemView() {
                @Override
                public View getItemView(int position, View convertView) {
                    if (convertView == null) {
                        PhotoItem photoItem = new PhotoItem(context, null, false, new ArrayList());

                        photoItem.setWidth(gvPhoto.getWidth(), 3); //一列放三張圖
                        convertView = photoItem.getView();
                    }
                    convertView.setTag(photoUriList.get(position));

                    return convertView;
                }
            };
            gvPhoto.setAdapter(new BaseTypeAdapter(iItemView, photoUriList));

        }
    }

    private List<String> getAllImageUri () {
        List<String> result = new ArrayList<String>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Images.Media.DATE_MODIFIED);

        if (cursor.moveToFirst()) {
            do {
                int index = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                String uri = cursor.getString(index);

                if (uri.indexOf("/Camera/") > 0 && !result.contains(uri)) {
                    result.add(cursor.getString(index));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return result;
    }

    private void findViews(View rootView) {
        gvPhoto = (GridView) rootView.findViewById(R.id.gvPhoto);
        ivLoading = (ImageView) rootView.findViewById(R.id.ivLoading);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
        if (scrollState == SCROLL_STATE_IDLE) {
            loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
        } else {
            cancelAllTasks();
        }
    }

    public void cancelAllTasks() {
        if (taskCollection != null) {
            for (ImageViewBitmapAsyncTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        // 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，
        // 因此在这里为首次进入程序开启下载任务。
        if (isFirstEnter && visibleItemCount > 0) {
            loadBitmaps(firstVisibleItem, visibleItemCount);
            isFirstEnter = false;
        }
    }

    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        try {
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount + 9; i++) {
                String uri = photoUriList.get(i);
                Bitmap bitmap = bitmapLruCache.get(uri);
                View photoItemView = gvPhoto.findViewWithTag(uri);
                ImageView ivPhoto = (ImageView) photoItemView.findViewById(R.id.ivPhoto);

                if (bitmap == null) {
                    ImageViewBitmapAsyncTask task = new ImageViewBitmapAsyncTask(ivPhoto, bitmapLruCache, taskCollection);;

                    taskCollection.add(task);
                    task.execute(uri);
                } else {
                    if (photoItemView != null) {
                        ivPhoto.setImageBitmap(bitmap);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
        }
    }
}
