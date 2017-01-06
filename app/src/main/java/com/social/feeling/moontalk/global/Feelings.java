package com.social.feeling.moontalk.global;

import android.content.Context;
import android.util.Log;

import com.empire.vmd.client.android_lib.util.FileUtil;
import com.social.feeling.moontalk.datamodel.Feeling;
import com.social.feeling.moontalk.datamodel.Quote;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2016/8/7.
 */
public class Feelings {
    private static final String FAKE_FEELINGS_FILE = "feeling.txt";
    private Context context;
    public List<Feeling> feelingList;

    public Feelings(Context ctx) {
        context = ctx;
        getFeelingList();
    }

    public void postFeeling(Feeling feeling) {
        if (feelingList != null) {
            feelingList.add(feeling);
            saveFakeFeelingsFile();
        }
    }

    private void saveFakeFeelingsFile() {
        JSONArray ja = new JSONArray();

        for (Feeling feeling : feelingList) {
            JSONObject jo = feeling.toJSONObject();

            ja.put(jo);
        }
        new FileUtil(context).saveExternalFile(MoonTalkConfig.EXTERNAL_DIR, FAKE_FEELINGS_FILE, ja.toString());
    }

    public void getFeelingList() {
        String strFeelings = new FileUtil(context).getStringFromExternalFile(MoonTalkConfig.EXTERNAL_DIR, FAKE_FEELINGS_FILE);

        feelingList = new ArrayList<Feeling>();
        try {
            JSONArray ja = (strFeelings == null || strFeelings.isEmpty()) ? new JSONArray() : new JSONArray(strFeelings);

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                feelingList.add(new Feeling(jo));
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
            feelingList = null;
        }
    }
}
