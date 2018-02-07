package com.social.feeling.moontalk.datamodel;

import com.empire.vmd.client.android_lib.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lidondon on 2017/8/23.
 */

public class Action {
    public String id;
    public String title;
    public String location;
    public Date date;
    public List<String> photoUrlList;


    public static List<Action> getFakeActionList() {
        List<Action> result = new ArrayList<Action>();
        Calendar calendar = Calendar.getInstance();
        Action a1 = new Action();
        Action aEnd = new Action();

        //Test if order by date, so mean to disorder
        a1.title = "首發活動";
        aEnd.title = "最後活動";
        calendar.setTime(a1.date);
        calendar.add(Calendar.DAY_OF_YEAR, -2);
        a1.date = calendar.getTime();
        result.add(new Action()); //mean to disorder
        result.add(a1);
        calendar.setTime(aEnd.date);
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        aEnd.date = calendar.getTime();

        for (int i = 0; i < 10; i++) {
            result.add(new Action());
        }
        result.add(aEnd);

        return result;
    }

    public Action() {
        title = "虛擬活動";
        location = "台灣";
        date = new Date();
        id = date.toString();
    }
}
