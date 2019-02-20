package com.daily.news.subscription.more.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;

import cn.daily.news.biz.core.network.compatible.LoadViewHolder;

/**
 * Created by lixinke on 2017/7/17.
 */

public class SearchColumnFragment extends SearchBaseFragment {
    private String mKeyword;

    public SearchColumnFragment() {
        new SearchPresenter(this, new SearchStore());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mKeyword = getArguments().getString("keyword");
        }
    }

    @Override
    public String[] getParams() {
        return new String[]{mKeyword};
    }

    @Override
    public View emptyView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.subscription_search_empty,parent,false);
    }

    @Override
    public LoadViewHolder getProgressBar() {
        return new LoadViewHolder(mRecyclerView, (ViewGroup) mRecyclerView.getParent());
    }
}
