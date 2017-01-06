package com.empire.vmd.client.android_lib.extendview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by lidondon on 2016/7/5.
 */
public class MultiLineNumberPick extends NumberPicker {

    public MultiLineNumberPick(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }



    private void updateView(View view) {
        TextView textView = (TextView) view;

        //textView.setTextSize(14);
        textView.setSingleLine(false);
        //textView.setMaxLines(2);
    }

}
