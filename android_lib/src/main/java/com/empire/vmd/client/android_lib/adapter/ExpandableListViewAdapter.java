package com.empire.vmd.client.android_lib.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * Created by lidondon on 2015/9/27.
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    ICallBackContext context;
    List groupList;
    List<List> childrenList;

    public ExpandableListViewAdapter(ICallBackContext ctx, List gList, List cList) {
        context = ctx;
        groupList = gList;
        childrenList = cList;
    }

    @Override
    public int getGroupCount() {
        return (groupList == null) ? 0 : groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (childrenList.get(groupPosition) == null) ? 0 : childrenList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return (groupList == null) ? null : groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return (childrenList.get(groupPosition) == null) ? null : childrenList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //convertView = (convertView == null) ? context.getGroupView(groupPosition) : convertView;
        convertView = context.getGroupView(groupPosition);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //convertView = (convertView == null) ? context.getChildView(groupPosition, childPosition) : convertView;
        convertView = context.getChildView(groupPosition, childPosition);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface ICallBackContext {
        public View getGroupView(int groupPosition);
        public View getChildView(int groupPosition, int childrenPosition);
    }
}
