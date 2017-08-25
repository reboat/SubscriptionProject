package com.daily.news.subscription.article;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.base.HeaderAdapter;
import com.daily.news.subscription.base.LinearLayoutColorDivider;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleFragment extends Fragment implements ArticleContract.View {

    private static final int DEFAULT_PAGE_SIZE = 10;
    @BindView(R2.id.article_recyclerView)
    XRecyclerView mRecyclerView;
    private View mRootView;

    private HeaderAdapter mHeaderAdapter;
    private ArticleAdapter mArticleAdapter;
    private List<ArticleResponse.DataBean.Article> mArticles;

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
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.addItemDecoration(new LinearLayoutColorDivider(getResources(), R.color.dddddd, R.dimen.divide_height, LinearLayoutManager.VERTICAL));
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore(mArticles.get(mArticles.size() - 1).sort_number, DEFAULT_PAGE_SIZE);
            }
        });
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
    public void updateValue(ArticleResponse response) {
        mArticleAdapter.updateValue(response.data.elements);
    }

    @Override
    public void loadMoreComplete(ArticleResponse response) {
        if (response.data.elements != null) {
            mArticleAdapter.addMore(response.data.elements);
            mRecyclerView.loadMoreComplete();
        }

        if (response.data.elements == null || response.data.elements.size() < DEFAULT_PAGE_SIZE) {
            mRecyclerView.setNoMore(true);
        }
    }

    @Override
    public void loadMoreError(String message) {
        mRecyclerView.loadMoreComplete();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
