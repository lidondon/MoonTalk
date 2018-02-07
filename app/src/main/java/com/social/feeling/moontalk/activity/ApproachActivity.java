package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.util.EffectUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.global.LoginData;
import com.social.feeling.moontalk.http.MediaProxy;

public class ApproachActivity extends BaseActivity {
    private static final int DURATION = 2000;
    private ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approach);
        findViews();
        //getImageFromServer();
        new EffectUtil().activateViewAnimations(ivLogo, getAnimations(), DURATION);
    }

    private void getImageFromServer() {
        MediaProxy.getProxy(ApproachActivity.this).loadImage(ivLogo
                , "https://hair-kids-nest.s3.amazonaws.com/uploads/pet_photo/image/1/thumb_IMG_1043.JPG", null);
    }

    private void findViews() {
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
    }

    private Animation[] getAnimations() {
        Animation[] result = new Animation[2];

        result[0] = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 1.5f
                , Animation.RELATIVE_TO_SELF, 0f);
        result[1] = new AlphaAnimation(0, 1);
        result[1].setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    Thread.sleep(500);
                    redirectTo();
                } catch (Exception e) {
                    Log.e(getClass().getName(), e.toString());
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return result;
    }

    private void redirectTo() {
        if (LoginData.getInstance(this).personData != null) {
            startActivity(new Intent(this, PortalActivity.class));
        } else {
            startActivity(new Intent(this, PortalActivity.class));
        }
        finish();
    }
}
