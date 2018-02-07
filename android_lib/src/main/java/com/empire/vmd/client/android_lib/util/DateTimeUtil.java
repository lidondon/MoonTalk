package com.empire.vmd.client.android_lib.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lidondon on 2017/7/30.
 */

public class DateTimeUtil {
    public static String getNowString(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date now = new Date();

        return simpleDateFormat.format(now);
    }
}
