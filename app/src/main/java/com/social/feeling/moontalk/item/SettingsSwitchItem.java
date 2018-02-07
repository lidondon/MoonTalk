package com.social.feeling.moontalk.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk._interface.IView;

/**
 * Created by lidondon on 2017/1/9.
 */
public class SettingsSwitchItem implements IView {
    private Context context;
    private RelativeLayout rlRoot;
    private TextView tvTitle;
    private Switch s;
    private String title;
    private ISwitch iSwitch;

    public interface ISwitch {
        public void on();
        public void off();
    }

    public SettingsSwitchItem(Context ctx, String t, ISwitch is) {
        context = ctx;
        title = t;
        iSwitch = is;
    }

    public View getView() {
        View resultView = LayoutInflater.from(context).inflate(R.layout.item_settings_switch, null);

        findViews(resultView);
        tvTitle.setText(title);
        s.setOnCheckedChangeListener(getSOnCheckedChangeListener());

        return resultView;
    }

    private void findViews(View rootView) {
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        s = (Switch) rootView.findViewById(R.id.s);
    }

    private CompoundButton.OnCheckedChangeListener getSOnCheckedChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (iSwitch != null) {
                    if (isChecked) {
                        iSwitch.on();
                    } else {
                        iSwitch.off();
                    }
                }
            }
        };
    }
}
