package com.social.feeling.moontalk.http;

/**
 * Created by vincentchan on 2017/3/6.
 */

public interface IPostEventListener {
    public void onBeginPostResponse(String json);
    public void onPostTextResponse(String json);
    public void onPostImageResponse(String json);
    public void onGetFeelingListResponse(String json);
    public void onGetFeelingTextResponse(String json);
    public void onGetFeelingImageResponse(String json);
    public void onEndPostResponse(String json);

    public class RPC {
        public static final int BEGIN_POST = 0;
        public static final int POST_TEXT = 1;
        public static final int POST_IMAGE = 2;
        public static final int POST_FEELING_LIST = 3;
        public static final int POST_FEELING_TEXT = 4;
        public static final int GET_FEELING_IMAGE = 5;
        public static final int END_POST = 6;
    }
}
