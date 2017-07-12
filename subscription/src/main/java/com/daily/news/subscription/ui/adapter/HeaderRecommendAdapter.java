package com.daily.news.subscription.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 带有HeaderView的订阅Adapter
 */

public class HeaderRecommendAdapter extends RecyclerView.Adapter {
    private static int NORMAL_VIEW_TYPE = 10;

    private List<View> mHeaderViews = new ArrayList<>();
    private RecommendAdapter mRecommendAdapter;

    /**
     * recyclerView添加顶部View,正常Item的视图类型更新。
     *
     * @param view
     */
    public void addHeaderView(View view) {
        mHeaderViews.add(view);
        NORMAL_VIEW_TYPE = mHeaderViews.size() + NORMAL_VIEW_TYPE;
        notifyDataSetChanged();
    }

    public void removeHeaderView(View view) {
        mHeaderViews.remove(view);
        notifyDataSetChanged();
    }

    public void setRecommendAdapter(RecommendAdapter adapter) {
        mRecommendAdapter = adapter;
    }

    /**
     * 用position作为HeaderView的ViewType
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderViews.size() > 0 && position < mHeaderViews.size()) {
            return position;
        }
        return NORMAL_VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType <= mHeaderViews.size()) {
            return new RecyclerView.ViewHolder(mHeaderViews.get(viewType)) {
            };
        } else if (viewType == NORMAL_VIEW_TYPE) {
            return mRecommendAdapter.onCreateViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= mHeaderViews.size()) {
            mRecommendAdapter.onBindViewHolder((RecommendAdapter.RecommendViewHolder) holder, position - mHeaderViews.size());
        }
    }

    @Override
    public int getItemCount() {
        if (mRecommendAdapter != null) {
            return mHeaderViews.size() + mRecommendAdapter.getItemCount();
        }
        return 0;
    }
}
