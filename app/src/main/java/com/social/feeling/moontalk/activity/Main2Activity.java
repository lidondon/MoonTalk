package com.social.feeling.moontalk.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.empire.vmd.client.android_lib.activity.BaseFragmentActivity;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.fragment.NewFragment;
import com.social.feeling.moontalk.fragment.PersonalFragment;
import com.social.feeling.moontalk.fragment.FeelingPostFragment;

public class Main2Activity extends BaseFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //MainController.getInstance(this);
        initTabContent();
    }

    private void initTabContent() {
        FragmentTabHost tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        View vFeeling = LayoutInflater.from(this).inflate(R.layout.tab_feeling, null);
        TabHost.TabSpec tsFeeling = tabHost.newTabSpec("FEELING_POST").setIndicator(vFeeling);
        View vChatting = LayoutInflater.from(this).inflate(R.layout.tab_chatting, null);
        TabHost.TabSpec tsChatting = tabHost.newTabSpec("CHATTING").setIndicator(vChatting);
        View vMore = LayoutInflater.from(this).inflate(R.layout.tab_more, null);
        TabHost.TabSpec tsMore = tabHost.newTabSpec("MORE").setIndicator(vMore);
        View vActivity = LayoutInflater.from(this).inflate(R.layout.tab_activity, null);
        TabHost.TabSpec tsActivity = tabHost.newTabSpec("ACTIVITY").setIndicator(vActivity);

        tabHost.setup(this, getSupportFragmentManager(), R.id.realTabContent);
        //1
        tabHost.addTab(tsFeeling, FeelingPostFragment.class, null);
//        tabHost.addTab(tabHost.newTabSpec("Apple")
//                        .setIndicator("Apple", getResources().getDrawable(R.drawable.feelings)),
//                AppleFragment.class,
//                null);
        //2
        tabHost.addTab(tsChatting, NewFragment.class, null);
//        tabHost.addTab(tabHost.newTabSpec("Google")
//                        .setIndicator("Google"),
//                NewFragment.class,
//                null);
        //3
        tabHost.addTab(tsMore, PersonalFragment.class, null);
//        tabHost.addTab(tabHost.newTabSpec("Facebook")
//                        .setIndicator("Facebook"),
//                PersonalFragment.class,
//                null);
        //4
        tabHost.addTab(tsActivity, NewFragment.class, null);
//        tabHost.addTab(tabHost.newTabSpec("Twitter")
//                        .setIndicator("Twitter"),
//                TwitterFragment.class,
//                null);
    }
    /**************************
     *
     * 給子頁籤呼叫用
     *
     **************************/
//    public String getAppleData(){
//        return "Apple 123";
//    }
//    public String getGoogleData(){
//        return "Google 456";
//    }
//    public String getFacebookData(){
//        return "Facebook 789";
//    }
//    public String getTwitterData(){
//        return "Twitter abc";
//    }
}
