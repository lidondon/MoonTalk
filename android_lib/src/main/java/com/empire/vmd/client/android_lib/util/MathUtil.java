package com.empire.vmd.client.android_lib.util;

/**
 * Created by lidondon on 2016/1/28.
 */
public class MathUtil {
    public static float getAngleBySlope(float x, float y) {
        return (float) Math.atan2(y, x);
    }

    public static double getDistance(float distanceX, float distanceY) {
        return Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
    }
}
