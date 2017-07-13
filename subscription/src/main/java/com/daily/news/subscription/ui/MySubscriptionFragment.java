package com.daily.news.subscription.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.ui.adapter.HeaderAdapter;
import com.daily.news.subscription.ui.adapter.MySubAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySubscriptionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R2.id.my_sub_recyclerView)
    RecyclerView mRecyclerView;
    private View mRootView;

    private HeaderAdapter mHeaderAdapter;

    public MySubscriptionFragment() {
    }

    public static MySubscriptionFragment newInstance(String param1, String param2) {
        MySubscriptionFragment fragment = new MySubscriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_my_subscription, container, false);
        ButterKnife.bind(this, mRootView);

        mHeaderAdapter = new HeaderAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHeaderAdapter.setInternalAdapter(new MySubAdapter(getActivity()));
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        View headerView = inflater.inflate(R.layout.my_subscription_header, container, false);
        mHeaderAdapter.addHeaderView(headerView);

        headerView.findViewById(R.id.my_sub_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        headerView.findViewById(R.id.my_sub_more_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRecyclerView.setAdapter(mHeaderAdapter);
        return mRootView;
    }

}
