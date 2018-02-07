package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.extendview.PowerImageView;
import com.empire.vmd.client.android_lib.util.EffectUtil;
import com.empire.vmd.client.android_lib.util.OtherUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.global.Colors;
import com.social.feeling.moontalk.global.PostFeeling;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerActivity extends BaseActivity {
    //public static final String CHECKED_COLOR_ID = "checkedColor";
    private static final int SWITCH_DURATION = 1000;
    private static final int DEMO_DURATION = 3000;
    private PostFeeling postFeeling = PostFeeling.getInstance();
    private RelativeLayout rlColors;
    private ImageView ivUp;
    private ImageView ivDown;
    private TextView tvCommit;
    private TextView tvCancel;
    private PowerImageView pivDemo;
    private Animation[] animations;
    private int imageIndex;
    private OtherUtil otherUtil;
    private EffectUtil effectUtil;
    private boolean switching;
    private Colors colors;
    private List<String> checkedPhotoList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            effectUtil.activateViewAnimations(ivUp, animations, SWITCH_DURATION);
        }
    };

    private Handler removeDemoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pivDemo.setVisibility(View.GONE);
            ivUp.setImageResource(colors.colorObjectList.get(imageIndex).colorImageResource);
            rlColors.setOnClickListener(getSwitchColorListener());
            tvCommit.setOnClickListener(getTvCommitListener());
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    };

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            try {
                while (switching) {
                    handler.sendEmptyMessage(0);
                    Thread.sleep(SWITCH_DURATION + 50);
                }
            } catch (Exception e) {
                Log.e(getClass().getName(), e.getMessage().toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        postFeeling.addActivity(this);
        findViews();
        initUtils();
        removeDemo();
    }

    private View.OnClickListener getTvCommitListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColorPickerActivity.this, QuotePickerActivity.class);
                postFeeling.feeling.checkedColorId = colors.colorObjectList.get(imageIndex).id;
                startActivity(intent);
            }
        };
    }

//    private void getExtrasData() {
//        Bundle bundle = getIntent().getExtras();
//
//        if (bundle != null) {
//            checkedPhotoList = bundle.getStringArrayList(PreparePostActivity.CHECKED_PHOTO_LIST);
//        }
//    }

    private void removeDemo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(DEMO_DURATION);
                    removeDemoHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    Log.e(ColorPickerActivity.this.getClass().getName(), e.toString());
                }
            }
        }).start();
    }

    private void initUtils() {
        colors = Colors.getInstance();
        otherUtil = new OtherUtil();
        animations = getAnimations();
        effectUtil = new EffectUtil();
    }

    private void findViews() {
        rlColors = (RelativeLayout) findViewById(R.id.rlColors);
        ivUp = (ImageView) findViewById(R.id.ivUp);
        ivDown = (ImageView) findViewById(R.id.ivDown);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCommit = (TextView) findViewById(R.id.tvCommit);
        pivDemo = (PowerImageView) findViewById(R.id.pivDemo);
    }

    private Animation[] getAnimations() {
        Animation[] result = new Animation[2];

        result[0] = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 1f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f);
        result[1] = new AlphaAnimation(1, 0);
        result[1].setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageIndex = otherUtil.getCircleNextIndex(colors.colorObjectList.size(), imageIndex);
                ivDown.setImageResource(colors.colorObjectList.get(imageIndex).colorImageResource);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivUp.setImageResource(colors.colorObjectList.get(imageIndex).colorImageResource);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return result;
    }

    private View.OnClickListener getSwitchColorListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switching) {
                    stopSwitching();
                } else {
                    startSwitching();
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (switching) {
            stopSwitching();
        }
    }

    private void startSwitching() {
        switching = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (switching) {
                        handler.sendEmptyMessage(0);
                        Thread.sleep(SWITCH_DURATION + 50);
                    }
                } catch (Exception e) {
                    Log.e(getClass().getName(), e.getMessage().toString());
                }
            }
        }).start();
    }

    private void stopSwitching() {
        try {
            switching = false;
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postFeeling.feeling.checkedColorId = null;
        postFeeling.removeActivity(this);
    }
}
