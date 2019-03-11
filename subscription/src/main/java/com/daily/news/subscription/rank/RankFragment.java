package com.daily.news.subscription.rank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.column.ColumnAdapter;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.ui.divider.ListSpaceDivider;
import com.zjrb.daily.db.bean.ChannelBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.DailyFragment;

public class RankFragment extends DailyFragment implements OnItemClickListener {


    @BindView(R2.id.recycler)
    RecyclerView mRecycler;

    RankAdapter mAdapter;

    List<RankResponse> rankResponses = new ArrayList<>();

    public static Fragment fragment(ChannelBean channel) {
        RankFragment fragment = new RankFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subscription_fragment_rank, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initRecycleView();
    }

    private void initRecycleView() {
        rankResponses.add(new RankResponse());
        rankResponses.add(new RankResponse());
        rankResponses.add(new RankResponse());
        rankResponses.add(new RankResponse());
        rankResponses.add(new RankResponse());
        rankResponses.add(new RankResponse());
        rankResponses.add(new RankResponse());
        rankResponses.add(new RankResponse());
        rankResponses.add(new RankResponse());
        rankResponses.add(new RankResponse());

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.addItemDecoration(new ListSpaceDivider(0.5d, R.color
                ._dddddd_343434, true));
        mAdapter = new RankAdapter(rankResponses);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemClick(View itemView, int position) {

    }
}
