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
import com.zjrb.core.utils.SettingManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    private String mCity="杭州";

    @BindView(R2.id.progressBar_container)
    View mProgressBarContainer;

    private BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constants.Action.SUBSCRIBE_SUCCESS.equals(intent.getAction())){
                mPresenter.subscribe("杭州");
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_subscription_home, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver,new IntentFilter(Constants.Action.SUBSCRIBE_SUCCESS));
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
                mAMapLocationClient=new AMapLocationClient(getActivity());
                mAMapLocationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        if(aMapLocation.getProvince()!=null && aMapLocation.getProvince().contains("浙江")){
                            mCity=aMapLocation.getCity();
                            mPresenter.subscribe(mCity);
                        }else{
                            mPresenter.subscribe(mCity);
                        }
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
    }


    public static Fragment newInstance() {
        SubscriptionFragment fragment = new SubscriptionFragment();
        new SubscriptionPresenter(fragment, new SubscriptionStore());
        return fragment;
    }
}
