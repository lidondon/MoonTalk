package com.social.feeling.moontalk.datamodel;

import android.accounts.Account;
import android.app.job.JobScheduler;
import android.content.Context;
import android.hardware.SensorEvent;
import android.util.Log;

import com.social.feeling.moontalk.global.FeelingTags;
import com.social.feeling.moontalk.sqlite.DbHelper;
import com.social.feeling.moontalk.util.SQLiteUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lidondon on 2016/7/27.
 */
//public class Feeling implements Serializable {
public class Feeling {
    private static final String TAG = "Feeling";
    public static final String ID = "Id";
    public static final String ACCOUNT = "Account";
    public static final String NAME = "Name";
    public static final String SENSE = "Sense";
    public static final String PHOTO_URI = "PhotoUri";
    public static final String QUOTE = "QuoteText";
    public static final String CHECKED_COLOR_ID = "CheckedColorId";
    public static final String CHECKED_PHOTO_COUNT = "PhotoCount";
    public static final String CHECKED_PHOTO_LIST = "CheckedPhotoList";
    public static final String PHOTO_RUI = "PhotoUri";
    //public static final String TAG_LIST = "TagList";
    public static final String PERMISSION_LIST = "PermissionList";
    public static final String FEELING_TAG = "FeelingTag";
    public static final String THOUGHT = "Thought";
    public String id;
    public String person_id = "3"; //for test
    public String account;
    public String name;
    public String photoUri;
    //public Quote quote;
    public String quote;
    public String checkedColorId;
    public List<String> checkedPhotoList;
    //public List<FeelingTag> tagList;
    public List<String> permissionList;
    public String thought;
    public Sense sense;

    public Feeling() {

    }

    public static Feeling getFeelingByUidFromLocal(Context ctx, String uid) {
        Feeling result = null;
        SQLiteUtil sqLiteUtil = new SQLiteUtil(ctx);

        try {
            HashMap<String, String> parameters = new HashMap<String, String>();
            String strJson = null;

            parameters.put(SQLiteUtil.COLUMN_UID, uid);
            strJson = sqLiteUtil.getLocalData(DbHelper.FEELINGS, parameters);
            result = new Feeling(strJson);
        } catch (Exception e) {
            Log.e(Feeling.class.toString(), e.toString());
        }

        return result;
    }

    public Feeling(String strJson) throws Exception {
        this(new JSONObject(strJson));
    }

    public Feeling(JSONObject jo) {
        try {
            id = jo.getString(ID);
            //account = jo.getString(ACCOUNT);
            name = jo.getString(NAME);
            sense = new Sense(jo.getString(SENSE));
            photoUri = (jo.has(PHOTO_RUI)) ? jo.getString(PHOTO_RUI) : null;
            //quote = new Quote(jo.getJSONObject(QUOTE));
            quote = jo.getString(QUOTE);
            checkedColorId = jo.getString(CHECKED_COLOR_ID);
            checkedPhotoList = getCheckedPhotoList(jo.getJSONArray(CHECKED_PHOTO_LIST));
            //tagList = getTagList(jo.getJSONArray(TAG_LIST));
            thought = jo.getString(THOUGHT);
        } catch (Exception e) {
            Log.e(TAG, "Feeling: " + e.toString());
            clearData();
        }
    }

    public void clearData() {
        id = null;
        account = null;
        name = null;
        sense = null;
        photoUri = null;
        quote = null;
        checkedColorId = null;
        checkedPhotoList = null;
        thought = null;
    }

    public List<String> getCheckedPhotoList(JSONArray ja) {
        return getStringList(ja, PHOTO_RUI);
    }

    public List<String> getPermissionList(JSONArray ja) {
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
            int checkedPhotoCount = (checkedPhotoList == null) ? 0 : checkedPhotoList.size();

            result.put(ID, id);
            result.put(ACCOUNT, account);
            result.put(NAME, name);
            result.put(SENSE, sense.id);
            result.put(PHOTO_RUI, photoUri);
            //result.put(QUOTE, quote.toJsonObject());
            result.put(QUOTE, quote);
            result.put(CHECKED_COLOR_ID, checkedColorId);
            result.put(CHECKED_PHOTO_COUNT, checkedPhotoCount);
            result.put(CHECKED_PHOTO_LIST, getCheckedPhotoListJSONArray());
            //result.put(TAG_LIST, getTagListJSONArray()); //已修改為sense
            result.put(THOUGHT, thought);
            result.put(PERMISSION_LIST, getPermissionListJSONArray());
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
            result = null;
        }

        return result;
    }

    private JSONArray getPermissionListJSONArray() {
        JSONArray result = new JSONArray();

        try {
            if (permissionList != null) {
                for (String account : permissionList) {
                    JSONObject jo = new JSONObject();

                    jo.put(ACCOUNT, account);
                    result.put(jo);
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
            result = null;
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

//    public JSONArray getTagListJSONArray() {
//        JSONArray result = new JSONArray();
//
//        try {
//            if (tagList != null) {
//                for (FeelingTag tag : tagList) {
//                    JSONObject jo = new JSONObject();
//
//                    jo.put(FEELING_TAG, tag.id);
//                    result.put(jo);
//                }
//            }
//        } catch (Exception e) {
//            Log.e(getClass().getName(), e. toString());
//            result = null;
//        }
//
//        return result;
//    }
}
