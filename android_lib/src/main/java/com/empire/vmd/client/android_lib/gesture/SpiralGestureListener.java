package com.empire.vmd.client.android_lib.gesture;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.empire.vmd.client.android_lib.util.MathUtil;
import com.empire.vmd.client.android_lib.util.OtherUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2016/1/10.
 */
public class SpiralGestureListener implements GestureDetector.OnGestureListener {
    private static final String TAG = "SPIRAL_GESTURE";
    /*
        將座標的變化劃分成四種狀況1: ++; 2:-+; 3:--; 4:+-
        假設以數學的第一象限為起點，順時鐘跟逆時鐘的座標變化定義如下
     */
    private static final int[] CLOCKWISE_CHAHGE_STATUS_ARRAY = {1, 2, 3, 4};
    private static final int[] COUNTERCLOCKWISE_CHAHGE_STATUS_ARRAY = {3, 2, 1, 4};
    public static final int CLOCKWISE = 1;
    public static final int COUNTERCLOCKWISE = -1;
    public static final int DEFAULT = 0;
    private static final int DEFAULT_CHANGES_PER_CIRCLE = 2;
    private static final float DEFAULT_TOLERABLE_DISTANCE = 500;
    private boolean scrolling;
    private boolean circleComplete;
    private float lastX;
    private float lastY;
    private int direction;
    private int currentChangeStatus;
    //private int changeStatusIndex;
    private List<Integer> changeStatusList;
    private IMission iMission;
    private int changesPerCircle;
    //private float anglesPerChange;
    //private float deltaSlopeSum;
    private float distanceSum;
    private float circleDistanceSum;
    private float distancePerChange;
    private float incorrectDistanceSum;
    //private OtherUtil otherUtil;
    private float tolerableDistance;

    public interface IMission {
        public void clockwiseExecute();
        public void counterclockwiseExecute();
        public void interrupt();
    }

    public SpiralGestureListener(IMission im) {
        lastX = -1;
        lastY = -1;
        iMission = im;
        //otherUtil = new OtherUtil();
        tolerableDistance = DEFAULT_TOLERABLE_DISTANCE;
        changesPerCircle = DEFAULT_CHANGES_PER_CIRCLE;
        //anglesPerChange = 360 / changesPerCircle;
    }

    public void setTolerableDistance(String td) {
        try {
            tolerableDistance = (td.isEmpty()) ? tolerableDistance : Float.parseFloat(td);
        } catch (Exception e) {

        }
    }

    public void setChangesPerCircle(int count) {
        changesPerCircle = count;
        //anglesPerChange = 360 / changesPerCircle;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i(TAG, "onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i(TAG, "onDown");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        int newChangeStatus = 0;

        scrolling = true;
        lastX = (lastX == -1) ? e1.getX() : lastX;
        lastY = (lastY == -1) ? e1.getY() : lastY;
        newChangeStatus = getChangeStatus(lastX, lastY, e2.getX(), e2.getY());
        processDirection(newChangeStatus, distanceX, distanceY);
        if (circleComplete) {
            //deltaSlopeSum += MathUtil.getAngleBySlope(Math.abs(distanceX), Math.abs(distanceY));
            distanceSum += MathUtil.getDistance(distanceX, distanceY);
            if (distanceSum >= distancePerChange) {
            //if (deltaSlopeSum >= anglesPerChange) {
                if (direction == CLOCKWISE) {
                    iMission.clockwiseExecute();
                } else {
                    iMission.counterclockwiseExecute();
                }
                distanceSum = 0;
                //deltaSlopeSum = 0;
            }
        }

        return true;
    }

    private void processDirection(int newChangeStatus, float distanceX, float distanceY) {
        if (newChangeStatus != DEFAULT) { //0的狀況直接跳過不做處理（在Ｘ軸或Ｙ軸上，也就是X沒有變化或是Ｙ沒有變化）
            changeStatusList = (changeStatusList == null) ? new ArrayList<Integer>() : changeStatusList;
            circleDistanceSum += MathUtil.getDistance(distanceX, distanceY); //用來記錄轉一圈的距離
            if (currentChangeStatus == DEFAULT) { //一定是最初第一次進入onScroll
                currentChangeStatus = newChangeStatus;
                changeStatusList.add(currentChangeStatus);
            } else if (currentChangeStatus != newChangeStatus) {
                Log.i(TAG, "changeStatus: " + currentChangeStatus);
                if (direction == DEFAULT) {
                    direction = getDirection(newChangeStatus);
                    Log.i(TAG, "direction: " + direction);
                } else {
                    if (newChangeStatus == getNextChangeStatus(direction, changeStatusList.get(changeStatusList.size() - 1))) {
                        incorrectDistanceSum = 0;
                        currentChangeStatus = newChangeStatus;
                        changeStatusList.add(currentChangeStatus);
                        if (changeStatusList.size() % 4 == 0) {
                            //因為一進入第四種狀態就判定為圓圈，因此實際只畫到3/4圈，因此反向推出一圈的距離
                            distancePerChange = (circleDistanceSum * 4 / 3) / changesPerCircle;
                            circleDistanceSum = 0;
                        }
                        circleComplete = (changeStatusList.size() >= CLOCKWISE_CHAHGE_STATUS_ARRAY.length) ? true : false;
                    } else {
                        circleComplete = false;
                        incorrectDistanceSum += MathUtil.getDistance(distanceX, distanceY);
                        if (incorrectDistanceSum >= tolerableDistance) {
                            Log.i(TAG, "interrupt distance: " + incorrectDistanceSum);
                            initOnScroll();
                            iMission.interrupt();
                        } else {
                            Log.i(TAG, "status: " + newChangeStatus + "  incorrectDistance: " + incorrectDistanceSum);
                        }
                    }
                }
            }
        }
    }

    private void initOnScroll() {
        lastX = -1;
        lastY = -1;
        changeStatusList = null;
        direction = DEFAULT;
        currentChangeStatus = DEFAULT;
        scrolling = false;
        circleComplete = false;
        distanceSum = 0;
        circleDistanceSum = 0;
        //deltaSlopeSum = 0;
        incorrectDistanceSum = 0;
    }

    private int getDirection(int newChangeStatus) {
        int result = DEFAULT;
        
        //只有在出現第二種改變狀態時需要判斷順時鐘或逆時鐘
        if (changeStatusList.size() == 1) {
            if (newChangeStatus == getNextChangeStatus(CLOCKWISE, changeStatusList.get(0))) {
                result = CLOCKWISE;
            } else if (newChangeStatus == getNextChangeStatus(COUNTERCLOCKWISE, changeStatusList.get(0))) {
                result = COUNTERCLOCKWISE;
            }

            if (result != DEFAULT) {
                currentChangeStatus = newChangeStatus;
                changeStatusList.add(currentChangeStatus);
            } else {
                initOnScroll();
            }
        }
        
        return result;
    }

    private int getNextChangeStatus(int type, int changeStatus) {
        int result = DEFAULT;
        int[] changeStatusArr = (type == CLOCKWISE) ? CLOCKWISE_CHAHGE_STATUS_ARRAY : COUNTERCLOCKWISE_CHAHGE_STATUS_ARRAY;
        int currentIndex = getIndexOfChangeStatusArr(changeStatusArr, changeStatus);

        if (currentIndex != -1) {
            int nextIndex = new OtherUtil().getCircleNextIndex(changeStatusArr.length, currentIndex);

            result = changeStatusArr[nextIndex];
        } else {
            Log.e(TAG, "can not catch the index of change status array");
        }

        return result;
    }

    private int getIndexOfChangeStatusArr(int[] changeStatusArr, int changeStatus) {
        int result = -1;

        for (int i = 0; i < changeStatusArr.length; i++) {
            if (changeStatusArr[i] == changeStatus) {
                result = i;
                break;
            }
        }

        return result;
    }

    /**
     *
     * @param lX lastX
     * @param lY lastY
     * @param cX currentX
     * @param cY currentY
     * @return change status
     */
    private int getChangeStatus(float lX, float lY, float cX, float cY) {
        int result = 0;
        float deltaX = cX - lX;
        float deltaY = cY - lY;

        if (deltaX > 0 && deltaY > 0) {
            result = 1;
        } else if (deltaX < 0 && deltaY > 0) {
            result = 2;
        } else if (deltaX < 0 && deltaY < 0) {
            result = 3;
        } else if(deltaX > 0 && deltaY < 0) {
            result = 4;
        }

        return result;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i(TAG, "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        initOnScroll();
        Log.i(TAG, "onFling");

        return true;
    }
}
