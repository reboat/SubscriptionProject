package com.daily.news.subscription.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixinke on 2017/7/13.
 */

public class HeaderAdapter extends RecyclerView.Adapter {
    private static final int COUNT_TEMP = 10;

    private List<View> mHeaderViews;
    private RecyclerView.Adapter mInternalAdapter;

    public HeaderAdapter() {
        mHeaderViews = new ArrayList<>();
    }

    public void addHeaderView(View headerView) {
        mHeaderViews.add(headerView);
        notifyItemInserted(mHeaderViews.size() - 1);
    }

    public void removeHeaderView(View headerView) {
        int position = mHeaderViews.indexOf(headerView);
        mHeaderViews.remove(headerView);
        notifyItemRemoved(position);
    }

    public RecyclerView.Adapter getInternalAdapter() {
        return mInternalAdapter;
    }
    public void setInternalAdapter(RecyclerView.Adapter adapter) {
        mInternalAdapter = adapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderViews.size() > 0 && position < mHeaderViews.size()) {
            return position;
        } else if (mInternalAdapter != null) {
            return mInternalAdapter.getItemViewType(position - mHeaderViews.size()) + mHeaderViews.size() + COUNT_TEMP;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType < mHeaderViews.size()) {
            return new RecyclerView.ViewHolder(mHeaderViews.get(viewType)) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }
        return mInternalAdapter.onCreateViewHolder(parent, viewType - mHeaderViews.size() - COUNT_TEMP);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= mHeaderViews.size()) {
            mInternalAdapter.onBindViewHolder(holder, position - mHeaderViews.size());
        }
    }

    @Override
    public int getItemCount() {
        if (mInternalAdapter != null) {
            return mHeaderViews.size() + mInternalAdapter.getItemCount();
        }
        return mHeaderViews.size();
    }
}
