package com.social.feeling.moontalk.datamodel;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.empire.vmd.client.android_lib.httpproxy.VolleyRequest;
import com.empire.vmd.client.android_lib.util.FileUtil;
import com.social.feeling.moontalk.global.FileConfig;
import com.social.feeling.moontalk.http.WebConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2016/7/6.
 */
public class Quote {
    public static final String FAKE_QUOTE_FILE = "quotes.txt";
    public static final String CLASS_NAME = "Quote";
    public static final String ID = "id";
    public static final String TEXT = "text";
    public static final String REPLACEMENTS = "replacements";
    public static final String START_INDEX = "startIndex";
    public static final String LENGTH = "length";
    public static final String WORDS = "words";
    public static final String SELECTED_INDEX = "selectedReplacementIndex";
    public String id;
    public String text;
    public String currentText;
    public List<Replacement> replacementList;
    public List<Integer> deltaList;

    public interface IQuoteListListener {
        public void processQuoteList(List<Quote> quoteList);
    }

    public Quote() {

    }

    public Quote(JSONObject jo) {
        try {
            id = jo.getString(ID);
            text = jo.getString(TEXT);
            replacementList = getReplacementList(jo.getJSONArray(REPLACEMENTS), this);
            setCurrentText();
        } catch (Exception e) {
            Log.e(CLASS_NAME, e.toString());
            clearData();
        }
    }

    private void clearData() {
        id = null;
        text = null;
        currentText = null;
        replacementList = null;
        deltaList = null;
    }

    public static class Replacement {
        private Quote quote;
        public int startIndex;
        public int length;
        public List<String> wordList;
        protected int selectedReplacementIndex;

        public Replacement(Quote q) {
            quote = q;
        }

        public int getSelectedReplacementIndex() {
            return selectedReplacementIndex;
        }

        public void setSelectedReplacementIndex(int index) {
            selectedReplacementIndex = index;
            quote.setCurrentText();
        }
    }

    protected void setCurrentText() {
        if (replacementList.size() > 0) {
            int firstIndex = replacementList.get(0).startIndex;

            deltaList = new ArrayList<Integer>();
            currentText = (firstIndex == 0) ? "" : text.substring(0, firstIndex);
            for (int i = 0; i < replacementList.size(); i++) {
                Quote.Replacement replacement = replacementList.get(i);
                String orgWord = replacement.wordList.get(0);
                String selectedWord = replacement.wordList.get(replacement.selectedReplacementIndex);
                int delta;

                if (replacement.selectedReplacementIndex != 0) {
                    delta = selectedWord.length() - orgWord.length();
                } else {
                    delta = 0;
                }
                deltaList.add(delta);
                currentText += selectedWord;
                if (replacement.startIndex + replacement.length < text.length()) {
                    String tmpText = "";

                    if (i == replacementList.size() - 1) {
                        tmpText = text.substring(replacement.startIndex + replacement.length);
                    } else {
                        tmpText = text.substring(replacement.startIndex + replacement.length
                                , replacementList.get(i + 1).startIndex);
                    }
                    currentText += tmpText;
                }
            }
        } else {
            currentText = text;
        }
    }

    public int getSoFarDelta(int count) {
        int result = 0;

        for (int i = 0; i < count; i++) {
            result += deltaList.get(i);
        }

        return result;
    }

    public static void getQuoteList(Context context, final IQuoteListListener iQuoteListListener) {
        VolleyRequest volleyRequest = VolleyRequest.getInstance(context);
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray root) {
                List<Quote> quoteList = new ArrayList<Quote>();

                try {
                    for (int i = 0; i < root.length(); i++) {
                        JSONObject jo = root.getJSONObject(i);
                        Quote quote = new Quote(jo);

                        quoteList.add(quote);
                    }
                    iQuoteListListener.processQuoteList(quoteList);
                } catch (Exception e) {
                    quoteList = null;
                    Log.e(CLASS_NAME, e.toString());
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(CLASS_NAME, error.toString());
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, WebConfig.GET_QUOTES, listener, errorListener);

        volleyRequest.addRequest(jsonArrayRequest);
        //String strQuotes = new FileUtil(context).getStringFromExternalFile(FileConfig.EXTERNAL_DIR, FAKE_QUOTE_FILE);

//        if (strQuotes != null && !strQuotes.isEmpty()) {
//            try {
//                JSONArray root = new JSONArray(strQuotes);
//
//                result = (root.length() > 0) ? new ArrayList<Quote>() : null;
//                for (int i = 0; i < root.length(); i++) {
//                    JSONObject jo = root.getJSONObject(i);
//                    Quote quote = new Quote(jo);
//
//                    result.add(quote);
//                }
//            } catch (Exception e) {
//                result = null;
//                Log.e(CLASS_NAME, e.toString());
//            }
//        }
    }

//    public static Quote getQuote(JSONObject jo) {
//        Quote result = new Quote();
//
//        try {
//            result.account = jo.getString(ID);
//            result.text = jo.getString(TEXT);
//            result.replacementList = getReplacementList(jo.getJSONArray(REPLACEMENTS), result);
//            result.setCurrentText();
//        } catch (Exception e) {
//            Log.e(CLASS_NAME, e.toString());
//            result = null;
//        }
//
//        return result;
//    }

    private static List<Replacement> getReplacementList(JSONArray replacements, Quote q) {
        List<Replacement> result = null;

        if (replacements.length() > 0) {
            try {
                result = new ArrayList<Replacement>();
                for (int i = 0; i < replacements.length(); i++) {
                    Replacement replacement = new Replacement(q);
                    JSONObject jo = replacements.getJSONObject(i);

                    replacement.startIndex = jo.getInt(START_INDEX);
                    replacement.length = jo.getInt(LENGTH);
                    replacement.wordList = getWordList(jo.getJSONArray(WORDS));
                    if (jo.has(SELECTED_INDEX)) {
                        replacement.selectedReplacementIndex = jo.getInt(SELECTED_INDEX);
                    }
                    result.add(replacement);
                }
            } catch (Exception e) {
                result = null;
                Log.e(CLASS_NAME, e.toString());
            }
        }

        return result;
    }

    private static List<String> getWordList(JSONArray words) {
        List<String> result = null;

        if (words.length() > 0) {
            try {
                result = new ArrayList<String>();
                for (int i = 0; i < words.length(); i++) {
                    JSONObject jo = words.getJSONObject(i);
                    String word = jo.getString(TEXT);

                    result.add(word);
                }
            } catch (Exception e) {
                result = null;
                Log.e(CLASS_NAME, e.toString());
            }
        }

        return result;
    }

    public static List<String> getQuoteTextList(List<Quote> quoteList) {
        List<String> result = null;

        if (quoteList != null && quoteList.size() > 0) {
            result = new ArrayList<String>();
            for (int i = 0; i < quoteList.size(); i++) {
                result.add(quoteList.get(i).text);
            }
        }

        return result;
    }

    /* make fake data */

    public static void makeFakeData(Context ctx) {
        JSONArray root = new JSONArray();
        JSONObject jo1 = new JSONObject();
        JSONArray jo1Replacements = new JSONArray();
        JSONObject jo1Replacement1 = new JSONObject();
        JSONArray jo1Replacement1Words = new JSONArray();
        JSONObject jo1Replacement1Word1 = new JSONObject();
        JSONObject jo1Replacement1Word2 = new JSONObject();
        JSONObject jo2 = new JSONObject();
        JSONArray jo2Replacements = new JSONArray();
        JSONObject jo2Replacement1 = new JSONObject();
        JSONArray jo2Replacement1Words = new JSONArray();
        JSONObject jo2Replacement1Word1 = new JSONObject();
        JSONObject jo2Replacement1Word2 = new JSONObject();
        JSONObject jo2Replacement1Word3 = new JSONObject();
        JSONObject jo2Replacement2 = new JSONObject();
        JSONArray jo2Replacement2Words = new JSONArray();
        JSONObject jo2Replacement2Word1 = new JSONObject();
        JSONObject jo2Replacement2Word2 = new JSONObject();
        JSONObject jo3 = new JSONObject();
        JSONArray jo3Replacements = new JSONArray();
        JSONObject jo3Replacement1 = new JSONObject();
        JSONArray jo3Replacement1Words = new JSONArray();
        JSONObject jo3Replacement1Word1 = new JSONObject();
        JSONObject jo3Replacement1Word2 = new JSONObject();

        try {
            jo1.put(ID, "001");
            jo1.put(TEXT, "星期四也一起放個颱風假好嗎？");
            jo1.put(REPLACEMENTS, jo1Replacements);
            jo1Replacements.put(jo1Replacement1);
            jo1Replacement1.put(START_INDEX, 2);
            jo1Replacement1.put(LENGTH, 1);
            jo1Replacement1.put(WORDS, jo1Replacement1Words);
            jo1Replacement1Words.put(jo1Replacement1Word1);
            jo1Replacement1Word1.put(TEXT, "四");
            jo1Replacement1Words.put(jo1Replacement1Word2);
            jo1Replacement1Word2.put(TEXT, "日");
            /////////////////////////////////
            jo2.put(ID, "002");
            jo2.put(TEXT, "天下無難事，只怕有心人，床前明月光，疑是地上霜，舉頭望明月");
            jo2.put(REPLACEMENTS, jo2Replacements);
            jo2Replacements.put(jo2Replacement1);
            jo2Replacement1.put(START_INDEX, 0);
            jo2Replacement1.put(LENGTH, 2);
            jo2Replacement1.put(WORDS, jo2Replacement1Words);
            jo2Replacement1Words.put(jo2Replacement1Word1);
            jo2Replacement1Word1.put(TEXT, "天下");
            jo2Replacement1Words.put(jo2Replacement1Word2);
            jo2Replacement1Word2.put(TEXT, "地上");
            jo2Replacements.put(jo2Replacement2);
            jo2Replacement2.put(START_INDEX, 10);
            jo2Replacement2.put(LENGTH, 1);
            jo2Replacement2.put(WORDS, jo2Replacement2Words);
            jo2Replacement2Words.put(jo2Replacement2Word1);
            jo2Replacement2Word1.put(TEXT, "人");
            jo2Replacement2Words.put(jo2Replacement2Word2);
            jo2Replacement2Word2.put(TEXT, "爛渣");
            //////////////////////////////
            jo3.put(ID, "003");
            jo3.put(TEXT, "人比人氣死人");
            jo3.put(REPLACEMENTS, jo3Replacements);
            jo3Replacements.put(jo3Replacement1);
            jo3Replacement1.put(START_INDEX, 2);
            jo3Replacement1.put(LENGTH, 1);
            jo3Replacement1.put(WORDS, jo3Replacement1Words);
            jo3Replacement1Words.put(jo3Replacement1Word1);
            jo3Replacement1Word1.put(TEXT, "人");
            jo3Replacement1Words.put(jo3Replacement1Word2);
            jo3Replacement1Word2.put(TEXT, "豬");
            root.put(jo1);
            root.put(jo2);
            root.put(jo3);
        } catch (Exception e) {
            Log.e("Quote", e.toString());
        }
        new FileUtil(ctx).saveExternalFile(FileConfig.EXTERNAL_DIR, "quotes.txt", root.toString());
    }

    public static void copyFakeData(Context ctx, List<Quote> quoteList) {
        JSONArray ja = new JSONArray();

        for (int i = 0; i < 12; i++) {
            quoteList.add(quoteList.get(0));
        }

        for (int i = 0; i < quoteList.size(); i++) {
            try {
                ja.put(quoteList.get(i).toJsonObject());
            } catch (Exception ex) {
                Log.e("AAA", ex.toString());
            }
        }
        new FileUtil(ctx).saveExternalFile(FileConfig.EXTERNAL_DIR, FAKE_QUOTE_FILE, ja.toString());
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        try {
            jo.put(ID, id);
            jo.put(TEXT, text);
            jo.put(REPLACEMENTS, getReplacementJsonArray());
        } catch (Exception ex) {

        }

        return jo;
    }

    public JSONArray getReplacementJsonArray() {
        JSONArray ja = new JSONArray();

        for (int i = 0; i < replacementList.size(); i++) {
            ja.put(getReplacementJsonObject(replacementList.get(i)));
        }

        return ja;
    }

    public JSONObject getReplacementJsonObject(Replacement r) {
        JSONObject jo = new JSONObject();

        try {
            jo.put(START_INDEX, r.startIndex);
            jo.put(LENGTH, r.length);
            jo.put(SELECTED_INDEX, r.selectedReplacementIndex);
            jo.put(WORDS, getWordJsonArray(r.wordList));
        } catch (Exception ex) {

        }

        return jo;
    }

    public JSONArray getWordJsonArray(List<String> wList) {
        JSONArray ja = new JSONArray();

        for (int i = 0; i < wList.size(); i++) {
            ja.put(getWordJsonObject(wList.get(i)));
        }

        return ja;
    }

    private static JSONObject getWordJsonObject(String s) {
        JSONObject jo = new JSONObject();

        try {
            jo.put(TEXT, s);
        } catch (Exception ex) {

        }

        return jo;
    }
}
