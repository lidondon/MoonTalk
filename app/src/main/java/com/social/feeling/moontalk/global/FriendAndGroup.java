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
    private static final String INVITEE = "invitee";
    private static final String INVITER = "inviter";
    private static final String DATA = "data";
    private volatile static FriendAndGroup friendAndGroup;
    private Context context;
    public List<PersonData> friendList = new ArrayList<PersonData>();
    public List<PersonData> inviterList = new ArrayList<PersonData>();
    public List<groupOfFriend> groupOfFriendList = new ArrayList<groupOfFriend>();
    public List<ListInfo> listOfList = new ArrayList<>();

    protected class ListInfo {
        public String name;
        public List list;
    }

    private FriendAndGroup(Context ctx) {
        context = ctx;
        makeFakeFriends();
        makeFakeGroup();
        queryInviter(null); //先不用在這呼叫，因為這是非同步，等外面真的有需要在呼叫然後更新頁面顯示
        initListOfList();
    }

    private void initListOfList() {
        ListInfo liGroupOfFriend = new ListInfo();
        ListInfo liInviter = new ListInfo();
        ListInfo liFriend = new ListInfo();

        liGroupOfFriend.name = context.getResources().getString(R.string.group);
        liInviter.name = context.getResources().getString(R.string.invite);
        liFriend.name = context.getResources().getString(R.string.friend);
        listOfList.add(liGroupOfFriend);
        listOfList.add(liInviter);
        listOfList.add(liFriend);
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
        PersonData f1 = new PersonData();
        PersonData f2 = new PersonData();
        PersonData f3 = new PersonData();
        PersonData f4 = new PersonData();
        PersonData f5 = new PersonData();
        PersonData f6 = new PersonData();
        PersonData f7 = new PersonData();

        f1.account = "Jean";
        f2.account = "Michael";
        f3.account = "Vincent";
        f4.account = "Sister";
        f5.account = "Lai";
        f6.account = "Coco";
        f7.account = "Dodo";
        f1.name = "小林";
        f2.name = "大林";
        f3.name = "小Ｖ";
        f4.name = "姊姊";
        f5.name = "BOSS";
        f6.name = "可可";
        f7.name = "多多";
        friendList.add(f1);
        friendList.add(f2);
        friendList.add(f3);
        friendList.add(f4);
        friendList.add(f5);
        friendList.add(f6);
        friendList.add(f7);
    }

    private void makeFakeGroup() {
        groupOfFriend g1 = new groupOfFriend();
        groupOfFriend g2 = new groupOfFriend();
        groupOfFriend g3 = new groupOfFriend();

        g1.id = "Lin";
        g2.id = "Family";
        g3.id = "Dog";
        g1.name = "林";
        g2.name = "林家宗親";
        g3.name = "狗狗團";
        g1.friendList = new ArrayList<>();
        g1.friendList.add(friendList.get(0));
        g1.friendList.add(friendList.get(1));
        g2.friendList = new ArrayList<>();
        g2.friendList.add(friendList.get(0));
        g2.friendList.add(friendList.get(1));
        g2.friendList.add(friendList.get(2));
        g2.friendList.add(friendList.get(3));
        g2.friendList.add(friendList.get(4));
        g3.friendList = new ArrayList<>();
        g3.friendList.add(friendList.get(5));
        g3.friendList.add(friendList.get(6));
        groupOfFriendList.add(g1);
        groupOfFriendList.add(g2);
        groupOfFriendList.add(g3);
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

    public void queryInviter(Handler handler) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        com.android.volley.Response.Listener<String> listener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                Log.e(TAG, strResponse);
                try {
                    JSONObject resJo = new JSONObject(strResponse);
                    if (resJo.getInt(WebConfig.Response.ERROR) == AccountManager.Response.SUCCESS) {
                        JSONArray jsonArray = resJo.getJSONArray(DATA);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            PersonData personData = new PersonData(jo);

                            inviterList.add(personData);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebConfig.QUERY_INVITER, listener, errorListener) {
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
