package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.adapter.BaseTypeAdapter;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.PersonData;
import com.social.feeling.moontalk.item.SettingsItem;

import java.util.ArrayList;
import java.util.List;

public class PersonDataActivity extends Activity {
    private ListView lvSettings;
    private ImageView ivBack;
    private List<SettingsItem> settingsItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_data);
        findViews();
        initSettingsItemList();
        initLvSettings();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViews() {
        lvSettings = (ListView) findViewById(R.id.lvSettings);
        ivBack = (ImageView) findViewById(R.id.ivBack);
    }

    private void initLvSettings() {
        BaseTypeAdapter.IItemView iItemView = new BaseTypeAdapter.IItemView() {
            @Override
            public View getItemView(int position, View convertView) {
                if (convertView != null) {
                    convertView.getDrawingCache().recycle();
                }
                convertView = settingsItemList.get(position).getView();

                return convertView;
            }
        };
        BaseTypeAdapter adapter = new BaseTypeAdapter(iItemView, settingsItemList);

        lvSettings.setAdapter(adapter);
    }

    private void initSettingsItemList() {
        settingsItemList = new ArrayList<SettingsItem>();
        settingsItemList.add(getAccountItem());
        settingsItemList.add(getPhoneNumberItem());
        settingsItemList.add(getEmailItem());
    }

    private SettingsItem getAccountItem() {
        SettingsItem.IMission iMission = new SettingsItem.IMission() {
            @Override
            public void executeMission() {
                Toast.makeText(PersonDataActivity.this, "你點了’帳戶名‘", Toast.LENGTH_SHORT).show();
            }
        };

        return new SettingsItem(this, getResources().getString(R.string.account_name)
                , PersonData.NAME, iMission);
    }

    private SettingsItem getPhoneNumberItem() {
        SettingsItem.IMission iMission = new SettingsItem.IMission() {
            @Override
            public void executeMission() {
                Toast.makeText(PersonDataActivity.this, "你點了’電話號碼‘", Toast.LENGTH_SHORT).show();
            }
        };

        return new SettingsItem(this, getResources().getString(R.string.phone_number), iMission);
    }

    private SettingsItem getEmailItem() {
        SettingsItem.IMission iMission = new SettingsItem.IMission() {
            @Override
            public void executeMission() {
                Toast.makeText(PersonDataActivity.this, "你點了’電子信箱‘", Toast.LENGTH_SHORT).show();
            }
        };

        return new SettingsItem(this, getResources().getString(R.string.email), iMission);
    }
}
