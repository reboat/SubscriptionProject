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
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.ui.divider.ListSpaceDivider;
import com.zjrb.daily.db.bean.ChannelBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.daily.news.biz.core.DailyFragment;
import cn.daily.news.biz.core.nav.Nav;

public class RankFragment extends DailyFragment implements OnItemClickListener,ClassifyDialogFragment.OnClassifySelectListener {

    public static final String SHOWTOPVIEW = "showTopView";

    @BindView(R2.id.recycler)
    RecyclerView mRecycler;

    RankAdapter mAdapter;

    List<RankResponse> rankResponses = new ArrayList<>();
    @BindView(R2.id.tab_week_bar)
    FrameLayout tabWeekBar;
    @BindView(R2.id.tab_month_bar)
    FrameLayout tabMonthBar;
    @BindView(R2.id.classify)
    TextView classify;
    @BindView(R2.id.all_rank)
    TextView allRank;
    @BindView(R2.id.top_view)
    RelativeLayout topView;

    String classifName = "总榜";

    public static Fragment instance(ChannelBean channel, boolean showTopView) {
        RankFragment fragment = new RankFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOWTOPVIEW, showTopView);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subscription_fragment_rank, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        boolean showTopView = getArguments().getBoolean(SHOWTOPVIEW);
        topView.setVisibility(showTopView ? View.VISIBLE : View.GONE);
        tabWeekBar.setSelected(true);

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

    @OnClick({R2.id.tab_week_bar, R2.id.tab_month_bar, R2.id.classify, R2.id.all_rank})
    public void onViewClicked(View view) {
            int id = view.getId();
            if(id == R.id.tab_week_bar){
                tabWeekBar.setSelected(true);
                tabMonthBar.setSelected(false);
            }else if(id == R.id.tab_month_bar){
                tabWeekBar.setSelected(false);
                tabMonthBar.setSelected(true);
            }
            else if(id == R.id.classify){
                ClassifyDialogFragment fragment = ClassifyDialogFragment.instance(classifName);
                fragment.setClassifySelectListener(this);
                fragment.show(getFragmentManager(), "RankFragment");

            }
            else if(id == R.id.all_rank){
                Nav.with(this).toPath("/subscription/rank");

            }

    }

    /**
     * 类型选择回调
     * @param id
     */
    @Override
    public void select(int id, String classifName) {
        this.classifName = classifName;
        classify.setText(classifName);

    }
}
