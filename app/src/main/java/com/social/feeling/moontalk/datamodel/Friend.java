package com.social.feeling.moontalk.datamodel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lidondon on 2016/8/22.
 */
public class Friend {
    private static final String ACCOUNT = "Account";
    private static final String NAME = "Name";
    private static final String PHOTO_URI = "PhotoUri";
    public String account;
    public String name;
    public String photoUri;



    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();

        try {
            result.put(ACCOUNT, account);
            result.put(NAME, name);
            result.put(PHOTO_URI, photoUri);
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
        }

        return result;
    }
}
