package com.empire.vmd.client.android_lib.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

/**
 * Created by lidondon on 2015/9/8.
 */
public class TabUtil {
    private Context context;

    public TabUtil (Context ctx) {
        context = ctx;
    }

    public void setTabContent(RadioGroup radioGroup, String[] textArr, int layoutResource) {
        int width = getRadioButtonWidth(textArr.length);

        for (int i = 0; i < textArr.length; i++) {
            LayoutInflater inflater = (LayoutInflater) radioGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RadioButton radioButton = (RadioButton) inflater.inflate(layoutResource, null);

            radioButton.setId(i);
            radioButton.setChecked(i == 0);
            radioButton.setText(textArr[i]);
            setRadioButtonWidthAndHeight(radioButton, width);
            radioGroup.addView(radioButton);
        }
    }

    public void setTabContent(RadioGroup radioGroup, int[] resources, int layoutResource) {
        int width = getRadioButtonWidth(resources.length);

        for (int i = 0; i < resources.length; i++) {
            LayoutInflater inflater = (LayoutInflater) radioGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RadioButton radioButton = (RadioButton) inflater.inflate(layoutResource, null);
            Drawable drawable = context.getResources().getDrawable(resources[i]);

            radioButton.setId(i);
            radioButton.setChecked(i == 0);
            radioButton.setBackgroundResource(resources[i]);
            setRadioButtonWidthAndHeight(radioButton, width);
            radioGroup.addView(radioButton);
        }
    }

    private int getRadioButtonWidth(int count) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        return screenWidth / count;
    }

    private void setRadioButtonWidthAndHeight(RadioButton radioButton, int width) {
        LayoutParams params = new LayoutParams(width, LayoutParams.MATCH_PARENT);
        radioButton.setLayoutParams(params);
    }

    public void bindTabAndPager(final RadioGroup radioGroup, final ViewPager viewPager) {
        if (radioGroup.getChildCount() == viewPager.getAdapter().getCount()) {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    viewPager.setCurrentItem(checkedId, false);
                }
            });

            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    radioGroup.check(radioGroup.getChildAt(position).getId());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            Log.e(this.getClass().getName(), "radioGroup's number and viewPage's are not the same !");
        }
    }

    public void bindImageViewNavigatorAndPager(final ViewPager viewPager,final ImageView imageView, final int[] ivResourceArr) {
        if (viewPager.getAdapter().getCount() == ivResourceArr.length) {
            imageView.setImageResource(ivResourceArr[0]); //初始化
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    imageView.setImageResource(ivResourceArr[position]);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            Log.e(this.getClass().getName(), "ivResourceArr's number and viewPage's are not the same !\"");
        }
    }
}
