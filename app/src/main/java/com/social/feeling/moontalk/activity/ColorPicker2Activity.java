package com.social.feeling.moontalk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.extendview.PowerImageView;
import com.empire.vmd.client.android_lib.util.EffectUtil;
import com.empire.vmd.client.android_lib.util.ImageUtil;
import com.empire.vmd.client.android_lib.util.OtherUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.global.Colors;
import com.social.feeling.moontalk.global.PostFeeling;

import java.util.List;

public class ColorPicker2Activity extends AppCompatActivity {
    private int colorItemWidth = 150;
    private int colorItemHeight = 150;
    private PostFeeling postFeeling = PostFeeling.getInstance();
    private TextView tvCommit;
    private TextView tvCancel;
    private Animation[] animations;
    private int imageIndex;
//    private OtherUtil otherUtil;
//    private EffectUtil effectUtil;
    private boolean switching;
    private Colors colors;
    private List<String> checkedPhotoList;
    private ImageView ivShownColor;
    private LinearLayout llColors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker2);
        postFeeling.addActivity(this);
        findViews();
        initUtils();
        ivShownColor.setImageResource(colors.colorObjectList.get(0).colorImageResource);
        tvCommit.setOnClickListener(getTvCommitListener());
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initUtils() {
        colors = Colors.getInstance();
//        otherUtil = new OtherUtil();
//        effectUtil = new EffectUtil();
        initLlColors();
    }

//    private View.OnClickListener getTvCommitListener() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ColorPickerActivity.this, QuotePickerActivity.class);
//                postFeeling.feeling.checkedColorId = colors.colorObjectList.get(imageIndex).id;
//                startActivity(intent);
//            }
//        };
//    }

    private void findViews() {
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCommit = (TextView) findViewById(R.id.tvCommit);
        llColors = (LinearLayout) findViewById(R.id.llColors);
        ivShownColor = (ImageView) findViewById(R.id.ivShownColor);
    }

    private void initLlColors() {
        if (colors != null) {
            for (int i = 0; i < colors.colorObjectList.size(); i++) {
                final int position = i;
                ImageView iv = getPhotoImageView(colors.colorObjectList.get(position).colorImageResource);

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivShownColor.setImageResource(colors.colorObjectList.get(position).colorImageResource);
                        imageIndex = position;
                    }
                });
                llColors.addView(iv);
            }
        }
    }

    private ImageView getPhotoImageView(int colorResource) {
        ImageView result = new ImageView(ColorPicker2Activity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);

        result.setPadding(5, 5, 5, 5);
        params.width = colorItemWidth;
        params.height = colorItemHeight;
        result.setLayoutParams(params);
        result.setImageResource(colorResource);
//        if (uri == null) {
//            result.setImageResource(R.drawable.default_photo);
//        } else {
//            result.setImageBitmap(ImageUtil.getThumbnailBitmap(uri, colorItemWidth, colorItemHeight));
//        }
        result.setScaleType(ImageView.ScaleType.FIT_XY);

        return result;
    }

    private View.OnClickListener getTvCommitListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColorPicker2Activity.this, QuotePickerActivity.class);
                postFeeling.feeling.checkedColorId = colors.colorObjectList.get(imageIndex).id;
                startActivity(intent);
            }
        };
    }
}
