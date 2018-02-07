package com.social.feeling.moontalk.item;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.util.ImageUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk._interface.IView;
import com.social.feeling.moontalk.activity.PersonalActivity;
import com.social.feeling.moontalk.datamodel.Feeling;
import com.social.feeling.moontalk.datamodel.PersonData;
import com.social.feeling.moontalk.fragment.FeelingPhotoFragment;
import com.social.feeling.moontalk.fragment.NewFeelingPhotoFragment;
import com.social.feeling.moontalk.global.Colors;

import static android.graphics.BitmapFactory.decodeResource;

/**
 * Created by lidondon on 2017/3/2.
 */
public class NewFeelingItem implements IView {
    private Context context;
    private Feeling feeling;
    private ViewHolder viewHolder;
    private LruCache<String, Bitmap> bitmapLruCache;

    public NewFeelingItem(Context ctx, Feeling f, LruCache<String, Bitmap> bpLruCache) {
        context = ctx;
        feeling = f;
        bitmapLruCache = bpLruCache;
    }

    public View getView() {
        View resultView = getInflater().inflate(R.layout.item_new_feeling, null);

        findViews(resultView);
        viewHolder.setContent(feeling);

        return resultView;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    private void findViews(View rootView) {
        viewHolder = new ViewHolder();
        viewHolder.llPhotos = (LinearLayout) rootView.findViewById(R.id.llPhotos);
        viewHolder.ivColor = (ImageView) rootView.findViewById(R.id.ivColor);
        viewHolder.ivPortrait = (ImageView) rootView.findViewById(R.id.ivPortrait);
        viewHolder.tvQuote = (TextView) rootView.findViewById(R.id.tvQuote);
        viewHolder.tvName = (TextView) rootView.findViewById(R.id.tvName);
        viewHolder.tvThought = (TextView) rootView.findViewById(R.id.tvThought);
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder {
        private NewFeelingPhotoFragment photoFragment;
        private LinearLayout llPhotos;
        private ImageView ivColor;
        private ImageView ivPortrait;
        private TextView tvQuote;
        private TextView tvName;
        private TextView tvThought;

        public void setContent(Feeling f) {
            if (f != null) {
                llPhotos.removeAllViews();
                initPhotos(f);
                ImageUtil.setImageViewRoundedCorner(context.getResources(), ivColor, Colors.getInstance().getColorResource(f.checkedColorId), 300);
                //ImageUtil.setImageViewRoundedCorner(context.getResources(), ivColor, R.drawable.brown, 10000);
                //ivColor.setImageResource(Colors.getInstance().getColorResource(f.checkedColorId));
                tvQuote.setText(f.quote);
                tvName.setText(f.name);
                tvThought.setText(f.thought);

                ImageUtil.setImageViewRoundedCorner(context.getResources(), ivPortrait, R.drawable.me2, 100);
                ivPortrait.setOnClickListener(getIvPortraitOnClickListener(f.account));
                //ivPortrait.setImageBitmap(ImageUtil.getRoundRectBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.me), 30));
                //setImageViewCorner(Colors.getInstance().getColorResource(f.checkedColorId));
            }
        }

        private View.OnClickListener getIvPortraitOnClickListener(final String account) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PersonalActivity.class);
                    Bundle bundle = new Bundle();

                    //bundle.putString(PersonData.ACCOUNT, account);
                    bundle.putString(PersonData.ACCOUNT, "fake"); //now use fake account
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            };
        }

        private void initPhotos(Feeling f) {
            photoFragment = new NewFeelingPhotoFragment(context, f.checkedPhotoList, bitmapLruCache);
            viewHolder.llPhotos.addView(photoFragment.getView());
        }

//        private void setImageViewCorner(int colorResource) {
//            if (ivColor != null && ivPortrait != null) {
//                Bitmap bpColor = decodeResource(context.getResources(), colorResource);
//                Bitmap bpPortrait = decodeResource(context.getResources(), R.drawable.me);
//
//                if (bpColor != null && bpPortrait != null) {
//                    ivColor.setImageBitmap(ImageUtil.getRoundBitmap(bpColor, 50));
//                    ivPortrait.setImageBitmap(ImageUtil.getRoundBitmap(bpPortrait, 50));
//                }
//            }
//        }
    }
}
