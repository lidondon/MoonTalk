package com.social.feeling.moontalk.datamodel;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lidondon on 2017/10/21.
 */
public class PersonData implements Serializable {
    public static final String TAG = "PersonData";
    public static final String ACCOUNT = "account";
    public static final String NAME = "username";
    private static final String PHOTO_URL = "PhotoUrl";
    public static final String PASSWORD = "password";
    public String account;
    public String name;
    public String photoUrl;

    public PersonData() {

    }

    public PersonData(JSONObject jo) {
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
