package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.social.feeling.moontalk.R;

public class LoginActivity extends BaseActivity {
    private EditText etAccount;
    private EditText etPassword;
    private Button btnCancel;
    private Button btnCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        btnCommit.setOnClickListener(getBtnCommitOnClickListener());
        btnCancel.setOnClickListener(getBtnCancelOnClickListener());
    }

    private void findViews() {
        etAccount = (EditText) findViewById(R.id.etAccount);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCommit = (Button) findViewById(R.id.btnCommit);
    }

    private View.OnClickListener getBtnCancelOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PortalActivity.class));
                finish();
            }
        };
    }

    private View.OnClickListener getBtnCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(etAccount.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty())) {
                    Toast.makeText(LoginActivity.this, R.string.enter_account_password, Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        };
    }
}
