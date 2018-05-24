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
import com.google.gson.Gson;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.core.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;

/**
 * Created by gaoyangzhen on 2018/3/14.
 */

public class SearchBaseFragment extends Fragment implements SearchContract.View, SearchBaseAdapter.OnSubscribeListener, com.zjrb.core.common.base.adapter.OnItemClickListener {

    private static final int REQUEST_CODE_TO_DETAIL = 1110;

    String data = "{\n" +
            "        \"red_boat_columns\":[{\n" +
            "                \"id\": 125,\n" +
            "                \"name\": \"浙江发布\",\n" +
            "                \"pic_url\": \"http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg\",\n" +
            "                \"subscribe_count\": 100,\n" +
            "                \"article_count\": 200,\n" +
            "                \"subscribe_count_general\": \"3.4万\",\n" +
            "                \"article_count_general\": \"145.6万\",             \n" +
            "                \"subscribed\": false,\n" +
            "                \"sort_number\": 1\n" +
            "            }, {\n" +
            "                \"id\": 126,\n" +
            "                \"name\": \"浙江公安\",\n" +
            "                \"pic_url\": \"http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg\",\n" +
            "                \"subscribe_count\": 100,\n" +
            "                \"article_count\": 200,\n" +
            "                \"subscribe_count_general\": \"3.4万\",\n" +
            "                \"article_count_general\": \"145.6万\",               \n" +
            "                \"subscribed\": false,\n" +
            "                \"sort_number\": 2\n" +
            "            }\n" +
            "        ],\n" +
            "        \"general_columns\": [{\n" +
            "                \"id\": 127,\n" +
            "                \"name\": \"浙江发布\",\n" +
            "                \"pic_url\": \"http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg\",\n" +
            "                \"subscribe_count\": 100,\n" +
            "                \"article_count\": 200,\n" +
            "                \"subscribe_count_general\": \"3.4万\",\n" +
            "                \"article_count_general\": \"145.6万\",             \n" +
            "                \"subscribed\": false,\n" +
            "                \"sort_number\": 1\n" +
            "            }, {\n" +
            "                \"id\": 128,\n" +
            "                \"name\": \"浙江公安\",\n" +
            "                \"pic_url\": \"http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg\",\n" +
            "                \"subscribe_count\": 100,\n" +
            "                \"article_count\": 200,\n" +
            "                \"subscribe_count_general\": \"3.4万\",\n" +
            "                \"article_count_general\": \"145.6万\",               \n" +
            "                \"subscribed\": false,\n" +
            "                \"sort_number\": 2\n" +
            "            }\n" +
            "        ]\n" +
            "    }";

    SearchResponse.DataBean dataBean;


    @BindView(R2.id.column_recyclerView)
    protected RecyclerView mRecyclerView;
    List<SearchResponse.DataBean.ColumnBean> mColumns;
    SearchBaseAdapter mColumnAdapter;

    @BindView(R2.id.column_empty_container)
    ViewGroup mEmptyContainer;


    private SearchContract.Presenter mPresenter;
    private HeaderRefresh mHeaderRefresh;

    public SearchBaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendRequest(getParams());

//        dataBean = new Gson().fromJson(data, SearchResponse.DataBean.class);
//        updateValue(dataBean);
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

    protected SearchBaseAdapter createColumnAdapter(List<SearchResponse.DataBean.ColumnBean> columns) {
        return new SearchBaseAdapter(columns);
    }

    public void addHeaderView(View headerView) {
        mColumnAdapter.addHeaderView(headerView);
    }


    @Override
    public void onSubscribe(SearchResponse.DataBean.ColumnBean bean) {
        mPresenter.submitSubscribe(bean);
        bean.subscribed = !bean.subscribed;
        mColumnAdapter.notifyDataSetChanged();
    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
//        Intent intent = new Intent(Constants.Action.SUBSCRIBE_SUCCESS);
//        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        Intent intent = new Intent(Constants.Action.SUBSCRIBE_SUCCESS);
        intent.putExtra(Constants.Name.SUBSCRIBE, bean.subscribed);
        intent.putExtra(Constants.Name.ID, bean.id);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
//        getActivity().setResult(Activity.RESULT_OK,intent);
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

        if ((dataBean.red_boat_columns == null || dataBean.red_boat_columns.size() == 0) && (dataBean.general_columns == null || dataBean.general_columns.size() == 0)) {
            View emptyView = emptyView(LayoutInflater.from(getActivity()), (ViewGroup) getView());
            if (emptyView != null) {
                mEmptyContainer.removeAllViews();
                mEmptyContainer.addView(emptyView);
            }
            mEmptyContainer.setVisibility(View.VISIBLE);
        } else {
            mEmptyContainer.setVisibility(View.GONE);
        }

        mColumnAdapter.updateValues(dataBean.red_boat_columns, dataBean.general_columns);
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
//                    Intent intent = new Intent(Constants.Action.SUBSCRIBE_SUCCESS);
//                    intent.putExtra(Constants.Name.SUBSCRIBE, subscribe);
//                    intent.putExtra(Constants.Name.ID, id);
//                    getActivity().setResult(Activity.RESULT_OK,intent);
                    break;
                }
            }

        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Nav.with(this).to(Uri.parse("http://www.8531.cn/subscription/detail")
                .buildUpon()
                .appendQueryParameter("id", String.valueOf(mColumns.get(position).id))
                .build()
                .toString(),REQUEST_CODE_TO_DETAIL);

        ColumnResponse.DataBean.ColumnBean bean = getItem(position);
        if (bean != null) {
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new Analytics.AnalyticsBuilder(getContext(), "500003", "500003")
                    .setEvenName("搜索页面")
                    .setPageType("订阅更多页面")
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setOtherInfo(otherInfoStr)
                    .build()
                    .send();
        }
    }


}
