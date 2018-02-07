package com.empire.vmd.client.android_lib.util;

import android.content.Context;
import android.util.Log;

import com.empire.vmd.client.android_lib.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by lidondon on 2015/9/27.
 */
public class XmlDocUtil {
    private static final String LOG_TAG = "XmlDocUtil";

    public Element getRootElementFromRawResources(Context context, int rowResource) {
        InputStream inputStream = context.getResources().openRawResource(rowResource);

        return getRootElement(inputStream);
    }

    public Element getRootElement(InputStream inputStream) {
        Element root = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);  //以樹狀格式存於記憶體中﹐比較耗記憶體

            root = doc.getDocumentElement();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage().toString());
        }

        return root;
    }

}

