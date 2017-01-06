package com.empire.vmd.client.android_lib.component;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.R;
import com.empire.vmd.client.android_lib.async.AddPhotoItemAsyncTask;
import com.empire.vmd.client.android_lib.async.ImageViewBitmapAsyncTask;
import com.empire.vmd.client.android_lib.extendview.AutoChangeLineLayout;
import com.empire.vmd.client.android_lib.extendview.PowerImageView;
import com.empire.vmd.client.android_lib.item.PhotoItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lidondon on 2016/6/20.
 */
public class PhotoAlbumComponent {
    private Context context;
    private AutoChangeLineLayout autoChangeLineLayout;
    private List<String> checkedPhotoList;
    private int photoRowNum;
    private int photoWidth;
    private List<String> checkedList;
    private RelativeLayout rlButtons;
    private ImageView ivLoading;
    private TextView tvCommit;
    private TextView tvCancel;
    private IListeners iListeners;
    private Set<AddPhotoItemAsyncTask> taskCollection;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String uri = msg.obj.toString();
            PhotoItem photoItem = new PhotoItem(context, uri, false, checkedList);
            AddPhotoItemAsyncTask task = new AddPhotoItemAsyncTask(photoItem, taskCollection);

            taskCollection.add(task);
            ivLoading.setVisibility(View.GONE);
            rlButtons.setVisibility(View.VISIBLE);
            photoItem.setWidth(autoChangeLineLayout.getWidth(), photoRowNum);
            task.execute(uri);
            if (autoChangeLineLayout.getChildCount() == 0) {
                PhotoItem ivCamera = new PhotoItem(context, R.drawable.camera);
                View photoView = null;

                ivCamera.setWidth(autoChangeLineLayout.getWidth(), photoRowNum);
                photoView = ivCamera.getView();
                autoChangeLineLayout.addView(photoView);
                photoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iListeners.camera();
                    }
                });
            }
            autoChangeLineLayout.addView(photoItem.getView());
        }
    };

    public PhotoAlbumComponent(Context ctx, int rowNum, IListeners il) {
        context = ctx;
        photoRowNum = rowNum;
        checkedList = new ArrayList<String>();
        iListeners = il;
        taskCollection = new HashSet<AddPhotoItemAsyncTask>();
    }

    public void setPhotoRowNum(int num) {
        photoRowNum = num;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView() {
        View resultView = getInflater().inflate(R.layout.component_photo_album, null);

        findViews(resultView);
        checkedPhotoList = getAllImageUri();
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iListeners.commit(checkedList);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iListeners.cancel();
            }
        });

        return resultView;
    }

    private void findViews(View rootView) {
        rlButtons = (RelativeLayout) rootView.findViewById(R.id.rlButtons);
        autoChangeLineLayout = (AutoChangeLineLayout) rootView.findViewById(R.id.autoChangeLineLayout);
        ivLoading = (ImageView) rootView.findViewById(R.id.ivLoading);
        tvCommit = (TextView) rootView.findViewById(R.id.tvCommit);
        tvCancel = (TextView) rootView.findViewById(R.id.tvCancel);
    }

    public void cancelAllTasks() {
        for (AddPhotoItemAsyncTask task : taskCollection) {
            if (task != null) {
                task.cancel(false);
            }
        }
    }

    private List<String> getAllImageUri () {
        final List<String> result = new ArrayList<String>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        , null, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

                if (cursor.moveToFirst()) {
                    do {
                        int index = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                        String uri = cursor.getString(index);

                        //if (uri.indexOf("/Camera/") > 0 && !result.contains(uri)) {
                            Message msg = Message.obtain();

                            msg.obj = uri;
                            handler.sendMessage(msg);
                            result.add(uri);
                        //}
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            }
        }).start();

        return result;
    }

    public interface IListeners {
        public void cancel();
        public void commit(List<String> checkedPhotos);
        public void camera();
    }
}
