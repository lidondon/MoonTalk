package com.social.feeling.moontalk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;

import com.empire.vmd.client.android_lib.util.OtherUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.Quote;

/**
 * Created by lidondon on 2016/8/2.
 */
public class WordPickerDialog extends Dialog {
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

    public WordPickerDialog(Context ctx, Quote.Replacement r, IWordPickerMission iwpm) {
        super(ctx);
        replacement = r;
        iWordPickerMission = iwpm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_word_picker);
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

    private void initNpWord() {
        npWord.setDisplayedValues(new OtherUtil().getStringArrayFromList(replacement.wordList));
        npWord.setMinValue(0);
        npWord.setMaxValue(replacement.wordList.size() - 1);
        npWord.setValue(replacement.getSelectedIndex());
        npWord.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止跳出鍵盤
    }

    private View.OnClickListener getBtnCommitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replacement.setSelectedIndex(npWord.getValue());
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
