package com.social.feeling.moontalk.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.social.feeling.moontalk.sqlite.DbHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lidondon on 2017/2/15.
 */
public class SQLiteUtil {
    public static final String TAG = "SQLiteUtil";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_CONTENT = "content";
    private Context context;
    private SQLiteDatabase db;

    public SQLiteUtil(Context ctx) {
        context = ctx;
        db = new DbHelper(context).getWritableDatabase();
    }

    public String getLocalData(String tableName, HashMap<String, String> parameters) {
        String result = null;

        if (db != null) {
            String where = getWhereString(parameters);
            Cursor c = db.query(tableName, null, where, null, null, null, null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                result = c.getString(c.getColumnIndex(COLUMN_CONTENT));
            }
        }

        return result;
    }

    public void updateLocalData(String tableName, HashMap<String, String> paras, HashMap<String, String> whereParas) {
        if (db != null && paras != null && paras.keySet().size() > 0) {
            ContentValues contentValues = getContentValues(paras);

            if (whereParas == null || getLocalData(tableName, whereParas) == null) {
                db.insert(tableName, null, contentValues);
            } else {
                String where = getWhereString(whereParas);

                db.update(tableName, contentValues, where, null);
            }
        }
    }

    public void newLocalData(String tableName, HashMap<String, String> paras) {
        if (db != null && paras != null && paras.keySet().size() > 0) {
            ContentValues contentValues = getContentValues(paras);

            if (getLocalData(tableName, paras) == null) {
                db.insert(tableName, null, contentValues);
            } else {
                Log.e(getClass().toString(), "Data has been exist");
            }
        }
    }

    public void deleteLocalData(String tableName, HashMap<String, String> paras) {
        String where = getWhereString(paras);

        if (db != null) {
            //TODO 可以改更正式
            try {
                db.delete(tableName, where, null);
            } catch (Exception e) {
                Log.e(TAG, "deleteLocalData: " + e.toString());
            }
        }
    }

    public void updateLocalDataBatch(final String tableName, final List<HashMap<String, String>> parasList, final HashMap<String, String> whereParas) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (HashMap<String, String> paras : parasList) {
                    updateLocalData(tableName, paras, whereParas);
                }
            }
        }).start();
    }

    private String getWhereString(HashMap<String, String> parameters) {
        String result = null;

        if (parameters != null && parameters.keySet().size() > 0) {
            for (String key : parameters.keySet()) {
                result = (result == null) ? "" : result + " and ";
                result += key + " = '" + parameters.get(key) + "'";
            }
        }

        return result;
    }

    private ContentValues getContentValues(HashMap<String, String> parameters) {
        ContentValues result = null;

        if (parameters != null && parameters.keySet().size() > 0) {
            result = new ContentValues();
            for (String key : parameters.keySet()) {
                result.put(key, parameters.get(key));
            }
        }

        return result;
    }

    @Override
     protected void finalize() throws Throwable {
        super.finalize();
        if (db != null) {
            db.close();
        }
    }
}
