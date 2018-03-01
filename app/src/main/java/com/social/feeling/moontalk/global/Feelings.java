package com.social.feeling.moontalk.global;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.empire.vmd.client.android_lib.httpproxy.VolleyRequest;
import com.empire.vmd.client.android_lib.util.FileUtil;
import com.social.feeling.moontalk.datamodel.Feeling;
import com.social.feeling.moontalk.datamodel.Quote;
import com.social.feeling.moontalk.http.FeelingManager;
import com.social.feeling.moontalk.http.ServerResponse;
import com.social.feeling.moontalk.http.WebConfig;
import com.social.feeling.moontalk.sqlite.DbHelper;
import com.social.feeling.moontalk.util.SQLiteUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lidondon on 2016/8/7.
 */
public class Feelings {
    //private static final String FAKE_FEELINGS_FILE = "feeling.txt";
    private static final String TAG = "Feelings";
    private static final String DATA = "data";
    public static final int FROM_LOCAL = 0;
    public static final int FROM_WEB = 1;
    public static final int REFRESH = 1;
    private int loadCountAtOnce;
    private Context context;
    private List<String> idList;
    private List<Feeling> feelingList;
    private List<Feeling> newFeelingList;
    private Handler handler;
    private SQLiteUtil sqLiteUtil;
    private LoginData loginData;
    private int beginIndex;

    public Feelings(Context ctx, Handler h, int lCount) {
        context = ctx;
        handler = h;
        sqLiteUtil = new SQLiteUtil(context);
        loginData = LoginData.getInstance(context);
        loadCountAtOnce = lCount;
        feelingList = new ArrayList<>();
        //getFeelingListFromWeb(false, lCount);
        getFeelingsId();
    }

//    private void saveFakeFeelingsFile() {
//        JSONArray ja = new JSONArray();
//
//        for (Feeling feeling : fakeFeelingList) {
//            JSONObject jo = feeling.toJSONObject();
//
//            if (jo != null) {
//                ja.put(jo);
//            }
//        }
//
//        if (ja.length() > 0) {
//            new FileUtil(context).saveExternalFile(FileConfig.EXTERNAL_DIR, FAKE_FEELINGS_FILE, ja.toString());
//        }
//    }

//    public void getFeelingListFromWeb(final boolean refresh, final int lCount) {
//        if (handler != null) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //String strFeelings = null;
//                    Message message = Message.obtain();
//                    //fake for testing
//                    String strFeelings = new FileUtil(context).getStringFromExternalFile(FileConfig.EXTERNAL_DIR, FAKE_FEELINGS_FILE);
//
//                    if (strFeelings != null && !strFeelings.isEmpty()) {
//                        //bellow content is simulating server
//                        int max = 0;
//
//                        fakeFeelingList = parseFeelingList(strFeelings);
//                        if (refresh) {
//                            feelingList = new ArrayList<Feeling>();
//                            message.arg2 = REFRESH;
//                        } else {
//                            feelingList = (feelingList == null) ? new ArrayList<Feeling>() : feelingList;
//                        }
//                        newFeelingList = new ArrayList<Feeling>();
//                        max = (feelingList.size() + lCount > fakeFeelingList.size()) ? fakeFeelingList.size() : feelingList.size() + lCount;
//                        for (int i = feelingList.size(); i < max; i++) {
//                            newFeelingList.add(fakeFeelingList.get(i));
//                        }
//                        feelingList.addAll(newFeelingList);
//                        // simulate end
//                        message.obj = newFeelingList;
//                        handler.sendMessage(message);
//                        //save to local
//                        saveFeelings2Local(strFeelings);
//                    } else {
//                        getFeelingListFromLocal(ServerResponse.SERVER_NO_RESPONSE);
//                    }
//                }
//            }).start();
//        }
//    }

    public void getFeelingsId() {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jo) {
                try {
                    int response = jo.getInt(FeelingManager.ERR);

                    if (response == FeelingManager.Response.SUCCESS) {
                        idList = parseIdListFromJSONArray(jo.getJSONArray(FeelingManager.IDS));
                        getNextBlockFeelingList(false);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getFeelingsId: " + e.toString());
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Feelings.class.toString(), "getFeelingsId: " + error.toString());
            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebConfig.GET_ALL_FEELINGS_ID, listener, errorListener) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put(WebConfig.SET_COOKIE, loginData.session);

                return headers;
            }
        };

        volleyRequest.addRequest(jsonObjectRequest);
    }

    public void getNextBlockFeelingList(final boolean refresh) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        Response.Listener<String> listener = new Response.Listener<String>() {
            //JsonObjectRequest不知為何不會執行getParams來傳參數
//            @Override
//            public void onResponse(JsonObject jo) {
//                try {
//                    int response = jo.getInt(FeelingManager.ERR);
//
//                    if (response == FeelingManager.Response.SUCCESS) {
//                        String s = jo.toString();
//                        Log.e(TAG, "test: " + s);
//                    }
//                } catch (Exception e) {
//                    Log.e(TAG, "getNextBlockFeelingList: " + e.toString());
//                }
//            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jo = new JSONObject(s);
                    int response = jo.getInt(FeelingManager.ERR);

                    if (response == FeelingManager.Response.SUCCESS) {
                        JSONArray ja = new JSONArray(jo.getString(DATA));
                        Message msg = Message.obtain();

                        newFeelingList = new ArrayList<>();
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject joFeeling = ja.getJSONObject(i);
                            Feeling feeling = new Feeling(joFeeling);

                            newFeelingList.add(feeling);
                        }

                        if (refresh) {
                            msg.arg2 = REFRESH;
                        }
                        msg.obj = newFeelingList;
                        feelingList.addAll(newFeelingList);
                        handler.sendMessage(msg);
                        saveFeelings2Local(ja.toString());
                    }

                } catch (Exception e) {
                    Log.e(TAG, "getNextBlockFeelingList: " + e.toString());
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Feelings.class.toString(), "getFeelingListByIdList: " + error.toString());
            }
        };
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, WebConfig.GET_FEELINGS_BY_IDS, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> result = new HashMap<String, String>();
                try {
                    int stopIndex = (beginIndex + loadCountAtOnce > idList.size()) ? idList.size() : beginIndex + loadCountAtOnce;
                    JSONObject jo = getIdListJSONObject(stopIndex);

                    if (jo != null) {
                        result.put(FeelingManager.IDS,jo.toString());
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

                return result;
            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();

                headers.put(WebConfig.SET_COOKIE, loginData.session);

                return headers;
            }
        };

        volleyRequest.addRequest(jsonObjectRequest);
    }

    /**
     *
     * @param
     * @param stopIndex excluded
     * @return
     */
    private JSONObject getIdListJSONObject(int stopIndex) {
        JSONObject result = null;

        if (idList != null && stopIndex <= idList.size() && stopIndex > beginIndex) {
            JSONArray ja = new JSONArray();

            try {
                result = new JSONObject();
                for (int i = beginIndex; i < stopIndex; i++) {
                    ja.put(idList.get(i));
                }
                result.put(FeelingManager.IDS, ja);
                beginIndex = stopIndex;
            } catch (Exception e) {
                Log.e(TAG, "getIdListJSONObject: " + e.toString());
            }
        }

        return result;
    }

    private List<String> parseIdListFromJSONArray(JSONArray ids) {
        List<String> result = null;

        if (ids != null && ids.length() > 0) {
            result = new ArrayList<String>();
            for (int i = 0; i < ids.length(); i++) {
                try {
                    result.add(ids.getString(i));
                } catch (Exception e) {
                    Log.e(TAG, "parseIdListFromJSONArray: " + e.toString());
                }
            }
        }

        return result;
    }

    private void saveFeelings2Local(String strFeelings) {
        HashMap<String, String> paras = new HashMap<String, String>();
        HashMap<String, String> whereParas = new HashMap<String, String>();

        paras.put(SQLiteUtil.COLUMN_NAME, DbHelper.FEELINGS);
        paras.put(SQLiteUtil.COLUMN_CONTENT, strFeelings);
        whereParas.put(SQLiteUtil.COLUMN_NAME, DbHelper.FEELINGS);
        sqLiteUtil.updateLocalData(DbHelper.LOCAL_DATA, paras, whereParas);
        separateSaveFeelingLocal(strFeelings);
    }

    private void separateSaveFeelingLocal(String strFeelings) {
        List<Feeling> fList = parseFeelingList(strFeelings);

        sqLiteUtil.deleteLocalData(DbHelper.FEELINGS, null);
        if (fList != null && fList.size() > 0) {
            List<HashMap<String, String>> parasList = new ArrayList<HashMap<String, String>>();

            for (Feeling feeling : fList) {
                HashMap<String, String> parameters = new HashMap<String, String>();

                parameters.put(SQLiteUtil.COLUMN_UID, feeling.id);
                parameters.put(SQLiteUtil.COLUMN_CONTENT, feeling.toJSONObject().toString());
                parasList.add(parameters);
            }
            sqLiteUtil.updateLocalDataBatch(DbHelper.FEELINGS, parasList, null);
        }
    }

    public void getFeelingListFromLocal(int serverResponse) {
        if (handler != null) {
            Message message = Message.obtain();
            HashMap<String, String> parameters = new HashMap<>();
            String strFeelings = null;

            parameters.put(SQLiteUtil.COLUMN_NAME, DbHelper.FEELINGS);
            strFeelings = sqLiteUtil.getLocalData(DbHelper.LOCAL_DATA, parameters);
            message.obj = feelingList = parseFeelingList(strFeelings);
            if (serverResponse == ServerResponse.SERVER_NO_RESPONSE) {
                message.arg1 = serverResponse;
            }
            handler.sendMessage(message);
        }
    }

    private ArrayList<Feeling> parseFeelingList(String strFeelings) {
        ArrayList<Feeling> result = null;

        try {
            if (strFeelings != null && !strFeelings.isEmpty()) {
                JSONArray ja = new JSONArray(strFeelings);

                result = parseFeelingList(ja);
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
            result = null;
        }

        return result;
    }

    private ArrayList<Feeling> parseFeelingList(JSONArray ja) {
        ArrayList<Feeling> result = new ArrayList<>();

        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                result.add(new Feeling(jo));
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
            result = null;
        }

        return result;
    }


    // TODO: 2017/4/5 之後要改寫成真正上傳到server
    public static void postFeeling(Context ctx, Feeling feeling) {
        FeelingManager feelingManager = FeelingManager.getInstance(ctx);
        Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        feelingManager.beginPost(PostFeeling.getInstance().feeling, h);
    }
}
