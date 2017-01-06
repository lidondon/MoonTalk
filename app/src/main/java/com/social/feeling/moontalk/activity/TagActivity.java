package com.social.feeling.moontalk.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.activity.BaseFragmentActivity;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.fragment.GalleryFragment;
import com.social.feeling.moontalk.fragment.TagPickerFragment;
import com.social.feeling.moontalk.global.PostFeeling;

public class TagActivity extends BaseFragmentActivity {
    private TagPickerFragment tagsFragment;
    private GalleryFragment imagesFragment;
    private TextView tvQuote;
    private TextView tvCommit;
    private TextView tvCancel;
    private PostFeeling postFeeling = PostFeeling.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        postFeeling.addActivity(this);
        findViews();
        initPhotos();
        tvQuote.setText(postFeeling.feeling.quote.currentText);
        initFlTags();
        tvCommit.setOnClickListener(getTvCommitOnClickListener());
        tvCancel.setOnClickListener(getTvCancelOnClickListener());
    }

    private View.OnClickListener getTvCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postFeeling.feeling.tagList = tagsFragment.getTagList();
                startActivity(new Intent(TagActivity.this, PostActivity.class));
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

    private void findViews() {
        tvQuote = (TextView) findViewById(R.id.tvQuote);
        tvCommit = (TextView) findViewById(R.id.tvCommit);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
    }

    private void initPhotos() {
        imagesFragment = new GalleryFragment(this, postFeeling.feeling.checkedPhotoList, GalleryFragment.HORIZONTAL);
        getSupportFragmentManager().beginTransaction().replace(R.id.flPhotos, imagesFragment).commit();
    }

    private void initFlTags() {
        tagsFragment = new TagPickerFragment(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.flTags, tagsFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postFeeling.feeling.tagList = null;
        postFeeling.removeActivity(this);
    }
}
