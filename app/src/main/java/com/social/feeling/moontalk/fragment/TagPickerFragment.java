package com.social.feeling.moontalk.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.empire.vmd.client.android_lib.util.ConvertUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.FeelingTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2016/7/25.
 */
public class TagPickerFragment extends Fragment {
    private static final int ADD = 1;
    private static final int MINUS = -1;
    private static final int TAG_COUNT = 6;
    private static final int TAG_MARGIN_LEFT = 10;
    private Context context;
    private RelativeLayout rlRoot;
    private ImageView ivTag1;
    private ImageView ivTag2;
    private ImageView ivTag3;
    private ImageView ivTag4;
    private ImageView ivTag5;
    private ImageView ivTag6;
    private ImageView ivEye;
    private ImageView ivEar;
    private ImageView ivMouth;
    private ImageView ivNose;
    private ImageView ivTouch;
    private ImageView ivFeel;
    private int screenWidth;
    private int screenHeight;
    private float shiftX = -1;
    private float shiftY = -1;
    //private List<Integer> ivResourceList = new ArrayList<Integer>();
    private List<FeelingTag> tagList = new ArrayList<FeelingTag>();


    public TagPickerFragment() {}

    @SuppressLint("ValidFragment")
    public TagPickerFragment(Context ctx) {
        context = ctx;
    }

    public List<FeelingTag> getTagList() {
        return tagList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.fragment_tag_picker, container, false);

        findViews(resultView);
        getScreenWidthAndHeight();
        resetTagsWidthAndHeight();
        ivEye.setOnTouchListener(getDragListener());
        ivEar.setOnTouchListener(getDragListener());
        ivMouth.setOnTouchListener(getDragListener());
        ivNose.setOnTouchListener(getDragListener());
        ivTouch.setOnTouchListener(getDragListener());
        ivFeel.setOnTouchListener(getDragListener());

        return resultView;
    }

    private ImageView getCurrentIvTag(int index) {
        ImageView result = null;

        switch (index) {
            case 0:
                result = ivTag1;
                break;
            case 1:
                result = ivTag2;
                break;
            case 2:
                result = ivTag3;
                break;
            case 3:
                result = ivTag4;
                break;
            case 4:
                result = ivTag5;
                break;
            case 5:
                result = ivTag6;
                break;
        }

        return result;
    }

    private View.OnTouchListener getDragListener() {
        return new View.OnTouchListener() {
            private float x, y; //點到圖片裡面的X,Y軸位置
            private int mx, my; //圖片被拖曳的X ,Y軸距離長度
            private int orgX;
            private int orgY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int viewWidth = v.getWidth();
                int viewHeight = v.getHeight();

                //v.bringToFront();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        shiftX = event.getRawX() - x - v.getLeft();
                        shiftY = event.getRawY() - y - v.getTop();
                        orgX = v.getLeft();
                        orgY = v.getTop();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mx = (int) (event.getRawX() - x - shiftX);
                        my = (int) (event.getRawY() - y - shiftY);
                        if (mx < 0) {
                            mx = 0;
                        }

                        if (my < 0) {
                            my = 0;
                        }

                        if (mx + viewWidth > rlRoot.getWidth()) {
                            mx = rlRoot.getWidth() - viewWidth;
                        }

                        if (my + viewHeight > rlRoot.getHeight()) {
                            my = rlRoot.getHeight() - viewHeight;
                        }
                        //l 和 t 是元件左邊緣和上邊緣相對於父類左邊緣和上邊緣的距离
                        //r 和 b是元件右邊緣和下邊緣相對於父纇元件左邊緣和上邊緣的距离
                        v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
                        v.postInvalidate();
                        if (isOverlap(getCurrentIvTag(tagList.size()), v)) {
                            int id = v.getId();

                            tagList.add(getTagByResource(id));
                            refreshIvTags();
                            ((ImageView) v).setImageResource(getMinusTagResourceByResource(id));
                            v.layout(orgX, orgY, orgX + v.getWidth(), orgY + v.getHeight());
                            v.setOnTouchListener(getNoUseListener());
                            v.setOnClickListener(getRecoveryOnClickListener((ImageView) v));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        v.layout(orgX, orgY, orgX + v.getWidth(), orgY + v.getHeight());
                        v.postInvalidate();
                        break;
                }

                return true; //true代表點事件不穿透
            }
        };
    }

    private View.OnClickListener getRecoveryOnClickListener(final ImageView iv) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ivResource = getTagByResource(iv.getId()).resource;

                iv.setImageResource(ivResource);
                iv.setOnTouchListener(getDragListener());
                minusIvTag(ivResource);
            }
        };
    }

    private void minusIvTag(int ivResource) {
        ImageView iv = null;

        removeTag(ivResource);
        iv = getCurrentIvTag(tagList.size());
        iv.setImageResource(R.drawable.add_tag);

        refreshIvTags();
    }

    private void refreshIvTags() {
        int visible = View.VISIBLE;

        for (int i = 0; i < TAG_COUNT; i++) {
            ImageView tmpIv = getCurrentIvTag(i);

            if (i < tagList.size()) {
                tmpIv.setImageResource(tagList.get(i).resource);
            } else if (i == tagList.size()) {
                getCurrentIvTag(i).setImageResource(R.drawable.add_tag);
            } else {
                visible = View.INVISIBLE;
            }
            tmpIv.setVisibility(visible);
        }
    }

    private void removeTag(int resource) {
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).resource == resource) {
                tagList.remove(i);
                break;
            }
        }
    }

    private View.OnTouchListener getNoUseListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        };
    }

    private FeelingTag getTagByResource(int ivId) {
        FeelingTag result = null;

        switch (ivId) {
            case R.id.ivEye:
                result = new FeelingTag("eye", R.drawable.eye);
                break;
            case R.id.ivEar:
                result = new FeelingTag("ear", R.drawable.ear);
                break;
            case R.id.ivMouth:
                result = new FeelingTag("mouth", R.drawable.mouth);
                break;
            case R.id.ivNose:
                result = new FeelingTag("nose", R.drawable.nose);
                break;
            case R.id.ivTouch:
                result = new FeelingTag("touch", R.drawable.touch);
                break;
            case R.id.ivFeel:
                result = new FeelingTag("feel", R.drawable.feel);
                break;
        }

        return result;
    }

    private int getMinusTagResourceByResource(int ivId) {
        int result = 0;

        switch (ivId) {
            case R.id.ivEye:
                result = R.drawable.eye_minus;
                break;
            case R.id.ivEar:
                result = R.drawable.ear_minus;
                break;
            case R.id.ivMouth:
                result = R.drawable.mouth_minus;
                break;
            case R.id.ivNose:
                result = R.drawable.nose_minus;
                break;
            case R.id.ivTouch:
                result = R.drawable.touch_minus;
                break;
            case R.id.ivFeel:
                result = R.drawable.feel_minus;
                break;
        }

        return result;
    }

    private void findViews(View rootView) {
        rlRoot = (RelativeLayout) rootView.findViewById(R.id.rlRoot);
        ivTag1 = (ImageView) rootView.findViewById(R.id.ivTag1);
        ivTag2 = (ImageView) rootView.findViewById(R.id.ivTag2);
        ivTag3 = (ImageView) rootView.findViewById(R.id.ivTag3);
        ivTag4 = (ImageView) rootView.findViewById(R.id.ivTag4);
        ivTag5 = (ImageView) rootView.findViewById(R.id.ivTag5);
        ivTag6 = (ImageView) rootView.findViewById(R.id.ivTag6);
        ivEye = (ImageView) rootView.findViewById(R.id.ivEye);
        ivEar = (ImageView) rootView.findViewById(R.id.ivEar);
        ivMouth = (ImageView) rootView.findViewById(R.id.ivMouth);
        ivNose = (ImageView) rootView.findViewById(R.id.ivNose);
        ivTouch = (ImageView) rootView.findViewById(R.id.ivTouch);
        ivFeel = (ImageView) rootView.findViewById(R.id.ivFeel);
    }

    private void resetTagsWidthAndHeight() {
//        ViewTreeObserver viewTreeObserver = rlRoot.getViewTreeObserver();
//
//        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                ivTag1.setLayoutParams(getTagsParams());
//                ivTag2.setLayoutParams(getTagsParams());
//                ivTag3.setLayoutParams(getTagsParams());
//                ivTag4.setLayoutParams(getTagsParams());
//                ivTag5.setLayoutParams(getTagsParams());
//                ivEye.setLayoutParams(getTagsParams());
//                ivEar.setLayoutParams(getTagsParams());
//                ivMouth.setLayoutParams(getTagsParams());
//                ivNose.setLayoutParams(getTagsParams());
//                ivTouch.setLayoutParams(getTagsParams());
//            }
//        });
    }

    private RelativeLayout.LayoutParams getTagsParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT
                , RelativeLayout.LayoutParams.WRAP_CONTENT);
        int margin = (int) ConvertUtil.convertDp2Pixel(context, TAG_MARGIN_LEFT);
        int widthAndHeight = (rlRoot.getWidth() - 6 * margin) / 5; //五張圖有六個間隔（包含左右兩邊）

        params.width = widthAndHeight;
        params.height = widthAndHeight;

        return params;
    }

    private void getScreenWidthAndHeight() {
        DisplayMetrics dm = new DisplayMetrics();

        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    //兩個元件要在同一個父元件內
    private boolean isOverlap(View v1, View v2) {
        boolean result = false;

        if (v1 != null && v2 != null) {
            int v1Left = v1.getLeft();
            int v1Right = v1.getRight();
            int v1Top = v1.getTop();
            int v1Bottom = v1.getBottom();
            int v2Left = v2.getLeft();
            int v2Right = v2.getRight();
            int v2Top = v2.getTop();
            int v2Bottom = v2.getBottom();

            if (!((v2Left < v1Left && v2Left < v1Left) || (v2Left > v1Right && v2Right > v1Right))
                    && !((v2Top < v1Top && v2Bottom < v1Top) || (v2Top > v1Bottom && v2Bottom > v1Bottom))) {
                result = true;
            }
        }

        return result;
    }
}
