package com.daily.news.subscription.more.category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.constants.Constants;
import com.daily.news.subscription.more.column.ColumnAdapter;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.more.column.LocalColumnStore;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.daily.news.analytics.Analytics;

/**
 * Created by lixinke on 2017/10/20.
 */

public class CategoryColumnFragment extends ColumnFragment {
    private static final int REQUEST_CODE_TO_DETAIL = 1110;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_TO_DETAIL) {
            long id = data.getLongExtra(Constants.Name.ID, 0);
            boolean subscribe = data.getBooleanExtra(Constants.Name.SUBSCRIBE, false);
            for (int i = 0, size = getItemCount(); i < size; i++) {
                if (id == getItem(i).id) {
                    getItem(i).subscribed = subscribe;
                    getColumnAdapter().notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        super.subscribeSuc(bean);
        if (bean.subscribed) {
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new Analytics.AnalyticsBuilder(getContext(), "A0014", "A0014","subColumn", false)
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setPageType("订阅更多页面")
                    .setEvenName("“订阅”栏目")
                    .setOtherInfo(otherInfoStr)
                    .pageType("订阅更多页面")
                    .columnID(String.valueOf(bean.id))
                    .columnName(bean.name)
                    .operationType("订阅")
                    .build()
                    .send();
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Nav.with(this).to(Uri.parse("http://www.8531.cn/subscription/detail")
                .buildUpon()
                .appendQueryParameter("id", String.valueOf(getItem(position).id))
                .build()
                .toString(),REQUEST_CODE_TO_DETAIL);

        ColumnResponse.DataBean.ColumnBean bean = getItem(position);
        if (bean != null) {
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new Analytics.AnalyticsBuilder(getContext(), "500003", "500003", "toDetailColumn", false)
                    .setEvenName("点击栏目条目（头像+标题）")
                    .setPageType("订阅更多页面")
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setOtherInfo(otherInfoStr)
                    .pageType("订阅更多页面")
                    .columnID(String.valueOf(bean.id))
                    .columnName(bean.name)
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
            new Analytics.AnalyticsBuilder(getContext(), "A0114", "A0114","subColumn", false)
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setEvenName("“取消订阅”栏目")
                    .setPageType("订阅更多页面")
                    .setOtherInfo(otherInfoStr)
                    .pageType("订阅更多页面")
                    .columnID(String.valueOf(bean.id))
                    .columnName(bean.name)
                    .operationType("取消订阅")
                    .build()
                    .send();
        }
        //说明:点击时父类会取反,作为参数传给服务端，所以要放在super前
        super.onSubscribe(bean);
    }
}
