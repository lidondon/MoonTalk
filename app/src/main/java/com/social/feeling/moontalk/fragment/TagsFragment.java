package com.social.feeling.moontalk.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.empire.vmd.client.android_lib.util.ConvertUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.FeelingTag;

import java.util.List;

/**
 * Created by lidondon on 2016/9/5.
 */
public class TagsFragment extends Fragment {
    private static final int TAG_EDGE = 50;
    private static final int TAG_MARGIN_LEFT = 10;
    private boolean grey;
    private Context context;
    private LinearLayout llTags;
    private List<FeelingTag> tagList;
    private int colorResource = -1;

    public TagsFragment() {}

    @SuppressLint("ValidFragment")
    public TagsFragment(Context ctx, List<FeelingTag> tList) {
        context = ctx;
        tagList = tList;
        grey = true;
    }

    @SuppressLint("ValidFragment")
    public TagsFragment(Context ctx, List<FeelingTag> tList, int cr) {
        context = ctx;
        tagList = tList;
        colorResource = cr;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView =  inflater.inflate(R.layout.fragment_tags, container, false);
        findViews(resultView);
        if (colorResource != -1) {
            addTag(colorResource);
        }
        initLlTags();

        return resultView;
    }

    private void findViews(View rootView) {
        llTags = (LinearLayout) rootView.findViewById(R.id.llTags);
    }

    private void initLlTags() {
        if (tagList != null) {
            for (int i = 0; i < tagList.size(); i++) {
                addTag(tagList.get(i));
            }
        }
    }

    private void addTag(FeelingTag tag) {
        addTag(tag.resource);
    }

    private void addTag(int r) {
        ImageView imageView = new ImageView(context);
        int edge = (int) ConvertUtil.convertDp2Pixel(context, TAG_EDGE);
        int marginLeft = (int) ConvertUtil.convertDp2Pixel(context, TAG_MARGIN_LEFT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(edge, edge);

        params.setMargins(marginLeft, 0, 0, 0);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(grey ? getGreyImageResource(r) : r);
        llTags.addView(imageView);
    }

    private int getGreyImageResource(int r) {
        int result = 0;

        switch (r) {
            case R.drawable.eye:
                result = R.drawable.eye_grey;
                break;
            case R.drawable.ear:
                result = R.drawable.ear_grey;
                break;
            case R.drawable.nose:
                result = R.drawable.nose_grey;
                break;
            case R.drawable.mouth:
                result = R.drawable.mouth_grey;
                break;
            case R.drawable.touch:
                result = R.drawable.touch_grey;
                break;
            case R.drawable.feel:
                result = R.drawable.feel_grey;
                break;
        }

        return result;
    }
}
