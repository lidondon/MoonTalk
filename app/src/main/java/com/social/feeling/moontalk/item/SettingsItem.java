package com.social.feeling.moontalk.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk._interface.IView;

/**
 * Created by lidondon on 2017/1/9.
 */
public class SettingsItem implements IView {
    private Context context;
    private RelativeLayout rlRoot;
    private TextView tvTitle;
    private TextView tvSubtitle;
    private String title;
    private String subtitle;
    private IMission iMission;

    public interface IMission {
        public void executeMission();
    }

    public SettingsItem(Context ctx, String t, IMission im) {
        context = ctx;
        title = t;
        iMission = im;
    }

    public SettingsItem(Context ctx, String t, String st, IMission im) {
        context = ctx;
        title = t;
        subtitle = st;
        iMission = im;
    }

    public void setSubtitle(String st) {
        subtitle = st;
    }

    public View getView() {
        View resultView = LayoutInflater.from(context).inflate(R.layout.item_settings, null);

        findViews(resultView);
        tvTitle.setText(title);
        if (subtitle != null && !subtitle.isEmpty()) {
            tvSubtitle.setText(subtitle);
        }
        rlRoot.setOnClickListener(getRlRootOnClickListener());

        return resultView;
    }

    private void findViews(View rootView) {
        rlRoot = (RelativeLayout) rootView.findViewById(R.id.rlRoot);
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        tvSubtitle = (TextView) rootView.findViewById(R.id.tvSubtitle);
    }

    private View.OnClickListener getRlRootOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iMission != null) {
                    iMission.executeMission();
                }
            }
        };
    }
}
