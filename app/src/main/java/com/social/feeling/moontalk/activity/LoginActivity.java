package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.httpproxy.VolleyRequest;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Quote;
import com.social.feeling.moontalk.global.LoginData;
import com.social.feeling.moontalk.http.AccountManager;
import com.social.feeling.moontalk.http.WebConfig;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.HandlerBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    private EditText etAccount;
    private EditText etPassword;
    private Button btnCancel;
    private Button btnCommit;
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        accountManager = AccountManager.getInstance(this);
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
                //測試一下新格式而已
//                String s = "{err: 0, ids: [16,17]}";
//                try {
//                    JSONObject jo = new JSONObject(s);
//                    JSONArray ja = jo.getJSONArray("ids");
//                    String s1 = ja.getString(0);
//                    String s2 = ja.getString(1);
//
//                } catch (Exception e) {
//
//                }
                startActivity(new Intent(LoginActivity.this, PortalActivity.class));
                finish();
            }
        };
    }

    private View.OnClickListener getBtnCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (etAccount.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                if (false) {
                    Toast.makeText(LoginActivity.this, R.string.enter_account_password, Toast.LENGTH_SHORT).show();
                } else {
                    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            try {
                                int response = Integer.parseInt(msg.obj.toString());

                                switch (response) {
                                    case AccountManager.Response.ALREADY_LOGIN:
                                    case AccountManager.Response.SUCCESS:
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                        break;
                                    case AccountManager.Response.FAIL:
                                        Toast.makeText(LoginActivity.this, R.string.loading, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Login response parse int fail");
                            }
                        }
                    };
                    accountManager.login(etAccount.getText().toString(), etPassword.getText().toString(), handler);
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };
    }


}
