package com.social.feeling.moontalk.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Friend;
import com.social.feeling.moontalk.datamodel.groupOfFriend;
import com.social.feeling.moontalk.datamodel.PersonData;

import java.util.List;

/**
 * Created by lidondon on 2015/10/6.
 */
public class FriendGroupUtil {
    private Context context;

    public interface ICheckableContainer {
        public void addCheckedItem(Friend friend);
        public void removeCheckedItem(Friend friend);
    }

    public FriendGroupUtil(Context ctx) {
        context = ctx;
    }

    private LayoutInflater getInflater(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getFriendItemCheckableView(final ICheckableContainer icc, final Friend friend) {
        View view = getFriendItemView(friend, R.layout.item_friend_checkable);
        CheckBox cbFriend = (CheckBox) view.findViewById(R.id.checkBox);

        if (context != null) {
            cbFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        icc.addCheckedItem(friend);
                    } else {
                        icc.removeCheckedItem(friend);
                    }
                }
            });
        } else {
            Log.e(this.getClass().getName(), "do not set ICheckableContainer");
        }

        return view;
    }

    public View getFriendItemView(final Friend friend, int layoutResource) {
        View view = getInflater((Context) context).inflate(layoutResource, null);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        TextView tvFriendName = (TextView) view.findViewById(R.id.tvName);

        //ivIcon.setImageResource((friend.imgResource == 0) ? R.drawable.default_friend : friend.imgResource);
        tvFriendName.setText(friend.name);

        return view;
    }

    public void integrateFriendAndGroup(List<PersonData> friendList, groupOfFriend groupOfFriend) {
        integrateTwoFriendLists(friendList, groupOfFriend.friendList);
    }

    public List<PersonData> integrateTwoFriendLists(List<PersonData> fList1, List<PersonData> fList2) {
        if (fList1 != null && fList2 != null) {
            for (PersonData ui2 : fList2) {
                addFriendIfNotExist(fList1, ui2);
            }
        }

        return fList1;
    }

    public void addFriendIfNotExist(List<PersonData> friendList, PersonData friend) {
        if (!friendExist(friendList, friend)) {
            friendList.add(friend);
        }
    }

    public boolean friendExist(List<PersonData> friendList, PersonData friend) {
        boolean result = false;

        for (PersonData f : friendList) {
            if (f.account.equals(friend.account)) {
                result = true;
                break;
            }
        }

        return result;
    }
}
