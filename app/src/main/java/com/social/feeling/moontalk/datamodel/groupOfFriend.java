package com.social.feeling.moontalk.datamodel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2016/8/22.
 */
public class groupOfFriend {
    private static String ID = "Id";
    private static String NAME = "Name";
    private static String PHOTO_URI = "PhotoUri";
    private static String FRIENDS = "Friends";
    public String id;
    public String name;
    public String photoUri;
    public List<PersonData> friendList = new ArrayList<PersonData>();;

    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();

        try {
            result.put(ID, id);
            result.put(NAME, name);
            if (photoUri != null && !photoUri.isEmpty()) {
                result.put(PHOTO_URI, photoUri);
            }
            result.put(FRIENDS, getFriendListJSONArray());
        } catch (Exception e) {
            Log.e(getClass().getName() + ": " + "toJSONObject", e.toString());
        }


        return result;
    }

    private JSONArray getFriendListJSONArray() {
        JSONArray result = new JSONArray();

        if (friendList != null) {
            for (PersonData friend : friendList) {
                JSONObject jo = friend.toJSONObject();

                result.put(jo);
            }
        }

        return result;
    }
}
