package com.social.feeling.moontalk.datamodel;

import android.accounts.Account;
import android.util.Log;

import com.social.feeling.moontalk.global.FeelingTags;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2016/7/27.
 */
public class Feeling implements Serializable {
    public static final String ACCOUNT = "Account";
    public static final String NAME = "Name";
    public static final String PHOTO_URI = "PhotoUri";
    public static final String QUOTE = "Quote";
    public static final String CHECKED_COLOR_ID = "CheckedColorId";
    public static final String CHECKED_PHOTO_LIST = "CheckedPhotoList";
    public static final String PHOTO_RUI = "PhotoUri";
    public static final String TAG_LIST = "TagList";
    public static final String PERMISSION_LIST = "PermissionList";
    public static final String FEELING_TAG = "FeelingTag";
    public static final String THOUGHT = "Thought";
    public String account;
    public String name;
    public String photoUri;
    public Quote quote;
    public String checkedColorId;
    public List<String> checkedPhotoList;
    public List<FeelingTag> tagList;
    public List<String> permissionList;
    public String thought;

    public Feeling() {

    }

    public Feeling(JSONObject jo) {
        try {
            account = jo.getString(ACCOUNT);
            name = jo.getString(NAME);
            photoUri = (jo.has(PHOTO_RUI)) ? jo.getString(PHOTO_RUI) : null;
            quote = new Quote(jo.getJSONObject(QUOTE));
            checkedColorId = jo.getString(CHECKED_COLOR_ID);
            checkedPhotoList = getCheckedPhotoList(jo.getJSONArray(CHECKED_PHOTO_LIST));
            tagList = getTagList(jo.getJSONArray(TAG_LIST));
            thought = jo.getString(THOUGHT);
        } catch (Exception e) {
            clearData();
        }
    }

    public void clearData() {
        quote = null;
        checkedColorId = null;
        checkedPhotoList = null;
        tagList = null;
        thought = null;
    }

    public List<String> getCheckedPhotoList(JSONArray ja) {
        return getStringList(ja, PHOTO_RUI);
    }

    public List<FeelingTag> getTagList(JSONArray ja) {
        List<FeelingTag> result = new ArrayList<FeelingTag>();
        List<String> tagIdList = getStringList(ja, FEELING_TAG);
        FeelingTags feelingTags = FeelingTags.getInstance();

        for (String tagId : tagIdList) {
            result.add(feelingTags.getTagById(tagId));
        }

        return result;
    }

    public List<String> getStringList(JSONArray ja, String name) {
        List<String> result = new ArrayList<String>();

        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                result.add(jo.getString(name));
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
            result = null;
        }

        return result;
    }

    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();

        try {
            result.put(ACCOUNT, account);
            result.put(NAME, name);
            result.put(PHOTO_RUI, photoUri);
            result.put(QUOTE, quote.toJsonObject());
            result.put(CHECKED_COLOR_ID, checkedColorId);
            result.put(CHECKED_PHOTO_LIST, getCheckedPhotoListJSONArray());
            result.put(TAG_LIST, getTagListJSONArray());
            result.put(THOUGHT, thought);
            result.put(PERMISSION_LIST, getPermissionListJSONArray());
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
        }

        return result;
    }

    private JSONArray getPermissionListJSONArray() {
        JSONArray result = new JSONArray();

        try {
            for (String account : permissionList) {
                JSONObject jo = new JSONObject();

                jo.put(ACCOUNT, account);
                result.put(jo);
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
        }

        return result;
    }

    public JSONArray getCheckedPhotoListJSONArray() {
        JSONArray result = new JSONArray();

        try {
            if (checkedPhotoList != null) {
                for (String photoUri : checkedPhotoList) {
                    JSONObject jo = new JSONObject();

                    jo.put(PHOTO_RUI, photoUri);
                    result.put(jo);
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e. toString());
        }

        return result;
    }

    public JSONArray getTagListJSONArray() {
        JSONArray result = new JSONArray();

        try {
            for (FeelingTag tag : tagList) {
                JSONObject jo = new JSONObject();

                jo.put(FEELING_TAG, tag.id);
                result.put(jo);
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e. toString());
        }

        return result;
    }
}
