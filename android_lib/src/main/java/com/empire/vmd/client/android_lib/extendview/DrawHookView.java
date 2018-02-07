package com.empire.vmd.client.android_lib.extendview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.empire.vmd.client.android_lib.R;

/**
 * Created by lidondon on 2016/6/1.
 */
public class DrawHookView extends View {
    //绘制圆弧的进度值
    private int progress;
    private float line1StartX;
    private float line1StartY;
    private float line2StartX;
    private float line2StartY;
    private float line1toX;
    private float line1toY;
    private float line2toX;
    private float line2toY;
    private float cornerX;
    private int endY;
    private int increaseBase;
    private Paint paint;
    private ICompleteMission iCompleteMission;

    public interface ICompleteMission {
        public void mission();
    }

    public DrawHookView(Context context) {
        super(context);
        initPaint();
    }

    public DrawHookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        //设置画笔颜色
        paint.setColor(getResources().getColor(R.color.blue_green));
        //设置圆弧的宽度
        paint.setStrokeWidth(15);
        //设置圆弧为空心
        paint.setStyle(Paint.Style.STROKE);
        //消除锯齿
        paint.setAntiAlias(true);
    }

    public void setPaint(Paint p) {
        paint = p;
    }

    public void setiCompleteMission (ICompleteMission icm) {
        iCompleteMission = icm;
    }

    public DrawHookView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取圆心的x坐标
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int strokeWidth = (int) paint.getStrokeWidth();
        //圆弧半径
        int radius = ((centerX < centerY) ? centerX : centerY) - strokeWidth * 2;
        //定义的圆弧的形状和大小的界限
        RectF rectF = new RectF(centerX - radius - strokeWidth, centerY - radius - strokeWidth
                , centerX + radius + strokeWidth, centerY + radius + strokeWidth);
        float arcStart = 235;
        boolean complete = false;

        progress++;
        //根据进度画圆弧
        canvas.drawArc(rectF, arcStart, -360 * progress / 100, false, paint);
        if (progress >= 100) { //先等圆弧画完，才话对勾
            float downTime = (float) 1.2; //倍數
            float upTime = (float) 1.7; //倍數

            if (line1StartX == 0) { //整個method可以想像成一個迴圈，所以這邊是為了做第一次初始
                line1StartX = radius / 2;
                line1StartY = centerY - 10;
                increaseBase = 5;
                line1toX = line1StartX + increaseBase;
                line1toY = line1StartY + increaseBase * downTime;
                cornerX = centerX - 10;
                endY = centerY - radius / 2;
            }
            canvas.drawLine(line1StartX, line1StartY, line1toX, line1toY, paint);
            if (line1toX <= cornerX) { //50是向左偏移量
                line1toX += increaseBase;
                line1toY += increaseBase * downTime;
            } else {
                //減去三分之一的線寬是因為線太粗的話兩條線交接處醜醜的
                line2StartX = cornerX - strokeWidth / 4;
                line2StartY = line1toY - strokeWidth / 4;
                if (line2toX == 0) {
                    line2toX = line2StartX + increaseBase;
                    line2toY = line2StartY + increaseBase * upTime;
                }
                canvas.drawLine(line2StartX, line2StartY, line2toX, line2toY, paint);
                line2toX += increaseBase;
                line2toY -= increaseBase * upTime;
            }

            if(line2toY != 0 && line2toY <= endY) {
                complete = true;
                if (iCompleteMission != null) {
                    iCompleteMission.mission();
                }
            }
        }

        if (!complete) {
            //每隔10毫秒界面刷新
            postInvalidateDelayed(1);
        }
    }
}
