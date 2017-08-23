package com.daily.news.subscription.home.no;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daily.news.subscription.R;
import com.daily.news.subscription.home.Focus;
import com.daily.news.subscription.home.SubscriptionResponse;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class NoSubscriptionFragment extends ColumnFragment  {

    private static final String SUBSCRIPTION_DATA = "subscription_data";

    private Banner mFocusView;
    private List<Focus> mFocusBeen;

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
            mFocusBeen = dataBean.focus_list;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=super.onCreateView(inflater,container,savedInstanceState);
        initFocusView(inflater, container);
        initMoreHeader(inflater, container);

        return view;
    }




    /**
     * 初始化顶部图库
     *
     * @param inflater
     * @param container
     */
    private void initFocusView(LayoutInflater inflater, ViewGroup container) {
        mFocusView = (Banner) inflater.inflate(R.layout.item_focus, container, false);
        addHeaderView(mFocusView);
        mFocusView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mFocusView.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                RequestOptions options=new RequestOptions();
                options.centerCrop();
                options.placeholder(getResources().getDrawable(R.drawable.default_placeholder_big));
                Glide.with(context).load(((Focus)path).pic_url).apply(options).into(imageView);
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
        moreHeaderView.findViewById(R.id.no_subscription_more_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getString(R.string.daily_intent_action));
                intent.setData(Uri.parse("http://www.8531.cn/subscription/more"));
                startActivity(intent);
            }
        });
        addHeaderView(moreHeaderView);
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
