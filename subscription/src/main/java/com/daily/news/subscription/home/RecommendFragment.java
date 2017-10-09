package com.daily.news.subscription.home;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daily.news.subscription.R;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.more.column.LocalColumnStore;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.HeaderRefresh;

import java.util.ArrayList;
import java.util.Collections;
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
    private Banner focusBanner;

    public RecommendFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_recommend, container, false);
        mColumnFragment = (ColumnFragment) getChildFragmentManager().findFragmentById(R.id.column_fragment);
        mColumnPresenter = new ColumnPresenter(mColumnFragment, new LocalColumnStore(getArguments().<ColumnResponse.DataBean.ColumnBean>getParcelableArrayList(COLUMN_DATA)));
        mColumnFragment.setRefreshListener(this);

         focusBanner = setupBannerView(inflater, container, getArguments().<SubscriptionResponse.Focus>getParcelableArrayList(FOCUS_DATA));
        mColumnFragment.addHeaderView(focusBanner);
        mColumnFragment.addHeaderView(setupMoreSubscriptionView(inflater, container));


        return rootView;
    }

    //TODO 修改为通用的 Banner
    private Banner setupBannerView(LayoutInflater inflater, ViewGroup container, List<SubscriptionResponse.Focus> focuses) {

        final Banner focusBanner = (Banner) inflater.inflate(R.layout.subscription_item_focus, container, false);
        focusBanner.isAutoPlay(true);
        focusBanner.setIndicatorGravity(BannerConfig.RIGHT);
        focusBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);

        Observable.fromIterable(focuses).flatMap(new Function<SubscriptionResponse.Focus, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@io.reactivex.annotations.NonNull SubscriptionResponse.Focus focus) throws Exception {
                return Observable.just(focus.doc_title);
            }
        }).toList().subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<String> strings) throws Exception {
                focusBanner.setBannerTitles(strings);
            }
        });

        Observable.fromIterable(focuses).flatMap(new Function<SubscriptionResponse.Focus, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@io.reactivex.annotations.NonNull SubscriptionResponse.Focus focus) throws Exception {
                return Observable.just(focus.pic_url);
            }
        }).toList().subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<String> strings) throws Exception {
                focusBanner.setImages(strings);
            }
        });

        focusBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.placeholder(getResources().getDrawable(R.drawable.default_placeholder_big));
                Glide.with(context).load(path).apply(options).into(imageView);
            }
        });

        focusBanner.start();
        return focusBanner;
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
    public void updateValue(SubscriptionResponse.DataBean subscriptionResponse) {

    }

    @Override
    public void onRefreshComplete(SubscriptionResponse.DataBean dataBean) {
        mColumnFragment.setRefreshing(false);
        if (dataBean.has_subscribe) {
            Fragment fragment = SubscribedArticleFragment.newInstance(dataBean.article_list);
            getFragmentManager().beginTransaction().replace(R.id.subscription_container, fragment).commit();
        } else {
            Fragment fragment=RecommendFragment.newInstance(dataBean.focus_list,dataBean.recommend_list);
            getFragmentManager().beginTransaction().replace(R.id.subscription_container,fragment).commit();
        }
    }

    @Override
    public void onRefreshError(String message) {
        mColumnFragment.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mColumnFragment.setRefreshing(true);
        mPresenter.onRefresh("杭州");
    }
}
