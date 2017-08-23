package com.daily.news.subscription.more.column;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daily.news.subscription.LinearLayoutColorDivider;
import com.daily.news.subscription.OnItemClickListener;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.zjrb.coreprojectlibrary.ui.holder.HeaderRefreshHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ColumnFragment extends Fragment implements ColumnContract.View,
        ColumnAdapter.OnSubscribeListener,
        OnItemClickListener<Column> {
    public static final String ARG_ITEM_ID = "item_id";

    @BindView(R2.id.column_recyclerView)
    RecyclerView mRecyclerView;
    List<Column> mColumns;
    ColumnAdapter mColumnAdapter;

    @BindView(R2.id.column_tip_container)
    View mTipContainer;
    @BindView(R2.id.column_tip_view)
    TextView mTipView;
    @BindView(R2.id.column_progressBar)
    ProgressBar mProgressBar;


    private ColumnContract.Presenter mPresenter;
    private HeaderRefreshHolder mHeaderRefreshHolder;

    public ColumnFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            String itemId = getArguments().getString(ARG_ITEM_ID);
            mPresenter.setItemId(itemId);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.subscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_column, container, false);
        ButterKnife.bind(this, rootView);
        mTipContainer.setVisibility(View.GONE);
        setupRecycleView();
        return rootView;
    }

    private void setupRecycleView() {
        mColumns = new ArrayList<>();
        mColumnAdapter = new ColumnAdapter(mColumns);
        mColumnAdapter.setOnSubscribeListener(this);
        mColumnAdapter.setOnItemClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mColumnAdapter.setDatas(mColumns);
        mRecyclerView.setAdapter(mColumnAdapter);
        mRecyclerView.addItemDecoration(new LinearLayoutColorDivider(getResources(), R.color.dddddd, R.dimen.divide_height, LinearLayoutManager.VERTICAL));

        mHeaderRefreshHolder = new HeaderRefreshHolder(mRecyclerView);
        mHeaderRefreshHolder.setOnRefreshListener(new HeaderRefreshHolder.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        mColumnAdapter.addHeaderView(mHeaderRefreshHolder.getView());
    }

    public void addHeaderView(View headerView) {
        mColumnAdapter.addHeaderView(headerView);
    }

    @Override
    public void onItemClick(int position, Column bean) {
        Intent intent = new Intent(getString(R.string.daily_intent_action));
        intent.setData(Uri.parse("http://www.8531.cn/subscription/detail").buildUpon().appendQueryParameter("uid", bean.uid).build());
        startActivity(intent);
    }

    @Override
    public void onSubscribe(Column bean) {

    }

    @Override
    public void setPresenter(ColumnContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {
//        mTipContainer.setVisibility(View.VISIBLE);
//        mProgressBar.setVisibility(View.VISIBLE);
//        mTipView.setText(R.string.loading);
        mHeaderRefreshHolder.setRefreshing(true);
    }

    @Override
    public void updateValue(List<Column> columnsBeen) {
        mColumnAdapter.updateValues(columnsBeen);
    }

    @Override
    public void hideProgressBar() {
//        mTipContainer.setVisibility(View.GONE);
        mHeaderRefreshHolder.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
//        mTipContainer.setVisibility(View.VISIBLE);
//        mProgressBar.setVisibility(View.GONE);
//        mTipView.setText(message);
        mHeaderRefreshHolder.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
