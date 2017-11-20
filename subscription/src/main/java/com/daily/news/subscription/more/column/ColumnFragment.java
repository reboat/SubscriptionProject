package com.daily.news.subscription.more.column;

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
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.ui.widget.load.LoadViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ColumnFragment extends Fragment implements ColumnContract.View, ColumnAdapter.OnSubscribeListener, com.zjrb.core.common.base.adapter.OnItemClickListener {


    @BindView(R2.id.column_recyclerView)
    protected RecyclerView mRecyclerView;
    List<ColumnResponse.DataBean.ColumnBean> mColumns;
    ColumnAdapter mColumnAdapter;

    @BindView(R2.id.column_empty_container)
    ViewGroup mEmptyContainer;


    private ColumnContract.Presenter mPresenter;
    private HeaderRefresh mHeaderRefresh;

    public ColumnFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendRequest(getParams());
    }

    public void sendRequest(Object... params) {
        mPresenter.subscribe(params);
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
        mColumns = new ArrayList<>();
        mColumnAdapter = createColumnAdapter(mColumns);
        mColumnAdapter.setOnSubscribeListener(this);
        mColumnAdapter.setOnItemClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mColumnAdapter);
        mRecyclerView.addItemDecoration(new ListSpaceDivider(0.5d, R.attr.dc_dddddd, true));
    }

    protected ColumnAdapter createColumnAdapter(List<ColumnResponse.DataBean.ColumnBean> columns) {
        return new ColumnAdapter(columns);
    }

    public void addHeaderView(View headerView) {
        mColumnAdapter.addHeaderView(headerView);
    }


    @Override
    public void onSubscribe(ColumnResponse.DataBean.ColumnBean bean) {
        mPresenter.submitSubscribe(bean);
        bean.subscribed = !bean.subscribed;
        mColumnAdapter.notifyDataSetChanged();
    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        Intent intent = new Intent(Constants.Action.SUBSCRIBE_SUCCESS);
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

    public void removeItem(ColumnResponse.DataBean.ColumnBean bean) {
        if (mColumns != null && mColumns.size() > 0) {
            mColumns.remove(bean);
            mColumnAdapter.notifyDataSetChanged();
        }
        checkEmpty();
    }

    public ColumnResponse.DataBean.ColumnBean getItem(int position) {
        if (mColumns != null && mColumns.size() > position) {
            return mColumns.get(position);
        }
        return null;
    }

    public int getItemCount() {
        return mColumns != null ? mColumns.size() : 0;
    }

    public ColumnAdapter getColumnAdapter() {
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
    public void setPresenter(ColumnContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void updateValue(ColumnResponse.DataBean dataBean) {

        if (dataBean.elements == null || dataBean.elements.size() == 0) {
            View emptyView = emptyView(LayoutInflater.from(getActivity()), (ViewGroup) getView());
            if (emptyView != null) {
                mEmptyContainer.removeAllViews();
                mEmptyContainer.addView(emptyView);
            }
            mEmptyContainer.setVisibility(View.VISIBLE);
        } else {
            mEmptyContainer.setVisibility(View.GONE);
        }

        mColumnAdapter.updateValues(dataBean.elements);
        mColumnAdapter.notifyDataSetChanged();
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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
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
    public void onItemClick(View itemView, int position) {
        Nav.with(this).to(Uri.parse("http://www.8531.cn/subscription/detail")
                .buildUpon()
                .appendQueryParameter("id", String.valueOf(mColumns.get(position).id))
                .build()
                .toString());
    }
}
