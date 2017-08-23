package com.daily.news.subscription.home;

import android.content.Context;
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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daily.news.subscription.HeaderAdapter;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.article.ArticleAdapter;
import com.daily.news.subscription.more.column.ColumnAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zjrb.coreprojectlibrary.ui.holder.HeaderRefreshHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SubscriptionFragment extends Fragment implements SubscriptionContract.View {

    @BindView(R2.id.subscription_recyclerView)
    RecyclerView mRecyclerView;
    HeaderAdapter mHeaderAdapter;

    SubscriptionContract.Presenter mPresenter;
    private HeaderRefreshHolder mRefreshView;

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
        ButterKnife.bind(this, rootView);
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
        return rootView;
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
    public void onRefreshComplete(SubscriptionResponse subscriptionResponse) {
        mRefreshView.setRefreshing(false);
        mRecyclerView.setAdapter(null);
        mRecyclerView.setAdapter(mHeaderAdapter);
        updateValue(subscriptionResponse);
    }

    @Override
    public void onRefreshError(String message) {
        mRefreshView.setRefreshing(false);
    }

    @Override
    public void updateValue(SubscriptionResponse subscriptionBean) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (subscriptionBean.data.has_subscribe) {
            HeaderAdapter myAdapter = new HeaderAdapter();
            initMySubscriptionHeader(inflater, (ViewGroup) getView(), myAdapter);
            myAdapter.setInternalAdapter(new ArticleAdapter(subscriptionBean.data.article_list));
            mHeaderAdapter.setInternalAdapter(myAdapter);
            mHeaderAdapter.notifyDataSetChanged();
        } else if (!subscriptionBean.data.has_subscribe) {
            HeaderAdapter recommendAdapter = new HeaderAdapter();
            initFocusView(inflater, (ViewGroup) getView(), recommendAdapter, subscriptionBean.data.focus_list);
            initMoreHeader(inflater, (ViewGroup) getView(), recommendAdapter);
            recommendAdapter.setInternalAdapter(new ColumnAdapter(subscriptionBean.data.recommend_list));
            mHeaderAdapter.setInternalAdapter(recommendAdapter);
            mHeaderAdapter.notifyDataSetChanged();
        }
    }

    private void initMySubscriptionHeader(LayoutInflater inflater, ViewGroup container, HeaderAdapter myAdapter) {
        View headerView = inflater.inflate(R.layout.header_my_subscription, container, false);
        myAdapter.addHeaderView(headerView);

        headerView.findViewById(R.id.my_sub_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getString(R.string.daily_intent_action));
                intent.setData(Uri.parse("http://www.8531.cn/subscription/more/my/column"));
                startActivity(intent);
            }
        });

        headerView.findViewById(R.id.my_sub_more_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getString(R.string.daily_intent_action));
                intent.setData(Uri.parse("http://www.8531.cn/subscription/more"));
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化顶部图库
     *
     * @param inflater
     * @param container
     */
    private void initFocusView(LayoutInflater inflater, ViewGroup container, HeaderAdapter headerAdapter, List<Focus> focuses) {
        Banner mFocusView = (Banner) inflater.inflate(R.layout.item_focus, container, false);
        headerAdapter.addHeaderView(mFocusView);
        mFocusView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mFocusView.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.placeholder(getResources().getDrawable(R.drawable.default_placeholder_big));
                Glide.with(context).load(((Focus) path).pic_url).apply(options).into(imageView);
            }

        });
        mFocusView.isAutoPlay(true);
        mFocusView.setIndicatorGravity(BannerConfig.RIGHT);
        mFocusView.setImages(focuses);
        List<String> title = new ArrayList<>();
        for (int i = 0; i < focuses.size(); i++) {
            title.add(focuses.get(i).doc_title);
        }
        mFocusView.setBannerTitles(title);
        mFocusView.start();
    }

    /**
     * 初始化"大家都在看"栏目
     *
     * @param inflater
     * @param container
     */
    private void initMoreHeader(LayoutInflater inflater, ViewGroup container, HeaderAdapter headerAdapter) {
        View moreHeaderView = inflater.inflate(R.layout.header_more, container, false);
        moreHeaderView.findViewById(R.id.no_subscription_more_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getString(R.string.daily_intent_action));
                intent.setData(Uri.parse("http://www.8531.cn/subscription/more"));
                startActivity(intent);
            }
        });
        headerAdapter.addHeaderView(moreHeaderView);
    }

    @Override
    public void showError(String message) {
        mRefreshView.setRefreshing(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
