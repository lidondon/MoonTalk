package com.empire.vmd.client.android_lib.listener;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

/**
 * Created by lidondon on 2016/11/14.
 */
public class ListViewOnScrollListener implements OnScrollListener {
    private static final int DEFAULT_SCROLL_THRESHOLD = 50;
    private int lastScrollY; //第一个可视的item的顶部坐标
    private int previousFirstVisibleItem; //上一次滑动的第一个可视item的索引值
    private ListView listView;
    /**
     * 滑动距离响应的临界值，这个值可根据需要自己指定
     * 只有只有滑动距离大于mScrollThreshold，才会响应滑动动作
     */
    private int scrollThreshold;
    private IOnScrollEvent iOnScrollEvent;

    public interface IOnScrollEvent {
        public void onScrollUp();
        public void onScrollDown();
    }

    public ListViewOnScrollListener(ListView lv, IOnScrollEvent iose) {
        listView = lv;
        iOnScrollEvent = iose;
        scrollThreshold = DEFAULT_SCROLL_THRESHOLD;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    //核心方法，该方法封装了滑动方向判断的逻辑，但ListView产生滑动之后，该方法会被调用。
    //1.首先，判断滑动后第一个可视的item和滑动前是否同一个，如果是同一个，进入第2步，否则进入第3步
    //2.则这次滑动距离小于一个Item的高度，比较第一个可视的item的顶部坐标在滑动前后的差值，就知道了滑动的距离
    //3.这个好办，直接比较滑动前后firstVisibleItem的值就可以判断滑动方向了。
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount != 0) {
            // 滑动距离：不超过一个item的高度
            if (this.isSameRow(firstVisibleItem)) {
                int newScrollY = this.getTopItemScrollY();
                //判断滑动距离是否大于 scrollThreshold
                boolean isSignificantDelta = Math.abs(this.lastScrollY - newScrollY) > this.scrollThreshold;
                if (isSignificantDelta) {
                    //对于第一个可视的item，根据其前后两次的顶部坐标判断滑动方向
                    if (lastScrollY > newScrollY) {
                        iOnScrollEvent.onScrollUp();
                    } else {
                        iOnScrollEvent.onScrollDown();
                    }
                }
                this.lastScrollY = newScrollY;
            } else {//根据第一个可视Item的索引值不同，判断滑动方向
                if (firstVisibleItem > this.previousFirstVisibleItem) {
                    iOnScrollEvent.onScrollUp();
                } else {
                    iOnScrollEvent.onScrollDown();
                }
                lastScrollY = this.getTopItemScrollY();
                previousFirstVisibleItem = firstVisibleItem;
            }
        }
    }

    public void setScrollThreshold(int st) {
        scrollThreshold = st;
    }

    private boolean isSameRow(int firstVisibleItem) {
        return firstVisibleItem == this.previousFirstVisibleItem;
    }

    private int getTopItemScrollY() {
        int result = 0;

        if (this.listView != null && this.listView.getChildAt(0) != null) {
            View topChild = this.listView.getChildAt(0);

            result = topChild.getTop();
        }

        return result;
    }
}
