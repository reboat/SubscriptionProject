package com.daily.news.subscription.rank;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.zjrb.core.ui.widget.CompatViewPager;
import com.zjrb.daily.db.bean.ChannelBean;
import com.zjrb.daily.news.ui.widget.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.DailyActivity;

public class RankActivtiy extends DailyActivity {

    @BindView(R2.id.rank_back)
    ImageView rankBack;
    @BindView(R2.id.tab_week_bar)
    FrameLayout tabWeekBar;
    @BindView(R2.id.tab_month_bar)
    FrameLayout tabMonthBar;
    @BindView(R2.id.top_time)
    TextView topTime;
    @BindView(R2.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R2.id.view_pager)
    CompatViewPager viewPager;

    private RankPagerAdapter mPagerAdapter;
    private OnPageChangeListener mOnPageChangeListener;
    private List<ChannelBean> channels = new ArrayList<>();
    int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity_rank);
        ButterKnife.bind(this);
        tabWeekBar.setSelected(true);
        mOnPageChangeListener = new OnPageChangeListener();
        initChannels();


    }

    private void initChannels() {
        channels.clear();
        channels.add(new ChannelBean((long) 1, "总榜", true, true, "normal", "", 1));
        channels.add(new ChannelBean((long) 2, "政务榜", true, true, "normal", "", 2));
        channels.add(new ChannelBean((long) 3, "党团榜", true, true, "normal", "", 3));
        channels.add(new ChannelBean((long) 4, "生活榜", true, true, "normal", "", 4));

        initViewPager(channels);
    }

    private void initViewPager(List<ChannelBean> channels) {
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        try {
            if (channels != null && !channels.isEmpty()) {
                mPagerAdapter = new RankPagerAdapter(getSupportFragmentManager(), channels);
                viewPager.setAdapter(mPagerAdapter);
                tabLayout.setViewPager(viewPager);
                if (mPosition < mPagerAdapter.getCount()) {
                    viewPager.setCurrentItem(mPosition);
                    mOnPageChangeListener.onPageSelected(mPosition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    ChannelBean data;

    class OnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            data = mPagerAdapter.getData(position);
            if (data == null) return;

            String lastID = "";
            String lastName = "";
            if (mPosition >= 0 && mPosition != position && mPosition < mPagerAdapter.getCount()) {
                ChannelBean oldData = mPagerAdapter.getData(mPosition);
                lastID = oldData.getNav_parameter();
                lastName = oldData.getName();
            }



            mPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }

    }

    @OnClick({R2.id.rank_back, R2.id.tab_week_bar, R2.id.tab_month_bar})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.rank_back) {
            finish();
        } else if (id == R.id.tab_week_bar) {
            tabWeekBar.setSelected(true);
            tabMonthBar.setSelected(false);

        } else if (id == R.id.tab_month_bar) {
            tabWeekBar.setSelected(false);
            tabMonthBar.setSelected(true);
        }
    }
}
