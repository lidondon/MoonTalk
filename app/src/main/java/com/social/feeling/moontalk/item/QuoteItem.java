package com.social.feeling.moontalk.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Quote;

/**
 * Created by lidondon on 2016/7/8.
 */
public class QuoteItem {
    private Context context;
    private IOnClickListener iOnClickListener;
    private ViewHolder viewHolder;
    private String quoteText;
    private Quote quote;
    private View resultView;

    public interface IOnClickListener {
        public void onClick(QuoteItem qi);
    }

    public QuoteItem(Context ctx, String qt) {
        context = ctx;
        quoteText = qt;
    }

    public QuoteItem(Context ctx, Quote q, IOnClickListener iocl) {
        context = ctx;
        quote = q;
        iOnClickListener = iocl;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView() {
        if (resultView == null) {
            resultView = getInflater().inflate(R.layout.item_quote, null);

            findViews(resultView);
            if (quote != null) {
                viewHolder.setContent(quote);
            } else {
                viewHolder.setContent(quoteText);
            }
        }

        return resultView;
    }

    public Quote getQuote() {
        return quote;
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

        public void setContent(Quote q) {
            tvQuoteText.setText(q.text);
            tvQuoteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iOnClickListener != null) {
                        iOnClickListener.onClick(QuoteItem.this);
                    }
                }
            });
        }

        public void isChecked(boolean checked) {
            if (checked) {
                tvQuoteText.setBackgroundColor(context.getResources().getColor(R.color.pink));
            } else {
                tvQuoteText.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            }
        }
    }
}
