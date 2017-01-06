package com.empire.vmd.client.android_lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.R;

/**
 * Created by lidondon on 2016/2/4.
 */
public class OneEditTextDialog extends Dialog {
    private Context context;
    private IMission iMission;
    private TextView tvMessage;
    private EditText etInput;
    private Button btnCommit;
    private Button btnCancel;
    private String message;

    public interface IMission {
        public void executeMission(String numberStr);
    }

    public OneEditTextDialog(Context ctx, String msg, IMission im) {
        super(ctx);
        context = ctx;
        message = msg;
        iMission = im;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_one_edit_text);
        getViews();
        tvMessage.setText(message);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etInput.getText().toString().isEmpty()) {
                    iMission.executeMission(etInput.getText().toString());
                    dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void getViews() {
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        etInput = (EditText) findViewById(R.id.etInput);
        btnCommit = (Button) findViewById(R.id.btnCommit);
        btnCancel = (Button) findViewById(R.id.btnCancel);
    }
}
