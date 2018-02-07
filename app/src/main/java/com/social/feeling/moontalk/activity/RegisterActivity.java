package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.httpproxy.HttpRequest;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.global.LoginData;
import com.social.feeling.moontalk.http.AccountManager;
import com.social.feeling.moontalk.http.IAccountEventListener;
import com.social.feeling.moontalk.http.ServerResponse;

import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends Activity {
    private EditText etAccount;
    private EditText etPassword;
    private EditText etConfirm;
    private EditText etNickname;
    private EditText etEmail;
    private TextView tvCommit;
    private TextView tvCancel;
    private TextView tvError;
    private AccountManager accountManager;

    private IAccountEventListener iAccountEventListener = new IAccountEventListener() {
        @Override
        public void onLoginResponse(HashMap<String, String> map) {
            try {
                JSONObject jsonObject = new JSONObject(map.get(HttpRequest.RESPONSE));

                if (jsonObject != null) {
                    if (ServerResponse.RESULT_OK == jsonObject.getInt(ServerResponse.ERROR)) {
                        accountManager.getAccountInfo(jsonObject.getString(HttpRequest.SESSION), etAccount.getText().toString());
                    } else {

                    }
                }
            } catch (Exception e) {

            }
        }

        @Override
        public void onSessionResponse(String json) {

        }

        @Override
        public void onGetAccountInfoResponse(HashMap<String, String> map) {
            Toast.makeText(RegisterActivity.this, "Getting account data", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLogoutResponse(String json) {

        }

        @Override
        public void onCheckAccountResponse(String json) {

        }

        @Override
        public void onRegisterResponse(HashMap<String, String> map) {
            try {
                JSONObject jsonObject = new JSONObject(map.get(HttpRequest.RESPONSE));

                if (jsonObject != null) {
                    int ii = jsonObject.getInt(ServerResponse.ERROR);
                    if (ServerResponse.RESULT_OK == jsonObject.getInt(ServerResponse.ERROR)) {
                        new AlertDialog.Builder(RegisterActivity.this).setMessage(R.string.register_success)
                        .setPositiveButton(getResources().getText(R.string.commit),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //accountManager.login(etAccount.getText().toString(), etPassword.getText().toString());
                                }
                            }).show();
                    } else {

                    }
                }
            } catch (Exception e) {
                Log.e(getClass().toString(), e.toString());
            }
        }

        @Override
        public void onUploadAccountPictureResponse(String json) {

        }

        @Override
        public void onResetPasswordResponse(String json) {

        }

        @Override
        public void onModifyPasswordResponse(String json) {

        }

        @Override
        public void onModifyEmailResponse(String json) {

        }

        @Override
        public void onAuthenticationResponse(String json) {

        }

        @Override
        public void noResponse() {
            new AlertDialog.Builder(RegisterActivity.this).setTitle(R.string.warning).setMessage(R.string.server_no_response).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        accountManager = AccountManager.getInstance(this);
        accountManager.setAccountEventListener(iAccountEventListener);
        tvCommit.setOnClickListener(getTvCommitOnClickListener());
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViews() {
        etAccount = (EditText) findViewById(R.id.etAccount);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirm = (EditText) findViewById(R.id.etConfirm);
        etNickname = (EditText) findViewById(R.id.etNickname);
        etEmail = (EditText) findViewById(R.id.etEmail);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCommit = (TextView) findViewById(R.id.tvCommit);
        tvError = (TextView) findViewById(R.id.tvError);
    }

    private View.OnClickListener getTvCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    accountManager.register(etAccount.getText().toString(), etPassword.getText().toString()
                            , etNickname.getText().toString(), etEmail.getText().toString());
                }
            }
        };
    }

    private boolean checkInput() {
        boolean result = true;

        if (etAccount.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()
                || etNickname.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty()) {
            showErrorMessage(R.string.enter_not_complete);
            result = false;
        } else {
            if (!etPassword.getText().toString().equals(etConfirm.getText().toString())) {
                showErrorMessage(R.string.confirm_incorrect);
                result = false;
            }
        }

        return result;
    }

    private void showErrorMessage(int errorResource) {
        tvError.setText(errorResource);
        tvError.setVisibility(View.VISIBLE);
    }
}
