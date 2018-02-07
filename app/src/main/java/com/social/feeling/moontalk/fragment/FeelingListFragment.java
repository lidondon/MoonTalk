package com.social.feeling.moontalk.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.empire.vmd.client.android_lib.adapter.BaseTypeAdapter;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.activity.FeelingDetailActivity;
import com.social.feeling.moontalk.datamodel.Feeling;
import com.social.feeling.moontalk.global.Feelings;
import com.social.feeling.moontalk.item.FeelingItem;
import com.social.feeling.moontalk.item.NewFeelingItem;

import java.util.List;

/**
 * Created by lidondon on 2016/10/13.
 */
public class FeelingListFragment {
    private Context context;
    private View resultView;
    private List<Feeling> feelingList;
    private ListView lvFeelings;
    private LruCache<String, Bitmap> bitmapLruCache;
    private View headerView;
    private TextView tvLoading;
    private BaseTypeAdapter adapter;
    private IUpdater updater;


    public interface IUpdater {
        public void update();
    }

    public FeelingListFragment(Context ctx, List<Feeling> fList) {
        context = ctx;
        feelingList = fList;
        initBitmapLruCache();
    }

    public FeelingListFragment(Context ctx, List<Feeling> fList, IUpdater iu) {
        context = ctx;
        feelingList = fList;
        initBitmapLruCache();
        updater = iu;
    }

    public void removeUpdater() {
        updater = null;
    }

    public void setHeaderView(View hv) {
        headerView = hv;
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    private void initBitmapLruCache() {
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        bitmapLruCache = new android.support.v4.util.LruCache<String, Bitmap>(cacheSize);
    }

    public View getView() {
        resultView = getInflater().inflate(R.layout.fragment_feeling_list, null);
        findViews(resultView);
        refreshWall(feelingList);
        initPartialLoading();

        return resultView;
    }

    private void findViews(View rootView) {
        lvFeelings = (ListView) rootView.findViewById(R.id.lvFeelings);
        tvLoading = (TextView) rootView.findViewById(R.id.tvLoading);
    }

    public ListView getLvFeelings() {
        return lvFeelings;
    }

    private void initPartialLoading() {
        if (updater != null) {
            lvFeelings.setOnScrollListener(new AbsListView.OnScrollListener() {
                private int visibleLast;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    int itemsLastIndex = adapter.getCount() - 1;

                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLast == itemsLastIndex) {
                        updater.update();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    visibleLast = firstVisibleItem + visibleItemCount - 1;
                }
            });
        }
    }

    public View refreshWall(final List<Feeling> fList) {
        initLvFeelings(fList);
        lvFeelings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feeling feeling = fList.get(position);
                Intent intent = new Intent(context, FeelingDetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString(Feeling.class.getName(), feeling.id);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return resultView;
    }

    public void addItems(List<Feeling> newFeelingList) {
        if (newFeelingList != null) {
            for (Feeling feeling : newFeelingList) {
                adapter.addItem(feeling);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void initLvFeelings(final List<Feeling> fList) {
        BaseTypeAdapter.IItemView iItemView = new BaseTypeAdapter.IItemView() {
            @Override
            public View getItemView(int position, View convertView) {
                if (convertView != null) {
                    ((NewFeelingItem.ViewHolder) convertView.getTag()).setContent(fList.get(position));
                } else {
                    NewFeelingItem feelingItem = new NewFeelingItem(context, fList.get(position), bitmapLruCache);

                    convertView = feelingItem.getView();
                    convertView.setTag(feelingItem.getViewHolder());
                }

                return convertView;
            }
        };
        adapter = new BaseTypeAdapter(iItemView, fList);

        if (headerView != null) {
            lvFeelings.addHeaderView(headerView);
        }
        lvFeelings.setAdapter(adapter);
    }

    public void switchLoading(boolean flag) {
        if (flag) {
            tvLoading.setVisibility(View.VISIBLE);
        } else {
            tvLoading.setVisibility(View.GONE);
        }
    }
//
//    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
//        lvFeelings.setOnScrollListener(onScrollListener);
//    }
}
