package com.empire.vmd.client.android_lib.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.empire.vmd.client.android_lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2016/3/14.
 */
public class RotateSwitchImageFragment extends Fragment {
    private static final String TAG = "RotateSwitchImage";
    private static final int QUEUE_MAX_NUMBER = 999;
    private int upIvResource;
    private boolean switching;
    //private int downIvResource;
    private float duration;
    private ImageView ivUp;
    private ImageView ivDown;
    private List<Integer> newImageSourceQueue;

    private Handler transparentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bm = (Bitmap) msg.obj;

            ivUp.setImageBitmap(bm);
        }
    };

    private Handler upDownSwitchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ivUp.setImageDrawable(ivDown.getDrawable());
            //ivUp.setAlpha(255);
            newImageSourceQueue.remove(0);
            switching = false;
        }
    };

    private Handler setDownHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int imgSrc = (int) msg.obj;

            ivDown.setImageResource(imgSrc);
            //ivUp.setAlpha(200);
        }
    };

    public RotateSwitchImageFragment() {}

    @SuppressLint("ValidFragment")
    public RotateSwitchImageFragment(int upIvSrc, float d) {
        upIvResource = upIvSrc;
        //downIvResource = downIvSrc;
        duration = d;
        newImageSourceQueue = new ArrayList<Integer>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.fragment_rotate_switch_image, container, false);

        getViews(resultView);
        ivUp.setImageResource(upIvResource);
        //ivUp.setImageBitmap(BitmapFactory.decodeResource(getResources(), upIvResource));
        //ivDown.setImageResource(downIvResource);

        return resultView;
    }

    private void getViews(View rootView) {
        ivUp = (ImageView) rootView.findViewById(R.id.ivUp);
        ivDown = (ImageView) rootView.findViewById(R.id.ivDown);
        //網路上說什麼系統開啟硬件加速所以非常容易突然壞掉，加上下面這行(關閉硬件加速)，就真的好了耶
        ivUp.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public boolean updateImageView(final int newIvSrc) {
        boolean result = false;

        //先判斷queue數目是否已滿
        if (newImageSourceQueue.size() < QUEUE_MAX_NUMBER) {
            result = true;
            newImageSourceQueue.add(newIvSrc);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!switching) {
                        switching = true;
                        Log.i(TAG, Thread.currentThread().getName() + " do first");
                        switchImage(newImageSourceQueue.get(0));
                    } else {
                        while (switching) {
                            try {
                                Thread.sleep(500);
                                Log.i(TAG, Thread.currentThread().getName() + " waiting");
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage().toString());
                            }
                        }
                        switching = true;
                        Log.i(TAG, Thread.currentThread().getName() + " jump out while");
                        switchImage(newImageSourceQueue.get(0));
                    }
                }
            }, "thread" + newImageSourceQueue.size()).start();
        }

        return result;
    }

    private void switchImage(int newIvSrc) {
        try {
            Bitmap bm = ((BitmapDrawable) ivUp.getDrawable()).getBitmap();
            Bitmap tmpBm = bm.copy(bm.getConfig(), true);
            int height = bm.getHeight();
            int width = bm.getWidth();
            int widthAndHeight = width + height;
            long sleepTime = (long) duration / height;
            int beginWidth = 0;
            Message downMsg = Message.obtain();

            downMsg.obj = newIvSrc;
            setDownHandler.sendMessage(downMsg);

            for (int i = 0; i < height; i++) {
                Message transparentMsg = Message.obtain();
                for (int j = 0; j < width; j++) {
                    tmpBm.setPixel(j, i, Color.TRANSPARENT);
                }
                transparentMsg.obj = tmpBm;
                transparentHandler.sendMessage(transparentMsg);
                Thread.sleep(sleepTime);
            }

//            for (int i = 0; i < widthAndHeight; i++) {
//                Message transparentMsg = Message.obtain();
//
//                beginWidth = (i >= height) ? i - height + 1 : 0;
//                for (int j = beginWidth; j <= ((i < width - 1) ? i : width - 1); j++) {
//                    for (int k = 0; k < height; k++) {
//                        if (j + k == i) {
//                            tmpBm.setPixel(j, k, Color.TRANSPARENT);
//                            break;
//                        }
//                    }
//                }
//                transparentMsg.obj = tmpBm;
//                transparentHandler.sendMessage(transparentMsg);
//                Thread.sleep(sleepTime);
//            }
            upDownSwitchHandler.sendEmptyMessage(0);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage().toString());
        }
    }
}
