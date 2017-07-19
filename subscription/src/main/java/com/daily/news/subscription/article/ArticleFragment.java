package com.daily.news.subscription.article;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.Article;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.base.HeaderAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleFragment extends Fragment {
    protected static final String ARTICLES = "articles";

    @BindView(R2.id.my_sub_recyclerView)
    RecyclerView mRecyclerView;
    private View mRootView;

    private HeaderAdapter mHeaderAdapter;
    private List<Article> mArticles;

    public ArticleFragment() {
    }

    public static ArticleFragment newInstance(ArrayList<Article> articles) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARTICLES, articles);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArticles = getArguments().getParcelableArrayList(ARTICLES);
            mHeaderAdapter = new HeaderAdapter();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_my_subscription, container, false);
        ButterKnife.bind(this, mRootView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHeaderAdapter.setInternalAdapter(new ArticleAdapter(mArticles));
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setAdapter(mHeaderAdapter);
        return mRootView;
    }

    public void addHeaderView(View headerView) {
        mHeaderAdapter.addHeaderView(headerView);
    }

}
