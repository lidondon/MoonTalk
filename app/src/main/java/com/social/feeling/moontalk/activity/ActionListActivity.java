package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.empire.vmd.client.android_lib.adapter.BaseTypeAdapter;
import com.empire.vmd.client.android_lib.item.BaseListViewItem;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Action;
import com.social.feeling.moontalk.item.NewFeelingItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActionListActivity extends AppCompatActivity {
    private static final int PHOTO_PICKER_ACTIVITY_NUM = 0;
    private ListView lvAction;
    private List<Action> actionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_list);
        findViews();
        actionList = Action.getFakeActionList();
        sortActionListByDate();
        initLvAction();
    }

    private void initLvAction() {
        if (actionList != null) {
            BaseTypeAdapter.IItemView iItemView = new BaseTypeAdapter.IItemView() {
                @Override
                public View getItemView(int position, View convertView) {
                    Action action = actionList.get(position);

                    if (convertView != null) {
                        BaseListViewItem item = (BaseListViewItem) convertView.getTag();
                        item.setContent(R.drawable.color_in_eye, action.title, action.location);
                    } else {
                        BaseListViewItem item = new BaseListViewItem(ActionListActivity.this
                                , R.drawable.color_in_eye, action.title, action.location);

                        convertView = item.getView();
                        convertView.setTag(item);
                    }

                    return convertView;
                }
            };
            BaseTypeAdapter adapter = new BaseTypeAdapter(iItemView, actionList);

            lvAction.setAdapter(adapter);
            lvAction.setOnItemClickListener(getLvActionOnItemClickListener());
        }
    }

    private void findViews() {
        lvAction = (ListView) findViewById(R.id.lvAction);
    }

    private void sortActionListByDate() {
        Collections.sort(actionList, new Comparator<Action>() {
            public int compare(Action o1, Action o2) {
                return o1.date.compareTo(o2.date);
            }
        });
    }

    private AdapterView.OnItemClickListener getLvActionOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ActionListActivity.this, PhotoPickerActivity.class);
                Bundle bundle = new Bundle();

                bundle.putBoolean(PhotoPickerActivity.HAS_CAMERA, false);
                intent.putExtras(bundle);
                startActivityForResult(intent, PHOTO_PICKER_ACTIVITY_NUM);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PHOTO_PICKER_ACTIVITY_NUM) {
            Intent intent = new Intent();

            intent.putExtras(data.getExtras());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
