package com.empire.vmd.client.android_lib.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.empire.vmd.client.android_lib.R;
import com.empire.vmd.client.android_lib.dialog.OneEditTextDialog;

/**
 * Created by lidondon on 2016/11/30.
 */
public class BaseFragmentActivity extends FragmentActivity {
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isTaskRoot()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                DialogInterface.OnClickListener positiveOnClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                };
                DialogInterface.OnClickListener negativeOnClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                builder.setTitle(getResources().getString(R.string.sure_finish_app));
                builder.setPositiveButton(R.string.commit, positiveOnClickListener);
                builder.setNegativeButton(R.string.cancel, negativeOnClickListener);
                builder.create().show();
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
