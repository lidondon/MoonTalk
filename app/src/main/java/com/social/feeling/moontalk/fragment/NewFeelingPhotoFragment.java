package com.social.feeling.moontalk.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.util.ConvertUtil;
import com.empire.vmd.client.android_lib.util.ImageUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.activity.ApproachActivity;
import com.social.feeling.moontalk.http.MediaProxy;
import com.social.feeling.moontalk.http.WebConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lidondon on 2016/8/9.
 */
public class NewFeelingPhotoFragment {
    private static final int PHOTO_WIDTH = 480;
    private static final int PHOTO_HEIGHT = 480;
    private static final int PHOTO_MAX_COUNT = 3;
    private static final float PADDING = 10;
    private static final float MARGIN = 6;
    private Context context;
    private View resultView;
    private ImageView ivAdd;
    private LinearLayout llPhotos;
    private List<ImageView> ivList;
    private List<String> photoUrlList;
    //private int layoutIndex;
    private LruCache<String, Bitmap> bitmapLruCache;

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            IvAndBp ivAndBp = (IvAndBp) msg.obj;
//
//            ivAndBp.imageView.setImageBitmap(ivAndBp.bitmap);
//            llPhotos.addView(ivAndBp.imageView);
//        }
//    };
//
//    protected class IvAndBp {
//        protected ImageView imageView;
//        protected Bitmap bitmap;
//
//        protected IvAndBp(ImageView iv, Bitmap bp) {
//            imageView = iv;
//            bitmap = bp;
//        }
//    }

    public NewFeelingPhotoFragment(Context ctx, List<String> pul) {
        context = ctx;
        photoUrlList = pul;
    }

    public NewFeelingPhotoFragment(Context ctx, List<String> list, LruCache<String, Bitmap> bpLruCache) {
        context = ctx;
        photoUrlList = list;
        bitmapLruCache = bpLruCache;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView() {
        //getLayoutIndex();
        resultView = getInflater().inflate(R.layout.fragment_feeling_photo_new, null);
        findViews(resultView);
        initImageViews();

        return resultView;
    }

    private void findViews(View rootView) {
        llPhotos = (LinearLayout) rootView.findViewById(R.id.llPhotos);
        ivAdd = (ImageView) rootView.findViewById(R.id.ivAdd);
    }

//    private void getLayoutIndex() {
//        if (photoUrlList != null) {
//            int maxCount = (photoUrlList.size() > LAYOUT_COUNT) ? LAYOUT_COUNT : photoUrlList.size();
//
//            if (maxCount >= 1) {
//                if (maxCount <= 2) {
//                    layoutIndex = maxCount - 1;
//                } else {
//                    layoutIndex = new Random().nextInt(maxCount - 2) + 2; //不是讓他選 0-4是要選3-4
//                }
//            }
//        }
//    }

    private void initImageViews() {
        ivList = getImageViewList();
        if (ivList.size() > 0) {
            for (int i = 0; i < ivList.size(); i++) {
                String photoUri = WebConfig.SERVER + photoUrlList.get(i);
                Bitmap bp = bitmapLruCache.get(photoUri);
                //Bitmap bp = bitmapLruCache.get("http://hair-kids-nest.s3.amazonaws.com/uploads/pet_photo/image/8/images-9.jpeg");
                ImageView iv = ivList.get(i);
                final int index = i;

                if (bp != null) {
                    iv.setImageBitmap(bp);
                } else {
                    MediaProxy.getProxy(context).loadImage(iv, photoUri, bitmapLruCache);
//                    MediaProxy.getProxy(context).loadImage(iv
//                            , "http://hair-kids-nest.s3.amazonaws.com/uploads/pet_photo/image/8/images-9.jpeg", bitmapLruCache);
                }
                llPhotos.addView(iv);

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Message message = Message.obtain();
//                        String photoUri = photoUrlList.get(index);
//                        Bitmap bp = bitmapLruCache.get(photoUri);
//
//                        if (bp == null) {
//                            // fake, load from phone
//                            //bp = ImageUtil.getThumbnailBitmap(photoUri, PHOTO_WIDTH, PHOTO_HEIGHT);
//                        } else {
//
//                        }
//
//                        if (bp != null) {
//                            bitmapLruCache.put(photoUri, bp);
//                            message.obj = new IvAndBp(ivList.get(index), bp);
//                            handler.sendMessage(message);
//                        }
//                    }
//                }).start();
            }
        }
    }

//    private List<ImageView> getImageViewList() {
//        List<ImageView> result = new ArrayList<ImageView>();
//
//        result.add((ImageView) resultView.findViewById(R.id.iv1));
//        if (layoutIndex > 0) {
//            result.add((ImageView) resultView.findViewById(R.id.iv2));
//            if (layoutIndex > 1) {
//                result.add((ImageView) resultView.findViewById(R.id.iv3));
//                if (layoutIndex > 2) {
//                    result.add((ImageView) resultView.findViewById(R.id.iv4));
//                }
//            }
//        }
//
//        return result;
//    }

    private List<ImageView> getImageViewList() {
        List<ImageView> result = new ArrayList<ImageView>();

        if (photoUrlList != null) {
            int maxCount = photoUrlList.size();;

            if (photoUrlList.size() > PHOTO_MAX_COUNT) {
                maxCount = PHOTO_MAX_COUNT;
                ivAdd.setVisibility(View.VISIBLE);
                ivAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            for (int i = 0; i < maxCount; i++) {
                result.add(getIvPhoto(maxCount - 1, i));
            }
        }

        return result;
    }

    private ImageView getIvPhoto (int maxIndex, int index) {
        ImageView result = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.MATCH_PARENT);
        int margin = (int) ConvertUtil.convertDp2Pixel(context, MARGIN);
        int rightMargin = (index == 0) ? margin : margin / 2;
        int leftMargin = (index == maxIndex) ? margin : margin / 2;

        params.weight = 1;
        params.setMargins(leftMargin, margin, rightMargin, margin);
        //params.setMargins(margin, margin, margin, margin);
        //result.setPadding(leftPadding, padding, padding, padding);
        result.setLayoutParams(params);
        result.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return result;
    }
}
