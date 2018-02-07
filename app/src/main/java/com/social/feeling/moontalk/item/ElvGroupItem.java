package com.social.feeling.moontalk.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.social.feeling.moontalk.R;

/**
 * Created by lidondon on 2015/11/25.
 */
public class ElvGroupItem {
    private Context context;
    private String groupName;
    private TextView tvGroupName;

    public ElvGroupItem(Context ctx, String gName) {
        context = ctx;
        groupName = gName;
    }

    public View getView() {
        View resultView = getInflater().inflate(R.layout.item_elv_group, null);

        getViews(resultView);
        tvGroupName.setText(groupName);

        return resultView;
    }

    private void getViews(View rootView) {
        tvGroupName = (TextView) rootView.findViewById(R.id.tvGroupName);
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }
}
