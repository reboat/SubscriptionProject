package com.daily.news.subscription.home;

import android.support.v4.app.Fragment;
import android.view.View;

import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics.AnalyticsBuilder;

/**
 * Created by lixinke on 2017/9/26.
 */

public class RecommendColumnFragment extends ColumnFragment_home {

    @BindView(R2.id.column_subscribe_success_notify)
    View mSucNotifyView;

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        if (bean.subscribed && mSucNotifyView.getVisibility() == View.GONE) {
            mSucNotifyView.setVisibility(View.VISIBLE);
        } else if (!isHasSubscribe() && mSucNotifyView.getVisibility() == View.VISIBLE) {
            mSucNotifyView.setVisibility(View.GONE);
        }

        if (bean.subscribed) {
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new AnalyticsBuilder(getContext(), "A0014", "A0014", "subColumn", false)
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setPageType("订阅首页")
                    .setEvenName("点击\"订阅\"栏目")
                    .setOtherInfo(otherInfoStr)
                    .pageType("订阅首页")
                    .columnID(String.valueOf(bean.id))
                    .columnName(bean.name)
                    .operationType("订阅")
                    .build()
                    .send();
        }
    }

    @Override
    public void onSubscribe(ColumnResponse.DataBean.ColumnBean bean) {
        if (bean.subscribed) {
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new AnalyticsBuilder(getContext(), "A0114", "A0114","subColumn", false)
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setEvenName("点击“取消订阅”栏目")
                    .setPageType("订阅首页")
                    .setOtherInfo(otherInfoStr)
                    .pageType("订阅首页")
                    .columnID(String.valueOf(bean.id))
                    .columnName(bean.name)
                    .operationType("取消订阅")
                    .build()
                    .send();
        }
        //说明:点击时父类会取反,作为参数传给服务端，所以要放在super前
        super.onSubscribe(bean);

    }

    @Override
    public void onItemClick(View itemView, int position) {
        super.onItemClick(itemView, position);
        ColumnResponse.DataBean.ColumnBean bean = getItem(position);
        if (bean != null) {
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new AnalyticsBuilder(getContext(), "500003", "500003", "RecommendAreaClick", false)
                    .setEvenName("点击推荐栏目列表（头像+标题）")
                    .setPageType("订阅首页")
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setOtherInfo(otherInfoStr)
                    .pageType("订阅首页")
                    .recommendContentID(String.valueOf(bean.id))
                    .recommendContentName(bean.name)
                    .build()
                    .send();
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
