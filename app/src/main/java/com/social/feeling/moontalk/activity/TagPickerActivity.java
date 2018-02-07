package com.social.feeling.moontalk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.activity.BaseFragmentActivity;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.fragment.TagPickerFragment;
import com.social.feeling.moontalk.global.PostFeeling;

public class TagPickerActivity extends BaseFragmentActivity {
    private TagPickerFragment tagsFragment;
    private TextView tvCommit;
    private TextView tvCancel;
    private PostFeeling postFeeling = PostFeeling.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_picker);
        findViews();
        initFlTags();
        tvCommit.setOnClickListener(getTvCommitOnClickListener());
        tvCancel.setOnClickListener(getTvCancelOnClickListener());
    }

    private void findViews() {
        tvCommit = (TextView) findViewById(R.id.tvCommit);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
    }

    private void initFlTags() {
        tagsFragment = new TagPickerFragment(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.flTags, tagsFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        postFeeling.feeling.tagList = null;
//        postFeeling.removeActivity(this);
    }

    private View.OnClickListener getTvCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                postFeeling.feeling.tagList = tagsFragment.getTagList();
//                startActivity(new Intent(TagPickerActivity.this, ColorPicker2Activity.class));
            }
        };
    }

    private View.OnClickListener getTvCancelOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }
}
