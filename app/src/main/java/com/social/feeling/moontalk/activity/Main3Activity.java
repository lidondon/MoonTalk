package com.social.feeling.moontalk.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.empire.vmd.client.android_lib.activity.BaseFragmentActivity;
import com.empire.vmd.client.android_lib.adapter.TabFragmentPagerAdapter;
import com.empire.vmd.client.android_lib.util.TabUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.fragment.NewFragment;
import com.social.feeling.moontalk.fragment.PersonalFragment;
import com.social.feeling.moontalk.fragment.FeelingPostFragment;
import com.social.feeling.moontalk.global.LoginData;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends BaseFragmentActivity {
    private TabUtil tabUtil = new TabUtil(this);
    public List<Fragment> fragmentList;
    protected RadioGroup radioGroup;
    protected ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        //MainController.getInstance(this);
        initTabFunction();
        tabUtil.bindTabAndPager(radioGroup, viewPager);
    }

    private void findViews() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        viewPager = (ViewPager) findViewById(R.id.pager);
    }

    private void initTabFunction() {
        getFragments();
        viewPager.setAdapter(new TabFragmentPagerAdapter(Main3Activity.this.getSupportFragmentManager(), this, fragmentList));
        setTabContent();
        tabUtil.bindTabAndPager(radioGroup, viewPager);
    }

    private void getFragments() {
        fragmentList = (fragmentList == null) ? new ArrayList<Fragment>() : fragmentList;

        //fragmentList.add(new FeelingPostFragment(this));
        fragmentList.add(new NewFragment(this));
        fragmentList.add(new NewFragment(this));
        fragmentList.add(new PersonalFragment());
        fragmentList.add(new NewFragment(this));
    }

    private void setTabContent() {
//        String[] tabTexts = {getResources().getString(R.string.wall), getResources().getString(R.string.new_post)
//                , getResources().getString(R.string.personal_post), getResources().getString(R.string.more)};
        String[] tabTexts = {"Wall", "New", "Personal", "More"};
        //int[] resources = {R.drawable.feelings, R.drawable.chatting, R.drawable.personal, R.drawable.activity};

//
        tabUtil.setTabContent(radioGroup, tabTexts, R.layout.tab_radio_button);

        //tabUtil.setTabContent(radioGroup, resources, R.layout.tab_radio_button);
    }

    public void setViewPagerIndex(int index) {
        ((RadioButton) radioGroup.getChildAt(index)).setChecked(true);
    }
}
