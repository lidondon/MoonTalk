package com.social.feeling.moontalk.global;

import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.datamodel.FeelingTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidondon on 2016/8/10.
 */
public class FeelingTags {
    private static volatile FeelingTags feelingTags;
    public List<FeelingTag> allFeelingTagList;

    private FeelingTags() {
        initAllFeelingTagList();
    }

    public static FeelingTags getInstance() {
        if (feelingTags == null) {
            synchronized (FeelingTags.class) {
                if (feelingTags == null) {
                    feelingTags = new FeelingTags();
                }
            }
        }

        return feelingTags;
    }

    private void initAllFeelingTagList() {
        allFeelingTagList = new ArrayList<FeelingTag>();
        allFeelingTagList.add(new FeelingTag("eye", R.drawable.eye));
        allFeelingTagList.add(new FeelingTag("ear", R.drawable.ear));
        allFeelingTagList.add(new FeelingTag("mouth", R.drawable.mouth));
        allFeelingTagList.add(new FeelingTag("nose", R.drawable.nose));
        allFeelingTagList.add(new FeelingTag("touch", R.drawable.touch));
        allFeelingTagList.add(new FeelingTag("feel", R.drawable.feel));
    }

    public FeelingTag getTagById(String id) {
        FeelingTag result = null;

        for (FeelingTag tag : allFeelingTagList) {
            if (tag.id.equals(id)) {
                result = tag;
            }
        }

        return result;
    }
}
