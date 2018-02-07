package com.social.feeling.moontalk.activity;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextServicesManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.empire.vmd.client.android_lib.httpproxy.VolleyRequest;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.PersonData;
import com.social.feeling.moontalk.global.LoginData;
import com.social.feeling.moontalk.http.AccountManager;
import com.social.feeling.moontalk.http.WebConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TestServerActivity extends AppCompatActivity {
    private static final String TAG = "TestServerActivity";
    private LoginData loginData = LoginData.getInstance(this);
    private EditText etChangeName;
    private Button btnChangeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_server);
        findViews();
        btnChangeName.setOnClickListener(getBtnChangeNameOnClickListener());
    }

    private void findViews() {
        etChangeName = (EditText) findViewById(R.id.etChangeName);
        btnChangeName = (Button) findViewById(R.id.btnChangeName);
    }

    private View.OnClickListener getBtnChangeNameOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etChangeName.getText().toString().isEmpty()) {
                    Toast.makeText(TestServerActivity.this, "請填入新名稱", Toast.LENGTH_SHORT).show();
                } else {
                    VolleyRequest volleyRequest = VolleyRequest.getInstance(TestServerActivity.this);
                    com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String strResponse) {
                            try {
                                JSONObject jo = new JSONObject(strResponse);
                                int response = jo.getInt("err");

                                if (response == AccountManager.Response.SUCCESS) {
                                    Toast.makeText(TestServerActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TestServerActivity.this, "修改失敗，err: " + response, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "login: " + e.toString());
                            }

                        }
                    };
                    com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    };
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.MODIFY_USER_NAME, listener, errorListener) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> result = new HashMap<String, String>();

                            result.put(PersonData.NAME, etChangeName.getText().toString());

                            return result;
                        }

                        @Override
                        public Map getHeaders() throws AuthFailureError {
                            Map headers = new HashMap();

                            headers.put(WebConfig.SET_COOKIE, loginData.session);

                            return headers;
                        }
                    };

                    volleyRequest.addRequest(stringRequest);
                }
            }
        };
    }
}
