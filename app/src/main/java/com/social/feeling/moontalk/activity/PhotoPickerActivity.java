package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.component.PhotoAlbumComponent;
import com.empire.vmd.client.android_lib.util.AndroidBuiltInUtil;
import com.empire.vmd.client.android_lib.util.FileUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.global.FileConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhotoPickerActivity extends BaseActivity {
    public static final String CHECKED_PHOTO_LIST = "checkedPhotoList";
    public static final String CAMERA_URI = "cameraUri";
    public static final String HAS_CAMERA = "hasCamera";
    private static final int DEFAULT_PHOTO_ROW_NUM = 3;
    private static final int START_CAMERA_CODE = 9;
    private LinearLayout llRoot;
    private PhotoAlbumComponent photoAlbumComponent;
    private File cameraFile;
    private boolean hasCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        findViews();
        getExtrasData();
        photoAlbumComponent = new PhotoAlbumComponent(this, DEFAULT_PHOTO_ROW_NUM, hasCamera, getListeners());
        llRoot.addView(photoAlbumComponent.getView());
    }

    private void getExtrasData() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            hasCamera = bundle.getBoolean(HAS_CAMERA);
        } else {
            hasCamera = true;
        }
    }

    private void findViews() {
        llRoot = (LinearLayout) findViewById(R.id.llRoot);
    }

    private PhotoAlbumComponent.IListeners getListeners() {
        return new PhotoAlbumComponent.IListeners() {
            @Override
            public void cancel() {
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void commit(List checkedPhotos) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putStringArrayList(CHECKED_PHOTO_LIST, (ArrayList<String>) checkedPhotos);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void camera() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateTime = sdf.format(new Date());
                Uri cameraUri = null;

                //external file
                cameraFile = new FileUtil(PhotoPickerActivity.this).getExternalFile(FileConfig.EXTERNAL_DIR, currentDateTime + ".png");
                //internal file
                //cameraFile = new File(getFilesDir(), currentDateTime + ".png");
                cameraUri = Uri.fromFile(cameraFile);
                new AndroidBuiltInUtil(PhotoPickerActivity.this).startCamera(START_CAMERA_CODE, cameraUri);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == START_CAMERA_CODE) {
//            Uri uri = (data != null) ? data.getData() : null;
//
//            if (uri != null) {
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//
//                bundle.putString(CAMERA_URI, uri.toString());
//                intent.putExtras(bundle);
//                setResult(RESULT_OK, intent);
//            }
            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            bundle.putString(CAMERA_URI, cameraFile.getAbsolutePath());
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        photoAlbumComponent.cancelAllTasks();
    }
}
