package com.social.feeling.moontalk.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.adapter.BaseTypeAdapter;
import com.empire.vmd.client.android_lib.util.DateTimeUtil;
import com.empire.vmd.client.android_lib.util.DialogUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Friend;
import com.social.feeling.moontalk.datamodel.PersonData;
import com.social.feeling.moontalk.dialog.SelectFriendsDialog;
import com.social.feeling.moontalk.global.Feelings;
import com.social.feeling.moontalk.global.LoginData;
import com.social.feeling.moontalk.global.PostFeeling;
import com.social.feeling.moontalk.item.FriendItem;
import com.social.feeling.moontalk.util.FriendGroupUtil;

import java.util.ArrayList;
import java.util.List;

public class SharePostActivity extends BaseActivity {
    private TextView tvAdd;
    private TextView tvCommit;
    private TextView tvCancel;
    private ListView lvFriends;
    //private Feelings feelings;
    private List<PersonData> selectedFriendList;
    private PostFeeling postFeeling = PostFeeling.getInstance();
    private LoginData loginData = LoginData.getInstance(SharePostActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);
        findViews();
        //feelings = new Feelings(this);
        tvAdd.setOnClickListener(getTvAddOnClickListener());
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCommit.setOnClickListener(getTvCommitOnClickListener());
        //lvFriends.setOnItemClickListener(getLvFriendsOnItemClickListener());
    }

    private View.OnClickListener getTvCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFriendList != null && selectedFriendList.size() > 0) {
                    postFeeling.feeling.account = loginData.personData.account;
                    postFeeling.feeling.name = loginData.personData.name;
                    postFeeling.feeling.photoUri = loginData.personData.photoUrl;
                    postFeeling.feeling.permissionList = getFriendAccountList();
                    postFeeling.feeling.id = DateTimeUtil.getNowString("yyyyMMdd-HHmmss");
                    Feelings.postFeeling(SharePostActivity.this, postFeeling.feeling);
                    //feelings.postFeeling(postFeeling.feeling);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(SharePostActivity.this, R.string.empty_selected_friends, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private List<String> getFriendAccountList() {
        List<String> result = new ArrayList<String>();

        for (PersonData f : selectedFriendList) {
            result.add(f.account);
        }

        return result;
    }

    private AdapterView.OnItemClickListener getLvFriendsOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SharePostActivity.this);
                String message = String.format(getResources().getString(R.string.sure_delete_member), selectedFriendList.get(position).name);
                final int p = position;

                builder.setMessage(message);
                builder.setPositiveButton(R.string.commit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedFriendList.remove(p);
                        refreshLvMembers(selectedFriendList);
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        };
    }

    private View.OnClickListener getTvAddOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectFriendsDialog.IReceiveCheckedFriendList iReceiveCheckedFriendList = new SelectFriendsDialog.IReceiveCheckedFriendList() {
                    @Override
                    public void getSelectedFriendList(List<PersonData> friendList) {
                        selectedFriendList = (selectedFriendList == null) ? friendList
                                : new FriendGroupUtil(SharePostActivity.this).integrateTwoFriendLists(selectedFriendList, friendList);
                        refreshLvMembers(selectedFriendList);
                    }

                    @Override
                    public Context getContext() {
                        return SharePostActivity.this;
                    }
                };
                SelectFriendsDialog selectFriendsDialog = new SelectFriendsDialog(iReceiveCheckedFriendList);

                new DialogUtil().setHeightAndWidth(SharePostActivity.this, selectFriendsDialog, 0.7, 0.9);
                selectFriendsDialog.show();
            }
        };
    }

    private void findViews() {
        tvAdd = (TextView) findViewById(R.id.tvAdd);
        tvCommit = (TextView) findViewById(R.id.tvCommit);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        lvFriends = (ListView) findViewById(R.id.lvFriends);
    }

    private void refreshLvMembers(List<PersonData> fList) {
        if (fList != null) {
            BaseTypeAdapter.IItemView iItemView = new BaseTypeAdapter.IItemView() {
                @Override
                public View getItemView(int position, View convertView) {
                    return new FriendItem(SharePostActivity.this, selectedFriendList.get(position)
                        , selectedFriendList, getIDeleteMission()).getView();
                }
            };
            lvFriends.setAdapter(new BaseTypeAdapter(iItemView, fList));
        }
    }

    private FriendItem.IDeleteMission getIDeleteMission() {
        return new FriendItem.IDeleteMission() {
            @Override
            public void executeDeleteMission() {
                refreshLvMembers(selectedFriendList);
            }
        };
    }
}
