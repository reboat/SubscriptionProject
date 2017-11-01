package com.daily.news.subscription.home;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.more.column.LocalColumnStore;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.daily.news.bean.FocusBean;
import com.zjrb.daily.news.ui.holder.HeaderBannerHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment implements SubscriptionContract.View,
        HeaderRefresh.OnRefreshListener, RecommendColumnFragment.OnRefresh {


    private static final String COLUMN_DATA = "column_data";
    private static final String FOCUS_DATA = "focus_data";
    private SubscriptionContract.Presenter mPresenter;
    private ColumnFragment mColumnFragment;
    private View mHeaderBanner;

    public RecommendFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_recommend, container, false);
        mColumnFragment = (ColumnFragment) getChildFragmentManager().findFragmentById(R.id.column_fragment);
        new ColumnPresenter(mColumnFragment, new LocalColumnStore(getArguments().<ColumnResponse.DataBean.ColumnBean>getParcelableArrayList(COLUMN_DATA)));
        mColumnFragment.setRefreshListener(this);

        mHeaderBanner = setupBannerView(getArguments().<SubscriptionResponse.Focus>getParcelableArrayList(FOCUS_DATA));
        if (mHeaderBanner != null) {
            mColumnFragment.addHeaderView(mHeaderBanner);
        }
        mColumnFragment.addHeaderView(setupMoreSubscriptionView(inflater, container));
        return rootView;
    }

    private View setupBannerView(List<SubscriptionResponse.Focus> focuses) {
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
        HeaderBannerHolder bannerHolder = new HeaderBannerHolder(mColumnFragment.getRecyclerView());
        bannerHolder.setData(focusBeans);
        return bannerHolder.getItemView();
    }

    private View setupMoreSubscriptionView(LayoutInflater inflater, ViewGroup container) {
        View moreHeaderView = inflater.inflate(R.layout.subscription_header_more, container, false);
        moreHeaderView.findViewById(R.id.no_subscription_more_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(v.getContext()).to("http://www.8531.cn/subscription/more");
            }
        });
        return moreHeaderView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static Fragment newInstance(List<SubscriptionResponse.Focus> focus_list, List<ColumnResponse.DataBean.ColumnBean> recommend_list) {
        RecommendFragment fragment = new RecommendFragment();
        new SubscriptionPresenter(fragment, new SubscriptionStore());
        focus_list.removeAll(Collections.singleton(null));
        recommend_list.removeAll(Collections.singleton(null));

        Bundle args = new Bundle();
        args.putParcelableArrayList(RecommendFragment.COLUMN_DATA, (ArrayList<? extends Parcelable>) recommend_list);
        args.putParcelableArrayList(RecommendFragment.FOCUS_DATA, (ArrayList<? extends Parcelable>) focus_list);
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
        mColumnFragment.setRefreshing(false);
        FragmentManager fragmentManager = getFragmentManager();
        if (dataBean.has_subscribe && fragmentManager != null) {
            Fragment fragment = SubscribedArticleFragment.newInstance(dataBean.article_list);
            fragmentManager.beginTransaction().replace(R.id.subscription_container, fragment).commit();
        } else if (!dataBean.has_subscribe && fragmentManager != null) {
            Fragment fragment = RecommendFragment.newInstance(dataBean.focus_list, dataBean.recommend_list);
            fragmentManager.beginTransaction().replace(R.id.subscription_container, fragment).commit();
        }
    }

    @Override
    public void onRefreshError(String message) {
        mColumnFragment.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mColumnFragment.setRefreshing(true);
        mPresenter.onRefresh();
    }
}
