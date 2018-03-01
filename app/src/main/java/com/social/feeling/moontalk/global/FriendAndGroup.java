package com.social.feeling.moontalk.global;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.empire.vmd.client.android_lib.httpproxy.VolleyRequest;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.groupOfFriend;
import com.social.feeling.moontalk.datamodel.PersonData;
import com.social.feeling.moontalk.http.AccountManager;
import com.social.feeling.moontalk.http.WebConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lidondon on 2016/8/22.
 */
public class FriendAndGroup {
    private static final String TAG = "FriendAndGroup";
    public static final String REQUESTOR = "requestor";
    public static final String RECIPIENT = "recipient";
    private static final String INVITEE = "invitee";
    private static final String FRIEND = "friend";
    private static final String DATA = "data";
    public static final int FRIEND_GROUP_INDEX = 0;
    public static final int REQUESTOR_INDEX = 1;
    public static final int RECIPIENT_INDEX = 2;
    public static final int FRIEND_INDEX = 3;
    private volatile static FriendAndGroup friendAndGroup;
    private Context context;
    private List<PersonData> friendList;
    private List<PersonData> recipientList;
    private List<PersonData> requesterList;
    private List<groupOfFriend> groupOfFriendList;
    public List<ListInfo> listOfList = new ArrayList<>();

    public class ListInfo {
        public String name;
        public List list;
    }

    private FriendAndGroup(Context ctx) {
        context = ctx;
        refreshAllData(null);
    }

    private void initListOfList() {
        ListInfo liGroupOfFriend = new ListInfo();
        ListInfo liRequester = new ListInfo();
        ListInfo liRecipient = new ListInfo();
        ListInfo liFriend = new ListInfo();

        liGroupOfFriend.name = context.getResources().getString(R.string.group);
        liRequester.name = context.getResources().getString(R.string.request);
        liRecipient.name = context.getResources().getString(R.string.recipient);
        liFriend.name = context.getResources().getString(R.string.friend);
        listOfList.clear();
        listOfList.add(liGroupOfFriend);
        listOfList.add(liRequester);
        listOfList.add(liRecipient);
        listOfList.add(liFriend);
    }

    public void refreshAllData(Handler handler) {
        friendList = new ArrayList<PersonData>();
        recipientList = new ArrayList<PersonData>();
        requesterList = new ArrayList<PersonData>();
        groupOfFriendList = new ArrayList<groupOfFriend>();
//        queryFriend(null);
//        queryRequester(null);
//        queryRecipient(null);
        queryFriendManagement(handler);
        initListOfList();
    }

    public static FriendAndGroup getInstance(Context ctx) {
        if (friendAndGroup == null) {
            synchronized (LoginData.class) {
                if (friendAndGroup == null) {
                    friendAndGroup = new FriendAndGroup(ctx);
                }
            }
        }

        return friendAndGroup;
    }

    public void makeFakeFriends() {
//        PersonData f1 = new PersonData();
//        PersonData f2 = new PersonData();
//        PersonData f3 = new PersonData();
//        PersonData f4 = new PersonData();
//        PersonData f5 = new PersonData();
//        PersonData f6 = new PersonData();
//        PersonData f7 = new PersonData();
//
//        f1.account = "Jean";
//        f2.account = "Michael";
//        f3.account = "Vincent";
//        f4.account = "Sister";
//        f5.account = "Lai";
//        f6.account = "Coco";
//        f7.account = "Dodo";
//        f1.name = "小林";
//        f2.name = "大林";
//        f3.name = "小Ｖ";
//        f4.name = "姊姊";
//        f5.name = "BOSS";
//        f6.name = "可可";
//        f7.name = "多多";
//        friendList.add(f1);
//        friendList.add(f2);
//        friendList.add(f3);
//        friendList.add(f4);
//        friendList.add(f5);
//        friendList.add(f6);
//        friendList.add(f7);
    }

    private void makeFakeGroup() {
//        groupOfFriend g1 = new groupOfFriend();
//        groupOfFriend g2 = new groupOfFriend();
//        groupOfFriend g3 = new groupOfFriend();
//
//        g1.id = "Lin";
//        g2.id = "Family";
//        g3.id = "Dog";
//        g1.name = "林";
//        g2.name = "林家宗親";
//        g3.name = "狗狗團";
//        g1.friendList = new ArrayList<>();
//        g1.friendList.add(friendList.get(0));
//        g1.friendList.add(friendList.get(1));
//        g2.friendList = new ArrayList<>();
//        g2.friendList.add(friendList.get(0));
//        g2.friendList.add(friendList.get(1));
//        g2.friendList.add(friendList.get(2));
//        g2.friendList.add(friendList.get(3));
//        g2.friendList.add(friendList.get(4));
//        g3.friendList = new ArrayList<>();
//        g3.friendList.add(friendList.get(5));
//        g3.friendList.add(friendList.get(6));
//        groupOfFriendList.add(g1);
//        groupOfFriendList.add(g2);
//        groupOfFriendList.add(g3);
    }

    public void inviteFriend(final String account) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                Log.e(TAG, strResponse);
                try {
                    JSONObject jo = new JSONObject(strResponse);
                    int status = jo.getInt(WebConfig.Response.ERROR);
                    int resourceMsg = (status == AccountManager.Response.SUCCESS) ? R.string.already_sent_request : R.string.request_fail;

                    Toast.makeText(context, resourceMsg, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, "inviteFriend: " + e.toString());
                }

            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.INVITE_FRIEND, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> result = new HashMap<String, String>();

                result.put(INVITEE, account);

                return result;
            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();

                headers.put(WebConfig.SET_COOKIE, LoginData.getInstance(context).session);

                return headers;
            }
        };

        volleyRequest.addRequest(stringRequest);
    }

    public void queryFriendManagement(final Handler handler) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                Log.e(TAG, strResponse);
                try {
                    JSONObject resJo = new JSONObject(strResponse);
                    //if (resJo.getInt(WebConfig.Response.ERROR) == AccountManager.Response.SUCCESS) {
                    if (true) { //error code之後會重訂的樣子
                        resJo = resJo.getJSONObject(DATA);
                        if (resJo.has(FRIEND)) {
                            JSONArray jsonArray = resJo.getJSONArray(FRIEND);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                PersonData personData = new PersonData(jo);

                                friendList.add(personData);
                            }
                            listOfList.get(FRIEND_INDEX).list = friendList;
                        }

                        if (resJo.has(REQUESTOR)) {
                            JSONArray jsonArray = resJo.getJSONArray(REQUESTOR);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                PersonData personData = new PersonData(jo);

                                requesterList.add(personData);
                            }
                            listOfList.get(REQUESTOR_INDEX).list = requesterList;
                        }

                        if (resJo.has(RECIPIENT)) {
                            JSONArray jsonArray = resJo.getJSONArray(RECIPIENT);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                PersonData personData = new PersonData(jo);

                                recipientList.add(personData);
                            }
                            listOfList.get(RECIPIENT_INDEX).list = recipientList;
                        }

                        if (handler != null) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "inviteFriend: " + e.toString());
                }

            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.QUERY_FRIEND_MANAGEMENT, listener, errorListener) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();

                headers.put(WebConfig.SET_COOKIE, LoginData.getInstance(context).session);

                return headers;
            }
        };

        volleyRequest.addRequest(stringRequest);
    }

    public void queryFriend(Handler handler) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                Log.e(TAG, strResponse);
                try {
                    JSONObject resJo = new JSONObject(strResponse);
                    if (resJo.getInt(WebConfig.Response.ERROR) == AccountManager.Response.SUCCESS) {
                        JSONArray jsonArray = resJo.getJSONArray(FRIEND);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            PersonData personData = new PersonData(jo);

                            friendList.add(personData);
                        }
                        listOfList.get(FRIEND_INDEX).list = friendList;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "inviteFriend: " + e.toString());
                }

            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.QUERY_FRIEND, listener, errorListener) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();

                headers.put(WebConfig.SET_COOKIE, LoginData.getInstance(context).session);

                return headers;
            }
        };

        volleyRequest.addRequest(stringRequest);
    }

    public void queryRequester(Handler handler) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        final com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                Log.e(TAG, strResponse);
                try {
                    JSONObject resJo = new JSONObject(strResponse);
                    if (resJo.getInt(WebConfig.Response.ERROR) == AccountManager.Response.SUCCESS) {
                        JSONArray jsonArray = resJo.getJSONArray(REQUESTOR);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            PersonData personData = new PersonData(jo);

                            recipientList.add(personData);
                        }
                        listOfList.get(RECIPIENT_INDEX).list = recipientList;

                    }
                } catch (Exception e) {
                    Log.e(TAG, "inviteFriend: " + e.toString());
                }

            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.QUERY_REQUESTER, listener, errorListener) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();

                headers.put(WebConfig.SET_COOKIE, LoginData.getInstance(context).session);

                return headers;
            }
        };

        volleyRequest.addRequest(stringRequest);
    }

    public void queryRecipient(Handler handler) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                Log.e(TAG, strResponse);
                try {
                    JSONObject resJo = new JSONObject(strResponse);
                    if (resJo.getInt(WebConfig.Response.ERROR) == AccountManager.Response.SUCCESS) {
                        JSONArray jsonArray = resJo.getJSONArray(RECIPIENT);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            PersonData personData = new PersonData(jo);

                            requesterList.add(personData);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "inviteFriend: " + e.toString());
                }

            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.QUERY_RECIPIENT, listener, errorListener) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();

                headers.put(WebConfig.SET_COOKIE, LoginData.getInstance(context).session);

                return headers;
            }
        };

        volleyRequest.addRequest(stringRequest);
    }

    public void receiveInvitation(final String account, final Handler handler) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                Log.e(TAG, strResponse);
                try {
                    JSONObject resJo = new JSONObject(strResponse);
                    int errorCode = resJo.getInt(WebConfig.Response.ERROR);

                    if (errorCode == AccountManager.Response.SUCCESS) {
                        refreshAllData(handler);
                    } else {
                        Toast.makeText(context, "發生錯誤，error code: " + errorCode, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "inviteFriend: " + e.toString());
                }

            }
        };
        com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        };
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.ACCEPT_FRIEND, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> result = new HashMap<String, String>();

//                result.put("username", account);
//                result.put("password", password);
                result.put("inviter", account);

                return result;
            }
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();

                headers.put(WebConfig.SET_COOKIE, LoginData.getInstance(context).session);

                return headers;
            }
        };

        volleyRequest.addRequest(stringRequest);
    }

}
