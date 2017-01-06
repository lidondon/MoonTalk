package com.social.feeling.moontalk.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.activity.BaseActivity;
import com.empire.vmd.client.android_lib.extendview.AutoChangeLineLayout;
import com.empire.vmd.client.android_lib.util.OtherUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Quote;
import com.social.feeling.moontalk.global.PostFeeling;

import org.w3c.dom.Text;

import java.util.List;

public class WordPickerActivity extends BaseActivity {
    public static final String REPLACEMENT_INDEX = "replacementIndex";
    public int textSize = 20;
    private AutoChangeLineLayout autoChangeLineLayout;
    private PostFeeling postFeeling = PostFeeling.getInstance();
    private Quote quote = postFeeling.feeling.quote;
    private int replacementIndex;
    private TextView tvCommit;
    private TextView tvCancel;
    private NumberPicker wordPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_picker);
        postFeeling.addActivity(this);
        findViews();
        getExtrasData();
        initComponent();
        tvCommit.setOnClickListener(getTvCommitOnclickListener());
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getExtrasData() {
        Bundle bundle = getIntent().getExtras();

        //quote = (Quote) bundle.getSerializable(Quote.CLASS_NAME);
        replacementIndex = bundle.getInt(REPLACEMENT_INDEX);
    }

    private void findViews() {
        autoChangeLineLayout = (AutoChangeLineLayout) findViewById(R.id.autoChangeLineLayout);
        tvCommit = (TextView) findViewById(R.id.tvCommit);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
    }

    private View.OnClickListener getTvCommitOnclickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordPickerActivity.this, ReplacementPickerActivity.class);
                //Bundle bundle = new Bundle();

                quote.replacementList.get(replacementIndex).setSelectedIndex(wordPicker.getValue());
//                bundle.putSerializable(Quote.CLASS_NAME, quote);
//                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        };
    }

    private void initComponent() {
        if (quote !=null && quote.replacementList.size() > 0) {
            Quote.Replacement replacement = quote.replacementList.get(replacementIndex);
            String former = (replacement.startIndex == 0) ? null : quote.currentText.substring(0, replacement.startIndex - 1);
            String latter = quote.currentText.substring(replacement.startIndex + replacement.length + quote.getSoFarDelta(replacementIndex));

            if (former != null) {
                addText(former);
            }
            wordPicker = getWordPicker(replacement.wordList, replacement.getSelectedIndex());
            autoChangeLineLayout.addView(wordPicker);
            addText(latter);
        }
    }

    private NumberPicker getWordPicker(List<String> wordList, int index) {
        NumberPicker result = new NumberPicker(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        result.setDisplayedValues(new OtherUtil().getStringArrayFromList(wordList));
        result.setMinValue(0);
        result.setMaxValue(wordList.size() - 1);
        result.setLayoutParams(params);
        result.setValue(index);

        return result;
    }

    private void addText(String text) {
        for (int i = 0; i < text.length(); i++) {
            autoChangeLineLayout.addView(getTextView(text.substring(i, i + 1)));
        }
    }

    private TextView getTextView(String text) {
        TextView textView = new TextView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        textView.setTextSize(textSize);
        textView.setText(text);
        textView.setLayoutParams(params);

        return textView;
    }
}
