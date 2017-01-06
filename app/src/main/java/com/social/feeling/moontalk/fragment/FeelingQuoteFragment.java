package com.social.feeling.moontalk.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.util.ImageUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Feeling;

import java.util.List;
import java.util.zip.CheckedInputStream;

/**
 * Created by lidondon on 2016/8/11.
 */
public class FeelingQuoteFragment {
    private static final int PHOTO_WIDTH = 100;
    private static final int PHOTO_HEIGHT = 100;
    private Context context;
    private Feeling feeling;
    private ImageView ivPhoto;
    private TextView tvName;
    private TextView tvThought;
    private TextView tvQuote;

    public FeelingQuoteFragment(Context ctx, Feeling f) {
        context = ctx;
        feeling = f;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView() {
        View resultView = getInflater().inflate(R.layout.fragment_feeling_quote, null);

        findViews(resultView);
        if (feeling.photoUri == null) {
            ivPhoto.setImageResource(R.drawable.no_man);
        } else {
            ivPhoto.setImageBitmap(ImageUtil.getThumbnailBitmap(feeling.photoUri, PHOTO_WIDTH, PHOTO_HEIGHT));
        }
        tvName.setText(feeling.name);
        tvThought.setText(feeling.thought);
        tvQuote.setText(feeling.quote.currentText);

        return resultView;
    }

    private void findViews(View rootView) {
        ivPhoto = (ImageView) rootView.findViewById(R.id.ivPhoto);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        tvThought = (TextView) rootView.findViewById(R.id.tvThought);
        tvQuote = (TextView) rootView.findViewById(R.id.tvQuote);
    }
}
