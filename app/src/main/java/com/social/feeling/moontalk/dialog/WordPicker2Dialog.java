package com.social.feeling.moontalk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.util.OtherUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Quote;

/**
 * Created by lidondon on 2016/11/22.
 */
public class WordPicker2Dialog extends Dialog {
    private Context context;
    private Button btnCommit;
    private Button btnCancel;
    private NumberPicker npWord;
    private int replacementIndex;
    private Quote.Replacement replacement;
    private IWordPickerMission iWordPickerMission;

    public interface IWordPickerMission {
        public void doMission();
    }

    public WordPicker2Dialog(Context ctx, Quote.Replacement r, IWordPickerMission iwpm) {
        super(ctx);
        replacement = r;
        iWordPickerMission = iwpm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initcontentView();
        findViews();
        initNpWord();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnCommit.setOnClickListener(getBtnCommitOnClickListener());
    }

    private void initcontentView() {
        if (replacement != null && replacement.wordList != null && replacement.wordList.size() > 0) {
            TextView tvWord1 = (TextView) findViewById(R.id.tvWord1);
            TextView tvWord2 = (TextView) findViewById(R.id.tvWord2);

            setTvWordOnClickListener(tvWord1, 0);
            setTvWordOnClickListener(tvWord2, 1);
            switch (replacement.wordList.size()) {
                case 3:
                    TextView tvWord3 = (TextView) findViewById(R.id.tvWord3);

                    setContentView(R.layout.dialog_word_picker_2_3);
                    break;
                case 4:
                    TextView tvWord4 = (TextView) findViewById(R.id.tvWord4);

                    setContentView(R.layout.dialog_word_picker_2_4);
                    break;
                default:
                    setContentView(R.layout.dialog_word_picker_2_2);
                    break;
            }
        }
    }

    private void setTvWordOnClickListener(TextView tvWord, final int index) {
        tvWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replacement.setSelectedReplacementIndex(index);
                if (iWordPickerMission != null) {
                    iWordPickerMission.doMission();
                }
                dismiss();
            }
        });
    }

    private void initNpWord() {
        npWord.setDisplayedValues(new OtherUtil().getStringArrayFromList(replacement.wordList));
        npWord.setMinValue(0);
        npWord.setMaxValue(replacement.wordList.size() - 1);
        npWord.setValue(replacement.getSelectedReplacementIndex());
        npWord.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止跳出鍵盤
    }

    private View.OnClickListener getBtnCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replacement.setSelectedReplacementIndex(npWord.getValue());
                iWordPickerMission.doMission();
                dismiss();
            }
        };
    }

    private void findViews() {
        btnCommit = (Button) findViewById(R.id.btnCommit);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        npWord = (NumberPicker) findViewById(R.id.npWord);
    }
}
