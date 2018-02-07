package com.empire.vmd.client.android_lib.util;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

/**
 * Created by lidondon on 2016/3/6.
 */
public class EffectUtil {

    public void activateViewAnimations(View view, Animation[] animations, long duration) {
        AnimationSet animationSet = new AnimationSet(true);

        for (Animation animation : animations) {
            animation.setDuration(duration);
            animationSet.addAnimation(animation);
        }
        view.startAnimation(animationSet);
    }

//    public  void effectChange(final ImageView iv, final int ivResource) {
//        Animation animation = new AlphaAnimation(1, 0);
//
//        animation.setInterpolator(new AccelerateInterpolator());
//        animation.setDuration(duration);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            public void onAnimationEnd(Animation animation) {
//                fadeIn(iv, ivResource);
//            }
//
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//
//            public void onAnimationStart(Animation animation) {
//
//            }
//        });
//        iv.startAnimation(animation);
//    }
//
//    public void fadeIn(final ImageView iv, final int ivResource) {
//        Animation animation = new AlphaAnimation(0, 1);
//        animation.setInterpolator(new AccelerateInterpolator());
//        animation.setDuration(duration);
//
//        animation.setAnimationListener(new Animation.AnimationListener()
//        {
//            public void onAnimationEnd(Animation animation)
//            {
//
//            }
//
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//
//            public void onAnimationStart(Animation animation) {
//                iv.setImageResource(ivResource);
//            }
//        });
//        iv.startAnimation(animation);
//    }
}
