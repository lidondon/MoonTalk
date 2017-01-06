package com.social.feeling.moontalk.item;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.gesture.HorizontalGestureListener;
import com.empire.vmd.client.android_lib.util.EffectUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Friend;

import java.util.List;

/**
 * Created by lidondon on 2015/11/28.
 */
public class FriendItem implements View.OnTouchListener {
    private static final int DURATION = 1000;
    private static final int UNKNOWN = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private Context context;
    private View resultView;
    private Friend friend;
    private ImageView ivIcon;
    private TextView tvName;
    private TextView tvDelete;
    private TextView tvCancel;
    private RelativeLayout rlData;
    private RelativeLayout rlOperation;
    private GestureDetector gestureDetector;
    private HorizontalGestureListener horizontalGestureListener;
    private EffectUtil effectUtil;
    private List<Friend> selectedFriendList;
    private IDeleteMission iDeleteMission;

    public interface IDeleteMission {
        public void executeDeleteMission();
    }

    public FriendItem(Context ctx, Friend f, List<Friend> sfList, IDeleteMission idm) {
        context = ctx;
        friend = f;
        selectedFriendList = sfList;
        iDeleteMission = idm;
        initHorizontalGestureListener();
        gestureDetector = new GestureDetector(horizontalGestureListener);
        effectUtil = new EffectUtil();
    }

    private void initHorizontalGestureListener() {
        HorizontalGestureListener.IHorizontalMission iHorizontalMission = new HorizontalGestureListener.IHorizontalMission() {
            @Override
            public void executeLeftMission() {
                effectUtil.activateViewAnimations(rlData, getHideRlDataAnimations(LEFT), DURATION);
            }

            @Override
            public void executeRightMission() {
                effectUtil.activateViewAnimations(rlData, getHideRlDataAnimations(RIGHT), DURATION);
            }
        };

        horizontalGestureListener = new HorizontalGestureListener(iHorizontalMission);
    }

    private LayoutInflater getInflater() {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        resultView = getInflater().inflate(R.layout.item_friend_fake_delete, null);
        getViews(resultView);
        setContent();
        resultView.setOnTouchListener(this);

        return resultView;
    }

    private View.OnTouchListener getEmptyOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        };
    }

    private void setContent() {
        if (friend != null) {
            if (friend.photoUri == null) {
                ivIcon.setImageResource(R.drawable.no_man);
            } else {

            }
            tvName.setText(friend.name);
        }
    }

    private void getViews(View rootView) {
        ivIcon = (ImageView) rootView.findViewById(R.id.ivIcon);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        tvDelete = (TextView) rootView.findViewById(R.id.tvDelete);
        tvCancel = (TextView) rootView.findViewById(R.id.tvCancel);
        rlData = (RelativeLayout) rootView.findViewById(R.id.rlData);
        rlOperation = (RelativeLayout) rootView.findViewById(R.id.rlOperation);
    }

    private Animation[] getHideRlDataAnimations(int direction) {
        Animation[] result = new Animation[2];

        result[0] = getHideRlDataAnimation(direction, rlData);
        result[1] = new AlphaAnimation(1, 0);

        return result;
    }

    private Animation[] getShowRlDataAnimations() {
        Animation[] result = new Animation[2];

        result[0] = getHideRlDataAnimation(UNKNOWN, rlData);
        result[1] = new AlphaAnimation(0, 1);

        return result;
    }

    private Animation getHideRlDataAnimation(int direction, final View view) {
        float startX = 0f;
        float endX = 0f;
        if (direction == UNKNOWN) {
            startX = 1f;
        } else {
            endX = (direction == LEFT) ? -1f : 1f;
        }
        final Animation result = new TranslateAnimation(Animation.RELATIVE_TO_SELF, startX
                , Animation.RELATIVE_TO_SELF, endX
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f);


        result.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (view.getVisibility() == View.GONE) {
                    view.setVisibility(View.VISIBLE);
                    resultView.setOnTouchListener(FriendItem.this);
                    tvCancel.setOnClickListener(getEmptyOnClickListener());
                    tvDelete.setOnClickListener(getEmptyOnClickListener());
                } else {
                    view.setVisibility(View.GONE);
                    resultView.setOnTouchListener(getEmptyOnTouchListener());
                    tvCancel.setOnClickListener(getTvCancelOnClickListener());
                    tvDelete.setOnClickListener(getTvDeleteOnClickListener());
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return result;
    }

    private View.OnClickListener getEmptyOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    private View.OnClickListener getTvCancelOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                effectUtil.activateViewAnimations(rlData, getShowRlDataAnimations(), DURATION);
            }
        };
    }

    private View.OnClickListener getTvDeleteOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iDeleteMission != null) {
                    selectedFriendList.remove(friend);
                    iDeleteMission.executeDeleteMission();
//                    Handler handler = new Handler() {
//                        @Override
//                        public void handleMessage(Message msg) {
//                            iDeleteMission.executeDeleteMission();
//                        }
//                    };


                    //handler.sendEmptyMessage(0);
                }
            }
        };
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
