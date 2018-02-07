package com.social.feeling.moontalk.datamodel;

import java.io.Serializable;

/**
 * Created by lidondon on 2016/7/29.
 */
public class FeelingTag implements Serializable {
    public String id;
    public int resource;

    public FeelingTag() {}

    public FeelingTag(String i, int r) {
        id = i;
        resource = r;
    }
}

