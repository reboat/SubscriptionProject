package com.daily.news.subscription.article;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.HeaderAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleFragment extends Fragment implements ArticleContract.View {

    @BindView(R2.id.article_recyclerView)
    RecyclerView mRecyclerView;
    private View mRootView;

    private HeaderAdapter mHeaderAdapter;
    private ArticleAdapter mArticleAdapter;
    private List<Article> mArticles;

    private ArticleContract.Presenter mPresenter;

    public ArticleFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHeaderAdapter = new HeaderAdapter();
        mArticles = new ArrayList<>();
        mArticleAdapter = new ArticleAdapter(mArticles);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.subscribe();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_article, container, false);
        ButterKnife.bind(this, mRootView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHeaderAdapter.setInternalAdapter(mArticleAdapter);
        mRecyclerView.setAdapter(mHeaderAdapter);
        return mRootView;
    }

    public void addHeaderView(View headerView) {
        mHeaderAdapter.addHeaderView(headerView);
    }

    @Override
    public void setPresenter(ArticleContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void updateValue(List<Article> articles) {
        mArticleAdapter.updateValue(articles);
    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.unsubscribe();
    }
}
