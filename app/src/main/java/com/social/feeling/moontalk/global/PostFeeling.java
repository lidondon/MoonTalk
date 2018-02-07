package com.social.feeling.moontalk.global;

import android.app.Activity;

import com.social.feeling.moontalk.datamodel.Feeling;
import com.social.feeling.moontalk.datamodel.Quote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2016/7/27.
 */
public class PostFeeling {
    private static volatile PostFeeling postFeeling;
    private static final List<Activity> activityList = new ArrayList<Activity>();
    public Quote quote;
    public Feeling feeling;

    private PostFeeling() {
        feeling = new Feeling();
    }

    public static PostFeeling getInstance() {
        if (postFeeling == null) {
            synchronized (PostFeeling.class) {
                if (postFeeling == null) {
                    postFeeling = new PostFeeling();
                }
            }
        }

        return postFeeling;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void clear() {
        feeling = new Feeling();
        for (int i = 0; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
        activityList.clear();
    }
}
