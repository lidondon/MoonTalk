package com.social.feeling.moontalk.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.empire.vmd.client.android_lib.httpproxy.HttpRequest;
import com.empire.vmd.client.android_lib.httpproxy.VolleyRequest;
import com.social.feeling.moontalk.datamodel.PersonData;
import com.social.feeling.moontalk.global.LoginData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.social.feeling.moontalk.http.WebConfig.RPC_GATEWAY;

/**
 * Created by vincentchan on 2017/1/22.
 */

public class AccountManager {
    private static final String TAG = "AccountManager";
    // User agent取消了
    public static final String USER_AGENT = "User-Agent";
    public static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String ERR = "err";
    private static volatile AccountManager accountManager;
    private HttpRequest httpRequest;
    private Context context;
    private IAccountEventListener mAccountEventListener;
    private String mCookie;
//    private String account;
//    private String password;
    private LoginData loginData;

    public class Response {
        public static final int ALREADY_LOGIN = 2;
        public static final int SUCCESS = 0;
        public static final int FAIL = -1;
        public static final String ERROR = "err";
    }

    public static AccountManager getInstance(Context ctx) {
        if (accountManager == null) {
            synchronized (AccountManager.class) {
                if (accountManager == null) {
                    accountManager = new AccountManager(ctx);
                }
            }
        }

        return accountManager;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mAccountEventListener != null) {
                if (msg.obj != null) {
                    HashMap<String, String> map = (HashMap<String, String>) msg.obj;
                    String strJson = map.get(HttpRequest.RESPONSE);
                    String session = map.get(HttpRequest.SESSION);

                    if (session != null) {
                        ServerResponse.session = session;
                    }

                    switch (msg.arg1) {
                        case IAccountEventListener.RPC.LOGIN:
                            mAccountEventListener.onLoginResponse(map);
                            break;
                        case IAccountEventListener.RPC.GET_ACCOUNT_INFO:
                            mAccountEventListener.onGetAccountInfoResponse(map);
                            break;
                        case IAccountEventListener.RPC.CHECK_ACCOUNT:
                            mAccountEventListener.onCheckAccountResponse(strJson);
                            break;
                        case IAccountEventListener.RPC.REGISTER:
                            mAccountEventListener.onRegisterResponse(map);
                            break;
                        case IAccountEventListener.LOCAL.GET_SESSION:
                            mAccountEventListener.onSessionResponse(strJson);
                            break;
                        default:
                            break;
                    }
                } else {
                    mAccountEventListener.noResponse();
                }
            }
        }
    };

    private AccountManager(Context ctx) {
        context = ctx;
        httpRequest = new HttpRequest(WebConfig.READ_TIMEOUT, WebConfig.CONNECT_TIMEOUT);
        loginData = LoginData.getInstance(context);
    }

    public void setAccountEventListener (IAccountEventListener listener) {
        mAccountEventListener = listener;
    }

    public void register(String account, String password, String nickname, String email) {
//        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
//        postParams.add(new BasicNameValuePair("f", Integer.toString(IAccountEventListener.RPC.REGISTER)));
//        postParams.add(new BasicNameValuePair("user", account));
//        postParams.add(new BasicNameValuePair("password", password));
//        postParams.add(new BasicNameValuePair("nickname", nickname));
//        postParams.add(new BasicNameValuePair("email", email));
        //sendHttpRequest(RPC_GATEWAY, IAccountEventListener.RPC.REGISTER, postParams);

        HashMap<String, String> mapParameter = new HashMap<String, String>();
        HashMap mapHeader = new HashMap();

        mapParameter.put(IAccountEventListener.Parameters.TYPE, IAccountEventListener.RPC.REGISTER + "");
        mapParameter.put(IAccountEventListener.Parameters.ACCOUNT, account);
        mapParameter.put(IAccountEventListener.Parameters.PASSWORD, password);
        mapParameter.put(IAccountEventListener.Parameters.NICKNAME, nickname);
        mapParameter.put(IAccountEventListener.Parameters.EMAIL, email);
        //mapHeader.put(USER_AGENT, USER_AGENT_VALUE);
        httpRequest.sendHttpRequest(RPC_GATEWAY, HttpRequest.POST, handler, new int[] { IAccountEventListener.RPC.REGISTER  }
                , mapParameter, mapHeader, WebConfig.SET_COOKIE);
    }

    /*
    public void login(String account, String password) {
        boolean org = false;
//        account = acc;
//        password = pw;
        if (org) {
//            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
//            postParams.add(new BasicNameValuePair("f", Integer.toString(IAccountEventListener.RPC.LOGIN)));
//            postParams.add(new BasicNameValuePair("user", account));
//            postParams.add(new BasicNameValuePair("passwd", passwd));
//            sendHttpRequest(RPC_GATEWAY, IAccountEventListener.RPC.LOGIN, postParams);
        } else {
            HashMap mapParameter = new HashMap();
            HashMap mapHeader = new HashMap();

            mapParameter.put(IAccountEventListener.Parameters.TYPE, IAccountEventListener.RPC.LOGIN + "");
            mapParameter.put(IAccountEventListener.Parameters.ACCOUNT, account);
            mapParameter.put(IAccountEventListener.Parameters.PASSWORD, password);
            //mapHeader.put(USER_AGENT, USER_AGENT_VALUE);
            httpRequest.sendHttpRequest(RPC_GATEWAY, HttpRequest.POST, handler, new int[] { IAccountEventListener.RPC.LOGIN }
                    , mapParameter, mapHeader, SET_COOKIE);
            //String url = RPC_GATEWAY + "?f=0&user=test&passwd=123";
            //mCookie = new HttpRequest().sendHttpRequest(url, HttpRequest.GET, IAccountEventListener.RPC.LOGIN, mapParameter, handler, mapHeader);
        }
    }
    */

    public void getAccountInfo(String session, String account) {
        HashMap mapParameter = new HashMap();
        HashMap mapHeader = new HashMap();

        mapParameter.put(IAccountEventListener.Parameters.TYPE, IAccountEventListener.RPC.GET_ACCOUNT_INFO + "");
        mapParameter.put(IAccountEventListener.Parameters.ACCOUNT, account);
        mapParameter.put(IAccountEventListener.Parameters.SESSION, session);
        //mapHeader.put(USER_AGENT, USER_AGENT_VALUE);
        //fake
        Message message = Message.obtain();
        message.arg1 = IAccountEventListener.RPC.GET_ACCOUNT_INFO;
        message.obj = new HashMap<String, String>();
        handler.sendMessage(message);
//        httpRequest.sendHttpRequest(RPC_GATEWAY, HttpRequest.POST, handler, new int[] { IAccountEventListener.RPC.GET_ACCOUNT_INFO }
//                , mapParameter, mapHeader, SET_COOKIE);
    }

    public void getSession() {
        Message msg = Message.obtain();

        msg.obj = mCookie==null ? "error" : mCookie;
        msg.arg1 = IAccountEventListener.LOCAL.GET_SESSION;
        handler.sendMessage(msg);
    }

    public void checkAccount(String account) {
//        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
//        postParams.add(new BasicNameValuePair("f", Integer.toString(IAccountEventListener.RPC.CHECK_ACCOUNT)));
//        postParams.add(new BasicNameValuePair("user", account));
        //sendHttpRequest(RPC_GATEWAY, IAccountEventListener.RPC.CHECKACCOUNT, postParams);
    }

//    private void sendHttpRequest(final String url, final int rpc, final List<NameValuePair> postParams) {
//        new AsyncTask<Void, Void, Void>() {
//            //@Override
//            protected Void doInBackground(Void... params) {
//                HttpClient client = new DefaultHttpClient();
//                HttpPost post = new HttpPost(url);
//                post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
//                if (mCookie != null)
//                    post.setHeader("Cookie", mCookie);
//                try {
//                    //宣告UrlEncodedFormEntity來編碼POST，指定使用UTF-8
//                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(postParams, HTTP.UTF_8);
//                    post.setEntity(ent);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                String message = null;
//                try {
//                    HttpResponse response = client.execute(post);
//                    HttpEntity entity = response.getEntity();
//                    message = EntityUtils.toString(entity);
//                    Header cookie = (Header) response.getFirstHeader("Set-Cookie");
//                    if (cookie != null) {
//                        message += "\nresponse session: " + cookie.getValue();
//                        if (mCookie == null) mCookie = cookie.getValue();
//                    }
//                    //message += "\nmCookie: " + mCookie;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    message = "IOException";
//                }
//                Message msg = Message.obtain();
//                msg.obj = message;
//                msg.arg1 = rpc;
//                handler.sendMessage(msg);
//
//                return null;
//            }
//        }.execute();
//    }

    public void login(final String account, final String password,final Handler h) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                Log.e(TAG, strResponse);
                try {
                    JSONObject jo = new JSONObject(strResponse);
                    int response = jo.getInt(ERR);

                    if (response == Response.SUCCESS) {
                        getLoginData(h);
                        //for test
//                        loginData.personData = new PersonData();
//                        loginData.personData.name = "假的";
//                        loginData.personData.account = "假的";
//                        Message msg = Message.obtain();
//                        msg.obj = "0";
//                        h.sendMessage(msg);
                        //for test end
                    } else {
                        Message message = Message.obtain();

                        message.obj = response;
                        h.handleMessage(message);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "login: " + e.toString());
                }

            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.GET_LOGIN_DATA, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> result = new HashMap<String, String>();

//                result.put("username", account);
//                result.put("password", password);
                result.put(ACCOUNT, "test1@abc.com");
                result.put(PASSWORD, "123456");

                return result;
            }

            @Override
            protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
                Log.e(TAG, response.headers.get(WebConfig.GET_COOKIE));
                loginData.session = response.headers.get(WebConfig.GET_COOKIE);

                return super.parseNetworkResponse(response);
            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                //headers.put(AccountManager.USER_AGENT, AccountManager.USER_AGENT);

                return headers;
            }
        };

        volleyRequest.addRequest(stringRequest);
    }

    public void getLoginData(final Handler h) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        com.android.volley.Response.Listener<JSONObject> listener = new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jo) {
                try {
                    int resCode = jo.getInt(ERR);
                    Message message = Message.obtain();

                    if (resCode == Response.SUCCESS) {

                        loginData.personData = new PersonData();
                        loginData.personData.name = jo.getString(NAME);
                        loginData.personData.account= jo.getString(ACCOUNT);
                        //loginData.personData.account= jo.getString(EMAIL);
                    }
                    message.obj = resCode;
                    h.handleMessage(message);
                } catch (Exception e) {
                    Log.e(TAG, "getLoginData: " + e.toString());
                }
            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(getClass().toString(), error.toString());
            }
        };
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, WebConfig.GET_USER_INFO, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> result = new HashMap<String, String>();

                result.put(ACCOUNT, "test1@abc.com");
                result.put(PASSWORD, "123456");

                return result;
            }

//            @Override
//            protected com.android.volley.Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                Log.e("VVV", response.headers.get(WebConfig.GET_COOKIE));
//
//
//                return super.parseNetworkResponse(response);
//            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                //headers.put(AccountManager.USER_AGENT, AccountManager.USER_AGENT); //之後改成不需要了
                headers.put(WebConfig.SET_COOKIE, loginData.session);

                return headers;
            }
        };

        volleyRequest.addRequest(jsonArrayRequest);
    }
}
