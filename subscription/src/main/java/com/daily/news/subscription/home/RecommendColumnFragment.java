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

        if (bean.subscribed) {
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("relatedColumn", String.valueOf(bean.id));
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new AnalyticsBuilder(getContext(), "A0014", "A0014")
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setObjectType(ObjectType.NewsType)
                    .setPageType("订阅首页")
                    .setEvenName("点击订阅栏目，订阅成功")
                    .setOtherInfo(otherInfoStr)
                    .build()
                    .send();
        }
    }

    @Override
    public void onSubscribe(ColumnResponse.DataBean.ColumnBean bean) {
        if (bean.subscribed) {
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("relatedColumn", String.valueOf(bean.id));
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new AnalyticsBuilder(getContext(), "A0114", "A0114")
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setEvenName("点击“取消订阅”栏目")
                    .setPageType("订阅首页")
                    .setObjectType(ObjectType.NewsType)
                    .setOtherInfo(otherInfoStr)
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
            otherInfo.put("relatedColumn", String.valueOf(bean.id));
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new AnalyticsBuilder(getContext(), "500003", "500003")
                    .setEvenName("点击推荐栏目列表（头像+标题）")
                    .setObjectType(ObjectType.NewsType)
                    .setPageType("订阅首页")
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setOtherInfo(otherInfoStr)
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
