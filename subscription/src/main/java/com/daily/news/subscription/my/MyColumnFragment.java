package com.daily.news.subscription.my;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.constants.Constants;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.FooterLoadMore;
import com.zjrb.core.recycleView.LoadMore;

import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.network.compatible.APIGetTask;
import cn.daily.news.biz.core.network.compatible.LoadViewHolder;

/**
 * Created by lixinke on 2017/7/17.
 */

public class MyColumnFragment extends ColumnFragment implements LoadMoreListener<ColumnResponse.DataBean> {
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

    private FooterLoadMore<ColumnResponse.DataBean> mFooterLoadMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter(Constants.Action.SUBSCRIBE_SUCCESS));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFooterLoadMore = new FooterLoadMore<>(mRecyclerView, this);
        mColumnAdapter.setFooterLoadMore(mFooterLoadMore.getItemView());
    }

    @Override
    protected float getDividerLeftMargin() {
        return 15;
    }

    @Override
    protected float getDividerRightMargin() {
        return 15;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onSubscribe(ColumnResponse.DataBean.ColumnBean bean) {

        new Analytics.AnalyticsBuilder(getContext(), bean.subscribed ? "A0114" : "A0014", "SubColumn", false)
                .name(bean.subscribed ? "订阅号取消订阅" : "订阅号订阅")
                .pageType("我的订阅页")
                .columnID(String.valueOf(bean.id))
                .seObjectType(ObjectType.C90)
                .columnName(bean.name)
                .operationType(bean.subscribed ? "取消订阅" : "订阅")
                .build()
                .send();

        super.onSubscribe(bean);
    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        super.subscribeSuc(bean);
        removeItem(bean);
        mFooterLoadMore.setState(LoadMore.TYPE_IDLE);
    }

    @Override
    protected void notifyDataChanged(ColumnResponse.DataBean.ColumnBean columnBean) {
        removeItem(columnBean);
        mFooterLoadMore.setState(LoadMore.TYPE_IDLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View emptyView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.subscription_my_subscription_empty, parent, false);
    }

    @Override
    public LoadViewHolder getProgressBar() {
        LoadViewHolder holder = new LoadViewHolder(mRecyclerView, (ViewGroup) mRecyclerView.getParent());
        return holder;
    }

    @Override
    public void onLoadMoreSuccess(ColumnResponse.DataBean data, LoadMore loadMore) {
        if (data != null && data.has_more) {
            loadMore.setState(LoadMore.TYPE_IDLE);
        } else {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }

        if (data != null && data.elements != null && data.elements.size() > 0) {
            mColumnAdapter.addData(data.elements, true);
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<ColumnResponse.DataBean> callback) {

        if (mColumnAdapter != null && mColumnAdapter.getDataSize() > 0) {
            long start = mColumnAdapter.getData(mColumnAdapter.getDataSize() - 1).id;
            new APIGetTask(callback) {
                @Override
                public void onSetupParams(Object... params) {
                    put("start", params[0]);
                }

                @Override
                public String getApi() {
                    return "/api/subscription/user_subscription";
                }
            }.exe(start);
        }else {
            mFooterLoadMore.setState(LoadMore.TYPE_IDLE);
        }


    }
}
