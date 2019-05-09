package com.daily.news.subscription.more.search;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.constants.Constants;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.widget.SubscriptionDivider;
import com.zjrb.core.recycleView.HeaderRefresh;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.network.compatible.LoadViewHolder;

/**
 * Created by gaoyangzhen on 2018/3/14.
 */

public class SearchBaseFragment extends Fragment implements SearchContract.View, SearchBaseAdapter.OnSubscribeListener, OnItemClickListener {

    private static final int REQUEST_CODE_TO_DETAIL = 1110;
    protected String mChannelName;
    protected String mChannelId;

    @BindView(R2.id.column_recyclerView)
    protected RecyclerView mRecyclerView;
    List<SearchResponse.DataBean.ColumnBean> mColumns;
    SearchBaseAdapter mColumnAdapter;

    @BindView(R2.id.column_empty_container)
    ViewGroup mEmptyContainer;


    private SearchContract.Presenter mPresenter;
    private HeaderRefresh mHeaderRefresh;
    private String mKeyword;

    public SearchBaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChannelName = getArguments().getString("channel_name");
            mChannelId = getArguments().getString("channel_id");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mKeyword = getParams()[0].toString();
        sendRequest(mKeyword);
    }

    public void sendRequest(Object... params) {
        mKeyword = params[0].toString();
        mPresenter.subscribe(mKeyword);
    }

    public Object[] getParams() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_column, container, false);
        ButterKnife.bind(this, rootView);
        setupRecycleView();
        return rootView;
    }

    private void setupRecycleView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mColumns = new ArrayList<>();
        mColumnAdapter = createColumnAdapter(mColumns);
        mColumnAdapter.setOnSubscribeListener(this);
        mColumnAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mColumnAdapter);
        mRecyclerView.addItemDecoration(new SubscriptionDivider(15, 15));
    }

    protected SearchBaseAdapter createColumnAdapter(List<SearchResponse.DataBean.ColumnBean> columns) {
        return new SearchBaseAdapter(mRecyclerView, columns);
    }

    public void addHeaderView(View headerView) {
        mColumnAdapter.addHeaderView(headerView);
    }


    @Override
    public void onSubscribe(SearchResponse.DataBean.ColumnBean bean) {

        new Analytics.AnalyticsBuilder(getContext(), bean.subscribed?"A0114":"A0014", "SubColumn", false)
                .name(bean.subscribed?"订阅号取消订阅":"订阅号订阅")
                .classID(mChannelId)
                .pageType("订阅号分类检索页面")
                .columnID(String.valueOf(bean.id))
                .seObjectType(ObjectType.C90)
                .classShortName(mChannelName)
                .columnName(bean.name)
                .selfChannelID(mChannelId)
                .channelName(mChannelName)
                .operationType(bean.subscribed?"取消订阅":"订阅")
                .build()
                .send();

        mPresenter.submitSubscribe(bean);
        bean.subscribed = !bean.subscribed;
        mColumnAdapter.notifyDataSetChanged();
    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        Intent intent = new Intent(Constants.Action.SUBSCRIBE_SUCCESS);
        intent.putExtra(Constants.Name.SUBSCRIBE, bean.subscribed);
        intent.putExtra(Constants.Name.ID, bean.id);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    protected boolean isHasSubscribe() {
        for (int i = 0, count = mColumns.size(); i < count; i++) {
            if (mColumns.get(i).subscribed) {
                return true;
            }
        }
        return false;
    }

    public void removeItem(SearchResponse.DataBean.ColumnBean bean) {
        if (mColumns != null && mColumns.size() > 0) {
            mColumns.remove(bean);
            mColumnAdapter.notifyDataSetChanged();
        }
        checkEmpty();
    }

    public SearchResponse.DataBean.ColumnBean getItem(int position) {
        if (mColumns != null && mColumns.size() > position) {
            return mColumns.get(position);
        }
        return null;
    }

    public int getItemCount() {
        return mColumns != null ? mColumns.size() : 0;
    }

    public SearchBaseAdapter getColumnAdapter() {
        return mColumnAdapter;
    }

    private void checkEmpty() {
        if (mColumns == null || mColumns.size() == 0) {
            View emptyView = emptyView(LayoutInflater.from(getContext()), (ViewGroup) getView());
            if (emptyView != null) {
                mEmptyContainer.removeAllViews();
                mEmptyContainer.addView(emptyView);
                mEmptyContainer.setVisibility(View.VISIBLE);
            }

        }
    }

    public View emptyView(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    @Override
    public void subscribeFail(ColumnResponse.DataBean.ColumnBean bean, String message) {
        bean.subscribed = !bean.subscribed;
        mColumnAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), bean.subscribed ? "取消订阅失败!" : "订阅失败!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void updateValue(SearchResponse.DataBean dataBean) {

        if (dataBean == null || dataBean.elements == null || dataBean.elements.size() == 0) {
            View emptyView = emptyView(LayoutInflater.from(getActivity()), (ViewGroup) getView());
            if (emptyView != null) {
                mEmptyContainer.removeAllViews();
                mEmptyContainer.addView(emptyView);
            }
            mEmptyContainer.setVisibility(View.VISIBLE);
        } else {
            mEmptyContainer.setVisibility(View.GONE);
            mColumnAdapter.updateKeyword(mKeyword);
            mColumnAdapter.updateValues(dataBean.elements);
            mColumnAdapter.notifyDataSetChanged();
        }

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
    public void onDestroyView() {
        mPresenter.unsubscribe();
        super.onDestroyView();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRefreshListener(HeaderRefresh.OnRefreshListener onRefreshListener) {
        mHeaderRefresh = new HeaderRefresh(mRecyclerView, onRefreshListener);
        mColumnAdapter.addHeaderView(mHeaderRefresh.getItemView());
    }

    public void setRefreshing(boolean refresh) {
        if (mHeaderRefresh != null) {
            mRecyclerView.scrollToPosition(0);
            mHeaderRefresh.setRefreshing(refresh);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_TO_DETAIL) {
            long id = data.getLongExtra(Constants.Name.ID, 0);
            boolean subscribe = data.getBooleanExtra(Constants.Name.SUBSCRIBE, false);
            for (int i = 0, size = getItemCount(); i < size; i++) {
                if (id == getItem(i).id) {
                    getItem(i).subscribed = subscribe;
                    getColumnAdapter().notifyItemChanged(i);
                    break;
                }
            }

        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Nav.with(this).toPath(new Uri.Builder().path("/subscription/detail")
                .appendQueryParameter("id", String.valueOf(mColumns.get(position).id))
                .appendQueryParameter("channel_id",mChannelId)
                .appendQueryParameter("channel_name",mChannelName)
                .build()
                .toString(), REQUEST_CODE_TO_DETAIL);

        ColumnResponse.DataBean.ColumnBean bean = getItem(position);
        if (bean != null) {
            new Analytics.AnalyticsBuilder(getContext(), "500003", "ToDetailColumn", false)
                    .name("点击订阅号条目")
                    .pageType("订阅号分类检索页面")
                    .columnID(String.valueOf(bean.id))
                    .seObjectType(ObjectType.C90)
                    .columnName(bean.name)
                    .build()
                    .send();
        }
    }


    public void clear() {
        mColumnAdapter.getData().clear();
        mColumnAdapter.notifyDataSetChanged();
        mColumnAdapter.restMoreState();
    }
}
