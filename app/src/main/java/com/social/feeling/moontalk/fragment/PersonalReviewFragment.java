package com.social.feeling.moontalk.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.social.feeling.moontalk.R;

/**
 * Created by lidondon on 2016/12/8.
 */
public class PersonalReviewFragment {
    private Context context;

    public PersonalReviewFragment(Context ctx) {
        context = ctx;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView() {
        View resultView = getInflater().inflate(R.layout.fragment_personal_review, null);

        findViews(resultView);

        return resultView;
    }

    private void findViews(View rootView) {

    }
}
