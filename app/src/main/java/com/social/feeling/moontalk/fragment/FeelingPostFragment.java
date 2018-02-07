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
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.activity.MainActivity;
import com.social.feeling.moontalk.activity.PreparePostActivity;
import com.social.feeling.moontalk.datamodel.Feeling;
import com.social.feeling.moontalk.global.Feelings;
import com.social.feeling.moontalk.http.ServerResponse;

import java.util.List;

/**
 * Created by lidondon on 2016/5/31.
 */
public class FeelingPostFragment extends Fragment {
    private static int createTimes;
    private Context context;
    private int loadingCountAtOnce = 1;
    private FeelingListFragment feelingListFragment;
    private LinearLayout llFeelings;
    private EditText etShare;
    private Feelings feelings;
    //private LruCache<String, Bitmap> bitmapLruCache;

    /*
    private Handler localFeelingsViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                List<Feeling> feelingList = (List<Feeling>) msg.obj;
//                feelings = new Feelings(context, Feelings.FROM_WEB);
//                feelingListFragment.refreshWall(feelings.feelingList);
                if (msg.arg1 == ServerResponse.SERVER_NO_RESPONSE) {
                    Toast.makeText(context, R.string.server_no_response, Toast.LENGTH_SHORT).show();
                }

                if (feelingList != null) {
//                    if (feelingListFragment == null) {
//                        feelingListFragment = new FeelingListFragment(context, feelingList);
//                        llFeelings.addView(feelingListFragment.getView());
//                    } else {
//                        llFeelings.removeAllViews();
//                        llFeelings.addView(feelingListFragment.refreshWall(feelingList));
//                    }
                    llFeelings.removeAllViews();
                    feelingListFragment = new FeelingListFragment(context, feelingList);
                    llFeelings.addView(feelingListFragment.getView());
                }
            }
        }
    };
    */

    private Handler webFeelingsViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                List<Feeling> feelingList = (List<Feeling>) msg.obj;

                if (feelingList != null && feelingList.size() > 0) {
                    if (msg.arg2 == Feelings.REFRESH) {
                        feelingListFragment = new FeelingListFragment(context, feelingList, getUpdater());
                        llFeelings.removeAllViews();
                        llFeelings.addView(feelingListFragment.getView());
                    } else {
                        if (feelingListFragment == null) {
                            feelingListFragment = new FeelingListFragment(context, feelingList, getUpdater());
                            llFeelings.addView(feelingListFragment.getView());
                        } else {
                            feelingListFragment.addItems(feelingList);
                        }
                    }
                }

                if (msg.arg1 == ServerResponse.SERVER_NO_RESPONSE) {
                    Toast.makeText(context, R.string.server_no_response, Toast.LENGTH_SHORT).show();
                    if (feelingListFragment != null) {
                        feelingListFragment.removeUpdater();
                    }
                }
            }

            if (feelingListFragment != null) {
                feelingListFragment.switchLoading(false);
            }
        }
    };

    public FeelingPostFragment() {}

    @SuppressLint("ValidFragment")
    public FeelingPostFragment(Context ctx) {
        context = ctx;
    }

//    @SuppressLint("ValidFragment")
//    public FeelingPostFragment(Context ctx, int lCount) {
//        context = ctx;
//        loadingCountAtOnce = lCount;
//    }

//    public void setLoadingCountAtOnce(int lCount) {
//        loadingCountAtOnce = lCount;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.fragment_feeling_post, container, false);

        if (createTimes < 2){
            findViews(resultView);
            context = (context == null) ? getActivity() : context;
            feelings = new Feelings(context, webFeelingsViewHandler, loadingCountAtOnce);
            etShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, PreparePostActivity.class));
                }
            });
            createTimes++;
        }

        return resultView;
    }


    private void findViews(View rootView) {
        llFeelings = (LinearLayout) rootView.findViewById(R.id.llFeelings);
        etShare = (EditText) rootView.findViewById(R.id.etShare);
    }

//    public Handler getFeelingsViewHandler() {
//        return localFeelingsViewHandler;
//    }

    public void refreshLlFeelings() {
//        MainActivity mainActivity = (MainActivity) context;
//        int type = Feelings.FROM_LOCAL;
//
//        if (!mainActivity.getFeelingPostInitiatedState()) {
//            type = Feelings.FROM_WEB;
//            mainActivity.setFeelingPostInitiated(true);
//        }
//        feelings = new Feelings(context, webFeelingsViewHandler, loadingCountAtOnce);
//        if (type == Feelings.FROM_WEB) {
//            feelings.getFeelingListFromWeb(loadingCountAtOnce);
//        } else {
//            feelings.getFeelingListFromLocal(ServerResponse.RESULT_OK);
//        }

        //feelings.getFeelingListFromWeb(true, loadingCountAtOnce);
        feelings.getNextBlockFeelingList(true);
    }

    public FeelingListFragment.IUpdater getUpdater() {
        return new FeelingListFragment.IUpdater() {
            @Override
            public void update() {
                //feelings.getFeelingListFromWeb(false, loadingCountAtOnce);
                feelings.getNextBlockFeelingList(false);
                feelingListFragment.switchLoading(true);
            }
        };
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
