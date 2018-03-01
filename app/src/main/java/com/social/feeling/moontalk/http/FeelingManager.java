package com.social.feeling.moontalk.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.empire.vmd.client.android_lib.httpproxy.MultipartRequest;
import com.empire.vmd.client.android_lib.httpproxy.VolleyRequest;
import com.social.feeling.moontalk.activity.PreparePostActivity;
import com.social.feeling.moontalk.datamodel.Feeling;
import com.social.feeling.moontalk.global.LoginData;

import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lidondon on 2017/4/20.
 */

public class FeelingManager {
    private static final String TAG = "FeelingManager";
    public static final String ERR = "err";
    private static final String ARTICLE = "article";
    private static final String PHOTO = "photoUrl";
    public static final String IDS = "ids";
    private static volatile FeelingManager feelingManager;
    private Context context;
    private IPostEventListener iPostEventListener;
    private LoginData loginData;

    public class Response {
        public static final int SUCCESS = 0;
        public static final int NOT_LOGIN = -1;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                switch (msg.arg1) {
                    case IPostEventListener.RPC.BEGIN_POST:
                        iPostEventListener.onBeginPostResponse(msg.obj.toString());
                        break;
                    case IPostEventListener.RPC.POST_TEXT:
                        iPostEventListener.onPostTextResponse(msg.obj.toString());
                        break;
                    case IPostEventListener.RPC.POST_IMAGE:
                        iPostEventListener.onPostImageResponse(msg.obj.toString());
                        break;
                    case IPostEventListener.RPC.POST_FEELING_LIST:
                        iPostEventListener.onGetFeelingListResponse(msg.obj.toString());
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public static FeelingManager getInstance(Context ctx) {
        if (feelingManager == null) {
            synchronized (FeelingManager.class) {
                if (feelingManager == null) {
                    feelingManager = new FeelingManager(ctx);
                }
            }
        }

        return feelingManager;
    }

    private FeelingManager(Context ctx) {
        context = ctx;
        loginData = LoginData.getInstance(context);
    }

    public void setIPostEventListener (IPostEventListener ipel) {
        iPostEventListener = ipel;
    }

    /* post feeling start */
    public void beginPost(final Feeling feeling, final Handler h) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                Log.e(TAG, "beginPost: " + strResponse);
                try {
                    JSONObject jo = new JSONObject(strResponse);
                    int response = jo.getInt(ERR);

                    if (response == AccountManager.Response.SUCCESS) {
                        postArticle(feeling, h);
                    } else {
                        Message message = Message.obtain();

                        message.obj = response;
                        h.handleMessage(message);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "beginPost catch: " + e.toString());
                }

            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "beginPost error: " + error.toString());
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.BEGIN_POST, listener, errorListener) {

//            @Override
//            protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
//                Log.e(TAG, response.headers.get(WebConfig.GET_COOKIE));
//                loginData.session = response.headers.get(WebConfig.GET_COOKIE);
//
//                return super.parseNetworkResponse(response);
//            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();

                headers.put(WebConfig.SET_COOKIE, loginData.session);

                return headers;
            }
        };

        volleyRequest.addRequest(stringRequest);
    }

    public void postArticle(final Feeling feeling, final Handler h) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        final List<String> photoUploadList = new ArrayList<String>();
        final Handler photoCompleteHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                photoUploadList.add("ok");
                if (photoUploadList.size() == feeling.checkedPhotoList.size()) {
                    stopPost(h);
                }
            }
        };
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                Log.e(TAG, "postArticle: " + strResponse);
                try {
                    JSONObject jo = new JSONObject(strResponse);
                    int response = jo.getInt(ERR);

                    if (response == AccountManager.Response.SUCCESS) {
                        for (String photoPath : feeling.checkedPhotoList) {
                            postPhoto(photoPath, photoCompleteHandler);
                        }
                    } else {
                        Message message = Message.obtain();

                        message.obj = response;
                        h.handleMessage(message);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "postArticle catch: " + e.toString());
                }

            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "postArticle error: " + error.toString());
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.POST_ARTICLE, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> result = new HashMap<String, String>();
                try {
                    String ss = feeling.toJSONObject().toString();
                    result.put(ARTICLE, feeling.toJSONObject().toString());
                } catch (Exception e) {
                    Log.e("COCO", e.toString());
                }


                return result;
            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put(WebConfig.SET_COOKIE, loginData.session);

                return headers;
            }
        };

        volleyRequest.addRequest(stringRequest);
    }

    private void postPhoto(String photoPath, final Handler h) {
        File file = new File(photoPath);
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    Log.e(TAG, "postPhoto: " + strResponse);
                    JSONObject jo = new JSONObject(strResponse);
                    int response = jo.getInt(ERR);

                    if (response == Response.SUCCESS) {
                        h.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "postPhoto catch: " + e.toString());
                }
            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("XXX", "postPhoto error: " + error.toString());
            }
        };
        MultipartRequest request = new MultipartRequest(WebConfig.POST_PHOTO, listener, errorListener, PHOTO, file, null) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put(AccountManager.USER_AGENT, AccountManager.USER_AGENT_VALUE);
                headers.put(WebConfig.SET_COOKIE, loginData.session);

                return headers;
            }
        };

        VolleyRequest.getInstance(context).addRequest(request);
    }

    private void stopPost(final Handler h) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                Log.e(TAG, "stopPost: " + strResponse);
                try {
                    JSONObject jo = new JSONObject(strResponse);
                    int response = jo.getInt(ERR);
                    Message message = Message.obtain();

                    message.obj = response;
                    h.handleMessage(message);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "stopPost error: " + error.toString());
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.STOP_POST, listener, errorListener) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put(WebConfig.SET_COOKIE, loginData.session);

                return headers;
            }
        };

        volleyRequest.addRequest(stringRequest);
    }
    /* post feeling end */
}
