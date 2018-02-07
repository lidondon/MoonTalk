package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.adapter.BaseTypeAdapter;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk._interface.IView;
import com.social.feeling.moontalk.global.LoginData;
import com.social.feeling.moontalk.item.SettingsItem;
import com.social.feeling.moontalk.item.SettingsSwitchItem;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity {
    private ListView lvSettings;
    private ImageView ivBack;
    private List<IView> settingsItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        settingsItemList = new ArrayList<IView>();
        settingsItemList.add(getInformItem());
        settingsItemList.add(getSaftyAndSecretItem());
        settingsItemList.add(getAboutFeelingItem());
        settingsItemList.add(getAutoLoginItem());
    }

    private SettingsSwitchItem getAutoLoginItem() {
        SettingsSwitchItem.ISwitch iSwitch = new SettingsSwitchItem.ISwitch() {
            @Override
            public void on() {
                Toast.makeText(SettingsActivity.this, R.string.auto_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void off() {
                Toast.makeText(SettingsActivity.this, "關閉自動登入", Toast.LENGTH_SHORT).show();
            }
        };

        return new SettingsSwitchItem(this, getResources().getString(R.string.auto_login), iSwitch);
    }

    private SettingsItem getInformItem() {
        return new SettingsItem(this, getResources().getString(R.string.inform), null);
    }

    private SettingsItem getSaftyAndSecretItem() {
        return new SettingsItem(this, getResources().getString(R.string.safety_and_secret), null);
    }

    private SettingsItem getAboutFeelingItem() {
        return new SettingsItem(this, getResources().getString(R.string.about_feeling), null);
    }
}
