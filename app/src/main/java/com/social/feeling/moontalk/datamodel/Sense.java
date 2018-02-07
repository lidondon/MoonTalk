package com.social.feeling.moontalk.datamodel;

import com.social.feeling.moontalk.R;

/**
 * Created by lidondon on 2017/3/17.
 */
public class Sense {
    public static final String EYE = "Eye";
    public static final String EAR = "Ear";
    public static final String MOUTH = "Mouth";
    public static final String NOSE = "Nose";
    public static final String TOUCH = "Touch";
    public static final String FEEL = "Feel";
    public String id;
    public int resource;
    public String text;

    public Sense(String i) {
        id = i;
        setCompleteInfo();
    }

    private void setCompleteInfo() {
        switch(id) {
            case EYE:
                resource = R.drawable.eye;
                text = "視覺";
                break;
            case EAR:
                resource = R.drawable.ear;
                text = "聽覺";
                break;
            case MOUTH:
                resource = R.drawable.mouth;
                text = "味覺";
                break;
            case NOSE:
                resource = R.drawable.nose;
                text = "嗅覺";
                break;
            case TOUCH:
                resource = R.drawable.touch;
                text = "觸覺";
                break;
            case FEEL:
                resource = R.drawable.feel;
                text = "靈覺";
                break;
        }
    }
}
