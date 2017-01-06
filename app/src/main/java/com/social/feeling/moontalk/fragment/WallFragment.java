package com.social.feeling.moontalk.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.activity.PreparePostActivity;
import com.social.feeling.moontalk.global.Feelings;

/**
 * Created by lidondon on 2016/5/31.
 */
public class WallFragment extends Fragment {
    private Context context;
    private FeelingListFragment feelingListFragment;
    private LinearLayout llFeelings;
    private EditText etShare;
    private Feelings feelings;
    //private LruCache<String, Bitmap> bitmapLruCache;

    private Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            feelings = new Feelings(context);
            feelingListFragment.refreshWall(feelings.feelingList);
        }
    };

    public WallFragment() {}

    @SuppressLint("ValidFragment")
    public WallFragment(Context ctx) {
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView =  inflater.inflate(R.layout.fragment_wall, container, false);
        findViews(resultView);
        context = (context == null) ? getActivity() : context;
        initLlFeelings();
        etShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, PreparePostActivity.class));
            }
        });

        return resultView;
    }

    private void findViews(View rootView) {
        llFeelings = (LinearLayout) rootView.findViewById(R.id.llFeelings);
        etShare = (EditText) rootView.findViewById(R.id.etShare);
    }

    public Handler getRefreshHandler() {
        return refreshHandler;
    }

    private void initLlFeelings() {
        feelings = new Feelings(context);
        feelingListFragment = new FeelingListFragment(context, feelings.feelingList);
        llFeelings.addView(feelingListFragment.getView());
    }

//    private void testLlFeelings() {
//        llFeelings.removeAllViews();
//        for (Feeling feeling : feelings.feelingList) {
//            FeelingItem feelingItem = new FeelingItem((FragmentActivity) context, feeling);
//
//            llFeelings.addView(getPaddingView());
//            llFeelings.addView(feelingItem.getView());
//        }
//    }
//
//    private View getPaddingView() {
//        TextView result = new TextView(context);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
//                , ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        params.height = (int) ConvertUtil.convertDp2Pixel(context, 10);
//        result.setLayoutParams(params);
//
//        return result;
//    }
}
