package com.daily.news.subscription.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.zjrb.core.common.biz.ResourceBiz;
import com.zjrb.core.db.SPHelper;
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
import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.Analytics.AnalyticsBuilder;

import static java.lang.System.in;


/**
 * 页面逻辑：
 * 1.有订阅时返回订阅的新闻，无订阅时返回推荐订阅栏目。
 * 2.点击订阅后页面下拉刷新，返回订阅栏目的新闻
 */
public class MySubscribedFragment extends Fragment implements SubscriptionContract.View, HeaderRefresh.OnRefreshListener {

    private static final int REQUEST_CODE_MY = 1001;

    private Unbinder mUnBinder;
    private SubscriptionContract.Presenter mPresenter;
    private ArticleFragment mArticleFragment;
    private List<ArticleResponse.DataBean.Article> mArticles;
    @BindView(R2.id.my_sub_guide)
    View mMySubscribedView;
    @BindView(R2.id.my_more_guide)
    View mMoreSubscribedView;

    private GuideView.Builder mStep1;
    private Analytics mAnalytics;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_subscribed_article, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);

        mArticleFragment = (ArticleFragment) getChildFragmentManager().findFragmentById(R.id.article_fragment);
        new ArticlePresenter(mArticleFragment, new SubscribeArticleStore(mArticles));
        mArticleFragment.setOnRefreshListener(this);

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
        mPresenter.onRefresh();
    }

    @OnClick(R2.id.my_sub_btn)
    public void gotoMySubscription() {
        Nav.with(this).to("http://www.8531.cn/subscription/more/my/column", REQUEST_CODE_MY);
        new AnalyticsBuilder(getContext(), "500006", "500006")
                .setEvenName("点击“我的订阅”")
                .setPageType("订阅首页")
                .build()
                .send();

    }

    @OnClick(R2.id.my_sub_more_btn)
    public void gotoMore() {
        //判断红船号开关，如果没有开关数据，默认是关闭的
        ResourceBiz resourceBiz = SPHelper.get().getObject(SPHelper.Key.INITIALIZATION_RESOURCES);
        if (resourceBiz != null && resourceBiz.feature_list != null) {
            int i = 0;
            for(ResourceBiz.FeatureListBean bean : resourceBiz.feature_list)
            {
                if(bean.name.equals("hch"))
                {
                    i = 1;
                    if(bean.enabled)
                    {
                        Nav.with(this).to("http://www.8531.cn/subscription/more_new");
                    }
                    else
                    {
                        Nav.with(this).to("http://www.8531.cn/subscription/more");
                    }
                    break;
                }
            }
            if(i == 0)
            {
                Nav.with(this).to("http://www.8531.cn/subscription/more");
            }
            }
            else
        {
            Nav.with(this).to("http://www.8531.cn/subscription/more");
        }

//        Nav.with(this).to("http://www.8531.cn/subscription/more");
        new AnalyticsBuilder(getContext(), "500007", "500007")
                .setEvenName("点击“订阅更多”")
                .setPageType("订阅首页")
                .build()
                .send();
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
        mAnalytics = new AnalyticsBuilder(getContext(), "A0010", "500001")
                .setEvenName("页面停留时长")
                .setPageType("订阅首页")
                .build();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAnalytics.sendWithDuration();
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
        mArticleFragment.setRefreshing(false);
        FragmentManager fragmentManager = getFragmentManager();

        if (!dataBean.has_subscribe && fragmentManager != null) {
            Fragment fragment = RecommendFragment.newInstance(dataBean.focus_list, dataBean.recommend_list);
            getFragmentManager().beginTransaction().replace(R.id.subscription_container, fragment).commitAllowingStateLoss();
        } else if (dataBean.has_subscribe && fragmentManager != null) {
            //解决切换用户的问题
            Fragment fragment = MySubscribedFragment.newInstance(dataBean.article_list);
            fragmentManager.beginTransaction().replace(R.id.subscription_container, fragment).commitAllowingStateLoss();
        }
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
        MySubscribedFragment fragment = new MySubscribedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        if (article_list != null && article_list.size() > 0) {
            article_list.removeAll(Collections.singleton(null));
        }
        fragment.initArticles(article_list);
        new SubscriptionPresenter(fragment, new SubscriptionStore());
        return fragment;
    }
}
