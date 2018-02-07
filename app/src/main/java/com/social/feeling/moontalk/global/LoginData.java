package com.social.feeling.moontalk.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.empire.vmd.client.android_lib.httpproxy.MultipartRequest;
import com.empire.vmd.client.android_lib.httpproxy.VolleyRequest;
import com.google.android.gms.plus.model.people.Person;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.activity.TestServerActivity;
import com.social.feeling.moontalk.datamodel.PersonData;
import com.social.feeling.moontalk.http.AccountManager;
import com.social.feeling.moontalk.http.FeelingManager;
import com.social.feeling.moontalk.http.WebConfig;
import com.social.feeling.moontalk.util.PrefUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lidondon on 2016/5/29.
 */
public class LoginData {
    private static final String TAG = "LoginData";
    public static final String PORTRAIT_URL = "PortraitUrl";
    private static final String PHOTO = "photoUrl";
    private volatile static LoginData loginData;
    private PrefUtil prefUtil;
    private Context context;
    public String session;
    public PersonData personData;
//    public String account;
//    public String name;
//    public String email;
//    public String photoUrl;
//    public String occupation;
//    public String liveIn;
    //public String travel;

    private LoginData (Context ctx) {
        context = ctx;
        //getContent();
    }

    public static LoginData getInstance(Context ctx) {
        if (loginData == null) {
            synchronized (LoginData.class) {
                if (loginData == null) {
                    loginData = new LoginData(ctx);
                }
            }
        }

        return loginData;
    }

    public void updateUserPhoto(String photoPath, final Handler handler) {
        File file = new File(photoPath);
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    JSONObject jo = new JSONObject(strResponse);
                    int response = jo.getInt(WebConfig.Response.ERROR);

                    if (response == FeelingManager.Response.SUCCESS) {
                        handler.sendEmptyMessage(0);
                        Toast.makeText(context, R.string.update_success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.update_fail, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "updateUserPhoto catch: " + e.toString());
                }
            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "updateUserPhoto: " + error.toString());
            }
        };
        MultipartRequest request = new MultipartRequest(WebConfig.MODIFY_USER_PHOTO, listener, errorListener, PHOTO, file, null) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put(AccountManager.USER_AGENT, AccountManager.USER_AGENT_VALUE);
                headers.put(WebConfig.SET_COOKIE, session);

                return headers;
            }
        };

        VolleyRequest.getInstance(context).addRequest(request);
    }


}
