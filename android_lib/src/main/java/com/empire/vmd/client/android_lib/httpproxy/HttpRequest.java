package com.empire.vmd.client.android_lib.httpproxy;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lidondon on 2017/4/14.
 */
public class HttpRequest {
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String SESSION = "Session";
    public static final String RESPONSE = "Response";
    private int readTimeout;
    private int connectTimeout;

    public HttpRequest(int rt, int ct) {
        readTimeout = rt;
        connectTimeout = ct;
    }

    public void sendHttpRequest(final String strUrl, final String actionType, final Handler handler, final int[] args, final HashMap mapParameter, final HashMap mapHeader, final String cookieHeader) {
        new AsyncTask<Void, Void, Void>() {
            //@Override
            protected Void doInBackground(Void... params) {
                HttpURLConnection connection = null;
                HashMap<String, String> mapResponse = new HashMap<String, String>();

                try {
                    URL url = new URL(strUrl);
                    String strParameter = null; // post请求的参数
                    OutputStream out = null;
                    int responseCode = -1;
                    Message message = Message.obtain();

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(actionType);
                    connection.setReadTimeout(readTimeout);// 设置读取超时为5秒
                    connection.setConnectTimeout(connectTimeout);// 设置连接网络超时为10秒
                    if (actionType.equals(POST)) {
                        connection.setDoOutput(true);// 设置此方法,允许向服务器输出内容
                        setHeader(connection, mapHeader);
                        strParameter = getParameterString(mapParameter);
                        // 获得一个输出流,向服务器写数据,默认情况下,系统不允许向服务器输出内容
                        out = connection.getOutputStream();// 获得一个输出流,向服务器写数据
                        out.write(strParameter.getBytes());
                        out.flush();
                        out.close();
                    }
                    responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream is = connection.getInputStream();
                        String cookie  = connection.getHeaderField(cookieHeader);

                        mapResponse.put(RESPONSE, getStringFromInputStream(is).trim());
                        if(cookie != null && !cookie.isEmpty()) {
                            mapResponse.put(SESSION, cookie);
                        }
                        message.obj = mapResponse;
                        setMessageArgs(message, args);
                        handler.sendMessage(message);
                    }
                } catch (Exception ex) {
                    Log.e(getClass().toString(), ex.toString());
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

                return null;
            }
        }.execute();
    }

    private void setMessageArgs(Message message, int[] args) {
        if (message != null && args != null && args.length > 0) {
            message.arg1 = args[0];
            if (args.length > 1) {
                message.arg2 = args[1];
            }
        }
    }

    private void setHeader(HttpURLConnection connection, HashMap mapHeader) {
        for (Object key : mapHeader.keySet()) {
            connection.setRequestProperty(key.toString(), mapHeader.get(key).toString());
        }
    }

    private String getParameterString(HashMap map) {
        String result = "";

        for (Object key : map.keySet()) {
            result += (result.isEmpty()) ? "" : "&";
            result += key.toString() + "=" + map.get(key).toString();
        }

        return result;
    }

    private String getStringFromInputStream(InputStream is) throws IOException {
        String result = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;

        // 一定要写len=is.read(buffer)
        // 如果while((is.read(buffer))!=-1)则无法将数据写入buffer中
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        result = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        os.close();

        return result;
    }
}
