package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.component.CompleteHookComponent;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Quote;
import com.social.feeling.moontalk.dialog.WordPickerDialog;
import com.social.feeling.moontalk.global.PostFeeling;

import java.util.ArrayList;
import java.util.List;

public class ReplacementPickerActivity extends BaseActivity {
    public static final int CODE = 9;
    private TextView tvQuote;
    private TextView tvCommit;
    private TextView tvCancel;
    private PostFeeling postFeeling = PostFeeling.getInstance();
    private Quote quote = postFeeling.feeling.quote;
//    private String checkedColorId;
//    private List<String> checkedPhotoList;

    private Spannable spannable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacement_picker);
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
    }

    private View.OnClickListener getTvCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReplacementPickerActivity.this, TagActivity.class));
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
                int end = start + replacement.wordList.get(replacement.getSelectedIndex()).length();
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
                        WordPickerDialog.IWordPickerMission iWordPickerMission = new WordPickerDialog.IWordPickerMission() {
                            @Override
                            public void doMission() {
                                refreshViews();
                            }
                        };

                        new WordPickerDialog(ReplacementPickerActivity.this, postFeeling.feeling.quote.replacementList.get(index)
                                , iWordPickerMission).show();
                    }
                };

                spannable.setSpan(clickableSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
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
