package com.social.feeling.moontalk.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.listener.ListViewOnScrollListener;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.activity.PostActivity;
import com.social.feeling.moontalk.activity.PreparePostActivity;
import com.social.feeling.moontalk.datamodel.Feeling;
import com.social.feeling.moontalk.global.Feelings;
import com.social.feeling.moontalk.global.LoginData;

import java.util.List;

/**
 * Created by lidondon on 2016/10/13.
 */
public class PersonalFragment extends Fragment {
    public static final int SAVED = 0;
    public static final int PERSONAL = 1;
    private Context context;
    private int type;
    private ImageView ivPhoto;
    private TextView tvName;
    private TextView tvOccupation;
    private TextView tvLiveIn;
    private TextView tvTravel;
    private TextView tvPersonalFeeling;
    private TextView tvSavedFeeling;
    private LinearLayout llFeelings;
    //private LinearLayout llReview;
    private LoginData loginData;
    private FeelingListFragment feelingListFragment;
    private int firstVI;
    private int lastFirstVI;
    private ListView lvFeeling;

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
    public PersonalFragment(Context ctx) {
        context = ctx;
        loginData = LoginData.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView =  inflater.inflate(R.layout.fragment_personal, container, false);

        findViews(resultView);
        if (context == null) {
            initData();
        }
        initPersonalData();
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

        return resultView;
    }

    private void initData() {
        context = getActivity();
        loginData = LoginData.getInstance(context);
    }

    private void findViews(View rootView) {
        ivPhoto = (ImageView) rootView.findViewById(R.id.ivPhoto);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        tvOccupation = (TextView) rootView.findViewById(R.id.tvOccupation);
        tvLiveIn = (TextView) rootView.findViewById(R.id.tvLiveIn);
        tvTravel = (TextView) rootView.findViewById(R.id.tvTravel);
        tvPersonalFeeling = (TextView) rootView.findViewById(R.id.tvPersonalFeeling);
        tvSavedFeeling = (TextView) rootView.findViewById(R.id.tvSavedFeeling);
        llFeelings = (LinearLayout) rootView.findViewById(R.id.llFeelings);
//        llReview = (LinearLayout) rootView.findViewById(R.id.llReview);
    }

    private void initPersonalData() {
        tvName.setText(loginData.name);
        tvOccupation.setText(loginData.occupation);
        tvLiveIn.setText(loginData.liveIn);
        tvTravel.setText(loginData.travel);
    }

    private void typeSwitch(int t) {
        Feelings feelings = new Feelings(context);

        llFeelings.removeAllViews();
        if (t == PERSONAL) {
            type = PERSONAL;
            //tvPersonalFeeling.setBackgroundResource(R.drawable.grey_8_rectangle);
            tvPersonalFeeling.setTextColor(getResources().getColor(R.color.black));
            //tvSavedFeeling.setBackgroundResource(R.drawable.grey_8_rectangle_stroke);
            tvSavedFeeling.setTextColor(getResources().getColor(R.color.grey_8));
            feelingListFragment = new FeelingListFragment(context, feelings.feelingList);
        } else {
            type = SAVED;
            //tvPersonalFeeling.setBackgroundResource(R.drawable.grey_8_rectangle_stroke);
            tvPersonalFeeling.setTextColor(getResources().getColor(R.color.grey_8));
            //tvSavedFeeling.setBackgroundResource(R.drawable.grey_8_rectangle);
            tvSavedFeeling.setTextColor(getResources().getColor(R.color.black));
            feelingListFragment = new FeelingListFragment(context, feelings.feelingList);
        }
        feelingListFragment.setHeaderView(new PersonalReviewFragment(context).getView());
        llFeelings.addView(feelingListFragment.getView());
        lvFeeling = feelingListFragment.getLvFeelings();
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

