package com.daily.news.subscription.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.MoreFragmentAdapter;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaoyangzhen on 2018/4/16.
 */

public class RecommendAdapter extends BaseRecyclerAdapter<ColumnResponse.DataBean.ColumnBean> {
    private List<ColumnResponse.DataBean.ColumnBean> mColumnBeen;
    private OnSubscribeListener mOnSubscribeListener;
    private FragmentActivity context;

    public RecommendAdapter(FragmentActivity context, List<ColumnResponse.DataBean.ColumnBean> data) {
        super(data);
        mColumnBeen = data;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        super.getItemCount();
        return super.getItemCount();
    }

    public void updateValues(List<ColumnResponse.DataBean.ColumnBean> columnsBeens) {
        mColumnBeen.clear();
        mColumnBeen.addAll(columnsBeens);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_item_recommend, parent, false);
//        return new RecommendViewHolder(itemView, mOnSubscribeListener, context);
        return null;
    }

    public void setOnSubscribeListener(OnSubscribeListener onSubscribeListener) {
        mOnSubscribeListener = onSubscribeListener;
    }

    /**
     * 点击和订阅回调
     */
    public interface OnSubscribeListener {
        void onSubscribe(ColumnResponse.DataBean.ColumnBean bean);
    }


    protected static class RecommendViewHolder extends BaseRecyclerViewHolder<ColumnResponse.DataBean.ColumnBean>{

        @BindView(R2.id.tab_red_sub)
        FrameLayout mTabRedSub;
        @BindView(R2.id.tab_my_sub)
        FrameLayout mTabMySub;
        @BindView(R2.id.more_container)
        ViewPager mMoreContainer;

        private ColumnFragment redFragment;
        private ColumnFragment myFragment;
        List<Fragment> fragmentList = new ArrayList<>();

        private OnSubscribeListener mOnSubscribeListener;

        public RecommendViewHolder(View itemView, OnSubscribeListener onSubscribeListener, FragmentActivity context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnSubscribeListener = onSubscribeListener;
            redFragment = new ColumnFragment();
            myFragment = new ColumnFragment();
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

        @Override
        public void bindView() {

        }
    }

}
