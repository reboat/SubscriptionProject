package com.daily.news.subscription.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daily.news.subscription.OnItemClickListener;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.model.SubscriptionBean;
import com.daily.news.subscription.ui.adapter.HeaderRecommendAdapter;
import com.daily.news.subscription.ui.adapter.RecommendAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendFragment extends Fragment implements RecommendAdapter.OnSubscribeListener, OnItemClickListener<SubscriptionBean.DataBean.RecommendBean> {

    private static final String SUBSCRIPTION_DATA = "subscription_data";

    @BindView(R2.id.recommend_progress_container)
    View mProgressContainer;
    @BindView(R2.id.recommend_recyclerView)
    RecyclerView mRecyclerView;

    private View mFocusView;
    private Banner mFocusBanner;
    private List<SubscriptionBean.DataBean.FocusBean> mFocusBeen;

    private List<SubscriptionBean.DataBean.RecommendBean> mRecommends;
    private RecommendAdapter mRecommendAdapter;

    private HeaderRecommendAdapter mHeaderRecommendAdapter;


    public static RecommendFragment newInstance(SubscriptionBean.DataBean dataBean) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle args = new Bundle();
        args.putParcelable(SUBSCRIPTION_DATA, dataBean);
        fragment.setArguments(args);
        return fragment;
    }

    public RecommendFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            SubscriptionBean.DataBean dataBean = getArguments().getParcelable(SUBSCRIPTION_DATA);
            mRecommends = dataBean.recommend_list;
            mFocusBeen = dataBean.focus_list;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this, root);
        mProgressContainer.setVisibility(View.GONE);

        mHeaderRecommendAdapter = new HeaderRecommendAdapter();
        mRecyclerView.setAdapter(mHeaderRecommendAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initRecommend();
        initFocusView(inflater, container);
        initRecommendHeader(inflater, container);

        return root;
    }

    /**
     * 初始化推荐相关
     */
    private void initRecommend() {
        mRecommendAdapter = new RecommendAdapter(getActivity(), mRecommends);
        mRecommendAdapter.setOnSubscribeListener(this);
        mRecommendAdapter.setOnItemClickListener(this);

        mHeaderRecommendAdapter.setRecommendAdapter(mRecommendAdapter);
    }


    /**
     * 初始化顶部图库
     *
     * @param inflater
     * @param container
     */
    private void initFocusView(LayoutInflater inflater, ViewGroup container) {
        mFocusView = inflater.inflate(R.layout.item_focus, container, false);
        mFocusBanner = (Banner) mFocusView.findViewById(R.id.focus_banner);
        mFocusBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mFocusBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(((SubscriptionBean.DataBean.FocusBean) path).pic_url).into(imageView);
            }
        });
        mFocusBanner.isAutoPlay(true);
        mFocusBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mFocusBanner.setImages(mFocusBeen);
        List<String> title = new ArrayList<>();
        for (int i = 0; i < mFocusBeen.size(); i++) {
            title.add(mFocusBeen.get(i).doc_title);
        }
        mFocusBanner.setBannerTitles(title);
        mFocusBanner.start();
        mHeaderRecommendAdapter.addHeaderView(mFocusView);
    }

    /**
     * 初始化"大家都在看"栏目
     *
     * @param inflater
     * @param container
     */
    private void initRecommendHeader(LayoutInflater inflater, ViewGroup container) {
        View recommendHeaderView = inflater.inflate(R.layout.recommend_header, container, false);
        recommendHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mHeaderRecommendAdapter.addHeaderView(recommendHeaderView);
    }

    /**
     * 点击订阅按钮
     *
     * @param recommend
     */
    @Override
    public void onSubscribe(SubscriptionBean.DataBean.RecommendBean recommend) {
        Toast.makeText(getActivity(), recommend.pic_url, Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击item
     *
     * @param position  点击item的位置
     * @param recommend
     */
    @Override
    public void onItemClick(int position, SubscriptionBean.DataBean.RecommendBean recommend) {
        Toast.makeText(getActivity(), recommend.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mFocusBanner != null) {
            mFocusBanner.stopAutoPlay();
        }
    }
}
