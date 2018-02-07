package com.empire.vmd.client.android_lib.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by lidondon on 2015/10/5.
 */
public class DialogUtil {

    public interface IDateSet {
        public void dateSet(int year, int month, int day);
    }

    public interface ITimeSet {
        public void timeSet(int hour, int minute);
    }

    public void setHeightAndWidth(Activity activity, Dialog dialog, double heightRate, double widthRate) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.height = (int) (display.getHeight() * heightRate);
        params.width = (int) (display.getWidth() * widthRate);
        window.setAttributes(params);
    }

    public DatePickerDialog getDatePickerDialog(Context ctx, final IDateSet iDateSet) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        DatePickerDialog result = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                iDateSet.dateSet(year, monthOfYear, dayOfMonth);
            }
        }, year, month, day);

        result.updateDate(year, month, day);

        return result;
    }

    public TimePickerDialog getTimePickerDialog(Context ctx, final ITimeSet iTimeSet) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        TimePickerDialog result = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                iTimeSet.timeSet(hourOfDay, minute);
            }
        }, hour, minute, false);

        result.updateTime(hour, minute);

        return result;
    }
}
