package com.social.feeling.moontalk.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.empire.vmd.client.android_lib.component.CompleteHookComponent;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.activity.PreparePostActivity;

/**
 * Created by lidondon on 2016/8/25.
 */
public class NewFragment extends Fragment {
    private Context context;
    private LinearLayout llRoot;

    public NewFragment() {}

    @SuppressLint("ValidFragment")
    public NewFragment(Context ctx) {
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView =  inflater.inflate(R.layout.fragment_new, container, false);
        findViews(resultView);

        return resultView;
    }

    private void findViews(View rootView) {
        llRoot = (LinearLayout) rootView.findViewById(R.id.llRoot);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //startActivity(new Intent(context, PreparePostActivity.class));
    }
}
