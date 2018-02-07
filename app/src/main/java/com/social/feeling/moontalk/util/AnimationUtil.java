package com.social.feeling.moontalk.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by lidondon on 2017/3/17.
 */
public class AnimationUtil {
    public static Animation[] getUpAppearAnimations() {
        Animation[] result = new Animation[2];

        result[0] = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 1f
                , Animation.RELATIVE_TO_SELF, 0f);
        result[1] = new AlphaAnimation(0, 1);

        return result;
    }

    public static Animation[] getDownDisappearAnimations() {
        Animation[] result = new Animation[2];

        result[0] = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 1f);
        result[1] = new AlphaAnimation(1, 0);

        return result;
    }
}
