package com.social.feeling.moontalk.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.util.FileUtil;
import com.empire.vmd.client.android_lib.util.ImageUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.activity.TestServerActivity;
import com.social.feeling.moontalk.datamodel.PersonData;
import com.social.feeling.moontalk.global.FileConfig;
import com.social.feeling.moontalk.global.LoginData;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lidondon on 2016/10/13.
 */
public class PersonalFragment extends Fragment {
    public static final String TAG = "PersonalFragment";
    public static final String PORTRAIT_FILE_NAME = "portrait.png";
    public static final int SAVED = 0;
    public static final int PERSONAL = 1;
    public static final int PICK_PHOTO_CODE = 1;
    private Context context;
    private int type;
    private ImageView ivPortrait;
    private TextView tvName;
    private TextView tvAccount;
    private TextView tvLiveIn;
    private TextView tvPersonalFeeling;
    private TextView tvSavedFeeling;
    private LinearLayout llFeelings;
    //private LinearLayout llReview;
    private PersonData personData;
    private FeelingListFragment feelingListFragment;
    private int firstVI;
    private int lastFirstVI;
    private ListView lvFeeling;
    private LinearLayout llSettings;
    private LinearLayout llTrace;
    private LinearLayout llAddFriend;

//    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
////            if (scrollState == SCROLL_STATE_IDLE && firstVI == 0 && llReview.getVisibility() == View.GONE) {
////                if (firstVI != lastFirstVI) {
////                    llReview.setVisibility(View.VISIBLE);
////                }
////                Log.e("AAA", "C  last: " + lastFirstVI + "', first: " + firstVI);
////            } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
////                if (llReview.getVisibility() == View.VISIBLE) {
////                    llReview.setVisibility(View.GONE);
////                    Log.e("AAA", "S last: " + lastFirstVI + "', first: " + firstVI);
////                }
////            }
////            lastFirstVI = firstVI; //因為一個feeling高度頗高，有時候往下滑了一下但開頭還是在第一個item
//            if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
//                Log.e("AAA", "SCROLL_STATE_TOUCH_SCROLL");
//            } else if (scrollState == SCROLL_STATE_FLING) {
//                Log.e("AAA", "SCROLL_STATE_FLING");
//            } else {
//                Log.e("AAA", "SCROLL_STATE_IDLE");
//                if (view.getFirstVisiblePosition() == 0) {
//                    llReview.setVisibility(View.VISIBLE);
//                } else {
//                    llReview.setVisibility(View.GONE);
//                }
//            }
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            firstVI = firstVisibleItem;
//        }
//    };

    public PersonalFragment() {}

    @SuppressLint("ValidFragment")
    public PersonalFragment(PersonData pd) {
        personData = pd;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView =  inflater.inflate(R.layout.fragment_personal, container, false);

        findViews(resultView);
        context = getActivity();

        if (personData != null) {
            initFunctions();
            initPersonalData();
            ivPortrait.setOnClickListener(getIvPortraitOnClickListener());
            typeSwitch(PERSONAL);
            tvPersonalFeeling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeSwitch(PERSONAL);
                }
            });
            tvSavedFeeling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeSwitch(SAVED);
                }
            });
            llSettings.setOnClickListener(getLlSettingsOnClickListener());
        }

        return resultView;
    }

//    private void getPersonData() {
//        Bundle bundle = getArguments();
//
//        if (bundle != null) {
//            personData = (PersonData) bundle.getSerializable(PERSONAL_DATA);
//        }
//    }

    private View.OnClickListener getLlSettingsOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, TestServerActivity.class));
            }
        };
    }

    private void findViews(View rootView) {
        ivPortrait = (ImageView) rootView.findViewById(R.id.ivPortrait);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        tvAccount = (TextView) rootView.findViewById(R.id.tvAccount);
        tvLiveIn = (TextView) rootView.findViewById(R.id.tvLiveIn);
        tvPersonalFeeling = (TextView) rootView.findViewById(R.id.tvPersonalFeeling);
        tvSavedFeeling = (TextView) rootView.findViewById(R.id.tvSavedFeeling);
        llFeelings = (LinearLayout) rootView.findViewById(R.id.llFeelings);
        llSettings = (LinearLayout) rootView.findViewById(R.id.llSettings);
        llTrace = (LinearLayout) rootView.findViewById(R.id.llTrace);
        llAddFriend = (LinearLayout) rootView.findViewById(R.id.llAddFriend);
//        llReview = (LinearLayout) rootView.findViewById(R.id.llReview);
    }

    private View.OnClickListener getIvPortraitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO_CODE);
            }
        };
    }

    private void initFunctions() {
        LoginData loginData = LoginData.getInstance(context);

        if (loginData.personData.name.equals(personData.name)) {
            llSettings.setVisibility(View.VISIBLE);
        } else {
            llTrace.setVisibility(View.VISIBLE);
            llAddFriend.setVisibility(View.VISIBLE);
        }
    }

    private void initPersonalData() {
        tvName.setText(personData.name);
        tvAccount.setText(personData.account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_CODE && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                String photoPath = new FileUtil(context).getAbsolutePath(uri);
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Toast.makeText(context, R.string.update_success, Toast.LENGTH_SHORT).show();
                    }
                };

                LoginData.getInstance(context).updateUserPhoto(photoPath, handler);
                bitmap = ImageUtil.getThumbnailBitmap(bitmap);
                if (bitmap != null) {
                    ivPortrait.setImageBitmap(bitmap);
                    saveLocalPortraitFile(bitmap);
                }
            } catch (Exception e) {
                Log.e(TAG, "onActivityResult: " + e.toString());
            }
        }
    }

    private void saveLocalPortraitFile(Bitmap bitmap) {
        FileUtil fileUtil = new FileUtil(context);
        //File file = fileUtil.getExternalFile(FileConfig.EXTERNAL_DIR, PORTRAIT_FILE_NAME);
        byte[] bytes = ImageUtil.getBitmapBytes(bitmap);

        fileUtil.saveExternalFile(FileConfig.EXTERNAL_DIR, PORTRAIT_FILE_NAME, bytes, true);


    }

    private void typeSwitch(int t) {
//        Feelings feelings = new Feelings(context, Feelings.FROM_WEB, null);
//
//        llFeelings.removeAllViews();
//        if (t == PERSONAL) {
//            type = PERSONAL;
//            //tvPersonalFeeling.setBackgroundResource(R.drawable.grey_8_rectangle);
//            tvPersonalFeeling.setTextColor(getResources().getColor(R.color.black));
//            //tvSavedFeeling.setBackgroundResource(R.drawable.grey_8_rectangle_stroke);
//            tvSavedFeeling.setTextColor(getResources().getColor(R.color.grey_8));
//            feelingListFragment = new FeelingListFragment(context, feelings.feelingList);
//        } else {
//            type = SAVED;
//            //tvPersonalFeeling.setBackgroundResource(R.drawable.grey_8_rectangle_stroke);
//            tvPersonalFeeling.setTextColor(getResources().getColor(R.color.grey_8));
//            //tvSavedFeeling.setBackgroundResource(R.drawable.grey_8_rectangle);
//            tvSavedFeeling.setTextColor(getResources().getColor(R.color.black));
//            feelingListFragment = new FeelingListFragment(context, feelings.feelingList);
//        }
//        feelingListFragment.setHeaderView(new PersonalReviewFragment(context).getView());
//        llFeelings.addView(feelingListFragment.getView());
//        lvFeeling = feelingListFragment.getLvFeelings();
//        feelingListFragment.setOnScrollListener(onScrollListener);
    }

//    private ListViewOnScrollListener getListViewOnScrollListener() {
//        ListViewOnScrollListener.IOnScrollEvent iOnScrollEvent = new ListViewOnScrollListener.IOnScrollEvent() {
//            @Override
//            public void onScrollUp() {
//                if (llReview.getVisibility() != View.GONE) {
//                    llReview.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onScrollDown() {
//                if (llReview.getVisibility() != View.VISIBLE) {
//                    llReview.setVisibility(View.VISIBLE);
//                }
//            }
//        };
//        return new ListViewOnScrollListener(lvFeeling, iOnScrollEvent);
//    }
}

