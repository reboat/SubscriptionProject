package com.daily.news.subscription.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.article.ArticleFragment;
import com.daily.news.subscription.article.ArticlePresenter;
import com.daily.news.subscription.article.ArticleResponse;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.load.LoadViewHolder;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 页面逻辑：
 * 1.有订阅时返回订阅的新闻，无订阅时返回推荐订阅栏目。
 * 2.点击订阅后页面下拉刷新，返回订阅栏目的新闻
 */
public class SubscribedArticleFragment extends Fragment implements SubscriptionContract.View, HeaderRefresh.OnRefreshListener {

    private static final int REQUEST_CODE_MY = 1001;

    private Unbinder mUnBinder;
    private SubscriptionContract.Presenter mPresenter;
    private ArticlePresenter mArticlePresenter;
    private ArticleFragment mArticleFragment;
    private List<ArticleResponse.DataBean.Article> mArticles;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_subscribed_article, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);

        mArticleFragment = (ArticleFragment) getChildFragmentManager().findFragmentById(R.id.article_fragment);
        mArticlePresenter = new ArticlePresenter(mArticleFragment, new SubscribeArticleStore(mArticles));
        mArticleFragment.setOnRefreshListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initArticles(List<ArticleResponse.DataBean.Article> article_list) {
        mArticles = article_list;
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh("杭州");
    }

    @OnClick(R2.id.my_sub_btn)
    public void gotoMySubscription() {
        Nav.with(this).to("http://www.8531.cn/subscription/more/my/column", REQUEST_CODE_MY);
    }

    @OnClick(R2.id.my_sub_more_btn)
    public void gotoMore() {
        Nav.with(this).to("http://www.8531.cn/subscription/more");
    }


    @Override
    public void setPresenter(SubscriptionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {
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
    public void updateValue(SubscriptionResponse.DataBean subscriptionResponse) {

    }

    @Override
    public void onRefreshComplete(SubscriptionResponse.DataBean dataBean) {
        if (dataBean.has_subscribe) {
            Fragment fragment = SubscribedArticleFragment.newInstance(dataBean.article_list);
            getFragmentManager().beginTransaction().replace(R.id.subscription_container, fragment).commit();
        } else {
            Fragment fragment = RecommendFragment.newInstance(dataBean.focus_list, dataBean.recommend_list);
            getFragmentManager().beginTransaction().replace(R.id.subscription_container, fragment).commit();
        }
        mArticleFragment.setRefreshing(false);
    }

    @Override
    public void onRefreshError(String message) {
        mArticleFragment.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    public static Fragment newInstance(List<ArticleResponse.DataBean.Article> article_list) {
        SubscribedArticleFragment fragment = new SubscribedArticleFragment();
        article_list.removeAll(Collections.singleton(null));
        fragment.initArticles(article_list);
        new SubscriptionPresenter(fragment, new SubscriptionStore());
        return fragment;
    }
}
