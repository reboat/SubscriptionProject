package com.daily.news.subscription.home;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.more.column.LocalColumnStore;
import com.daily.news.subscription.task.GetInitializeResourceTask;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.core.utils.StringUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.ad.AdList;
import com.zjrb.daily.ad.listener.AdListDataListener;
import com.zjrb.daily.ad.model.AdIndexModel;
import com.zjrb.daily.ad.model.AdModel;
import com.zjrb.daily.ad.model.AdType;
import com.zjrb.daily.news.bean.AdBean;
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
public class RecommendFragment extends Fragment implements SubscriptionContract.View,
        HeaderRefresh.OnRefreshListener, RecommendColumnFragment.OnRefresh {

    private static final String TAG_INITIALIZE_RESOURCE = "initialize_resource";

    private static final String COLUMN_DATA = "column_data";
    private static final String FOCUS_DATA = "focus_data";
    @BindView(R2.id.no_subscription_more_view)
    LinearLayout noSubscriptionMoreView;
    @BindView(R2.id.header_rel)
    RelativeLayout headerRel;
    private SubscriptionContract.Presenter mPresenter;
    private ColumnFragment_home mColumnFragment;
    private View mHeaderBanner;
    private int firstItemPosition = 0;

    private AdBean adBean;
    List<SubscriptionResponse.Focus> local_focuses;

    public RecommendFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_recommend, container, false);
        ButterKnife.bind(this, rootView);
        mColumnFragment = (ColumnFragment_home) getChildFragmentManager().findFragmentById(R.id.column_fragment);
        new ColumnPresenter(mColumnFragment, new LocalColumnStore(getArguments().<ColumnResponse.DataBean.ColumnBean>getParcelableArrayList(COLUMN_DATA)));
        mColumnFragment.setRefreshListener(this);
        setScrollListener();

        mHeaderBanner = setupBannerView(getArguments().<SubscriptionResponse.Focus>getParcelableArrayList(FOCUS_DATA));
        if (mHeaderBanner != null) {
            mColumnFragment.addHeaderView(mHeaderBanner);
        }
        mColumnFragment.addHeaderView(setupMoreSubscriptionView(inflater, container));
        return rootView;
    }

    private View setupBannerView(final List<SubscriptionResponse.Focus> focuses) {

        local_focuses = focuses;
        if (local_focuses == null) {
            local_focuses = new ArrayList<>();
        }
        if (adBean != null && adBean.getFocus() != null) {
            local_focuses = getFocusList(local_focuses, adBean.getFocus());
        }


        if (local_focuses == null || local_focuses.size() == 0) {
            return null;
        }
        List<FocusBean> focusBeans = new ArrayList<>();
        for (int i = 0; i < local_focuses.size(); i++) {
            FocusBean bean = new FocusBean();
            SubscriptionResponse.Focus focus = local_focuses.get(i);
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
//                super.onItemClick(item, position);
                if (ClickTracker.isDoubleClick()) return;

                SubscriptionResponse.Focus focus = local_focuses.get(position);
                if (!TextUtils.isEmpty(focus.doc_url)) {
                    Nav.with(item.getContext()).to(focus.doc_url);
                }

                new AnalyticsBuilder(getContext(), "200005", "200005", "AppContentClick", false)
                        .setClassifyID(String.valueOf(focus.channel_article_id))
                        .setPageType("订阅首页")
                        .setEvenName("焦点图点击")
                        .setObjectName(focus.doc_title)
                        .setObjectType(ObjectType.NewsType)
                        .selfNewsID(String.valueOf(focus.channel_article_id))
                        .newsTitle(focus.doc_title)
                        .pageType("订阅首页")
                        .objectType("焦点图")
                        .pubUrl(focus.doc_url)
                        .build()
                        .send();
            }
        };
        bannerHolder.setData(focusBeans);
        return bannerHolder.getItemView();
    }

    private List<SubscriptionResponse.Focus> getFocusList(List<SubscriptionResponse.Focus> focus_list, List<AdIndexModel> focus) {
        final List<SubscriptionResponse.Focus> resultList = focus_list;
        AdList.create(getActivity())
                .setSlots(focus)
                .setType(AdType.LOOP)
                .setAdListDataListener(new AdListDataListener() {
                    @Override
                    public void updateListData(List<AdModel> adList, List<String> adFails) {
                        if (adList != null && adList.size() != 0) {
                            for (int i = 0; i < adList.size(); i++) {
                                AdModel adModel = adList.get(i);
                                SubscriptionResponse.Focus focusBean = SubscriptionResponse.Focus.switchToAdModal(adModel);
                                if (adModel.getPosition() < resultList.size()) {
                                    resultList.add(adModel.getPosition(), focusBean);
                                } else {
                                    resultList.add(focusBean);
                                }
                            }
                        }
                    }
                })
                .build();

        return resultList;
    }

    private View setupMoreSubscriptionView(LayoutInflater inflater, ViewGroup container) {
        View moreHeaderView = inflater.inflate(R.layout.subscription_header_more, container, false);
        moreHeaderView.findViewById(R.id.no_subscription_more_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断红船号开关，如果没有开关数据，默认是关闭的
                GetInitializeResourceTask.createTask(RecommendFragment.this, TAG_INITIALIZE_RESOURCE);

//                Nav.with(v.getContext()).to("http://www.8531.cn/subscription/more");
                new AnalyticsBuilder(getContext(), "500002", "500002", "AppTabClick", false)
                        .setPageType("订阅首页")
                        .setEvenName("点击订阅更多")
                        .pageType("订阅首页")
                        .clickTabName("订阅更多")
                        .build()
                        .send();
            }
        });
        return moreHeaderView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initArticles(AdBean bean) {
        adBean = bean;
    }

    public static Fragment newInstance(List<SubscriptionResponse.Focus> focus_list, List<ColumnResponse.DataBean.ColumnBean> recommend_list, AdBean adBean) {
        RecommendFragment fragment = new RecommendFragment();
        new SubscriptionPresenter(fragment, new SubscriptionStore());
        if (focus_list != null && focus_list.size() > 0) {
            focus_list.removeAll(Collections.singleton(null));
        }
        if (recommend_list != null && recommend_list.size() > 0) {
            recommend_list.removeAll(Collections.singleton(null));
        }

        Bundle args = new Bundle();
        args.putParcelableArrayList(RecommendFragment.COLUMN_DATA, (ArrayList<? extends Parcelable>) recommend_list);
        args.putParcelableArrayList(RecommendFragment.FOCUS_DATA, (ArrayList<? extends Parcelable>) focus_list);
        fragment.initArticles(adBean);
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
            Fragment fragment = MySubscribedFragment.newInstance(dataBean.article_list, dataBean.adv_places);
            fragmentManager.beginTransaction().replace(R.id.subscription_container, fragment).commitAllowingStateLoss();
        } else if (!dataBean.has_subscribe && fragmentManager != null) {
            if (dataBean.hch_switch && !StringUtils.isEmpty(dataBean.hch_name)) {
                Fragment fragment = RecommendFragment_Redboat.newInstance(dataBean.focus_list, dataBean.recommend_list, dataBean.redboat_recommend_list, true, dataBean.hch_name, dataBean.adv_places);
                fragmentManager.beginTransaction().replace(R.id.subscription_container, fragment).commitAllowingStateLoss();
            } else {
                Fragment fragment = RecommendFragment.newInstance(dataBean.focus_list, dataBean.recommend_list, dataBean.adv_places);
                fragmentManager.beginTransaction().replace(R.id.subscription_container, fragment).commitAllowingStateLoss();
            }
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
                    if (firstItemPosition >= 2) {
                        headerRel.setVisibility(View.VISIBLE);
                    } else {
                        headerRel.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R2.id.no_subscription_more_view)
    public void onViewClicked() {
        //判断红船号开关，如果没有开关数据，默认是关闭的
        GetInitializeResourceTask.createTask(RecommendFragment.this, TAG_INITIALIZE_RESOURCE);

//                Nav.with(v.getContext()).to("http://www.8531.cn/subscription/more");
        new AnalyticsBuilder(getContext(), "500002", "500002", "AppTabClick", false)
                .setPageType("订阅首页")
                .setEvenName("点击订阅更多")
                .pageType("订阅首页")
                .clickTabName("订阅更多")
                .build()
                .send();
    }
}
