package com.social.feeling.moontalk.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.empire.vmd.client.android_lib.util.ConvertUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk._interface.IView;
import com.social.feeling.moontalk.datamodel.Feeling;
import com.social.feeling.moontalk.fragment.FeelingPhotoFragment;
import com.social.feeling.moontalk.fragment.FeelingQuoteFragment;
import com.social.feeling.moontalk.global.Colors;

/**
 * Created by lidondon on 2016/8/9.
 */
public class FeelingItem implements IView {
    private Context context;
    private Feeling feeling;
    private ViewHolder viewHolder;
    private LruCache<String, Bitmap> bitmapLruCache;

    public FeelingItem(Context ctx, Feeling f, LruCache<String, Bitmap> bpLruCache) {
        context = ctx;
        feeling = f;
        bitmapLruCache = bpLruCache;
    }

    public View getView() {
        View resultView = getInflater().inflate(R.layout.item_feeling, null);

        findViews(resultView);
        viewHolder.setContent(feeling);

        return resultView;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    private void findViews(View rootView) {
        viewHolder = new ViewHolder();
        viewHolder.ivColor = (ImageView) rootView.findViewById(R.id.ivColor);
        viewHolder.llPhotos = (LinearLayout) rootView.findViewById(R.id.llPhotos);
        viewHolder.llQuote = (LinearLayout) rootView.findViewById(R.id.llQuote);
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder {
        private FeelingPhotoFragment photoFragment;
        private LinearLayout llPhotos;
        private ImageView ivColor;
        private LinearLayout llQuote;
        private FeelingQuoteFragment quoteFragment;

        public void setContent(Feeling f) {
            if (f != null) {
                llPhotos.removeAllViews();
                llQuote.removeAllViews();
                initPhotos(f);
                ivColor.setImageResource(Colors.getInstance().getColorResource(f.checkedColorId));
                initQuote(f);
            }
        }

        private void recycleBitmap() {
            Bitmap bpLlPhoto = llPhotos.getDrawingCache();
            Bitmap bpLlQuote = llQuote.getDrawingCache();

            if (bpLlPhoto != null && !bpLlPhoto.isRecycled()) {
                bpLlPhoto.recycle();
            }

            if (bpLlQuote != null && !bpLlQuote.isRecycled()) {
                bpLlQuote.recycle();
            }
        }

        private void initPhotos(Feeling f) {
            photoFragment = new FeelingPhotoFragment(context, f.checkedPhotoList, bitmapLruCache);
            viewHolder.llPhotos.addView(photoFragment.getView());
        }

        private void initQuote(Feeling f) {
            quoteFragment = new FeelingQuoteFragment(context, f);
            //getFragmentManager().beginTransaction().replace(R.id.flQuote, quoteFragment).commit();
            llQuote.addView(quoteFragment.getView());
        }
    }
}
