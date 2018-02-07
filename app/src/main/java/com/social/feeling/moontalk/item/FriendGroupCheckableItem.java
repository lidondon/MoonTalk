package com.social.feeling.moontalk.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.groupOfFriend;

import java.util.List;

/**
 * Created by lidondon on 2015/11/26.
 */
public class FriendGroupCheckableItem {
    private Context context;
    private groupOfFriend groupOfFriend;
    private List<groupOfFriend> groupOfFriendList;
    private ImageView ivIcon;
    private TextView tvName;
    private CheckBox cbFriend;

    public FriendGroupCheckableItem(Context ctx, groupOfFriend fg, List<groupOfFriend> fgList) {
        context = ctx;
        groupOfFriend = fg;
        groupOfFriendList = fgList;
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
                if (groupOfFriendList != null) {
                    if (isChecked) {
                        groupOfFriendList.add(groupOfFriend);
                    } else {
                        groupOfFriendList.remove(groupOfFriend);
                    }
                }
            }
        });

        return resultView;
    }

    private void setContent() {
        if (groupOfFriend != null) {
            ivIcon.setImageResource(R.drawable.default_group);
            tvName.setText(groupOfFriend.name);
        }
    }

    private void getViews(View rootView) {
        ivIcon = (ImageView) rootView.findViewById(R.id.ivIcon);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        cbFriend = (CheckBox) rootView.findViewById(R.id.checkBox);
    }
}
