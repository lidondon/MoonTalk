package com.social.feeling.moontalk.item;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk._interface.IView;
import com.social.feeling.moontalk.datamodel.PersonData;
import com.social.feeling.moontalk.global.FriendAndGroup;

/**
 * Created by lidondon on 2018/2/28.
 */

public class RecipientItem implements IView {
    private static final int DURATION = 1000;
    private static final int UNKNOWN = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private Context context;
    private View resultView;
    private PersonData personData;
    private ImageView ivIcon;
    private TextView tvName;
    private Button btnReceive;
    private Handler handlerReceive;

    public RecipientItem(Context ctx, PersonData pd, Handler h) {
        context = ctx;
        personData = pd;
        handlerReceive = h;
    }

    private LayoutInflater getInflater() {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        resultView = getInflater().inflate(R.layout.item_recipient, null);
        getViews(resultView);
        setContent();
        btnReceive.setOnClickListener(getBtnReceiveOnClickListener());

        return resultView;
    }


    private void setContent() {
        if (personData != null) {
            if (personData.photoUrl == null) {
                ivIcon.setImageResource(R.drawable.no_man);
            } else {

            }
            tvName.setText(personData.name);
        }
    }

    private void getViews(View rootView) {
        ivIcon = (ImageView) rootView.findViewById(R.id.ivIcon);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        btnReceive = (Button) rootView.findViewById(R.id.btnReceive);
    }





    private View.OnClickListener getBtnReceiveOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendAndGroup.getInstance(context).receiveInvitation(personData.account, handlerReceive);
            }
        };
    }



}

