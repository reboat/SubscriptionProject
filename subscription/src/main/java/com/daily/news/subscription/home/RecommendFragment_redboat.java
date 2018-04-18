package com.daily.news.subscription.home;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.task.GetInitializeResourceTask;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.daily.news.bean.FocusBean;
import com.zjrb.daily.news.ui.holder.HeaderBannerHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.daily.news.analytics.Analytics;

/**
 * Created by gaoyangzhen on 2018/4/16.
 */

public class RecommendFragment_redboat extends Fragment implements SubscriptionContract.View,
        HeaderRefresh.OnRefreshListener, RecommendColumnFragment.OnRefresh {

    private static final String TAG_INITIALIZE_RESOURCE = "initialize_resource";

    private static final String COLUMN_DATA = "column_data";
    private static final String FOCUS_DATA = "focus_data";
    @BindView(R2.id.recyclerView_new)
    RecyclerView recyclerViewNew;
    Unbinder unbinder;
    private SubscriptionContract.Presenter mPresenter;
    private View mHeaderBanner;
    private HeaderRefresh mHeaderRefresh;

    RecommendAdapter recommendAdapter;
    List<ColumnResponse.DataBean.ColumnBean> mColumns;

    public RecommendFragment_redboat() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_recommend_redboat, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setupRecycleView();
        mHeaderBanner = setupBannerView(getArguments().<SubscriptionResponse.Focus>getParcelableArrayList(FOCUS_DATA));
        if (mHeaderBanner != null) {
            addHeaderView(mHeaderBanner);
        }
        setRefreshListener(this);
        addHeaderView(setupMoreSubscriptionView(inflater, container));

        addHeaderView(setupRecommendView(getArguments().<ColumnResponse.DataBean.ColumnBean>getParcelableArrayList(COLUMN_DATA)));

        return rootView;
    }

    private void setupRecycleView() {
        mColumns = new ArrayList<>();
        recommendAdapter = createRecommendAdapter(mColumns);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerViewNew.setLayoutManager(manager);
        recyclerViewNew.setAdapter(recommendAdapter);
        recyclerViewNew.addItemDecoration(new ListSpaceDivider(0.5d, R.attr.dc_dddddd, true));
    }

    protected RecommendAdapter createRecommendAdapter(List<ColumnResponse.DataBean.ColumnBean> columns) {
        return new RecommendAdapter(getActivity(), columns);
    }

    public void addHeaderView(View headerView) {

        recommendAdapter.addHeaderView(headerView);
    }

    public RecyclerView getRecyclerView() {
        return recyclerViewNew;
    }

    public void setRefreshListener(HeaderRefresh.OnRefreshListener onRefreshListener) {
        mHeaderRefresh = new HeaderRefresh(recyclerViewNew, onRefreshListener);
        recommendAdapter.addHeaderView(mHeaderRefresh.getItemView());
    }

    public void setRefreshing(boolean refresh) {
        if (mHeaderRefresh != null) {
            recyclerViewNew.scrollToPosition(0);
            mHeaderRefresh.setRefreshing(refresh);
        }
    }

    private View setupBannerView(final List<SubscriptionResponse.Focus> focuses) {
        if (focuses == null || focuses.size() == 0) {
            return null;
        }
        List<FocusBean> focusBeans = new ArrayList<>();
        for (int i = 0; i < focuses.size(); i++) {
            FocusBean bean = new FocusBean();
            SubscriptionResponse.Focus focus = focuses.get(i);
            bean.setId(focus.channel_article_id);
            bean.setImage_url(focus.pic_url);
            bean.setUrl(focus.doc_url);
            bean.setSort_number(focus.sort_number);
            bean.setTitle(focus.doc_title);
            bean.setTag(focus.tag);
            focusBeans.add(bean);
        }
        HeaderBannerHolder bannerHolder = new HeaderBannerHolder(getRecyclerView()) {
            @Override
            public void onItemClick(View item, int position) {
                super.onItemClick(item, position);
                SubscriptionResponse.Focus focus = focuses.get(position);
                new Analytics.AnalyticsBuilder(getContext(), "200005", "200005")
                        .setClassifyID(String.valueOf(focus.channel_article_id))
                        .setPageType("订阅首页")
                        .setEvenName("焦点图点击")
                        .setObjectName(focus.doc_title)
                        .setObjectType(ObjectType.NewsType)
                        .build()
                        .send();
            }
        };
        bannerHolder.setData(focusBeans);
        return bannerHolder.getItemView();
    }

    private View setupMoreSubscriptionView(LayoutInflater inflater, ViewGroup container) {
        View moreHeaderView = inflater.inflate(R.layout.subscription_header_more, container, false);
        moreHeaderView.findViewById(R.id.no_subscription_more_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断红船号开关，如果没有开关数据，默认是关闭的
                GetInitializeResourceTask.createTask(RecommendFragment_redboat.this, TAG_INITIALIZE_RESOURCE);

//                Nav.with(v.getContext()).to("http://www.8531.cn/subscription/more");
                new Analytics.AnalyticsBuilder(getContext(), "500002", "500002")
                        .setPageType("订阅首页")
                        .setEvenName("点击订阅更多")
                        .build()
                        .send();
            }
        });
        return moreHeaderView;
    }


    private View setupRecommendView(final List<ColumnResponse.DataBean.ColumnBean> recommend_list)
    {
        RecommendViewHolder recommendViewHolder = new RecommendViewHolder(getRecyclerView(), getActivity(), recommend_list);

        return recommendViewHolder.getItemView();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static Fragment newInstance(List<SubscriptionResponse.Focus> focus_list, List<ColumnResponse.DataBean.ColumnBean> recommend_list) {
        RecommendFragment_redboat fragment = new RecommendFragment_redboat();
        new SubscriptionPresenter(fragment, new SubscriptionStore());
        if (focus_list != null && focus_list.size() > 0) {
            focus_list.removeAll(Collections.singleton(null));
        }
        if (recommend_list != null && recommend_list.size() > 0) {
            recommend_list.removeAll(Collections.singleton(null));
        }

        Bundle args = new Bundle();
        args.putParcelableArrayList(RecommendFragment_redboat.COLUMN_DATA, (ArrayList<? extends Parcelable>) recommend_list);
        args.putParcelableArrayList(RecommendFragment_redboat.FOCUS_DATA, (ArrayList<? extends Parcelable>) focus_list);
        fragment.setArguments(args);
        return fragment;
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

    }

    @Override
    public LoadViewHolder getProgressBar() {
        return null;
    }


    @Override
    public void updateValue(SubscriptionResponse.DataBean subscriptionResponse) {

    }

    @Override
    public void onRefreshComplete(SubscriptionResponse.DataBean dataBean) {
        setRefreshing(false);
        FragmentManager fragmentManager = getFragmentManager();
        if (dataBean.has_subscribe && fragmentManager != null) {
            Fragment fragment = MySubscribedFragment.newInstance(dataBean.article_list);
            fragmentManager.beginTransaction().replace(R.id.subscription_container, fragment).commitAllowingStateLoss();
        } else if (!dataBean.has_subscribe && fragmentManager != null) {
            Fragment fragment = RecommendFragment_redboat.newInstance(dataBean.focus_list, dataBean.recommend_list);
            fragmentManager.beginTransaction().replace(R.id.subscription_container, fragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onRefreshError(String message) {
        setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        setRefreshing(true);
        mPresenter.onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

