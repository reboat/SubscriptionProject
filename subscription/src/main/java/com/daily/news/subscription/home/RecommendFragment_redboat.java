package com.daily.news.subscription.home;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.more.column.LocalColumnStore;
import com.daily.news.subscription.task.GetInitializeResourceTask;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.daily.news.bean.FocusBean;
import com.zjrb.daily.news.ui.holder.HeaderBannerHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics.AnalyticsBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment_redboat extends Fragment implements SubscriptionContract.View,
        HeaderRefresh.OnRefreshListener, RecommendColumnFragment.OnRefresh {

    private static final String TAG_INITIALIZE_RESOURCE = "initialize_resource";

    private static final String COLUMN_DATA = "column_data";
    private static final String COLUMN_DATA_REDBOAT = "column_data_redboat";
    private static final String FOCUS_DATA = "focus_data";
    @BindView(R2.id.no_subscription_more_view)
    LinearLayout noSubscriptionMoreView;
    @BindView(R2.id.header_rel)
    RelativeLayout headerRel;
    @BindView(R2.id.tab_red_sub)
    FrameLayout tabRedSub;
    @BindView(R2.id.tab_my_sub)
    FrameLayout tabMySub;
    private SubscriptionContract.Presenter mPresenter;
    private ColumnPresenter mColumnresenter;
    private ColumnFragment mColumnFragment;
    private View mHeaderBanner;
    private int firstItemPosition = 0;

    FrameLayout tabRedSub_bar;
    FrameLayout tabMySub_bar;


    boolean isRedboatChecked = true;

    public RecommendFragment_redboat() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_recommend_redboat, container, false);
        ButterKnife.bind(this, rootView);
        mColumnFragment = (ColumnFragment) getChildFragmentManager().findFragmentById(R.id.column_fragment);
        isRedboatChecked = getArguments().getBoolean("isRedboatChecked");
        mColumnresenter = new ColumnPresenter(mColumnFragment, new LocalColumnStore(getArguments().<ColumnResponse.DataBean.ColumnBean>getParcelableArrayList(isRedboatChecked ? COLUMN_DATA_REDBOAT : COLUMN_DATA)));

        mColumnFragment.setRefreshListener(this);
        setScrollListener();

        mHeaderBanner = setupBannerView(getArguments().<SubscriptionResponse.Focus>getParcelableArrayList(FOCUS_DATA));

        if (mHeaderBanner != null) {
            mColumnFragment.addHeaderView(mHeaderBanner);
        }
        mColumnFragment.addHeaderView(setupMoreSubscriptionView(inflater, container));
        onViewClicked(isRedboatChecked ? tabRedSub : tabMySub);
        return rootView;
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
        HeaderBannerHolder bannerHolder = new HeaderBannerHolder(mColumnFragment.getRecyclerView()) {
            @Override
            public void onItemClick(View item, int position) {
                super.onItemClick(item, position);
                SubscriptionResponse.Focus focus = focuses.get(position);
                new AnalyticsBuilder(getContext(), "200005", "200005")
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
        View moreHeaderView = inflater.inflate(R.layout.subscription_header_more_redboat, container, false);
        moreHeaderView.findViewById(R.id.no_subscription_more_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断红船号开关，如果没有开关数据，默认是关闭的
                GetInitializeResourceTask.createTask(RecommendFragment_redboat.this, TAG_INITIALIZE_RESOURCE);

//                Nav.with(v.getContext()).to("http://www.8531.cn/subscription/more");
                new AnalyticsBuilder(getContext(), "500002", "500002")
                        .setPageType("订阅首页")
                        .setEvenName("点击订阅更多")
                        .build()
                        .send();
            }
        });
        tabRedSub_bar = (FrameLayout) moreHeaderView.findViewById(R.id.tab_red_sub_bar);
        tabRedSub_bar.setSelected(true);
        tabRedSub_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked(tabRedSub);
            }
        });
        tabMySub_bar = (FrameLayout) moreHeaderView.findViewById(R.id.tab_my_sub_bar);
        tabMySub_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked(tabMySub);
            }
        });


        return moreHeaderView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static Fragment newInstance(List<SubscriptionResponse.Focus> focus_list, List<ColumnResponse.DataBean.ColumnBean> recommend_list, List<ColumnResponse.DataBean.ColumnBean> redboat_recommend_list, boolean isRedboatChecked) {
        RecommendFragment_redboat fragment = new RecommendFragment_redboat();
        new SubscriptionPresenter(fragment, new SubscriptionStore());
        if (focus_list != null && focus_list.size() > 0) {
            focus_list.removeAll(Collections.singleton(null));
        }
        if (recommend_list != null && recommend_list.size() > 0) {
            recommend_list.removeAll(Collections.singleton(null));
        }
        if (redboat_recommend_list != null && redboat_recommend_list.size() > 0) {
            redboat_recommend_list.removeAll(Collections.singleton(null));
        }


        Bundle args = new Bundle();
        args.putParcelableArrayList(RecommendFragment_redboat.COLUMN_DATA, (ArrayList<? extends Parcelable>) recommend_list);
        args.putParcelableArrayList(RecommendFragment_redboat.COLUMN_DATA_REDBOAT, (ArrayList<? extends Parcelable>) redboat_recommend_list);
        args.putParcelableArrayList(RecommendFragment_redboat.FOCUS_DATA, (ArrayList<? extends Parcelable>) focus_list);
        args.putBoolean("isRedboatChecked", isRedboatChecked);
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
            Fragment fragment = MySubscribedFragment.newInstance(dataBean.article_list);
            fragmentManager.beginTransaction().replace(R.id.subscription_container, fragment).commitAllowingStateLoss();
        } else if (!dataBean.has_subscribe && fragmentManager != null) {
            Fragment fragment = RecommendFragment_redboat.newInstance(dataBean.focus_list, dataBean.recommend_list, dataBean.redboat_recommend_list, isRedboatChecked);
            fragmentManager.beginTransaction().replace(R.id.subscription_container, fragment).commitAllowingStateLoss();
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

    /**
     * recycleview设置滑动监听，控制headview的置顶
     */
    public void setScrollListener() {
        mColumnFragment.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
//                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if (firstItemPosition < 2) {
                        headerRel.setVisibility(View.GONE);
                    } else {
                        headerRel.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @OnClick({R2.id.tab_red_sub, R2.id.tab_my_sub, R2.id.no_subscription_more_view})
    public void onViewClicked(View view) {

        if (view.getId() == R.id.tab_red_sub) {
            isRedboatChecked = true;
            mColumnresenter.refreshData(getArguments().<ColumnResponse.DataBean.ColumnBean>getParcelableArrayList(COLUMN_DATA_REDBOAT));
            tabRedSub.setSelected(true);
            tabRedSub_bar.setSelected(true);
            tabMySub.setSelected(false);
            tabMySub_bar.setSelected(false);

        }
        if (view.getId() == R.id.tab_my_sub) {
            isRedboatChecked = false;
            mColumnresenter.refreshData(getArguments().<ColumnResponse.DataBean.ColumnBean>getParcelableArrayList(COLUMN_DATA));
            tabMySub.setSelected(true);
            tabMySub_bar.setSelected(true);
            tabRedSub.setSelected(false);
            tabRedSub_bar.setSelected(false);
        }
        if (view.getId() == R.id.no_subscription_more_view) {
            //判断红船号开关，如果没有开关数据，默认是关闭的
            GetInitializeResourceTask.createTask(RecommendFragment_redboat.this, TAG_INITIALIZE_RESOURCE);

//                Nav.with(v.getContext()).to("http://www.8531.cn/subscription/more");
            new AnalyticsBuilder(getContext(), "500002", "500002")
                    .setPageType("订阅首页")
                    .setEvenName("点击订阅更多")
                    .build()
                    .send();
        }

    }
}
