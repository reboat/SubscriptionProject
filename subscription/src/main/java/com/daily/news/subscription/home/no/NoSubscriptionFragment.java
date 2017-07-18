package com.daily.news.subscription.home.no;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.base.HeaderAdapter;
import com.daily.news.subscription.base.OnItemClickListener;
import com.daily.news.subscription.more.column.Column;
import com.daily.news.subscription.home.Focus;
import com.daily.news.subscription.home.SubscriptionResponse;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoSubscriptionFragment extends Fragment implements NoSubscriptionAdapter.OnSubscribeListener, OnItemClickListener<Column> {

    private static final String SUBSCRIPTION_DATA = "subscription_data";

    @BindView(R2.id.recommend_recyclerView)
    RecyclerView mRecyclerView;

    private Banner mFocusView;
    private List<Focus> mFocusBeen;

    private List<Column> mRecommendBeen;
    private NoSubscriptionAdapter mRecommendAdapter;

    private HeaderAdapter mAdapter;


    public static NoSubscriptionFragment newInstance(SubscriptionResponse.DataBean dataBean) {
        NoSubscriptionFragment fragment = new NoSubscriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(SUBSCRIPTION_DATA, dataBean);
        fragment.setArguments(args);
        return fragment;
    }

    public NoSubscriptionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            SubscriptionResponse.DataBean dataBean = getArguments().getParcelable(SUBSCRIPTION_DATA);
            mRecommendBeen = dataBean.recommend_list;
            mFocusBeen = dataBean.focus_list;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this, root);

        mAdapter = new HeaderAdapter();


        initFocusView(inflater, container);
        initMoreHeader(inflater, container);
        initRecommend();

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }

    /**
     * 初始化推荐相关
     */
    private void initRecommend() {
        mRecommendAdapter = new NoSubscriptionAdapter(getActivity(), mRecommendBeen);
        mRecommendAdapter.setOnSubscribeListener(this);
        mRecommendAdapter.setOnItemClickListener(this);

        mAdapter.setInternalAdapter(mRecommendAdapter);
    }


    /**
     * 初始化顶部图库
     *
     * @param inflater
     * @param container
     */
    private void initFocusView(LayoutInflater inflater, ViewGroup container) {
        mFocusView = (Banner) inflater.inflate(R.layout.item_focus, container, false);
        mAdapter.addHeaderView(mFocusView);
        mFocusView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mFocusView.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(((Focus) path).pic_url).into(imageView);
            }
        });
        mFocusView.isAutoPlay(true);
        mFocusView.setIndicatorGravity(BannerConfig.RIGHT);
        mFocusView.setImages(mFocusBeen);
        List<String> title = new ArrayList<>();
        for (int i = 0; i < mFocusBeen.size(); i++) {
            title.add(mFocusBeen.get(i).doc_title);
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
    private void initMoreHeader(LayoutInflater inflater, ViewGroup container) {
        View moreHeaderView = inflater.inflate(R.layout.header_more, container, false);
        moreHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.intent.action.DAILY");
                intent.setData(Uri.parse("http://www.8531.cn/subscription/more"));
                startActivity(intent);
            }
        });
        mAdapter.addHeaderView(moreHeaderView);
    }

    /**
     * 点击订阅按钮
     *
     * @param recommend
     */
    @Override
    public void onSubscribe(Column recommend) {
        Toast.makeText(getActivity(), recommend.pic_url, Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击item
     *
     * @param position  点击item的位置
     * @param recommend
     */
    @Override
    public void onItemClick(int position, Column recommend) {
        Intent intent=new Intent("android.intent.action.DAILY");
        intent.setData(Uri.parse("http://www.8531.cn/subscription/detail").buildUpon().appendQueryParameter("uid",recommend.uid).build());
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mFocusView != null) {
            mFocusView.stopAutoPlay();
        }
    }
}
