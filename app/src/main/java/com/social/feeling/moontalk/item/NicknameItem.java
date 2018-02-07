package com.social.feeling.moontalk.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Feeling;

/**
 * Created by lidondon on 2016/8/19.
 */
public class NicknameItem {
    private Context context;
    private TextView tvNickname;
    private String nickname;

    public NicknameItem(Context ctx, String n) {
        context = ctx;
        nickname = n;
    }

    public View getView() {
        View resultView = getInflater().inflate(R.layout.item_nickname, null);

        findViews(resultView);
        tvNickname.setText(nickname);

        return resultView;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    private void findViews(View rootView) {
        tvNickname = (TextView) rootView.findViewById(R.id.tvNickname);
    }
}
