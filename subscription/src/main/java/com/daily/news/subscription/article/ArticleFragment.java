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
import com.daily.news.subscription.detail.DetailResponse;
import com.daily.news.subscription.widget.SubscriptionDivider;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.FooterLoadMore;
import com.zjrb.core.recycleView.HeaderRefresh;
import com.zjrb.core.recycleView.LoadMore;
import com.zjrb.core.recycleView.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.network.compatible.LoadViewHolder;

public class ArticleFragment extends Fragment implements LoadMoreListener<DetailResponse.DataBean>,
        ArticleContract.View,
        OnItemClickListener {

    private static final int DEFAULT_PAGE_SIZE = 10;
    @BindView(R2.id.article_recyclerView)
    RecyclerView mRecyclerView;
    private View mRootView;

    private ArticleAdapter mArticleAdapter;
    private List<ArticleResponse.DataBean.Article> mArticles;

    private ArticleContract.Presenter mPresenter;
    private FooterLoadMore<DetailResponse.DataBean> mLoadMore;

    private HeaderRefresh mHeaderRefresh;
    private DetailResponse.DataBean.DetailBean mDetailBean;

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
        //TODO 重启时会为空，重现方法修改系统->显示->字体大小
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.subscription_fragment_article, container, false);
        ButterKnife.bind(this, mRootView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mArticleAdapter);
        mRecyclerView.addItemDecoration(new SubscriptionDivider(15, 15));
        mLoadMore = new FooterLoadMore<>(mRecyclerView, this);
        mArticleAdapter.addFooterView(mLoadMore.getItemView());
        mArticleAdapter.setOnItemClickListener(this);

        return mRootView;
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Nav.with(getContext()).to(mArticles.get(position).getUrl());
        onItemClick(mArticles.get(position));
    }

    //用户埋点
    protected void onItemClick(ArticleResponse.DataBean.Article article) {
        new Analytics.AnalyticsBuilder(getContext(), "200007", "AppContentClick", false)
                .name("新闻列表点击")
                .selfObjectID(String.valueOf(article.getMlf_id()))
                .columnID(String.valueOf(mDetailBean.id))
                .classShortName(mDetailBean.name)
                .objectShortName(article.getList_title())
                .objectType("C01")
                .pageType("订阅号详情页")
                .ilurl(article.getUrl())
                .objectID(String.valueOf(article.getMlf_id()))
                .columnName(mDetailBean.name)
                .selfNewsID(String.valueOf(article.getId()))
                .pubUrl(article.getUrl())
                .newsID(String.valueOf(article.getId()))
                .newsTitle(article.getDoc_title())
                .build()
                .send();
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
        if (response.data.elements == null || response.data.elements.size() == 0) {
            mLoadMore.setState(LoadMore.TYPE_NO_MORE);
        } else {
            mLoadMore.setState(LoadMore.TYPE_IDLE);
        }
    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showError(Throwable message) {

    }

    @Override
    public LoadViewHolder getProgressBar() {
        return null;
    }

    @Override
    public void onLoadMoreSuccess(DetailResponse.DataBean data, LoadMore loadMore) {
        if (data != null) {
            mArticleAdapter.addData(data.elements, true);
        }
        if (data == null || data.elements == null || data.elements.size() == 0) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<DetailResponse.DataBean> callback) {
        if (mArticles != null && mArticles.size() > 0) {
            mPresenter.loadMore(mArticles.get(mArticles.size() - 1).getSort_number(), DEFAULT_PAGE_SIZE, callback);
        } else {
            mLoadMore.setState(LoadMore.TYPE_IDLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
    }

    public static Fragment newInstance(List<ArticleResponse.DataBean.Article> article_list) {
        return null;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setOnRefreshListener(HeaderRefresh.OnRefreshListener onRefreshListener) {
        mHeaderRefresh = new HeaderRefresh(mRecyclerView, onRefreshListener);
        mArticleAdapter.addHeaderView(mHeaderRefresh.getItemView());
    }

    public void setRefreshing(boolean refresh) {
        mHeaderRefresh.setRefreshing(refresh);
    }

    public void canRefresh(boolean canrefresh) {
        mHeaderRefresh.setCanrfresh(canrefresh);
    }

    public void setColumnId(DetailResponse.DataBean.DetailBean bean) {
        mDetailBean =bean;
    }
}
