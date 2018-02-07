package com.social.feeling.moontalk.global;

import com.social.feeling.moontalk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2016/5/16.
 */
public class Colors {
    private static volatile Colors colors;
    public List<ColorObject> colorObjectList = new ArrayList<ColorObject>();

    private Colors() {
        colorObjectList.add(new ColorObject("blue", R.drawable.blue));
        colorObjectList.add(new ColorObject("black", R.drawable.black));
        colorObjectList.add(new ColorObject("brown", R.drawable.brown));
        colorObjectList.add(new ColorObject("dark_green", R.drawable.dark_green));
        colorObjectList.add(new ColorObject("dark_green_2", R.drawable.dark_green_2));
        colorObjectList.add(new ColorObject("deep_blue", R.drawable.deep_blue));
        colorObjectList.add(new ColorObject("green", R.drawable.green));
        colorObjectList.add(new ColorObject("gray", R.drawable.gray));
        colorObjectList.add(new ColorObject("indigo", R.drawable.indigo));
        colorObjectList.add(new ColorObject("light_blue", R.drawable.light_blue));
        colorObjectList.add(new ColorObject("light_grey", R.drawable.light_grey));
        colorObjectList.add(new ColorObject("light_yellow", R.drawable.light_yellow));
        colorObjectList.add(new ColorObject("light_yellow_2", R.drawable.light_yellow_2));
        colorObjectList.add(new ColorObject("orange", R.drawable.orange));
        colorObjectList.add(new ColorObject("pink", R.drawable.pink));
        colorObjectList.add(new ColorObject("purple", R.drawable.purple));
        colorObjectList.add(new ColorObject("red", R.drawable.red));
        colorObjectList.add(new ColorObject("rose", R.drawable.rose));
        colorObjectList.add(new ColorObject("white", R.drawable.white));
        colorObjectList.add(new ColorObject("yellow", R.drawable.yellow));
    }

    public static Colors getInstance() {
        if (colors == null) {
            synchronized (Colors.class) {
                if (colors == null) {
                    colors = new Colors();
                }
            }
        }

        return colors;
    }

    public int getColorResource(String id) {
        int result = 0;

        if (id != null) {
            for (ColorObject co : colorObjectList) {
                if (id.equals(co.id)) {
                    result = co.colorImageResource;
                    break;
                }
            }
        }

        return result;
    }

    public class ColorObject {
        public String id;
        public int colorImageResource;

        public ColorObject(String t, int src) {
            id = t;
            colorImageResource = src;
        }
    }
}
