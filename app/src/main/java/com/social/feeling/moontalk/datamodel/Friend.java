package com.social.feeling.moontalk.datamodel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lidondon on 2016/8/22.
 */
public class Friend {
    private static final String TAG = "Friend";
    private static final String ACCOUNT = "Account";
    private static final String NAME = "Name";
    private static final String PHOTO_URL = "PhotoUrl";
    public String account;
    public String name;
    public String photoUrl;

    public Friend() {

    }

    public Friend(JSONObject jo) {
        try {
            account = jo.getString(ACCOUNT);
            name = jo.getString(NAME);
            photoUrl = jo.getString(PHOTO_URL);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();

        try {
            result.put(ACCOUNT, account);
            result.put(NAME, name);
            result.put(PHOTO_URL, photoUrl);
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
        }

        return result;
    }
}
