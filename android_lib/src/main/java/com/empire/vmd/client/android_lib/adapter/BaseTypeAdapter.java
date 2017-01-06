package com.empire.vmd.client.android_lib.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by lidondon on 2015/9/21.
 */
public class BaseTypeAdapter extends BaseAdapter {
    private boolean isCycle;
    private IItemView iItemView;
    private List objectList;
    private int maxNum;

    public BaseTypeAdapter(IItemView icbc, List objList) {
        iItemView = icbc;
        objectList = objList;
    }

    public BaseTypeAdapter(IItemView icbc, List objList, boolean ic) {
        iItemView = icbc;
        objectList = objList;
        isCycle = ic;
        maxNum = Integer.MAX_VALUE;
    }

    public BaseTypeAdapter(IItemView icbc, List objList, boolean ic, int max) {
        iItemView = icbc;
        objectList = objList;
        isCycle = ic;
        maxNum = max;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return objectList.get(position);
    }

    @Override
    public int getCount() {
        return (isCycle) ? maxNum : objectList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = iItemView.getItemView(position, convertView);

        return convertView;
    }

    public interface IItemView {
        public View getItemView(int position, View convertView);
    }
}
