package com.daily.news.subscription.home;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.HeaderAdapter;
import com.daily.news.subscription.OnItemClickListener;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.article.ArticleAdapter;
import com.daily.news.subscription.more.column.Column;
import com.daily.news.subscription.more.column.ColumnAdapter;
import com.idisfkj.loopview.LoopView;
import com.idisfkj.loopview.entity.LoopViewEntity;
import com.zjrb.coreprojectlibrary.nav.Nav;
import com.zjrb.coreprojectlibrary.ui.holder.HeaderRefreshHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 页面逻辑：
 * 1.有订阅时返回订阅的新闻，无订阅时返回推荐订阅栏目。
 * 2.点击订阅后页面下拉刷新，返回订阅栏目的新闻
 */
public class SubscriptionFragment extends Fragment implements SubscriptionContract.View {

    @BindView(R2.id.subscription_recyclerView)
    RecyclerView mRecyclerView;
    HeaderAdapter mHeaderAdapter;

    private SubscriptionContract.Presenter mPresenter;
    private HeaderRefreshHolder mRefreshView;
    private Unbinder mUnBinder;

    public SubscriptionFragment() {
    }

    public static SubscriptionFragment newInstance() {
        SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
        new SubscriptionPresenter(subscriptionFragment, new SubscriptionStore());
        return subscriptionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.subscribe();
    }

    @Override
    public void setPresenter(SubscriptionContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subscription_home, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);
        setupRecyclerView();
        return rootView;
    }

    /**
     * 初始化RecyclerView
     */
    private void setupRecyclerView() {
        mHeaderAdapter = new HeaderAdapter();
        mRecyclerView.setAdapter(mHeaderAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRefreshView = new HeaderRefreshHolder(mRecyclerView);
        mRefreshView.setOnRefreshListener(new HeaderRefreshHolder.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onRefresh();
            }
        });
        mHeaderAdapter.addHeaderView(mRefreshView.getView());
    }

    @Override
    public void hideProgressBar() {
        mRefreshView.setRefreshing(false);
    }

    @Override
    public void showProgressBar() {
        mRefreshView.setRefreshing(true);
    }

    @Override
    public void showError(String message) {
        mRefreshView.setRefreshing(false);
    }

    @Override
    public void onRefreshComplete(SubscriptionResponse subscriptionResponse) {
        mRefreshView.setRefreshing(false);
        //RecycleView会缓存ViewHolder，Adapter中的数据结构发生变化，但缓存的ViewHolder没有变化导致crash。重新设置Adapter清除缓存。
        mRecyclerView.setAdapter(mHeaderAdapter);
        updateValue(subscriptionResponse);
    }

    @Override
    public void onRefreshError(String message) {
        mRefreshView.setRefreshing(false);
    }

    /**
     * 网络请求返回时回调
     *
     * @param subscriptionBean
     */
    @Override
    public void updateValue(SubscriptionResponse subscriptionBean) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        HeaderAdapter contentAdapter = null;
        if (subscriptionBean.data.has_subscribe) {
            contentAdapter = createMySubscriptionAdapter(subscriptionBean, inflater);
        } else if (!subscriptionBean.data.has_subscribe) {
            contentAdapter = createRecommendAdapter(subscriptionBean, inflater);
        }

        mHeaderAdapter.setInternalAdapter(contentAdapter);
    }

    /**
     * 创建我的订阅Adapter
     *
     * @param subscriptionBean
     * @param inflater
     * @return
     */
    @NonNull
    private HeaderAdapter createMySubscriptionAdapter(SubscriptionResponse subscriptionBean, LayoutInflater inflater) {
        HeaderAdapter adapter = new HeaderAdapter();

        View headerView = setupMySubscriptionHeaderView(inflater, (ViewGroup) getView());
        adapter.addHeaderView(headerView);

        adapter.setInternalAdapter(new ArticleAdapter(subscriptionBean.data.article_list));
        return adapter;
    }

    /**
     * 无订阅时创建推荐Adapter
     *
     * @param subscriptionBean
     * @param inflater
     * @return
     */
    @NonNull
    private HeaderAdapter createRecommendAdapter(SubscriptionResponse subscriptionBean, LayoutInflater inflater) {
        HeaderAdapter adapter = new HeaderAdapter();

        View bannerView = setupBannerView(inflater, (ViewGroup) getView(), subscriptionBean.data.focus_list);
        adapter.addHeaderView(bannerView);

        View moreSubscriptionView = setupMoreSubscriptionView(inflater, (ViewGroup) getView());
        adapter.addHeaderView(moreSubscriptionView);

        ColumnAdapter columnAdapter = new ColumnAdapter(subscriptionBean.data.recommend_list);
        columnAdapter.setOnItemClickListener(new OnItemClickListener<Column>() {
            @Override
            public void onItemClick(int position, Column item) {
                Nav.with(getActivity()).to(Uri.parse("http://www.8531.cn/subscription/detail").buildUpon().appendQueryParameter("uid", item.uid).build(), 0);
            }
        });
        adapter.setInternalAdapter(columnAdapter);
        return adapter;
    }


    /**
     * 有订阅时，订阅栏目上的导航
     *
     * @param inflater
     * @param container
     * @return
     */
    @NonNull
    private View setupMySubscriptionHeaderView(LayoutInflater inflater, ViewGroup container) {
        View headerView = inflater.inflate(R.layout.header_my_subscription, container, false);

        headerView.findViewById(R.id.my_sub_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(v.getContext()).to("http://www.8531.cn/subscription/more/my/column");
            }
        });

        headerView.findViewById(R.id.my_sub_more_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(v.getContext()).to("http://www.8531.cn/subscription/more");
            }
        });
        return headerView;
    }


    /**
     * 无订阅时，推荐上的轮播图
     *
     * @param inflater
     * @param container
     * @param focuses
     * @return
     */
    @NonNull
    private View setupBannerView(LayoutInflater inflater, ViewGroup container, List<Focus> focuses) {

        final LoopView loopView = (LoopView) inflater.inflate(R.layout.item_focus, container, false);
        List<LoopViewEntity> list=new ArrayList<>();
        for(int i=0;i<focuses.size();i++){
            LoopViewEntity entity=new LoopViewEntity();
            entity.setImageUrl(focuses.get(i).pic_url);
            entity.setDescript(focuses.get(i).doc_title);
            list.add(entity);
        }
        loopView.setLoopData(list);
        return loopView;
    }

    /**
     * 无订阅页面，推荐上面的更多导航
     *
     * @param inflater
     * @param container
     * @return
     */
    @NonNull
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
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }
}
