package com.daily.news.subscription.more.category;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.more.column.ColumnAdapter;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.more.column.LocalColumnStore;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.daily.news.analytics.Analytics;

/**
 * Created by lixinke on 2017/10/20.
 */

public class CategoryColumnFragment extends ColumnFragment {
    public CategoryColumnFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        List<ColumnResponse.DataBean.ColumnBean> columnBeen = getArguments().getParcelableArrayList("columns");
        new ColumnPresenter(this, new LocalColumnStore(columnBeen));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected ColumnAdapter createColumnAdapter(List<ColumnResponse.DataBean.ColumnBean> columns) {
        return new CategoryColumnAdapter(columns);
    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        super.subscribeSuc(bean);
        if (bean.subscribed) {
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("relatedColumn", String.valueOf(bean.id));
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new Analytics.AnalyticsBuilder(getContext(), "A0014", "A0014")
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setObjectType(ObjectType.NewsType)
                    .setPageType("订阅更多页面")
                    .setEvenName("“订阅”栏目")
                    .setOtherInfo(otherInfoStr)
                    .build()
                    .send();
        }
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
            new Analytics.AnalyticsBuilder(getContext(), "500003", "500003")
                    .setEvenName("点击推荐栏目列表（头像+标题）")
                    .setObjectType(ObjectType.NewsType)
                    .setPageType("订阅更多页面")
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
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
            new Analytics.AnalyticsBuilder(getContext(), "A0114", "A0114")
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setEvenName("“取消订阅”栏目")
                    .setPageType("订阅首页")
                    .setObjectType(ObjectType.NewsType)
                    .setOtherInfo(otherInfoStr)
                    .build()
                    .send();
        }
        //说明:点击时父类会取反,作为参数传给服务端，所以要放在super前
        super.onSubscribe(bean);
    }
}
