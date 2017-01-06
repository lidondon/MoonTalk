package com.social.feeling.moontalk.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.FriendGroup;

import java.util.List;

/**
 * Created by lidondon on 2015/11/26.
 */
public class FriendGroupCheckableItem {
    private Context context;
    private FriendGroup friendGroup;
    private List<FriendGroup> friendGroupList;
    private ImageView ivIcon;
    private TextView tvName;
    private CheckBox cbFriend;

    public FriendGroupCheckableItem(Context ctx, FriendGroup fg, List<FriendGroup> fgList) {
        context = ctx;
        friendGroup = fg;
        friendGroupList = fgList;
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
                if (friendGroupList != null) {
                    if (isChecked) {
                        friendGroupList.add(friendGroup);
                    } else {
                        friendGroupList.remove(friendGroup);
                    }
                }
            }
        });

        return resultView;
    }

    private void setContent() {
        if (friendGroup != null) {
            ivIcon.setImageResource(R.drawable.default_group);
            tvName.setText(friendGroup.name);
        }
    }

    private void getViews(View rootView) {
        ivIcon = (ImageView) rootView.findViewById(R.id.ivIcon);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        cbFriend = (CheckBox) rootView.findViewById(R.id.checkBox);
    }
}
