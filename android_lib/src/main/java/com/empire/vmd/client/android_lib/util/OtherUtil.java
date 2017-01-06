package com.empire.vmd.client.android_lib.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lidondon on 2015/12/16.
 */
public class OtherUtil {
    public TextView getMarquee(Context ctx, String info, int width, int height, int textColorResource, int backgroundColorResource) {
        TextView result = new TextView(ctx);

        //目前好像顏色都是固定，根本沒有改變我們設定的顏色
        result.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        result.setTextColor(textColorResource);
        result.setText(info);
        result.setSingleLine(true);
        result.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        result.setMarqueeRepeatLimit(-1);
        result.hasFocusable();
        result.setFocusableInTouchMode(true);
        result.setBackgroundColor(backgroundColorResource);
        result.setSelected(true);

        return result;
    }

    public int getRandomContent(int[] contents) {
        return contents[new Random().nextInt(contents.length)];
    }

    public int getRandomContent(int[] contents, int maxIndex) {
        int index = (maxIndex > contents.length) ? contents.length : maxIndex;

        return contents[new Random().nextInt(index)];
    }

    public int getCircleNextIndex(int size, int index) {
        return (index < size - 1) ? index + 1 : 0;
    }

    public int getCirclePreviousIndex(int size, int index) {
        return (index > 0) ? index - 1 : size - 1;
    }

    public String[] getStringArrayFromList(List<String> list) {
        String[] result = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }

        return result;
    }

    public List<String> getStringListFromArray(String[] strings) {
        List<String> result = new ArrayList<String>();

        if (strings != null) {
            for (String str : strings) {
                result.add(str);
            }
        }

        return result;
    }
}
