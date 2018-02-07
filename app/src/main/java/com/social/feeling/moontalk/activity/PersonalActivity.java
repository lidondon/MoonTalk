package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.empire.vmd.client.android_lib.httpproxy.VolleyRequest;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Friend;
import com.social.feeling.moontalk.datamodel.PersonData;
import com.social.feeling.moontalk.fragment.PersonalFragment;
import com.social.feeling.moontalk.global.FriendAndGroup;
import com.social.feeling.moontalk.http.AccountManager;
import com.social.feeling.moontalk.http.WebConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PersonalActivity extends FragmentActivity {
    private static final String TAG = "PersonalActivity";
    private PersonData personData;
    private ImageView ivPortrait;
    private TextView tvName;
    private TextView tvAccount;
    private LinearLayout llTrace;
    private LinearLayout llAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        findViews();
        initPersonData();
        llAddFriend.setOnClickListener(getLlAddFriendOnClickListener());
    }

    private void findViews() {
        ivPortrait = (ImageView) findViewById(R.id.ivPortrait);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAccount = (TextView) findViewById(R.id.tvAccount);
        llTrace = (LinearLayout) findViewById(R.id.llTrace);
        llAddFriend = (LinearLayout) findViewById(R.id.llAddFriend);
    }

    //// TODO: 2017/10/21  要改成從Bundle取得account在跟server person取資料
    private void initPersonData() {
        Intent intent = getIntent();

        if (intent != null) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                getFakePersonData(bundle.getString(PersonData.ACCOUNT));
                tvName.setText(personData.name);
                tvAccount.setText(personData.account);
            }
        }
    }

    private void getFakePersonData(String account) {
//        VolleyRequest volleyRequest = VolleyRequest.getInstance(this);
//        com.android.volley.Response.Listener<JSONObject> listener = new com.android.volley.Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jo) {
//                try {
//
//                } catch (Exception e) {
//                    Log.e(TAG, "getFakePersonData: " + e.toString());
//                }
//            }
//        };
//        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(getClass().toString(), error.toString());
//            }
//        };
//        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, , listener, errorListener);
//        volleyRequest.addRequest(jsonArrayRequest);

        //下面是假資料，正確應該寫在volley回傳的listener
        personData = new PersonData();
        personData.account = "test9@abc.com";
        personData.name = "假的人";
    }

    private View.OnClickListener getLlAddFriendOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
                DialogInterface.OnClickListener positiveOnClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FriendAndGroup.getInstance(PersonalActivity.this).inviteFriend(personData.account);
                    }
                };
                DialogInterface.OnClickListener negativeOnClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                builder.setTitle(getResources().getString(R.string.add_friend));
                builder.setPositiveButton(R.string.commit, positiveOnClickListener);
                builder.setNegativeButton(R.string.cancel, negativeOnClickListener);
                builder.create().show();
            }
        };
    }
}
