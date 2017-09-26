package com.daily.news.subscription.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.zjrb.core.utils.SettingManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 页面逻辑：
 * 1.有订阅时返回订阅的新闻，无订阅时返回推荐订阅栏目。
 * 2.点击订阅后页面下拉刷新，返回订阅栏目的新闻
 */
public class SubscriptionFragment extends Fragment implements SubscriptionContract.View {

    private static final long DURATION_TIME = 24 * 60 * 60 * 1000;
    private Unbinder mUnBinder;
    private SubscriptionContract.Presenter mPresenter;

    @BindView(R2.id.progressBar_container)
    View mProgressBarContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subscription_home, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe("杭州");
    }

    @Override
    public void setPresenter(SubscriptionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {
        mProgressBarContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBarContainer.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable message) {
        Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateValue(SubscriptionResponse.DataBean subscriptionResponse) {
        Fragment fragment;
        if (subscriptionResponse.has_subscribe) {
            fragment = SubscribedArticleFragment.newInstance(subscriptionResponse.article_list);
        } else {
            fragment = RecommendFragment.newInstance(subscriptionResponse.focus_list, subscriptionResponse.recommend_list);
        }
        getFragmentManager().beginTransaction().replace(R.id.subscription_container, fragment).commit();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            long lastRefreshTime = SettingManager.getInstance().getLastSubscriptionRefreshTime();
            if (System.currentTimeMillis() - lastRefreshTime > DURATION_TIME) {
                mPresenter.subscribe("杭州");
            }
        }
    }

    @Override
    public void onRefreshComplete(SubscriptionResponse.DataBean dataBean) {

    }

    @Override
    public void onRefreshError(String message) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }


    public static Fragment newInstance() {
        SubscriptionFragment fragment = new SubscriptionFragment();
        new SubscriptionPresenter(fragment, new SubscriptionStore());
        return fragment;
    }
}
