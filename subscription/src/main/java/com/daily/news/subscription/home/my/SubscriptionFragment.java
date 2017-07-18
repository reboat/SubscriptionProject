package com.daily.news.subscription.home.my;


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

import com.daily.news.subscription.Article;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.base.HeaderAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionFragment extends Fragment {
    private static final String ARTICLES = "articles";

    @BindView(R2.id.my_sub_recyclerView)
    RecyclerView mRecyclerView;
    private View mRootView;

    private HeaderAdapter mHeaderAdapter;
    private List<Article> mArticles;

    public SubscriptionFragment() {
    }

    public static SubscriptionFragment newInstance(ArrayList<Article> articles) {
        SubscriptionFragment fragment = new SubscriptionFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARTICLES, articles);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArticles = getArguments().getParcelableArrayList(ARTICLES);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_my_subscription, container, false);
        ButterKnife.bind(this, mRootView);

        mHeaderAdapter = new HeaderAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHeaderAdapter.setInternalAdapter(new SubscriptionAdapter(mArticles));
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        View headerView = inflater.inflate(R.layout.header_my_subscription, container, false);
        mHeaderAdapter.addHeaderView(headerView);

        headerView.findViewById(R.id.my_sub_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.DAILY");
                intent.setData(Uri.parse("http://www.8531.cn/subscription/my/subscription"));
                startActivity(intent);
            }
        });

        headerView.findViewById(R.id.my_sub_more_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.intent.action.DAILY");
                intent.setData(Uri.parse("http://www.8531.cn/subscription/more"));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mHeaderAdapter);
        return mRootView;
    }

}
