package com.social.feeling.moontalk.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by vincentchan on 2017/3/6.
 */

public class PostManager {
    private static PostManager mInstance = null;

    private static final String RPC_GATEWAY = "http://iamfeeling.ddns.net:8080/MoonTalk/Gateway.jsp";
    private Context mActivity = null;
    private IAccountEventListener mAccountEventListener;
    private String mCookie = null;
    private String mAccount = null, mPasswd = null;

    public static PostManager getManager(Context app) {
        if (mInstance == null) {
            mInstance = new PostManager(app);
        }

        return mInstance;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case IAccountEventListener.RPC.LOGIN:
                    mAccountEventListener.onLoginResponse(null);
                    break;
                case IAccountEventListener.RPC.CHECK_ACCOUNT:
                    mAccountEventListener.onCheckAccountResponse(msg.obj.toString());
                    break;
                case IAccountEventListener.RPC.REGISTER:
                    //mAccountEventListener.onRegisterResponse(msg.obj.toString());
                    break;
                case IAccountEventListener.LOCAL.GET_SESSION:
                    mAccountEventListener.onSessionResponse(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    public PostManager(Context app) {
        mActivity = app;
    }
    public void setAccountEventListener (IAccountEventListener listener) {
        mAccountEventListener = listener;
    }

    public void getSession() {
        Message msg = Message.obtain();
        msg.obj = mCookie==null ? "error" : mCookie;
        msg.arg1 = IAccountEventListener.LOCAL.GET_SESSION;
        handler.sendMessage(msg);
    }
}
