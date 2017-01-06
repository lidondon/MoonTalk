package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.content.pm.LabeledIntent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.activity.BaseFragmentActivity;
import com.empire.vmd.client.android_lib.util.ImageUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Feeling;
import com.social.feeling.moontalk.fragment.GalleryFragment;
import com.social.feeling.moontalk.fragment.TagsFragment;
import com.social.feeling.moontalk.global.Colors;

public class FeelingDetailActivity extends BaseFragmentActivity {
    private static final int PHOTO_WIDTH = 120;
    private static final int PHOTO_HEIGHT = 120;
    private Feeling feeling;
    private ImageView ivPhoto;
    private TextView tvName;
    private TextView tvQuote;
    private TextView tvThought;
    //private ImageView ivColor;
    private TagsFragment tagsFragment;
    private GalleryFragment galleryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_detail);
        findViews();
        getExtrasData();
        initLlPerson();
        initPhotos();
        //ivColor.setImageResource(Colors.getInstance().getColorResource(feeling.checkedColorId));
        tvQuote.setText(feeling.quote.currentText);
        initFlTags();
        tvThought.setText(feeling.thought);
    }

    private void getExtrasData() {
        feeling = (Feeling) getIntent().getSerializableExtra(Feeling.class.getName());
    }

    private void findViews() {
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        tvName = (TextView) findViewById(R.id.tvName);
        tvQuote = (TextView) findViewById(R.id.tvQuote);
        tvThought = (TextView) findViewById(R.id.tvThought);
        //ivColor = (ImageView) findViewById(R.id.ivColor);
        //flTags = (FrameLayout) findViewById(R.id.flTags);
    }

    private void initLlPerson() {
        Bitmap bitmap = (feeling.photoUri != null) ? ImageUtil.getThumbnailBitmap(feeling.photoUri, PHOTO_WIDTH, PHOTO_HEIGHT)
                : null;

        if (bitmap != null) {
            ivPhoto.setImageBitmap(bitmap);
        } else {
            ivPhoto.setImageResource(R.drawable.no_man);
        }
        tvName.setText(feeling.name);
    }

    private void initPhotos() {
        galleryFragment = new GalleryFragment(this, feeling.checkedPhotoList, GalleryFragment.VERTICAL);
        getSupportFragmentManager().beginTransaction().replace(R.id.flPhotos, galleryFragment).commit();
    }

    private void initFlTags() {
        tagsFragment = new TagsFragment(this, feeling.tagList, Colors.getInstance().getColorResource(feeling.checkedColorId));
        getSupportFragmentManager().beginTransaction().replace(R.id.flTags, tagsFragment).commit();
    }
}
