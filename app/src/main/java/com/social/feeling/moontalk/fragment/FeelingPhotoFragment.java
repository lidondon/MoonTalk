package com.social.feeling.moontalk.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.empire.vmd.client.android_lib.util.ImageUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Feeling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lidondon on 2016/8/9.
 */
public class FeelingPhotoFragment {
    private static final int PHOTO_WIDTH = 480;
    private static final int PHOTO_HEIGHT = 480;
    private static final int LAYOUT_COUNT = 4;
    private static final int[] layouts = {R.layout.fragment_feeling_photo_1, R.layout.fragment_feeling_photo_2
        , R.layout.fragment_feeling_photo_3, R.layout.fragment_feeling_photo_4};
    private Context context;
    View resultView;
    private List<ImageView> ivList;
    private List<String> photoList;
    private int layoutIndex;
    private LruCache<String, Bitmap> bitmapLruCache;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            IvAndBp ivAndBp = (IvAndBp) msg.obj;

            ivAndBp.imageView.setImageBitmap(ivAndBp.bitmap);
        }
    };

    public FeelingPhotoFragment(Context ctx, List<String> list) {
        context = ctx;
        photoList = list;
    }

    public FeelingPhotoFragment(Context ctx, List<String> list, LruCache<String, Bitmap> bpLruCache) {
        context = ctx;
        photoList = list;
        bitmapLruCache = bpLruCache;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView() {
        getLayoutIndex();
        resultView = getInflater().inflate(layouts[layoutIndex], null);
        initImageViews();

        return resultView;
    }

    private void getLayoutIndex() {
        if (photoList != null) {
            int maxCount = (photoList.size() > LAYOUT_COUNT) ? LAYOUT_COUNT : photoList.size();

            if (maxCount >= 1) {
                if (maxCount <= 2) {
                    layoutIndex = maxCount - 1;
                } else {
                    layoutIndex = new Random().nextInt(maxCount - 2) + 2; //不是讓他選 0-4是要選3-4
                }
            }
        }
    }

    private void initImageViews() {
        ivList = getImageViewList();
        if (photoList != null && photoList.size() > 0) {
            for (int i = 0; i <= layoutIndex; i++) {
                final int index = i;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        String photoUri = photoList.get(index);
                        Bitmap bp = bitmapLruCache.get(photoUri);

                        if (bp == null) {
                            bp = ImageUtil.getThumbnailBitmap(photoUri, PHOTO_WIDTH, PHOTO_HEIGHT);
                        }
                        if (bp != null) {
                            bitmapLruCache.put(photoUri, bp);
                            message.obj = new IvAndBp(ivList.get(index), bp);
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            }
        }
    }

    private List<ImageView> getImageViewList() {
        List<ImageView> result = new ArrayList<ImageView>();

        result.add((ImageView) resultView.findViewById(R.id.iv1));
        if (layoutIndex > 0) {
            result.add((ImageView) resultView.findViewById(R.id.iv2));
            if (layoutIndex > 1) {
                result.add((ImageView) resultView.findViewById(R.id.iv3));
                if (layoutIndex > 2) {
                    result.add((ImageView) resultView.findViewById(R.id.iv4));
                }
            }
        }

        return result;
    }

    protected class IvAndBp {
        protected ImageView imageView;
        protected Bitmap bitmap;

        protected IvAndBp(ImageView iv, Bitmap bp) {
            imageView = iv;
            bitmap = bp;
        }
    }
}
