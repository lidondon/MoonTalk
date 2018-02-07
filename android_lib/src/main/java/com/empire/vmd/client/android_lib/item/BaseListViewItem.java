package com.empire.vmd.client.android_lib.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.R;

/**
 * Created by lidondon on 2017/8/24.
 */

public class BaseListViewItem {
    private Context context;
    private ImageView iv;
    private TextView tvTitle;
    private TextView tvSubtitle;
    private Bitmap bitmap;
    private String title;
    private String subTitle;
    private int ivResource;

    public BaseListViewItem(Context ctx, Bitmap bp, String t, String st) {
        context = ctx;
        bitmap = bp;
        title = t;
        subTitle = st;
    }

    //For demo
    public BaseListViewItem(Context ctx, int res, String t, String st) {
        context = ctx;
        ivResource = res;
        title = t;
        subTitle = st;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView() {
        View resultView = getInflater().inflate(R.layout.item_base_list_view, null);
        findViews(resultView);
        if (ivResource != 0) {
            iv.setImageResource(ivResource);
        } else {
            iv.setImageBitmap(bitmap);
        }
        tvTitle.setText(title);
        tvSubtitle.setText(subTitle);

        return resultView;
    }

    public void setContent(int res, String t, String st) {
        ivResource = res;
        title = t;
        subTitle = st;
        iv.setImageResource(ivResource);
        tvTitle.setText(title);
        tvSubtitle.setText(subTitle);
    }

    public void setContent(Bitmap bp, String t, String st) {
        bitmap = bp;
        title = t;
        subTitle = st;
        iv.setImageBitmap(bitmap);
        tvTitle.setText(title);
        tvSubtitle.setText(subTitle);
    }

    private void findViews(View rootView) {
        iv = (ImageView) rootView.findViewById(R.id.iv);
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        tvSubtitle = (TextView) rootView.findViewById(R.id.tvSubtitle);
    }
}
