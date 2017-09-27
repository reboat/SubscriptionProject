package com.daily.news.subscription.more.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.more.column.ColumnFragment;

/**
 * Created by lixinke on 2017/7/17.
 */

public class SearchColumnFragment extends ColumnFragment {
    private String mKeyword;

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
}
