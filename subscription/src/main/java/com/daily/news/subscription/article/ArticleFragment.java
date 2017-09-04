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
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleFragment extends Fragment implements ArticleContract.View, LoadMoreListener<ArticleResponse.DataBean> {

    private static final int DEFAULT_PAGE_SIZE = 10;
    @BindView(R2.id.article_recyclerView)
    RecyclerView mRecyclerView;
    private View mRootView;

    private ArticleAdapter mArticleAdapter;
    private List<ArticleResponse.DataBean.Article> mArticles;

    private ArticleContract.Presenter mPresenter;
    private FooterLoadMore<ArticleResponse.DataBean> mLoadMore;

    public ArticleFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mRecyclerView.setAdapter(mArticleAdapter);
        mRecyclerView.addItemDecoration(new ListSpaceDivider(1, R.color.category_divider_color, true));
        mLoadMore = new FooterLoadMore<>(mRecyclerView, this);
        mArticleAdapter.addFooterView(mLoadMore.getItemView());

        return mRootView;
    }

    public void addHeaderView(View headerView) {
        mArticleAdapter.addHeaderView(headerView);
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
    public void hideProgressBar() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void onLoadMoreSuccess(ArticleResponse.DataBean data, LoadMore loadMore) {
        mArticleAdapter.addMore(data.elements);
        if (data.elements.size() < DEFAULT_PAGE_SIZE) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<ArticleResponse.DataBean> callback) {
        if(mArticles==null || mArticles.size()==0){
            mLoadMore.setState(LoadMore.TYPE_NO_MORE);
        }else{
            mPresenter.loadMore(mArticles.get(mArticles.size()-1).sort_number,DEFAULT_PAGE_SIZE,callback);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.unsubscribe();
    }
}
