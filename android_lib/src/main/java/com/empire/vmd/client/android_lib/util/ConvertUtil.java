package com.empire.vmd.client.android_lib.util;

import android.content.Context;

/**
 * Created by lidondon on 2015/10/5.
 */
public class ConvertUtil {
    private ConvertUtil() {}

    public static float convertDp2Pixel(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static void test() {

    }
}
