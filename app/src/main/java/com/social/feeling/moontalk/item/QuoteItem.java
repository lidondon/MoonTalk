package com.social.feeling.moontalk.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.social.feeling.moontalk.R;

/**
 * Created by lidondon on 2016/7/8.
 */
public class QuoteItem {
    private Context context;
    private ViewHolder viewHolder;
    private String quoteText;
    private View resultView;

    public QuoteItem(Context ctx, String qt) {
        context = ctx;

        quoteText = qt;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView() {
        if (resultView == null) {
            resultView = getInflater().inflate(R.layout.item_quote, null);

            findViews(resultView);
            viewHolder.setContent(quoteText);
        }

        return resultView;
    }

    private void findViews(View rootView) {
        viewHolder = new ViewHolder();
        viewHolder.tvQuoteText = (TextView) rootView.findViewById(R.id.tvQuoteText);
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder {
        private TextView tvQuoteText;

        public void setContent(String text) {
            tvQuoteText.setText(text);
        }

        public void isChecked(boolean checked) {
            if (checked) {
                tvQuoteText.setBackgroundColor(context.getResources().getColor(R.color.white));
                tvQuoteText.setTextColor((context.getResources().getColor(R.color.grey_8)));
            } else {
                tvQuoteText.setBackgroundColor(context.getResources().getColor(R.color.grey_c));
                tvQuoteText.setTextColor((context.getResources().getColor(R.color.grey_a)));
            }
        }
    }
}
