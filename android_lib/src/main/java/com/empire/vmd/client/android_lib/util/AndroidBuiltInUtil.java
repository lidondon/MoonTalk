package com.empire.vmd.client.android_lib.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Created by lidondon on 2015/10/15.
 */
public class AndroidBuiltInUtil {
    private Context context;

    public AndroidBuiltInUtil(Context ctx) {
        context = ctx;
    }

    public String[] getContactsNumbers() {
        String[] result;
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = new String[] { Contacts.People.NUMBER };
        Cursor cursor = contentResolver.query(Contacts.People.CONTENT_URI, projection, null, null, Contacts.People.DEFAULT_SORT_ORDER);

        result = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < result.length; i++) {
            cursor.moveToPosition(i);
            result[i] = cursor.getString(0);
        }

        return result;
    }

    public void toggleKeyboard(View view, boolean toggle) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (toggle) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void startCamera(int requestCode, Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();

        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        return dm.widthPixels;
    }
}
