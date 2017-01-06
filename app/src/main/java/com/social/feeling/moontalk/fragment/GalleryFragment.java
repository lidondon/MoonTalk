package com.social.feeling.moontalk.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.empire.vmd.client.android_lib.util.AndroidBuiltInUtil;
import com.empire.vmd.client.android_lib.util.ImageUtil;
import com.social.feeling.moontalk.R;

import java.util.List;

/**
 * Created by lidondon on 2016/8/1.
 */
public class GalleryFragment extends Fragment {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final int DEFAULT_PADDING = 10;
    private static final int PHOTO_WIDTH = 480;
    private static final int PHOTO_HEIGHT = 480;
    private Context context;
    private int padding;
    private List<String> imageUriList;
    private LinearLayout llPhotos;
    private int orientation;
    private HorizontalScrollView hsv;
    private int photoWidth;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ImageView iv = (ImageView) msg.obj;

            llPhotos.addView(iv);
        }
    };

    public GalleryFragment() {}

    @SuppressLint("ValidFragment")
    public GalleryFragment(Context ctx, List<String> iList, int o) {
        context = ctx;
        imageUriList = iList;
        padding = DEFAULT_PADDING;
        orientation = o;
    }

    @SuppressLint("ValidFragment")
    public GalleryFragment(Context ctx, List<String> iList, int o, int pad) {
        context = ctx;
        imageUriList = iList;
        padding = pad;
        orientation = o;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout = (orientation == HORIZONTAL) ? R.layout.fragment_gallery_horizontal : R.layout.fragment_gallery_vertical;
        View resultView = inflater.inflate(layout, container, false);

        findViews(resultView);
        initPhotos();

        return resultView;
    }

    private void findViews(View rootView) {
        llPhotos = (LinearLayout) rootView.findViewById(R.id.llPhotos);
        hsv = (HorizontalScrollView) rootView.findViewById(R.id.hsv);
    }

    private void initPhotos() {
        if (imageUriList != null) {
            for (int i = 0; i < imageUriList.size(); i++) {
                addCheckedPhotos(imageUriList.get(i));
            }
        }
    }

    private void addCheckedPhotos(String uri) {
        Message msg = Message.obtain();
        ImageView iv = getPhotoImageView(uri);

        msg.obj = iv;
        handler.sendMessage(msg);
    }

    private ImageView getPhotoImageView(String uri) {
        ImageView result = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        int vPadding = (orientation == VERTICAL) ? 5 : 10;

        result.setPadding(10, vPadding, 10, vPadding);
        photoWidth = (photoWidth == 0) ? ((Activity) context).getWindowManager().getDefaultDisplay().getWidth() : photoWidth;
        params.width = photoWidth;
        result.setImageBitmap(ImageUtil.getThumbnailBitmap(uri, PHOTO_WIDTH, PHOTO_HEIGHT));

        result.setLayoutParams(params);
        result.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return result;
    }
}
