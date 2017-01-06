package com.social.feeling.moontalk.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.empire.vmd.client.android_lib.adapter.BaseTypeAdapter;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.activity.FeelingDetailActivity;
import com.social.feeling.moontalk.datamodel.Feeling;
import com.social.feeling.moontalk.global.Feelings;
import com.social.feeling.moontalk.item.FeelingItem;

import java.util.List;

/**
 * Created by lidondon on 2016/10/13.
 */
public class FeelingListFragment {
    private Context context;
    private List<Feeling> feelingList;
    private ListView lvFeelings;
    private LruCache<String, Bitmap> bitmapLruCache;
    private View headerView;

    public FeelingListFragment(Context ctx, List<Feeling> fList) {
        context = ctx;
        feelingList = fList;
        initBitmapLruCache();
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
        View resultView = getInflater().inflate(R.layout.fragment_feeling_list, null);

        findViews(resultView);
        refreshWall(feelingList);

        return resultView;
    }

    private void findViews(View rootView) {
        lvFeelings = (ListView) rootView.findViewById(R.id.lvFeelings);
    }

    public ListView getLvFeelings() {
        return lvFeelings;
    }

    public void refreshWall(final List<Feeling> fList) {
        initLvFeelings(fList);
        lvFeelings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feeling feeling = fList.get(position);
                Intent intent = new Intent(context, FeelingDetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable(Feeling.class.getName(), feeling);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    private void initLvFeelings(final List<Feeling> fList) {
        BaseTypeAdapter.IItemView iItemView = new BaseTypeAdapter.IItemView() {
            @Override
            public View getItemView(int position, View convertView) {
                if (convertView != null) {
                    ((FeelingItem.ViewHolder) convertView.getTag()).setContent(fList.get(position));
                } else {
                    FeelingItem feelingItem = new FeelingItem(context, fList.get(position), bitmapLruCache);

                    convertView = feelingItem.getView();
                    convertView.setTag(feelingItem.getViewHolder());
                }

                return convertView;
            }
        };
        BaseTypeAdapter adapter = new BaseTypeAdapter(iItemView, fList);

        if (headerView != null) {
            lvFeelings.addHeaderView(headerView);
        }
        lvFeelings.setAdapter(adapter);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        lvFeelings.setOnScrollListener(onScrollListener);
    }
}
