package com.social.feeling.moontalk.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.activity.FriendsManagementActivity;

/**
 * Created by lidondon on 2017/10/24.
 */

public class MoreFragment extends Fragment {
    private ImageView ivFriends;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View resultView =  inflater.inflate(R.layout.fragment_more, container, false);

        findViews(resultView);
        context = getContext();
        ivFriends.setOnClickListener(getIvFriendsOnClickListener());

        return resultView;
    }

    private void findViews(View rootView) {
        ivFriends = (ImageView) rootView.findViewById(R.id.ivFriends);
    }

    private View.OnClickListener getIvFriendsOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FriendsManagementActivity.class));
            }
        };
    }
}
