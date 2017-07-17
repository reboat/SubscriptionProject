package com.daily.news.subscription.more.detail;

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

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.CategoryBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment implements DetailContract.View, ColumnAdapter.OnItemClickListener {
    public static final String ARG_ITEM_ID = "item_id";

    @BindView(R2.id.more_detail_recyclerView)
    RecyclerView mRecyclerView;
    List<CategoryBean.DataBean.ElementsBean.ColumnsBean> mColumnsBeans;
    ColumnAdapter mColumnAdapter;

    @BindView(R2.id.more_detail_tip_container)
    View mTipContainer;
    @BindView(R2.id.more_detail_tip_view)
    TextView mTipView;
    @BindView(R2.id.more_detail_progressBar)
    ProgressBar mProgressBar;

    private DetailContract.Presenter mPresenter;

    public DetailFragment() {
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
        View rootView = inflater.inflate(R.layout.more_detail, container, false);
        ButterKnife.bind(this,rootView);
        setupRecycleView();
        return rootView;
    }

    private void setupRecycleView() {
        mColumnsBeans = new ArrayList<>();
        mColumnAdapter = new ColumnAdapter(mColumnsBeans);
        mColumnAdapter.setOnItemClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mColumnAdapter);
    }

    @Override
    public void onItemClick(int position, CategoryBean.DataBean.ElementsBean.ColumnsBean bean) {

    }

    @Override
    public void onSubscribe(CategoryBean.DataBean.ElementsBean.ColumnsBean bean) {

    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {
        mTipContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTipView.setText("加载中...");
    }

    @Override
    public void updateValue(List<CategoryBean.DataBean.ElementsBean.ColumnsBean> columnsBeen) {
        mColumnAdapter.updateValues(columnsBeen);
    }

    @Override
    public void hideProgressBar() {
        mTipContainer.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        mTipContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTipView.setText(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
