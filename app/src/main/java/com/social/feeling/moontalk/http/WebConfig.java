package com.social.feeling.moontalk.http;

/**
 * Created by lidondon on 2017/4/25.
 */

public abstract  class WebConfig {
    public static final String RPC_GATEWAY = "http://iamfeeling.ddns.net:8080/MoonTalk/Gateway.jsp";
    public static final int READ_TIMEOUT = 5000;
    public static final int CONNECT_TIMEOUT = 10000;
    //server v2
    public static final String SERVER = "http://triworld.ddns.net:8086";
    public static final String GATEWAY = "/app-gateway/func";
    public static final String GET_LOGIN_DATA = SERVER + GATEWAY + "/post/110";
    public static final String GET_USER_INFO = SERVER + GATEWAY + "/post/112";
    public static final String GET_QUOTES = SERVER + GATEWAY + "/get/14.json";
    //public static final String GET_FEELINGS = SERVER + GATEWAY + "/get/12.json";
    public static final String BEGIN_POST = SERVER + GATEWAY + "/post/120";
    public static final String POST_PHOTO = SERVER + GATEWAY + "/post/121";
    public static final String POST_ARTICLE = SERVER + GATEWAY + "/post/122";
    public static final String STOP_POST = SERVER + GATEWAY + "/post/125";
    public static final String GET_ALL_FEELINGS_ID = SERVER + GATEWAY + "/post/126";
    public static final String GET_FEELINGS_BY_IDS = SERVER + GATEWAY + "/post/127";
    public static final String MODIFY_USER_PHOTO = SERVER + GATEWAY + "/post/130";
    public static final String MODIFY_USER_NAME = SERVER + GATEWAY + "/post/131";
    public static final String INVITE_FRIEND = SERVER + GATEWAY + "/post/140";
    public static final String ACCEPT_FRIEND = SERVER + GATEWAY + "/post/141";
    public static final String QUERY_FRIEND = SERVER + GATEWAY + "/post/142";
    public static final String QUERY_INVITER = SERVER + GATEWAY + "/post/143";
    public static final String QUERY_INVITEE = SERVER + GATEWAY + "/post/144";


    public static final String GET_COOKIE = "Set-Cookie";
    public static final String SET_COOKIE = "Cookie";

    public class Response {
        public static final int ALREADY_LOGIN = 2;
        public static final int SUCCESS = 0;
        public static final int FAIL = -1;
        public static final String ERROR = "err";
    }
}
