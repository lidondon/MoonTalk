package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.dialog.OneEditTextDialog;
import com.empire.vmd.client.android_lib.util.AndroidBuiltInUtil;
import com.empire.vmd.client.android_lib.util.EffectUtil;
import com.empire.vmd.client.android_lib.util.ImageUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.fragment.GalleryFragment;
import com.social.feeling.moontalk.global.PostFeeling;

import java.util.ArrayList;
import java.util.List;

public class PreparePostActivity extends BaseActivity {
    private static final int ACTIVITY_NUM = 0;
    private static final int DURATION = 500;
    private static final int PHOTO_WIDTH = 480;
    private static final int PHOTO_HEIGHT = 480;
    private PostFeeling postFeeling = PostFeeling.getInstance();
    private LinearLayout llPhotos;
    private TextView tvLocalAlbum;
    private TextView tvMoonTalkAlbum;
    private TextView tvCancel;
    private TextView tvPreviousStep;
    private TextView tvGoOn;
    private EffectUtil effectUtil;
    private View defaultPhoto;
    private int photoWidth;
    private HorizontalScrollView hsv;

    private INextMission inmTvMoonTalk = new INextMission() {
        @Override
        public void executeNextMission() {
            effectUtil.activateViewAnimations(tvMoonTalkAlbum, getAnimations(tvMoonTalkAlbum, inmTvCancel), DURATION);
        }
    };

    private INextMission inmTvCancel = new INextMission() {
        @Override
        public void executeNextMission() {
            effectUtil.activateViewAnimations(tvCancel, getAnimations(tvCancel, null), DURATION);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            View v = (View) msg.obj;

            llPhotos.removeView(defaultPhoto);
            v.setOnClickListener(getIvPhotoOnClickListener(v));
            llPhotos.addView(v);
        }
    };

    private View.OnClickListener getIvPhotoOnClickListener(final View v) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeletePhotoDialog(v).show();
            }
        };
    }

    private AlertDialog getDeletePhotoDialog(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PreparePostActivity.this);

        builder.setMessage(R.string.sure_delete_photo);
        builder.setPositiveButton(R.string.commit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                llPhotos.removeView(v);
                if (llPhotos.getChildCount() == 0) {
                    llPhotos.addView(defaultPhoto);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_post);
        postFeeling.addActivity(this);
        findViews();
        effectAppear();
        defaultPhoto = getPhotoImageView(null);
        llPhotos.addView(defaultPhoto);
        tvLocalAlbum.setOnClickListener(getTvLocalAlbumOnClickListener());
        tvCancel.setOnClickListener(getFinishListener());
        tvGoOn.setOnClickListener(getTvGoOnListener());
        tvPreviousStep.setOnClickListener(getFinishListener());
    }

    private View.OnClickListener getFinishListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    private View.OnClickListener getTvGoOnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PreparePostActivity.this, ColorPicker2Activity.class));
            }
        };
    }

//    private View.OnClickListener getTvPreviousStepListener() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        };
//    }

    private void effectAppear() {
        effectUtil = new EffectUtil();
        //effectUtil.activateViewAnimations(tvLocalAlbum, getAnimations(tvLocalAlbum, inmTvMoonTalk), DURATION);
        effectUtil.activateViewAnimations(tvLocalAlbum, getAnimations(tvLocalAlbum, null), DURATION * 2);
        effectUtil.activateViewAnimations(tvMoonTalkAlbum, getAnimations(tvMoonTalkAlbum, null), DURATION * 3);
        effectUtil.activateViewAnimations(tvCancel, getAnimations(tvCancel, null), DURATION * 4);
    }

    private void findViews() {
        llPhotos = (LinearLayout) findViewById(R.id.llPhotos);
        tvLocalAlbum = (TextView) findViewById(R.id.tvLocalAlbum);
        tvMoonTalkAlbum = (TextView) findViewById(R.id.tvMoonTalkAlbum);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvGoOn = (TextView) findViewById(R.id.tvGoOn);
        tvPreviousStep = (TextView) findViewById(R.id.tvPreviousStep);
        hsv = (HorizontalScrollView) findViewById(R.id.hsv);
    }

    private View.OnClickListener getTvLocalAlbumOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PreparePostActivity.this, PhotoPickerActivity.class), ACTIVITY_NUM);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_NUM && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            final List<String> newCheckedPhotoList = bundle.getStringArrayList(PhotoPickerActivity.CHECKED_PHOTO_LIST);

            postFeeling.feeling.checkedPhotoList = (postFeeling.feeling.checkedPhotoList == null) ? new ArrayList<String>()
                    : postFeeling.feeling.checkedPhotoList;
            if (newCheckedPhotoList != null && newCheckedPhotoList.size() > 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (String uri : newCheckedPhotoList) {
                            addCheckedPhotos(uri);
                        }
                    }
                }).start();
                postFeeling.feeling.checkedPhotoList.addAll(newCheckedPhotoList);
            } else {
                //這邊是不是從相簿存取而是用拍照的
                String uri = bundle.getString(PhotoPickerActivity.CAMERA_URI);

                if (uri != null && !uri.isEmpty()) {
                    addCheckedPhotos(uri);
                    postFeeling.feeling.checkedPhotoList.add(uri);
                }
            }
        }
    }

    private void addCheckedPhotos(String uri) {
        Message msg = Message.obtain();
        View iv = getPhotoImageView(uri);

        msg.obj = iv;
        handler.sendMessage(msg);
    }

    private View getPhotoImageView(String uri) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View result = (View) inflater.inflate(R.layout.item_gallery, null);
//        ImageView iv = (ImageView) result.findViewById(R.id.ivPhoto);
        ImageView result = new ImageView(PreparePostActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        //int screenWidth = new AndroidBuiltInUtil(this).getScreenWidth();

        result.setPadding(10, 10, 10, 10);
        photoWidth = (photoWidth == 0) ? getWindowManager().getDefaultDisplay().getWidth() : photoWidth;
        params.width = photoWidth;
        result.setLayoutParams(params);
        if (uri == null) {
            result.setImageResource(R.drawable.default_photo);
            //params.width = new AndroidBuiltInUtil(this).getScreenWidth();
        } else {
            result.setImageBitmap(ImageUtil.getThumbnailBitmap(uri, PHOTO_WIDTH, PHOTO_HEIGHT));
        }
        result.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return result;
    }

    private Animation[] getAnimations(final View view, final INextMission iNextMission) {
        Animation[] result = new Animation[2];

        result[0] = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.6f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f);
        result[1] = new AlphaAnimation(0, 1);
        result[1].setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (iNextMission != null) {
                    iNextMission.executeNextMission();
                }
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return result;
    }

    private interface INextMission {
        public void executeNextMission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postFeeling.clear();
    }
}
