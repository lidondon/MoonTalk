package com.social.feeling.moontalk.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lidondon on 2017/2/15.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "IAmFeeling";
    private static final int DB_VERSION = 1;
    public static final String LOCAL_DATA = "local_data";
    public static final String FEELINGS = "feelings";
    //public static final String[] COLUMNS = {"_id", "name", "content"};

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE local_data (_id integer primary key autoincrement, " +
            " name text no null, content text no null)");
        db.execSQL("CREATE TABLE feelings (_id integer primary key autoincrement, " +
                " uid text no null, content text no null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS local_data");
        db.execSQL("DROP TABLE IF EXISTS feelings");
        onCreate(db);
    }
}
