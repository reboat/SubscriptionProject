package com.daily.news.subscription.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.MoreFragmentAdapter;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.more.column.LocalColumnStore;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.base.page.PageItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaoyangzhen on 2018/4/16.
 */

public  class RecommendViewHolder extends PageItem {

    @BindView(R2.id.tab_red_sub)
    FrameLayout mTabRedSub;
    @BindView(R2.id.tab_my_sub)
    FrameLayout mTabMySub;
    @BindView(R2.id.more_container)
    ViewPager mMoreContainer;

    private ColumnFragment redFragment;
    private ColumnFragment myFragment;
    List<Fragment> fragmentList = new ArrayList<>();


    public RecommendViewHolder(ViewGroup parent,  FragmentActivity context, List<ColumnResponse.DataBean.ColumnBean> recommend_list) {
        super(parent, R.layout.subscription_item_recommend);
        ButterKnife.bind(this, itemView);
        redFragment = new RecommendColumnFragment();
        new ColumnPresenter(redFragment, new LocalColumnStore(recommend_list));
        myFragment = new RecommendColumnFragment();
        new ColumnPresenter(myFragment, new LocalColumnStore(recommend_list));
        fragmentList.add(redFragment);
        fragmentList.add(myFragment);

        mMoreContainer.setAdapter(new MoreFragmentAdapter(context.getSupportFragmentManager(), fragmentList));
        mMoreContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Do Nothing
            }

            @Override
            public void onPageSelected(int position) {
                onViewClicked(position == 0? mTabRedSub : mTabMySub);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Do Nothing
            }
        });
    }



    @OnClick({R2.id.tab_red_sub, R2.id.tab_my_sub})
    public void onViewClicked(View view) {
        view.setSelected(true);
        if (view.getId() == R.id.tab_red_sub) {
            mTabRedSub.setSelected(false);
            mMoreContainer.setCurrentItem(0);
        } else if (view.getId() == R.id.tab_my_sub) {
            mTabMySub.setSelected(false);
            mMoreContainer.setCurrentItem(1);
        }
    }

}