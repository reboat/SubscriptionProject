package com.daily.news.subscription.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.constants.Constants;
import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.common.permission.AbsPermSingleCallBack;
import com.zjrb.core.common.permission.Permission;
import com.zjrb.core.common.permission.PermissionManager;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.core.utils.SettingManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 页面逻辑：
 * 1.有订阅时返回订阅的新闻，无订阅时返回推荐订阅栏目。
 * 2.点击订阅后页面下拉刷新，返回订阅栏目的新闻
 */
public class SubscriptionFragment extends BaseFragment implements SubscriptionContract.View {

    private static final long DURATION_TIME = 24 * 60 * 60 * 1000;
    private Unbinder mUnBinder;
    private SubscriptionContract.Presenter mPresenter;

    private AMapLocationClient mAMapLocationClient;
    private String mCity = "杭州";

    @BindView(R2.id.subscription_container)
    View mContainerView;


    private Disposable mDisposable;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (Constants.Action.SUBSCRIBE_SUCCESS.equals(intent.getAction())) {
                if (mEmitter != null && !mEmitter.isDisposed()) {
                    mEmitter.onNext(intent.getAction());
                }
            }
        }
    };
    private ObservableEmitter<String> mEmitter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_subscription_home, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter(Constants.Action.SUBSCRIBE_SUCCESS));

        mDisposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                mEmitter = e;
            }
        })
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mPresenter.subscribe(mCity);
                    }
                });

        return rootView;
    }

    //TODO 订阅地址获取
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SettingManager.getInstance().setSubscriptionRefreshTime(System.currentTimeMillis());
        PermissionManager.get().request(this, new AbsPermSingleCallBack() {
            @Override
            public void onGranted(boolean isAlreadyDef) {
                mAMapLocationClient = new AMapLocationClient(getActivity());
                mAMapLocationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        if (aMapLocation.getProvince() != null && aMapLocation.getProvince().contains("浙江")) {
                            String temp = aMapLocation.getCity();
                            if (temp.endsWith("市")) {
                                temp.substring(0, temp.indexOf("市"));
                                if(!TextUtils.isEmpty(temp)){
                                    mCity = temp;
                                }
                            }
                        }
                        mPresenter.subscribe(mCity);
                        mAMapLocationClient.stopLocation();
                    }
                });
                mAMapLocationClient.startLocation();
            }

            @Override
            public void onDenied(List<String> neverAskPerms) {
                mPresenter.subscribe(mCity);
            }
        }, Permission.LOCATION_COARSE);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
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
        Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public LoadViewHolder getProgressBar() {
        return new LoadViewHolder(mContainerView, (ViewGroup) mContainerView.getParent());
    }

    @Override
    public void updateValue(SubscriptionResponse.DataBean subscriptionResponse) {
        Fragment fragment;
        if (subscriptionResponse.has_subscribe) {
            fragment = SubscribedArticleFragment.newInstance(subscriptionResponse.article_list);
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
    }

    private void refreshData() {
        long lastRefreshTime = SettingManager.getInstance().getLastSubscriptionRefreshTime();
        if (System.currentTimeMillis() - lastRefreshTime > DURATION_TIME) {
            mPresenter.subscribe(mCity);
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
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }


    public static Fragment newInstance() {
        SubscriptionFragment fragment = new SubscriptionFragment();
        new SubscriptionPresenter(fragment, new SubscriptionStore());
        return fragment;
    }
}
