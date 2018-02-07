package com.social.feeling.moontalk.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.empire.vmd.client.android_lib.activity.BaseFragmentActivity;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.drawer.LeftDrawer;
import com.social.feeling.moontalk.fragment.FeelingPostFragment;
import com.social.feeling.moontalk.fragment.MoreFragment;
import com.social.feeling.moontalk.fragment.NewFragment;
import com.social.feeling.moontalk.fragment.PersonalFragment;
import com.social.feeling.moontalk.global.LoginData;
import com.social.feeling.moontalk.global.MainController;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity {
    //private static boolean feelingPostInitiated;
    //private TabUtil tabUtil = new TabUtil(this);
    //private Class[] fragmentClassArr = {FeelingPostFragment.class, NewFragment.class, PersonalFragment.class, NewFragment.class};
    //private int[] tabItemResource = {R.drawable.feeling_grey, R.drawable.chatting_grey, R.drawable.more_grey, R.drawable.activity_grey};
    private int[] tabItemResource = {R.drawable.tab_feeling_selector, R.drawable.tab_chatting_selector
            , R.drawable.tab_shopping_selector, R.drawable.tab_activity_selector, R.drawable.tab_more_selector};
    //private int[] tabItemSelectedResource = {R.drawable.feeling, R.drawable.chatting, R.drawable.more, R.drawable.activity};
    private int keepPageCount = 4;
    public List<Fragment> fragmentList;
    protected ViewPager viewPager;
    private FragmentTabHost fragmentTabHost;
    private LayoutInflater inflater;
    public DrawerLayout drawerLayout;
    private RelativeLayout rlDrawer;

    private FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        MainController.getInstance(this);
        initFragmentTabHost();
        bindViewPagerAndTab();
        initLeftDrawer();
//        initTabFunction();
//        tabUtil.bindTabAndPager(radioGroup, viewPager);
    }

    private void findViews() {
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewPager = (ViewPager) findViewById(R.id.pager);
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        rlDrawer = (RelativeLayout) findViewById(R.id.rlDrawer);
    }

//    public void setFeelingPostInitiated(boolean initiated) {
//        feelingPostInitiated = initiated;
//    }
//
//    public boolean getFeelingPostInitiatedState() {
//        return feelingPostInitiated;
//    }

//    private void initTabFunction() {
//        getFragments();
//        viewPager.setAdapter(new TabFragmentPagerAdapter(MainActivity.this.getSupportFragmentManager(), this, fragmentList));
//        setTabContent();
//        tabUtil.bindTabAndPager(radioGroup, viewPager);
//    }

    private void initFragmentTabHost() {
        getFragments();
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.pager);
        for (int i = 0; i < tabItemResource.length; i++) {
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(i + "").setIndicator(getTabItemView(i));

            fragmentTabHost.addTab(tabSpec, fragmentList.get(i).getClass(), null);
            //fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(tabItemSelectedResource[i]);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(keepPageCount);
    }

    private View getTabItemView(int index) {
        View resultView = inflater.inflate(R.layout.item_tab, null);
        ImageView iv = (ImageView) resultView.findViewById(R.id.iv);

        iv.setImageResource(tabItemResource[index]);

        return resultView;
    }

    private void getFragments() {
        Log.e("AAA", "add fragment");
        fragmentList = (fragmentList == null) ? new ArrayList<Fragment>() : fragmentList;
        fragmentList.add(new FeelingPostFragment(this));
        fragmentList.add(new NewFragment(this));
        fragmentList.add(new PersonalFragment(LoginData.getInstance(this).personData));
        fragmentList.add(new NewFragment(this));
        fragmentList.add(new MoreFragment());
    }

//    private PersonalFragment getPersonalFragment() {
//        PersonalFragment result = new PersonalFragment();
//        Bundle bundle = new Bundle();
//
//        bundle.putSerializable(PersonalFragment.PERSONAL_DATA, LoginData.getInstance(this).personData);
//        result.setArguments(bundle);
//
//        return result;
//    }

    private void bindViewPagerAndTab() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragmentTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                viewPager.setCurrentItem(fragmentTabHost.getCurrentTab());
            }
        });
    }

    private void initLeftDrawer() {
        rlDrawer.addView(new LeftDrawer(this, drawerLayout).getView());
    }

    public void setViewPagerIndex(int index) {
        //((RadioButton) radioGroup.getChildAt(index)).setChecked(true);
        viewPager.setCurrentItem(index);
    }
}
