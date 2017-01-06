package com.social.feeling.moontalk.global;

import android.content.Context;

import com.social.feeling.moontalk.datamodel.Friend;
import com.social.feeling.moontalk.datamodel.FriendGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2016/8/22.
 */
public class FriendAndGroup {
    private volatile static FriendAndGroup friendAndGroup;
    private Context context;
    public List<Friend> friendList = new ArrayList<Friend>();;
    public List<FriendGroup> friendGroupList = new ArrayList<FriendGroup>();

    private FriendAndGroup(Context ctx) {
        context = ctx;
        makeFakeFriends();
        makeFakeGroup();
    }

    public static FriendAndGroup getInstance(Context ctx) {
        if (friendAndGroup == null) {
            synchronized (LoginData.class) {
                if (friendAndGroup == null) {
                    friendAndGroup = new FriendAndGroup(ctx);
                }
            }
        }

        return friendAndGroup;
    }

    public void makeFakeFriends() {
        Friend f1 = new Friend();
        Friend f2 = new Friend();
        Friend f3 = new Friend();
        Friend f4 = new Friend();
        Friend f5 = new Friend();
        Friend f6 = new Friend();
        Friend f7 = new Friend();

        f1.account = "Jean";
        f2.account = "Michael";
        f3.account = "Vincent";
        f4.account = "Sister";
        f5.account = "Lai";
        f6.account = "Coco";
        f7.account = "Dodo";
        f1.name = "小林";
        f2.name = "大林";
        f3.name = "小Ｖ";
        f4.name = "姊姊";
        f5.name = "BOSS";
        f6.name = "可可";
        f7.name = "多多";
        friendList.add(f1);
        friendList.add(f2);
        friendList.add(f3);
        friendList.add(f4);
        friendList.add(f5);
        friendList.add(f6);
        friendList.add(f7);
    }

    private void makeFakeGroup() {
        FriendGroup g1 = new FriendGroup();
        FriendGroup g2 = new FriendGroup();
        FriendGroup g3 = new FriendGroup();

        g1.id = "Lin";
        g2.id = "Family";
        g3.id = "Dog";
        g1.name = "林";
        g2.name = "林家宗親";
        g3.name = "狗狗團";
        g1.friendList = new ArrayList<Friend>();
        g1.friendList.add(friendList.get(0));
        g1.friendList.add(friendList.get(1));
        g2.friendList = new ArrayList<Friend>();
        g2.friendList.add(friendList.get(0));
        g2.friendList.add(friendList.get(1));
        g2.friendList.add(friendList.get(2));
        g2.friendList.add(friendList.get(3));
        g2.friendList.add(friendList.get(4));
        g3.friendList = new ArrayList<Friend>();
        g3.friendList.add(friendList.get(5));
        g3.friendList.add(friendList.get(6));
        friendGroupList.add(g1);
        friendGroupList.add(g2);
        friendGroupList.add(g3);
    }

}
