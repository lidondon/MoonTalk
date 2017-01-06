package com.empire.vmd.client.android_lib.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ghost on 2015/8/21.
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragmentList;

    public TabFragmentPagerAdapter(FragmentManager fm, Context ctx, List<Fragment> fList) {
        super(fm);
        context = ctx;
        fragmentList = fList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }
}
