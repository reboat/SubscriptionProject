package com.daily.news.subscription.home;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.more.column.LocalColumnStore;
import com.idisfkj.loopview.LoopView;
import com.idisfkj.loopview.entity.LoopViewEntity;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.HeaderRefresh;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment implements SubscriptionContract.View, HeaderRefresh.OnRefreshListener {


    private static final String COLUMN_DATA = "column_data";
    private static final String FOCUS_DATA = "focus_data";
    private SubscriptionContract.Presenter mPresenter;
    private ColumnPresenter mColumnPresenter;
    private ColumnFragment mColumnFragment;

    public RecommendFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommend, container, false);
        mColumnFragment = (ColumnFragment) getChildFragmentManager().findFragmentById(R.id.column_fragment);
        mColumnPresenter = new ColumnPresenter(mColumnFragment, new LocalColumnStore(getArguments().<ColumnResponse.DataBean.ColumnBean>getParcelableArrayList(COLUMN_DATA)));
        mColumnFragment.setRefreshListener(this);

        View view = setupBannerView(inflater, container, getArguments().<SubscriptionResponse.Focus>getParcelableArrayList(FOCUS_DATA));
        mColumnFragment.addHeaderView(view);

        mColumnFragment.addHeaderView(setupMoreSubscriptionView(inflater,container));


        return rootView;
    }

    private View setupBannerView(LayoutInflater inflater, ViewGroup container, List<SubscriptionResponse.Focus> focuses) {
        if (focuses == null || focuses.size() == 0) {
            return null;
        }
        final LoopView loopView = (LoopView) inflater.inflate(R.layout.item_focus, container, false);
        Observable.fromIterable(focuses).flatMap(new Function<SubscriptionResponse.Focus, ObservableSource<LoopViewEntity>>() {
            @Override
            public ObservableSource<LoopViewEntity> apply(@io.reactivex.annotations.NonNull SubscriptionResponse.Focus focus) throws Exception {
                LoopViewEntity entity = new LoopViewEntity();
                entity.setDescript(focus.doc_title);
                entity.setImageUrl(focus.pic_url);
                return Observable.just(entity);
            }
        }).toList().subscribe(new Consumer<List<LoopViewEntity>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<LoopViewEntity> loopViewEntities) throws Exception {
                loopView.setLoopData(loopViewEntities);
            }
        });
        return loopView;
    }

    private View setupMoreSubscriptionView(LayoutInflater inflater, ViewGroup container) {
        View moreHeaderView = inflater.inflate(R.layout.header_more, container, false);
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
    public void showError(String message) {

    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {

    }

    @Override
    public void subscribeFail(ColumnResponse.DataBean.ColumnBean bean, String message) {

    }

    @Override
    public void updateValue(SubscriptionResponse.DataBean subscriptionResponse) {

    }

    @Override
    public void onRefreshComplete(SubscriptionResponse.DataBean dataBean) {
        mColumnFragment.setRefreshing(false);
        if (dataBean.has_subscribe) {
            Fragment fragment = SubscribedArticleFragment.newInstance(dataBean.article_list);
            getFragmentManager().beginTransaction().replace(R.id.subscription_container, fragment).commit();
        } else {
            mColumnPresenter.refreshData(dataBean.recommend_list);
        }
    }

    @Override
    public void onRefreshError(String message) {

    }

    @Override
    public void onRefresh() {
        mColumnFragment.setRefreshing(true);
        mPresenter.onRefresh("杭州");
    }
}
