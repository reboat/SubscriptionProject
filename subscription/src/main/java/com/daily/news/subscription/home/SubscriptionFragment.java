package com.daily.news.subscription.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.constants.Constants;
import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.core.utils.SettingManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;

/**
 * 页面逻辑：
 * 1.有订阅时返回订阅的新闻，无订阅时返回推荐订阅栏目。
 * 2.点击订阅后页面下拉刷新，返回订阅栏目的新闻
 */
public class SubscriptionFragment extends BaseFragment implements SubscriptionContract.View {

    private static final long DURATION_TIME = 24 * 60 * 60 * 1000;
    private Unbinder mUnBinder;
    private SubscriptionContract.Presenter mPresenter;
    private CompositeDisposable mDisposable;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (Constants.Action.SUBSCRIBE_SUCCESS.equals(intent.getAction())) {
                if (mEmitter != null) {
                    mEmitter.onNext(intent.getAction());
                }
            }
        }
    };

    @BindView(R2.id.subscription_container)
    View mContainerView;
    private Fragment fragment;
    private PublishProcessor<String> mEmitter;

    public SubscriptionFragment() {
        new SubscriptionPresenter(this, new SubscriptionStore());
        mDisposable = new CompositeDisposable();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_subscription_home, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter(Constants.Action.SUBSCRIBE_SUCCESS));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SettingManager.getInstance().setSubscriptionRefreshTime(System.currentTimeMillis());
        mPresenter.subscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        if (mEmitter != null) {
            mEmitter.onComplete();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mEmitter = PublishProcessor.create();
        mEmitter.takeLast(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mPresenter.subscribe();
                    }
                });
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
        if (getContext() != null) {
            Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public LoadViewHolder getProgressBar() {
        return new LoadViewHolder(mContainerView, (ViewGroup) mContainerView.getParent());
    }

    @Override
    public void updateValue(SubscriptionResponse.DataBean subscriptionResponse) {
        if (subscriptionResponse.has_subscribe) {
            fragment = MySubscribedFragment.newInstance(subscriptionResponse.article_list);
        } else {
            fragment = RecommendFragment.newInstance(subscriptionResponse.focus_list, subscriptionResponse.recommend_list);
        }
        getFragmentManager().beginTransaction().replace(R.id.subscription_container, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshData();
        }

        if (fragment instanceof MySubscribedFragment && fragment.isAdded()) {
            fragment.onHiddenChanged(hidden);
        }
    }

    private void refreshData() {
        long lastRefreshTime = SettingManager.getInstance().getLastSubscriptionRefreshTime();
        if (System.currentTimeMillis() - lastRefreshTime > DURATION_TIME) {
            mPresenter.subscribe();
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
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        mUnBinder.unbind();
        if (mDisposable != null) {
            mDisposable.clear();
        }
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
        super.onDestroyView();
    }


    public static Fragment newInstance() {
        SubscriptionFragment fragment = new SubscriptionFragment();
        return fragment;
    }
}
