package com.social.feeling.moontalk.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.util.EffectUtil;
import com.empire.vmd.client.android_lib.util.OtherUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Quote;
import com.social.feeling.moontalk.global.PostFeeling;
import com.social.feeling.moontalk.util.AnimationUtil;

public class ReplacementPickerActivity extends BaseActivity {
    public static final int CODE = 9;
    private static final int ALERT_DURATION = 800;
    private static final boolean OFF = false;
    private static final boolean ON = true;
    private EffectUtil effectUtil;
    private TextView tvQuote;
    private TextView tvCommit;
    private TextView tvCancel;
    private LinearLayout llWordPicker;
    private ImageView ivNo;
    private ImageView ivYes;
    private NumberPicker npWord;
    private PostFeeling postFeeling = PostFeeling.getInstance();
    private Quote quote = postFeeling.quote;
//    private String checkedColorId;
//    private List<String> checkedPhotoList;

    private Spannable spannable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacement_picker);
        postFeeling.addActivity(this);
        findViews();
        effectUtil = new EffectUtil();
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
        npWord = (NumberPicker) findViewById(R.id.npWord);
        llWordPicker = (LinearLayout) findViewById(R.id.llWordPicker);
        ivNo = (ImageView) findViewById(R.id.ivNo);
        ivYes = (ImageView) findViewById(R.id.ivYes);
    }

    private View.OnClickListener getTvCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postFeeling.feeling.quote = postFeeling.quote.currentText;
                startActivity(new Intent(ReplacementPickerActivity.this, PostActivity.class));
            }
        };
    }

    private void refreshViews() {
        spannable = Spannable.Factory.getInstance().newSpannable(quote.currentText);
        initClickableSpanList();
        tvQuote.setText(spannable);
        tvQuote.setMovementMethod(LinkMovementMethod.getInstance()); //一定要寫這行，才可以點擊tvQuote
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
                        /*WordPickerDialog.IWordPickerMission iWordPickerMission = new WordPickerDialog.IWordPickerMission() {
                            @Override
                            public void doMission() {
                                refreshViews();
                            }
                        };

                        new WordPickerDialog(ReplacementPickerActivity.this, postFeeling.feeling.quote.replacementList.get(index)
                                , iWordPickerMission).show();*/

                        //third method
                        initLlWordPicker(postFeeling.quote.replacementList.get(index));
                    }
                };

                spannable.setSpan(clickableSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void initLlWordPicker(final Quote.Replacement replacement) {
        switchLlWordPicker(ON);
        initNpWord(replacement);
        ivYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replacement.setSelectedReplacementIndex(npWord.getValue());
                refreshViews();
                switchLlWordPicker(OFF);
            }
        });
        ivNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLlWordPicker(OFF);
            }
        });
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

    private void switchLlWordPicker(boolean onOrOff) {
        if (onOrOff == ON) {
            if (llWordPicker.getVisibility() == View.INVISIBLE) {
                llWordPicker.setVisibility(View.VISIBLE);
                effectUtil.activateViewAnimations(llWordPicker, AnimationUtil.getUpAppearAnimations(), ALERT_DURATION);
            }
        } else {
            llWordPicker.setVisibility(View.INVISIBLE);
            effectUtil.activateViewAnimations(llWordPicker, AnimationUtil.getDownDisappearAnimations(), ALERT_DURATION);
        }
    }

    private void initNpWord(Quote.Replacement replacement) {
        npWord.setDisplayedValues(new OtherUtil().getStringArrayFromList(replacement.wordList));
        npWord.setMinValue(0);
        npWord.setMaxValue(replacement.wordList.size() - 1);
        npWord.setValue(replacement.getSelectedReplacementIndex());
        npWord.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止跳出鍵盤
    }
}
