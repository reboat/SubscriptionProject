package com.daily.news.subscription.my;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.constants.Constants;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnResponse;

import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.network.compatible.LoadViewHolder;

/**
 * Created by lixinke on 2017/7/17.
 */

public class MyColumnFragment extends ColumnFragment {
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.Action.SUBSCRIBE_SUCCESS.equals(intent.getAction())) {
                if (!intent.getBooleanExtra(Constants.Name.SUBSCRIBE, true)) {
                    sendRequest("杭州");
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter(Constants.Action.SUBSCRIBE_SUCCESS));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onSubscribe(ColumnResponse.DataBean.ColumnBean bean) {

        new Analytics.AnalyticsBuilder(getContext(), "A0114", "SubColumn", false)
                .name(bean.subscribed?"订阅号订阅":"订阅号取消订阅")
                .pageType("我的订阅页")
                .columnID(String.valueOf(bean.id))
                .columnName(bean.name)
                .objectType("C90")
                .operationType(bean.subscribed?"取消订阅":"订阅")
                .build()
                .send();


        super.onSubscribe(bean);
    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        super.subscribeSuc(bean);
        removeItem(bean);
    }

    @Override
    public View emptyView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.subscription_my_subscription_empty, parent, false);
    }

    @Override
    public LoadViewHolder getProgressBar() {
        LoadViewHolder holder=new LoadViewHolder(mRecyclerView, (ViewGroup) mRecyclerView.getParent());
        return holder;
    }
}
