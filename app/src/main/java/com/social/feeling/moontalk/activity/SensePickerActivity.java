package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.util.EffectUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Sense;
import com.social.feeling.moontalk.global.PostFeeling;
import com.social.feeling.moontalk.util.AnimationUtil;

import org.w3c.dom.Text;

public class SensePickerActivity extends Activity {
    private static final int ALERT_DURATION = 800;
    private static final boolean OFF = false;
    private static final boolean ON = true;
    private ListView lvSentence;
    private RelativeLayout rlConfirm;
    private TextView tvConfirm;
    private Button btnCommit;
    private Button btnCancel;
    private LinearLayout llSelected;
    private LinearLayout llEye;
    private LinearLayout llEar;
    private LinearLayout llMouth;
    private LinearLayout llNose;
    private LinearLayout llTouch;
    private LinearLayout llFeel;
    private PostFeeling postFeeling = PostFeeling.getInstance();
    private EffectUtil effectUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sense_picker);
        findViews();
        postFeeling.addActivity(this);
        effectUtil = new EffectUtil();
        llEye.setOnClickListener(getSenseItemOnClickListener());
        llEar.setOnClickListener(getSenseItemOnClickListener());
        llMouth.setOnClickListener(getSenseItemOnClickListener());
        llNose.setOnClickListener(getSenseItemOnClickListener());
        llTouch.setOnClickListener(getSenseItemOnClickListener());
        llFeel.setOnClickListener(getSenseItemOnClickListener());
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRlConfirm(OFF);
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRlConfirm(OFF);
                startActivity(new Intent(SensePickerActivity.this, ColorPicker2Activity.class));
            }
        });
    }

    private void findViews() {
        rlConfirm = (RelativeLayout) findViewById(R.id.rlConfirm);
        tvConfirm = (TextView) findViewById(R.id.tvSenseConfirm);
        btnCommit = (Button) findViewById(R.id.btnCommit);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        llEye = (LinearLayout) findViewById(R.id.llEye);
        llEar = (LinearLayout) findViewById(R.id.llEar);
        llMouth = (LinearLayout) findViewById(R.id.llMouth);
        llNose = (LinearLayout) findViewById(R.id.llNose);
        llTouch = (LinearLayout) findViewById(R.id.llTouch);
        llFeel = (LinearLayout) findViewById(R.id.llFeel);
    }

    private View.OnClickListener getSenseItemOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llSelected != v) {
                    if(llSelected != null) {
                        llSelected.setBackgroundColor(getResources().getColor(R.color.transparent));
                    }
                    llSelected = (LinearLayout) v;
                }
                v.setBackgroundColor(getResources().getColor(R.color.pink));
                tvConfirm.setText(getConfirmString());
                switchRlConfirm(ON);
            }
        };
    }

    private String getConfirmString() {
        String result = null;

        if (llSelected != null) {
            if (llSelected == llEye) {
                postFeeling.feeling.sense = new Sense(Sense.EYE);
            } else if (llSelected == llEar) {
                postFeeling.feeling.sense = new Sense(Sense.EAR);
            } else if (llSelected == llMouth) {
                postFeeling.feeling.sense = new Sense(Sense.MOUTH);
            } else if (llSelected == llNose) {
                postFeeling.feeling.sense = new Sense(Sense.NOSE);
            } else if (llSelected == llTouch) {
                postFeeling.feeling.sense = new Sense(Sense.TOUCH);
            } else {
                postFeeling.feeling.sense = new Sense(Sense.FEEL);
            }
            result = String.format(getResources().getString(R.string.sense_confirm), postFeeling.feeling.sense.text);
        }

        return result;
    }

    private void switchRlConfirm(boolean onOrOff) {
        if (onOrOff == ON) {
            if (rlConfirm.getVisibility() == View.GONE) {
                rlConfirm.setVisibility(View.VISIBLE);
                effectUtil.activateViewAnimations(rlConfirm, AnimationUtil.getUpAppearAnimations(), ALERT_DURATION);
            }
        } else {
            rlConfirm.setVisibility(View.GONE);
            effectUtil.activateViewAnimations(rlConfirm, AnimationUtil.getDownDisappearAnimations(), ALERT_DURATION);
        }
    }

}
