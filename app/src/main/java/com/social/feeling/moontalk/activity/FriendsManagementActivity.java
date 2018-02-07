package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.adapter.ExpandableListViewAdapter;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.groupOfFriend;
import com.social.feeling.moontalk.datamodel.PersonData;
import com.social.feeling.moontalk.global.FriendAndGroup;
import com.social.feeling.moontalk.item.ElvGroupItem;
import com.social.feeling.moontalk.item.FriendGroupCheckableItem;
import com.social.feeling.moontalk.item.FriendItem;

import java.util.ArrayList;
import java.util.List;

public class FriendsManagementActivity extends Activity implements ExpandableListViewAdapter.ICallBackContext {
    private ExpandableListView elvFriends;
    private List groupList;
    private List<List> childrenList;
    private List<groupOfFriend> groupOfFriendList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_management);
        findViews();
        elvFriends.setAdapter(getElvAdapter());
        openAllList();
    }

    private void findViews() {
        elvFriends = (ExpandableListView) findViewById(R.id.elvFriends);
    }

    private void openAllList() {
        for (int i = 0; i < groupList.size(); i++) {
            elvFriends.expandGroup(i);
        }
    }

    private ExpandableListViewAdapter getElvAdapter() {
        ExpandableListViewAdapter elvAdapter = null;
        FriendAndGroup friendAndGroup = FriendAndGroup.getInstance(this);

        if (friendAndGroup.friendList == null || friendAndGroup.friendList.size() == 0) {
            Toast.makeText(this, R.string.empty_friend, Toast.LENGTH_SHORT).show();
        } else {
            groupList = new ArrayList();
            childrenList = new ArrayList<List>();
            groupList.add("群組 (" + ((friendAndGroup.groupOfFriendList == null) ? 0 : friendAndGroup.groupOfFriendList.size()) + ")");
            groupList.add("好友 (" + ((friendAndGroup.friendList == null) ? 0 : friendAndGroup.friendList.size()) + ")");
            childrenList.add(friendAndGroup.groupOfFriendList);
            childrenList.add(friendAndGroup.friendList);
            elvAdapter = new ExpandableListViewAdapter(this, groupList, childrenList);
        }

        return elvAdapter;
    }

    @Override
    public View getGroupView(int groupPosition) {
        return new ElvGroupItem(this, groupList.get(groupPosition).toString()).getView();
    }

    @Override
    public View getChildView(int groupPosition, int childrenPosition) {
        View resultView;

        if (groupPosition == 0) {
            resultView = new FriendGroupCheckableItem(this, (groupOfFriend) childrenList.get(groupPosition).get(childrenPosition)
                    , groupOfFriendList).getView();
        } else  {
            resultView = new FriendItem(this, (PersonData) childrenList.get(groupPosition).get(childrenPosition)
                    , childrenList.get(groupPosition), null).getView();
        }

        return resultView;
    }
}
