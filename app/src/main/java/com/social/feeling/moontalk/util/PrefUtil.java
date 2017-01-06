package com.social.feeling.moontalk.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.social.feeling.moontalk.global.LoginData;

/**
 * Created by lidondon on 2016/5/29.
 */
public class PrefUtil {
    public static final String SHARED_PREFERENCE = "SharedPreference";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public PrefUtil(Context context) {
        pref = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveLoginData(String account, String name) {
        editor.putString(LoginData.LOGIN_ACCOUNT, account);
        editor.putString(LoginData.LOGIN_NAME, name);
        editor.apply();
    }
}
