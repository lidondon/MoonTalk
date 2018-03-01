package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.social.feeling.moontalk.item.RecipientItem;

import java.util.ArrayList;
import java.util.List;

public class FriendsManagementActivity extends Activity implements ExpandableListViewAdapter.ICallBackContext {
    private ExpandableListView elvFriends;
    private List groupList;
    private List<List> childrenList;
    private List<groupOfFriend> groupOfFriendList = new ArrayList<>();
    private FriendAndGroup friendAndGroup = FriendAndGroup.getInstance(this);

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

        //if (friendAndGroup.friendList == null || friendAndGroup.friendList.size() == 0) {
        if (false) {
            Toast.makeText(this, R.string.empty_friend, Toast.LENGTH_SHORT).show();
        } else {
            groupList = new ArrayList();
            childrenList = new ArrayList<List>();
//            groupList.add("群組 (" + ((friendAndGroup.groupOfFriendList == null) ? 0 : friendAndGroup.groupOfFriendList.size()) + ")");
//            groupList.add("好友 (" + ((friendAndGroup.friendList == null) ? 0 : friendAndGroup.friendList.size()) + ")");
//            childrenList.add(friendAndGroup.groupOfFriendList);
//            childrenList.add(friendAndGroup.friendList);
            for (FriendAndGroup.ListInfo listInfo : friendAndGroup.listOfList) {
                if (listInfo.list != null && listInfo.list.size() > 0) {
                    groupList.add(listInfo.name);
                    childrenList.add(listInfo.list);
                }
            }
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
        String groupName = groupList.get(groupPosition).toString();
        PersonData personData = (PersonData) childrenList.get(groupPosition).get(childrenPosition);

//        if (groupPosition == 0) {
//            resultView = new FriendGroupCheckableItem(this, (groupOfFriend) childrenList.get(groupPosition).get(childrenPosition)
//                    , groupOfFriendList).getView();
//        } else  {
//            resultView = new FriendItem(this, (PersonData) childrenList.get(groupPosition).get(childrenPosition)
//                    , childrenList.get(groupPosition), null).getView();
//        }
        if (groupName.equals(getResources().getString(R.string.request))) {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    elvFriends.setAdapter(getElvAdapter());
                    openAllList();
                }
            };

            resultView = new RecipientItem(this, personData, handler).getView();
        } else {
            resultView = new FriendItem(this, personData, childrenList.get(groupPosition), null).getView();
        }

        return resultView;
    }
}
