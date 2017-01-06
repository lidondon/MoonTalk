package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.social.feeling.moontalk.R;

public class PortalActivity extends BaseActivity {
    private Button btnRegister;
    private Button btnLogin;
    private Button btnFacebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_protal);
        findViews();
        btnRegister.setOnClickListener(getBtnRegisterOnClickListener());
        btnLogin.setOnClickListener(getBtnLoginOnClickListener());
        btnFacebookLogin.setOnClickListener(getBtnFacebookLoginOnClickListener());

    }

    private void findViews() {
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnFacebookLogin = (Button) findViewById(R.id.btnFacebookLogin);
    }

    private View.OnClickListener getBtnRegisterOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    private View.OnClickListener getBtnLoginOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PortalActivity.this, LoginActivity.class));
                finish();
            }
        };
    }

    private View.OnClickListener getBtnFacebookLoginOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }
}
