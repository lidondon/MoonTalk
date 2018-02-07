package com.social.feeling.moontalk.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.util.AndroidBuiltInUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.activity.PersonDataActivity;
import com.social.feeling.moontalk.activity.PortalActivity;
import com.social.feeling.moontalk.activity.SettingsActivity;

/**
 * Created by lidondon on 2017/1/6.
 */
public class LeftDrawer {
    private Context context;
    private LinearLayout llRoot;
    private LinearLayout llPersonal;
    private LinearLayout llQrCode;
    private LinearLayout llSettings;
    private LinearLayout llLogout;
    private DrawerLayout drawerLayout;

    public LeftDrawer(Context ctx, DrawerLayout dl) {
        context = ctx;
        drawerLayout = dl;
    }

    public View getView() {
        View resultView = LayoutInflater.from(context).inflate(R.layout.drawer_left, null);

        findViews(resultView);
        setLlRootHeight();
        llPersonal.setOnClickListener(getLlPersonalOnClickListener());
        llQrCode.setOnClickListener(getLlQrCodeOnClickListener());
        llSettings.setOnClickListener(getLlSettingsOnClickListener());
        llLogout.setOnClickListener(getLlLogoutOnClickListener());

        return resultView;
    }

    private void findViews(View rootView) {
        llRoot = (LinearLayout) rootView.findViewById(R.id.llRoot);
        llPersonal = (LinearLayout) rootView.findViewById(R.id.llPersonal);
        llQrCode = (LinearLayout) rootView.findViewById(R.id.llQrCode);
        llSettings = (LinearLayout) rootView.findViewById(R.id.llSettings);
        llLogout = (LinearLayout) rootView.findViewById(R.id.llLogout);
    }

    private View.OnClickListener getLlPersonalOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PersonDataActivity.class));
                drawerLayout.closeDrawers();
            }
        };
    }

    private View.OnClickListener getLlQrCodeOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Coming soon !", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
            }
        };
    }

    private View.OnClickListener getLlSettingsOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;

                activity.startActivity(new Intent(context, SettingsActivity.class));
                drawerLayout.closeDrawers();
            }
        };
    }

    private View.OnClickListener getLlLogoutOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PortalActivity.class));
                ((Activity) context).finish();
            }
        };
    }

    private void setLlRootHeight() { //useless
        ViewGroup.LayoutParams params = llRoot.getLayoutParams();

        if (params != null) {
            params.height = new AndroidBuiltInUtil(context).getScreenHeight();
            llRoot.setLayoutParams(params);
        }
    }
}
