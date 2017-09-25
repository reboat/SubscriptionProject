package com.daily.news.subscription.home;

import android.os.Bundle;
import android.os.Parcelable;
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

import java.util.ArrayList;
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

    private static final String ARTICLES = "Articles";
    private Unbinder mUnBinder;
    private SubscriptionContract.Presenter mPresenter;
    private ArticlePresenter mArticlePresenter;
    private ArticleFragment mArticleFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subscribed_article, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);
        mArticleFragment = (ArticleFragment) getChildFragmentManager().findFragmentById(R.id.article_fragment);
        mArticlePresenter = new ArticlePresenter(mArticleFragment, new SubscribeArticleStore(getArguments().<ArticleResponse.DataBean.Article>getParcelableArrayList(ARTICLES)));
        mArticleFragment.setOnRefreshListener(this);
        return rootView;
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh("杭州");
    }

    @OnClick(R2.id.my_sub_btn)
    public void gotoMySubscription(){
        Nav.with(getContext()).to("http://www.8531.cn/subscription/more/my/column");
    }

    @OnClick(R2.id.my_sub_more_btn)
    public void gotoMore(){
        Nav.with(getContext()).to("http://www.8531.cn/subscription/more");
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
    public void showError(String message) {

    }

    @Override
    public void updateValue(SubscriptionResponse.DataBean subscriptionResponse) {

    }

    @Override
    public void onRefreshComplete(SubscriptionResponse.DataBean dataBean) {
        if (dataBean.has_subscribe) {
            mArticlePresenter.refreshData(dataBean.article_list);
        } else {
            Fragment fragment = RecommendFragment.newInstance(dataBean.focus_list, dataBean.recommend_list);
            getFragmentManager().beginTransaction().replace(R.id.subscription_container, fragment).commit();
        }
        mArticleFragment.setRefreshing(false);
    }

    @Override
    public void onRefreshError(String message) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    public static Fragment newInstance(List<ArticleResponse.DataBean.Article> article_list) {
        SubscribedArticleFragment fragment = new SubscribedArticleFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SubscribedArticleFragment.ARTICLES, (ArrayList<? extends Parcelable>) article_list);
        fragment.setArguments(args);
        new SubscriptionPresenter(fragment, new SubscriptionStore());
        return fragment;
    }
}
