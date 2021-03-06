package com.social.feeling.moontalk.global;

import com.social.feeling.moontalk.activity.MainActivity;
import com.social.feeling.moontalk.fragment.FeelingPostFragment;

/**
 * Created by lidondon on 2016/8/8.
 */
public class MainController {
    public static final int FEELING_POST = 0;
    public static final int NEW_POST = 1;
    public static final int PERSONAL = 2;
    public static final int MORE = 3;
    private static volatile MainController mainController;
    private MainActivity mainActivity;

    private MainController(MainActivity activity) {
        mainActivity = activity;
    }

    public static MainController getInstance() {
        return mainController;
    }

    public static MainController getInstance(MainActivity activity) {
        if (mainController == null) {
            synchronized (MainController.class) {
                if (mainController == null) {
                    mainController = new MainController(activity);
                }
            }
        }

        return mainController;
    }

    public void setFragmentIndex(int index) {
        mainActivity.setViewPagerIndex(index);
    }

    public void refreshFeelingPost() {
        //mainActivity.setFeelingPostInitiated(false);
        ((FeelingPostFragment) mainActivity.fragmentList.get(FEELING_POST)).refreshLlFeelings();
    }
}
