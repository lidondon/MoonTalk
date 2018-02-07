package com.empire.vmd.client.android_lib.component;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.R;
import com.empire.vmd.client.android_lib.extendview.DrawHookView;

import org.w3c.dom.Text;

/**
 * Created by lidondon on 2016/6/4.
 */
public class CompleteHookComponent {
    private Context context;
    private LinearLayout llDhv;
    private TextView tvMessage;
    private String message;
    private Handler exteriorHandler;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tvMessage.setText(message);
        }
    };

    public CompleteHookComponent(Context ctx, String msg, Handler exHandler) {
        context = ctx;
        message = msg;
        exteriorHandler = exHandler;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView() {
        View resultView = getInflater().inflate(R.layout.component_complete_hook, null);
        final DrawHookView.ICompleteMission iCompleteMission;
        DrawHookView drawHookView = new DrawHookView(context);
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (message != null) {
                    handler.sendEmptyMessage(0);
                }

                if (exteriorHandler != null) {
                    try {
                        Thread.sleep(1000);
                        exteriorHandler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        Log.e(getClass().getName(), e.toString());
                    }
                }
            }
        };

        findViews(resultView);
        iCompleteMission = new DrawHookView.ICompleteMission() {
            @Override
            public void mission() {
                new Thread(runnable).start();
            }
        };
        drawHookView.setiCompleteMission(iCompleteMission);
        llDhv.addView(drawHookView);

        return resultView;
    }

    private void findViews(View rootView) {
        llDhv = (LinearLayout) rootView.findViewById(R.id.llDhv);
        tvMessage = (TextView) rootView.findViewById(R.id.tvMessage);
    }
}
