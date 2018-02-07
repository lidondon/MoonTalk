package com.social.feeling.moontalk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.adapter.BaseTypeAdapter;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Quote;
import com.social.feeling.moontalk.global.PostFeeling;
import com.social.feeling.moontalk.item.QuoteItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuotePicker2Activity extends BaseActivity {
    //private ListView lvQuotes;
    private TextView tvCommit;
    private TextView tvCancel;
    private TextView tvCompleteQuote;
    private TextView tvPages;
    private ImageView ivArrowLeft;
    private ImageView ivArrowRight;
    private LinearLayout llQuotes;
    private List<Quote> quoteList;
    private List<QuoteItem> quoteItemList;
    private List<View> quoteItemViewList;
    private PostFeeling postFeeling = PostFeeling.getInstance();
    private int selectedReplacementIndex;
    private int currentPage = 1;
    private int totalPage;
    private int itemCount;
    private QuoteItem selectedQuoteItem;
    private boolean getServerResponse;
    private View measureItemView;
    //private List<QuoteItem.ViewHolder> holderList = new ArrayList<QuoteItem.ViewHolder>();
    //private boolean isFirstEntry = true;
    //private int lastFirstVisibleItem;
    //private int lastMiddleNum;
    //private Map<Integer, QuoteItem> quoteItemMap;
    //private int upSpace;
    //private int downSpace;

    private Quote.IQuoteListListener iQuoteListListener = new Quote.IQuoteListListener() {
        @Override
        public void processQuoteList(List<Quote> qList) {
            initQuotes(qList);
            //initLvQuotes();
            tvCommit.setOnClickListener(getTvCommitOnClickListener());
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Quote.copyFakeData(QuotePickerActivity.this, quoteList);
                    finish();
                }
            });
            ivArrowLeft.setOnClickListener(getArrowLeftOnClickListener());
            ivArrowRight.setOnClickListener(getArrowRightOnClickListener());
            getServerResponse = true;
            setPagesInfo();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_picker_2);
        findViews();
        postFeeling.addActivity(this);
        Quote.getQuoteList(this, iQuoteListListener);
        addMeasureItemInLlQuote();
        //getExtrasData();
    }

    private void initLvQuotes() {
        if (quoteItemViewList != null && quoteItemViewList.size() > 0) {
            /*BaseTypeAdapter.IItemView iItemView = new BaseTypeAdapter.IItemView() {
                @Override
                public View getItemView(int position, View convertView) {
                    QuoteItem.ViewHolder viewHolder = null;
                    //因為這個list view設定不可滑動，所以就簡單的寫法就好不考慮回收
                    convertView = new QuoteItem(QuotePicker2Activity.this, quoteTextList.get(position)).getView();

                    return convertView;
                }
            };
            BaseTypeAdapter lvAdapter = new BaseTypeAdapter(iItemView, quoteTextList);
//            if (itemCount == 0) {
//                lvQuotes.setOnScrollListener(getLvQuotesOnScrollListener());
//            }
            lvQuotes.setAdapter(lvAdapter);
            disableLvQuotes();*/
            llQuotes.addView(quoteItemViewList.get(0));
        }
    }

//    private AbsListView.OnScrollListener getLvQuotesOnScrollListener() {
//        return new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                itemCount = totalItemCount;
//            }
//        };
//    }

//    private void getExtrasData() {
//        Bundle bundle = getIntent().getExtras();
//
//        if (bundle != null) {
//            checkedPhotoList = bundle.getStringArrayList(PreparePostActivity.CHECKED_PHOTO_LIST);
//            checkedColorId = bundle.getString(ColorPickerActivity.CHECKED_COLOR_ID);
//        }
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        try {
//            while (!getServerResponse) {
//                if (getServerResponse) {
//                    if (itemCount == 0 && llQuotes != null && quoteItemViewList != null && quoteItemViewList.size() > 0) {
//                        int llQuotesHeight = llQuotes.getHeight();
//                        int quoteItemHeight = quoteItemViewList.get(0).getHeight();
//
//                        itemCount = llQuotesHeight / quoteItemHeight;
//                        initLlQuotes(0, itemCount);
//                        totalPage = quoteList.size() / itemCount;
//                        totalPage = (totalPage % itemCount == 0) ? totalPage : totalPage + 1;
//                        tvPages.setText(currentPage + "/" + totalPage);
//                    }
//                    showArrows();
//                } else {
//                    Thread.sleep(200);
//                }
//            }
//        } catch (Exception e) {
//            Log.e(getClass().toString(), e.toString());
//        }
    }

    private void setPagesInfo() {
        if (itemCount == 0 && llQuotes != null && quoteItemViewList != null && quoteItemViewList.size() > 0) {
            int llQuotesHeight = llQuotes.getHeight();
            int quoteItemHeight = measureItemView.getHeight();

            if (llQuotesHeight != 0 && quoteItemHeight != 0) {
                itemCount = llQuotesHeight / quoteItemHeight;
                initLlQuotes(0, itemCount);
                totalPage = quoteList.size() / itemCount;
                totalPage = (totalPage % itemCount == 0) ? totalPage : totalPage + 1;
                tvPages.setText(currentPage + "/" + totalPage);
            }
        }
        showArrows();
    }

    private void showArrows() {
        ivArrowLeft.setVisibility((currentPage == 1) ? View.GONE : View.VISIBLE);
        ivArrowRight.setVisibility((currentPage == totalPage) ? View.GONE : View.VISIBLE);
    }

    private View.OnClickListener getArrowRightOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLlQuotes(currentPage * itemCount, getItemCount());
                currentPage++;
                showArrows();
            }
        };
    }

    private View.OnClickListener getArrowLeftOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLlQuotes((currentPage - 2) * itemCount, itemCount);
                currentPage--;
                showArrows();
            }
        };
    }

    private int getItemCount() {
        int beginIndex = currentPage * itemCount;
        int leftItem = quoteList.size() - beginIndex;
        int result = (leftItem > itemCount) ? itemCount : leftItem;

        return result;
    }

    private void findViews() {
        //npQuote = (NumberPicker) findViewById(R.account.npQuote);
        //lvQuotes = (ListView) findViewById(R.id.lvQuotes);
        tvCommit = (TextView) findViewById(R.id.tvCommit);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCompleteQuote = (TextView) findViewById(R.id.tvCompleteQuote);
        tvPages = (TextView) findViewById(R.id.tvPages);
        ivArrowLeft = (ImageView) findViewById(R.id.ivArrowLeft);
        ivArrowRight = (ImageView) findViewById(R.id.ivArrowRight);
        llQuotes = (LinearLayout) findViewById(R.id.llQuotes);
    }

    private void initQuotes(List<Quote> qList) {
        quoteList = qList;
        //quoteTextList = Quote.getQuoteTextList(quoteList);
        quoteItemList = new ArrayList<QuoteItem>();
        quoteItemViewList = new ArrayList<View>();
        for (Quote quote : quoteList) {
            QuoteItem.IOnClickListener iOnClickListener = new QuoteItem.IOnClickListener() {
                @Override
                public void onClick(QuoteItem qi) {
                    if (selectedQuoteItem != qi) {
                        Quote quote = qi.getQuote();

                        if (selectedQuoteItem != null) {
                            selectedQuoteItem.getViewHolder().isChecked(false);
                        }
                        selectedQuoteItem = qi;
                        selectedQuoteItem.getViewHolder().isChecked(true);
                        tvCompleteQuote.setText(quote.text);
                        postFeeling.quote = quote;
                    }
                }
            };
            QuoteItem quoteItem = new QuoteItem(this, quote, iOnClickListener);

            quoteItemList.add(quoteItem);
            quoteItemViewList.add(quoteItem.getView());
        }
    }

    private View.OnClickListener getTvCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuotePicker2Activity.this, ReplacementPickerActivity.class));
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postFeeling.feeling.quote = null;
        postFeeling.removeActivity(this);
    }

    private void initLlQuotes(int beginIndex, int count) {
        llQuotes.removeAllViews();
        for (int i = beginIndex; i < beginIndex + count; i++) {
            if (i == beginIndex) {
                if(selectedQuoteItem != null) {
                    selectedQuoteItem.getViewHolder().isChecked(false);
                }
                selectedQuoteItem = quoteItemList.get(beginIndex);
                selectedQuoteItem.getViewHolder().isChecked(true);
                postFeeling.quote = quoteList.get(beginIndex);
                tvCompleteQuote.setText(postFeeling.quote.text);
            }
            llQuotes.addView(quoteItemViewList.get(i));
        }
    }

    private void addMeasureItemInLlQuote() {
        QuoteItem measureItem = new QuoteItem(this, "measure");

        measureItemView = measureItem.getView();
        llQuotes.addView(measureItem.getView());
    }
}
