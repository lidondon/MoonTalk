package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.adapter.BaseTypeAdapter;
import com.example.feeling.MediaProxy;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Quote;
import com.social.feeling.moontalk.global.PostFeeling;
import com.social.feeling.moontalk.item.QuoteItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuotePickerActivity extends BaseActivity implements AbsListView.OnScrollListener {
    private ListView lvQuotes;
    private TextView tvCommit;
    private TextView tvCancel;
    private List<Quote> quoteList;
    private List<String> quoteTextList;
    private PostFeeling postFeeling = PostFeeling.getInstance();
//    private String checkedColorId;
//    private List<String> checkedPhotoList;
    private List<QuoteItem.ViewHolder> holderList = new ArrayList<QuoteItem.ViewHolder>();
    private boolean isFirstEntry = true;
    private int lastFirstVisibleItem;
    private int lastMiddleNum;
    private Map<Integer, QuoteItem> quoteItemMap;
    private int upSpace;
    private int downSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_picker);
        postFeeling.addActivity(this);
        //Quote.makeFakeData(this);
        initQuotes();
        //getExtrasData();
        findViews();
        initLvQuotes();
        tvCommit.setOnClickListener(getTvCommitOnClickListener());
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Quote.copyFakeData(QuotePickerActivity.this, quoteList);
                finish();
            }
        });
    }

    private void initLvQuotes() {
        if (quoteTextList != null && quoteTextList.size() > 0) {
            BaseTypeAdapter.IItemView iItemView = new BaseTypeAdapter.IItemView() {
                @Override
                public View getItemView(int position, View convertView) {
                    //QuoteItem.ViewHolder viewHolder = null;
                    QuoteItem quoteItem = quoteItemMap.get(position);

                    if (quoteItem == null) {
                        //quoteItem = new QuoteItem(QuotePickerActivity.this, quoteTextList.get(position % quoteTextList.size()));
                        quoteItem = new QuoteItem(QuotePickerActivity.this, quoteTextList.get(position));
                        quoteItemMap.put(position, quoteItem);
                    }
                    convertView = quoteItem.getView();
//                if (convertView != null) {
//                    int index = (int) convertView.getTag();
//
//                    quoteItemMap.get(index).getViewHolder().setContent(quoteTextList.get(position % quoteTextList.size()));
//                } else {
//                    QuoteItem quoteItem = new QuoteItem(QuotePickerActivity.this, quoteTextList.get(position % quoteTextList.size()));
//
//                    convertView = quoteItem.getView();
//                    //viewHolder = quoteItem.getViewHolder();
//                    convertView.setTag(position);
//                    quoteItemMap.put(position, quoteItem);
//                }

                    return convertView;
                }
            };
            BaseTypeAdapter lvAdapter = new BaseTypeAdapter(iItemView, quoteTextList);

            quoteItemMap = new HashMap<Integer, QuoteItem>();
            lvQuotes.setAdapter(lvAdapter);
            lvQuotes.setOnScrollListener(this);
        }
    }

//    private void getExtrasData() {
//        Bundle bundle = getIntent().getExtras();
//
//        if (bundle != null) {
//            checkedPhotoList = bundle.getStringArrayList(PreparePostActivity.CHECKED_PHOTO_LIST);
//            checkedColorId = bundle.getString(ColorPickerActivity.CHECKED_COLOR_ID);
//        }
//    }

    private void findViews() {
        //npQuote = (NumberPicker) findViewById(R.account.npQuote);
        lvQuotes = (ListView) findViewById(R.id.lvQuotes);
        tvCommit = (TextView) findViewById(R.id.tvCommit);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
    }

    private void initQuotes() {
        quoteList = Quote.getQuoteList(this);
        quoteTextList = Quote.getQuoteTextList(quoteList);
    }

    private View.OnClickListener getTvCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuotePickerActivity.this, ReplacementPicker2Activity.class);
                postFeeling.feeling.quote = quoteList.get(lastMiddleNum - upSpace);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            isFirstEntry = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isFirstEntry) {
            lastMiddleNum = firstVisibleItem + visibleItemCount / 2;
            if (lastMiddleNum != 0 && upSpace == 0) {
                upSpace = lastMiddleNum - firstVisibleItem;
                downSpace = firstVisibleItem + visibleItemCount - 1 - lastMiddleNum;
                addUpDownSpace(upSpace, downSpace);
            }
        } else if (firstVisibleItem != lastFirstVisibleItem) {
            int delta = firstVisibleItem - lastFirstVisibleItem;
            QuoteItem quoteItem = quoteItemMap.get(lastMiddleNum);

            if (quoteItem != null) {
                quoteItem.getViewHolder().isChecked(false);
            }
            lastMiddleNum += delta;
        }

        //不想貼重複的程式碼所以加外面這個判斷
        if (isFirstEntry || firstVisibleItem != lastFirstVisibleItem) {
            if (quoteItemMap != null) {
                QuoteItem quoteItem = quoteItemMap.get(lastMiddleNum);

                if (quoteItem != null) {
                    quoteItem.getViewHolder().isChecked(true);
                }
            }
        }
        lastFirstVisibleItem = firstVisibleItem;
    }

    private void addUpDownSpace(int up, int down) {
        for (int i = 0; i < up; i++) {
            quoteTextList.add(0, " ");
        }

        for (int i = 0; i < down; i++) {
                quoteTextList.add(" ");
        }
        initLvQuotes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postFeeling.feeling.quote = null;
        postFeeling.removeActivity(this);
    }

//    private List<String> getQuoteList() {
//        return new String[] {"五月天山雪，無花只有寒，笛中聞折柳，春色未曾看，曉戰隨金鼓，宵眠抱玉鞍，，願將腰下劍，直為斬樓蘭"
//            , "人與人之間，只有真誠相待，才是真正的朋友。誰要是算計朋友等於自己欺騙自己。"
//            , "家必自毀，而後人毀之。"
//            , "部曲縱橫聽指揮，元來棋算即兵機"};
//    }
}
