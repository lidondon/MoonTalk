package com.social.feeling.moontalk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.adapter.ExpandableListViewAdapter;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Friend;
import com.social.feeling.moontalk.datamodel.FriendGroup;
import com.social.feeling.moontalk.global.FriendAndGroup;
import com.social.feeling.moontalk.item.ElvGroupItem;
import com.social.feeling.moontalk.item.FriendCheckableItem;
import com.social.feeling.moontalk.item.FriendGroupCheckableItem;
import com.social.feeling.moontalk.util.FriendGroupUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2015/11/25.
 */
public class SelectFriendsDialog extends Dialog implements ExpandableListViewAdapter.ICallBackContext {
    private Context context;
    private IReceiveCheckedFriendList iReceiveCheckedFriendList;
    private Button btnCommit;
    private Button btnCancel;
    private ExpandableListView elvFriends;
    private List groupList;
    private List<List> childrenList;
    private List<Friend> friendSelectedList = new ArrayList<Friend>();
    private List<FriendGroup> friendGroupList = new ArrayList<FriendGroup>();

    public interface IReceiveCheckedFriendList {
        public void getSelectedFriendList(List<Friend> friendList);
        public Context getContext();
    }

    public SelectFriendsDialog(IReceiveCheckedFriendList ircfList) {
        super(ircfList.getContext());
        context = ircfList.getContext();
        iReceiveCheckedFriendList = ircfList;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_order_members);
        getViews();
        elvFriends.setAdapter(getElvAdapter());
        elvFriends.expandGroup(groupList.size() - 1); //打開最後一個群組
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendGroupUtil friendGroupUtil = new FriendGroupUtil(context);

                for (FriendGroup fgi : friendGroupList) {
                    friendGroupUtil.integrateFriendAndGroup(friendSelectedList, fgi);
                }
                iReceiveCheckedFriendList.getSelectedFriendList(friendSelectedList);
                dismiss();
            }
        });
    }

    private ExpandableListViewAdapter getElvAdapter() {
        ExpandableListViewAdapter elvAdapter = null;
        FriendAndGroup friendAndGroup = FriendAndGroup.getInstance(context);

        if (friendAndGroup.friendList == null || friendAndGroup.friendList.size() == 0) {
            Toast.makeText(context, R.string.empty_friend, Toast.LENGTH_SHORT).show();
            dismiss();
        } else {
            groupList = new ArrayList();
            childrenList = new ArrayList<List>();
            groupList.add("群組 (" + ((friendAndGroup.friendGroupList == null) ? 0 : friendAndGroup.friendGroupList.size()) + ")");
            groupList.add("好友 (" + ((friendAndGroup.friendList == null) ? 0 : friendAndGroup.friendList.size()) + ")");
            childrenList.add(friendAndGroup.friendGroupList);
            childrenList.add(friendAndGroup.friendList);
            elvAdapter = new ExpandableListViewAdapter(this, groupList, childrenList);
        }

        return elvAdapter;
    }

    private void getViews() {
        btnCommit = (Button) findViewById(R.id.btnCommit);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        elvFriends = (ExpandableListView) findViewById(R.id.elvFriends);
    }

    @Override
    public View getGroupView(int groupPosition) {
        return new ElvGroupItem(context, groupList.get(groupPosition).toString()).getView();
    }

    @Override
    public View getChildView(int groupPosition, int childrenPosition) {
        View resultView = null;

        if (groupPosition == 0) {
            resultView = new FriendGroupCheckableItem(context, (FriendGroup) childrenList.get(groupPosition).get(childrenPosition)
                    , friendGroupList).getView();
        } else {
            resultView = new FriendCheckableItem(context, (Friend) childrenList.get(groupPosition).get(childrenPosition)
                    , friendSelectedList).getView();
        }

        return resultView;
    }
}