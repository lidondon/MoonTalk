package com.social.feeling.moontalk.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.util.ConvertUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Quote;
import com.social.feeling.moontalk.global.PostFeeling;

public class ReplacementPicker2Activity extends BaseActivity {
    public static final int CODE = 9;
    public static final int HIDE = 0;
    public static final int SHOW = 1;
    private TextView tvQuote;
    private TextView tvCommit;
    private TextView tvCancel;
    private PostFeeling postFeeling = PostFeeling.getInstance();
    private Quote quote = postFeeling.quote;
    private LinearLayout llReplacements;
//    private String checkedColorId;
//    private List<String> checkedPhotoList;

    private Spannable spannable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacement_picker_2);
        postFeeling.addActivity(this);
        findViews();
        //getExtrasData();
        refreshViews();
        tvCommit.setOnClickListener(getTvCommitOnClickListener());
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Quote.copyFakeData(QuotePickerActivity.this, quoteList);
                finish();
            }
        });
    }

    private void findViews() {
        tvQuote = (TextView) findViewById(R.id.tvQuote);
        tvCommit = (TextView) findViewById(R.id.tvCommit);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        llReplacements = (LinearLayout) findViewById(R.id.llReplacements);
    }

    private View.OnClickListener getTvCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReplacementPicker2Activity.this, TagActivity.class));
            }
        };
    }

    private void refreshViews() {
        spannable = Spannable.Factory.getInstance().newSpannable(quote.currentText);
        initClickableSpanList();
        tvQuote.setText(spannable);
        tvQuote.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initClickableSpanList() {
        if (quote.replacementList != null) {
            for (int i = 0; i < quote.replacementList.size(); i++) {
                Quote.Replacement replacement = quote.replacementList.get(i);
                int start = replacement.startIndex + quote.getSoFarDelta(i);
                int end = start + replacement.wordList.get(replacement.getSelectedReplacementIndex()).length();
                final int index = i;
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        //first method
//                        Intent intent = new Intent(ReplacementPickerActivity.this, WordPickerActivity.class);
//                        Bundle bundle = new Bundle();
//
//                        bundle.putInt(WordPickerActivity.REPLACEMENT_INDEX, index);
//                        intent.putExtras(bundle);
//                        startActivityForResult(intent, CODE);

                        //second method
//                        WordPickerDialog.IWordPickerMission iWordPickerMission = new WordPickerDialog.IWordPickerMission() {
//                            @Override
//                            public void doMission() {
//                                refreshViews();
//                            }
//                        };
//
//                        new WordPickerDialog(ReplacementPicker2Activity.this, postFeeling.feeling.quote.replacementList.get(index)
//                                , iWordPickerMission).show();

                        //third method
                        initWordPicker(index);
                    }
                };

                spannable.setSpan(clickableSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void initWordPicker(int replacementIndex) {
        switchWordPicker(SHOW);
        for (int i = 0; i < postFeeling.quote.replacementList.get(replacementIndex).wordList.size(); i++) {
            llReplacements.addView(getTvReplacement(replacementIndex, i, postFeeling.quote.replacementList.get(replacementIndex).wordList.get(i)));
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private TextView getTvReplacement(final int replacementIndex, final int wordIndex, String text) {
        TextView result = new TextView(this);
        int padding = (int) ConvertUtil.convertDp2Pixel(this, 10);
        int marginTop = (int) ConvertUtil.convertDp2Pixel(this, 15);
        int textSize = (int) ConvertUtil.convertDp2Pixel(this, 10);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        result.setText(text);
        result.setPadding(padding, padding, padding, padding);
        result.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        result.setTextSize(textSize);
        result.setBackground(getResources().getDrawable(R.drawable.light_blue_rectangle_stroke));
        params.setMargins(0, marginTop, 0, 0);
        result.setLayoutParams(params);
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postFeeling.quote.replacementList.get(replacementIndex).setSelectedReplacementIndex(wordIndex);
                switchWordPicker(HIDE);
                refreshViews();
            }
        });

        return result;
    }

    private void switchWordPicker(int switchType) {
        if (switchType == SHOW) {
            llReplacements.setVisibility(View.VISIBLE);
            llReplacements.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            llReplacements.setVisibility(View.GONE);
            llReplacements.removeAllViews();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            //quote = (Quote) data.getExtras().getSerializable(Quote.CLASS_NAME);
            refreshViews();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postFeeling.removeActivity(this);
    }
}
