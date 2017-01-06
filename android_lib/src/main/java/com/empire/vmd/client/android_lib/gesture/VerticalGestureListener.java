package com.empire.vmd.client.android_lib.gesture;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by lidondon on 2016/11/15.
 */
    public class VerticalGestureListener  implements GestureDetector.OnGestureListener {
    private static final int DISTANCE_OF_EXECUTION = 60;
    private float deltaX;
    private float deltaY;
    private IHorizontalMission iHorizontalMission;

    public interface IHorizontalMission {
        public void executeLeftMission();

        public void executeRightMission();
    }

    public VerticalGestureListener(IHorizontalMission ihm) {
        iHorizontalMission = ihm;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        deltaX = 0;
        deltaY = 0;

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        deltaX += distanceX;
        if (deltaX >= DISTANCE_OF_EXECUTION) {
            iHorizontalMission.executeLeftMission();
        } else if (Math.abs(deltaX) >= DISTANCE_OF_EXECUTION) {
            iHorizontalMission.executeRightMission();
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        return true;
    }
}
