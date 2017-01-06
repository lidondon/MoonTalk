package com.social.feeling.moontalk.global;

import android.content.Context;
import android.content.SharedPreferences;

import com.social.feeling.moontalk.util.PrefUtil;

/**
 * Created by lidondon on 2016/5/29.
 */
public class LoginData {
    public static final String LOGIN_ACCOUNT = "LoginAccount";
    public static final String LOGIN_NAME = "LoginName";
    public static final String LOGIN_PHOTO = "LoginPhoto";
    private volatile static LoginData loginData;
    private PrefUtil prefUtil;
    private Context context;
    public String account;
    public String name;
    public String photo;
    public String occupation;
    public String liveIn;
    public String travel;

    private LoginData (Context ctx) {
        context = ctx;
        getContent();
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

    private void getContent() {
//        SharedPreferences pref = context.getSharedPreferences(prefUtil.SHARED_PREFERENCE, Context.MODE_PRIVATE);
//
//        account = pref.getString(LOGIN_ACCOUNT, "lidondon");
//        name = pref.getString(LOGIN_NAME, "李凍凍");
//        photo = pref.getString(LOGIN_PHOTO, "/storage/emulated/0/DCIM/Camera/P_20150624_114612.jpg");
//        occupation = "業餘工程師";
//        liveIn = "新北市, 台灣";
//        travel = "夏威夷, 德國, 瑞士";
        account = "lidondon";
        name = "李凍凍";
        //photo = "/storage/emulated/0/DCIM/Camera/P_20150624_114612.jpg";
        occupation = "業餘工程師";
        liveIn = "新北市, 台灣";
        travel = "夏威夷, 德國, 瑞士";
    }
}
