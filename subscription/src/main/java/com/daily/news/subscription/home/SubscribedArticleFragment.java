package com.daily.news.subscription.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.article.ArticleFragment;
import com.daily.news.subscription.article.ArticlePresenter;
import com.daily.news.subscription.article.ArticleResponse;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.GuideView;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.core.utils.UIUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
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
    private ArticleFragment mArticleFragment;
    private List<ArticleResponse.DataBean.Article> mArticles;
    private String mCity = "杭州";
    @BindView(R2.id.my_sub_guide)
    View mMySubscribedView;
    @BindView(R2.id.my_more_guide)
    View mMoreSubscribedView;

    private GuideView.Builder mStep1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_subscribed_article, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);

        mArticleFragment = (ArticleFragment) getChildFragmentManager().findFragmentById(R.id.article_fragment);
        new ArticlePresenter(mArticleFragment, new SubscribeArticleStore(mArticles));
        mArticleFragment.setOnRefreshListener(this);

        String city = getArguments() != null ? getArguments().getString("city") : "";
        mCity = TextUtils.isEmpty(city) ? "杭州" : city;


        GuideView.Builder step2 = new GuideView.Builder(getActivity())
                .setTag("moreSubscription")
                .setGuidePadding(0, UIUtils.dip2px(13), UIUtils.dip2px(9), 0)
                .setGravity(Gravity.RIGHT)
                .setAnchorView(mMoreSubscribedView)
                .setGuideResource(R.drawable.subscription_more_guide);
        mStep1 = new GuideView.Builder(getActivity())
                .setTag("mySubscription")
                .setNext(step2)
                .setGuidePadding(UIUtils.dip2px(20), UIUtils.dip2px(8), 0, 0)
                .setGravity(Gravity.LEFT)
                .setGuideResource(R.drawable.subscription_guide)
                .setAnchorView(mMySubscribedView);
//        mStep1.build();
        mMoreSubscribedView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mStep1.build();
                if (mMoreSubscribedView != null) {
                    mMoreSubscribedView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        return rootView;
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
    public void onHiddenChanged(boolean hidden) {
        mStep1.hide(hidden);
        mMoreSubscribedView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mStep1.build();
                if (mMoreSubscribedView != null) {
                    mMoreSubscribedView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
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
        if (!dataBean.has_subscribe) {
            Fragment fragment = RecommendFragment.newInstance(mCity, dataBean.focus_list, dataBean.recommend_list);
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

    public static Fragment newInstance(String city, List<ArticleResponse.DataBean.Article> article_list) {
        SubscribedArticleFragment fragment = new SubscribedArticleFragment();
        Bundle args = new Bundle();
        args.putString("city", city);
        fragment.setArguments(args);
        article_list.removeAll(Collections.singleton(null));
        fragment.initArticles(article_list);
        new SubscriptionPresenter(fragment, new SubscriptionStore());
        return fragment;
    }
}
