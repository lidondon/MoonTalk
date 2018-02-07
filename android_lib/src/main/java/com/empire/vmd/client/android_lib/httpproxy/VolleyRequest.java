package com.empire.vmd.client.android_lib.httpproxy;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by lidondon on 2017/9/5.
 */

public class VolleyRequest {
    private static volatile VolleyRequest volleyRequest;
    private RequestQueue requestQueue;

    private VolleyRequest(Context ctx) {
        requestQueue = Volley.newRequestQueue(ctx);
    }

    public static VolleyRequest getInstance(Context ctx) {
        if (volleyRequest == null) {
            synchronized (VolleyRequest.class) {
                if (volleyRequest == null) {
                    volleyRequest = new VolleyRequest(ctx);
                }
            }
        }

        return volleyRequest;
    }

    public void addRequest(Request request) {
        requestQueue.add(request);
    }
}
