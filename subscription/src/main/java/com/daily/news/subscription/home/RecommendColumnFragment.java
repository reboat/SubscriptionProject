package com.daily.news.subscription.home;

import android.support.v4.app.Fragment;
import android.view.View;

import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnResponse;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lixinke on 2017/9/26.
 */

public class RecommendColumnFragment extends ColumnFragment {

    @BindView(R2.id.column_subscribe_success_notify)
    View mSucNotifyView;

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        if (bean.subscribed && mSucNotifyView.getVisibility() == View.GONE) {
            mSucNotifyView.setVisibility(View.VISIBLE);
        } else if (!isHasSubscribe() && mSucNotifyView.getVisibility() == View.VISIBLE) {
            mSucNotifyView.setVisibility(View.GONE);
        }
    }

    @OnClick(R2.id.column_subscribe_success_notify)
    public void refreshData() {
        mSucNotifyView.setVisibility(View.GONE);
        Fragment fragment = getParentFragment();
        if (fragment instanceof OnRefresh) {
            ((OnRefresh) fragment).onRefresh();
        }
    }

    public static interface OnRefresh {
        void onRefresh();
    }
}
