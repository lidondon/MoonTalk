package com.social.feeling.moontalk.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Friend;
import com.social.feeling.moontalk.datamodel.PersonData;

import java.util.List;

/**
 * Created by lidondon on 2015/11/26.
 */
public class FriendCheckableItem {
    private Context context;
    private PersonData friend;
    private List<PersonData> friendList;
    private ImageView ivIcon;
    private TextView tvName;
    private CheckBox cbFriend;

    public FriendCheckableItem(Context ctx, PersonData f, List<PersonData> fList) {
        context = ctx;
        friend = f;
        friendList = fList;
    }

    private LayoutInflater getInflater() {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        View resultView = getInflater().inflate(R.layout.item_friend_checkable, null);

        getViews(resultView);
        setContent();
        cbFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (friendList != null) {
                    if (isChecked) {
                        friendList.add(friend);
                    } else {
                        friendList.remove(friend);
                    }
                }
            }
        });

        return resultView;
    }

    private void setContent() {
        if (friend != null) {
            if (friend.photoUrl == null) {
                ivIcon.setImageResource(R.drawable.no_man);
            } else {

            }
            tvName.setText(friend.name);
        }
    }

    private void getViews(View rootView) {
        ivIcon = (ImageView) rootView.findViewById(R.id.ivIcon);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        cbFriend = (CheckBox) rootView.findViewById(R.id.checkBox);
    }
}
