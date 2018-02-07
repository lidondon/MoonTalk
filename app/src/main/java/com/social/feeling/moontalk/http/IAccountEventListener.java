package com.social.feeling.moontalk.http;

import java.util.HashMap;

/**
 * Created by vincentchan on 2017/1/22.
 */

public interface IAccountEventListener {
    public void onLoginResponse(HashMap<String, String> map);
    public void onSessionResponse(String json);
    public void onGetAccountInfoResponse(HashMap<String, String> map);
    public void onLogoutResponse(String json);
    public void onCheckAccountResponse(String json);
    public void onRegisterResponse(HashMap<String, String> map);
    public void onUploadAccountPictureResponse(String json);
    public void onResetPasswordResponse(String json);
    public void onModifyPasswordResponse(String json);
    public void onModifyEmailResponse(String json);
    public void onAuthenticationResponse(String json);
    public void noResponse();

    public class LOCAL {
        public static final int GET_SESSION = 100;
    }

    public class RPC {
        public static final int LOGIN = 0;
        public static final int GET_ACCOUNT_INFO = 1;
        public static final int LOGOUT = 2;
        public static final int CHECK_ACCOUNT = 3;
        public static final int REGISTER = 4;
        public static final int UPLOAD_ACCOUNT_PICTURE = 5;
        public static final int RESET_PASSWORD = 6;
        public static final int MODIFY_PASSWORD = 7;
        public static final int MODIFY_EMAIL = 8;
        public static final int AUTHENTICATION = 9;
    }

    public abstract class Parameters {
        public static final String TYPE = "f";
        public static final String SESSION = "session";
        public static final String ACCOUNT = "user";
        public static final String PASSWORD = "passwd";
        public static final String NICKNAME = "nickname";
        public static final String EMAIL = "email";
    }
}
