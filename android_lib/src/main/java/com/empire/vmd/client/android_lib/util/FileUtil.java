package com.empire.vmd.client.android_lib.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lidondon on 2015/11/15.
 */
public class FileUtil {
    private Context context;

    public FileUtil(Context ctx) {
        context = ctx;
    }

    public String getStrFromInternalFile(String fileName) {
        String result = null;

        try {
            FileInputStream fis = context.openFileInput(fileName);

            result = getStringFromInputString(fis);
        } catch (Exception e) {
            Log.w(this.getClass().getName(), e.getMessage().toString());
        }

        return result;
    }

    public String getStringFromRaw(int id) {
        InputStream inputStream = context.getResources().openRawResource(id);

        return getStringFromInputString(inputStream);
    }

    public String getStringFromExternalFile(String strDir, String fileName) {
        File file = getExternalFile(strDir, fileName);
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
        } catch (Exception e) {
            Log.e(getClass().getName(), e.toString());
        }

        return getStringFromInputString(fis);
    }

    public String getStringFromInputString(InputStream inputStream) {
        String result = null;

        try {
            String tmpStr = null;
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bReader = new BufferedReader(reader);

            result = "";
            while ((tmpStr = bReader.readLine()) != null) {
                result += tmpStr;
            }
        } catch (Exception e) {
            Log.w(this.getClass().getName(), e.toString());
        }

        return result;
    }

    public void saveInternalFile(String fileName, String strData) {
        if (strData != null && !strData.isEmpty()) {
            try {
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);

                fos.write(strData.getBytes());
                fos.close();
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getMessage().toString());
            }
        }
    }

    public boolean canWriteToExternalStorage() {
        return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) ? true : false;
    }

    public void saveExternalFile(String strDir, String fileName, String strData) {
        saveExternalFile(strDir, fileName, strData.getBytes(), false);
    }

    public void saveExternalFile(String strDir, String fileName, byte[] bytes) {
        saveExternalFile(strDir, fileName, bytes, false);
    }

    public void saveExternalFile(String strDir, String fileName, byte[] bytes, boolean deleteFile) {
        File file = getExternalFile(strDir, fileName);

        if (file != null) {
            if (file.exists()) {
                file.delete();
            }

            try {
                FileOutputStream fos = new FileOutputStream(file);

                fos.write(bytes);
                fos.close();
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getMessage().toString());
            }
        }
    }

    public File getExternalFile(String strDir, String fileName) {
        File file = null;

        if (canWriteToExternalStorage()) {
            File fileDir = Environment.getExternalStorageDirectory();

            if (strDir != null && !strDir.isEmpty()) {
                fileDir = new File(fileDir.getAbsolutePath() + "/" + strDir + "/");
                if (!fileDir.exists()) {
                    fileDir.mkdir();
                }
            }
            file = new File(fileDir, fileName);
        }

        return file;
    }

    public String getAbsolutePath(Uri uri) {
        ContentResolver localContentResolver = context.getContentResolver();
        Cursor localCursor = localContentResolver.query(uri, null, null, null, null);

        localCursor.moveToFirst();

        return localCursor.getString(localCursor.getColumnIndex("_data"));
    }
}
