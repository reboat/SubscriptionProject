package com.daily.news.subscription.more.category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.constants.Constants;
import com.daily.news.subscription.more.column.ColumnAdapter;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.more.column.LocalColumnStore;
import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.FooterLoadMore;
import com.zjrb.core.recycleView.LoadMore;

import java.util.ArrayList;
import java.util.List;

import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.network.compatible.APIGetTask;
import cn.daily.news.biz.core.network.compatible.LoadViewHolder;

/**
 * Created by lixinke on 2017/10/20.
 */

public class CategoryColumnFragment extends ColumnFragment implements LoadMoreListener<ColumnResponse.DataBean> {
    private static final int REQUEST_CODE_TO_DETAIL = 1110;

    private FooterLoadMore<ColumnResponse.DataBean> mLoadMore;
    List<ColumnResponse.DataBean.ColumnBean> columnBeen;
    String className;
    boolean has_more;

    public CategoryColumnFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        className = getArguments().getString("className");
        new ColumnPresenter(this, new LocalColumnStore(columnBeen, className, -1, has_more, mType));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadMore = new FooterLoadMore<>(mRecyclerView, this);
        mColumnAdapter.addFooterView(mLoadMore.getItemView());
    }

    @Override
    protected float getDividerRightMargin() {
        return 15;
    }

    @Override
    protected float getDividerLeftMargin() {
        return 0;
    }

    @Override
    public LoadViewHolder getProgressBar() {
        return new LoadViewHolder(mRecyclerView, (ViewGroup) mRecyclerView.getParent());
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
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Nav.with(this).toPath(new Uri.Builder().path("/subscription/detail")
                .appendQueryParameter("id", String.valueOf(getItem(position).id))
                .build()
                .toString(), REQUEST_CODE_TO_DETAIL);

        ColumnResponse.DataBean.ColumnBean bean = getItem(position);
        if (bean != null) {
            new Analytics.AnalyticsBuilder(getContext(), "500003", "ToDetailColumn", false)
                    .name("点击订阅号条目")
                    .pageType("订阅号分类检索页面")
                    .columnID(String.valueOf(bean.id))
                    .seObjectType(ObjectType.C90)
                    .columnName(bean.name)
                    .build()
                    .send();
        }
    }

    @Override
    public void onSubscribe(ColumnResponse.DataBean.ColumnBean bean) {
        new Analytics.AnalyticsBuilder(getContext(), bean.subscribed ? "A0114" : "A0014", "SubColumn", false)
                .name(bean.subscribed ? "订阅号取消订阅" : "订阅号订阅")
                .pageType("订阅号分类检索页面")
                .columnID(String.valueOf(bean.id))
                .seObjectType(ObjectType.C90)
                .columnName(bean.name)
                .operationType(bean.subscribed ? "取消订阅" : "订阅")
                .build()
                .send();
        //说明:点击时父类会取反,作为参数传给服务端，所以要放在super前
        super.onSubscribe(bean);
    }

    @Override
    public void updateValue(ColumnResponse.DataBean dataBean) {
        if (columnBeen == null) {
            columnBeen = new ArrayList<>();
        }
        if (dataBean != null && dataBean.elements != null) {
            columnBeen.addAll(dataBean.elements);
        }

        tvTips.setText(mType == 1 ? "暂无起航号上线\n敬请期待" : "暂无订阅号上线\n敬请期待");

        super.updateValue(dataBean);
        if (dataBean.elements == null || dataBean.elements.size() == 0) {
            mLoadMore.setState(LoadMore.TYPE_IDLE);
        } else if (!dataBean.has_more) {
            mLoadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    @Override
    public void onLoadMoreSuccess(ColumnResponse.DataBean data, LoadMore loadMore) {
        columnBeen.addAll(data.elements);
        mColumnAdapter.addData(data.elements, true);
        if (data.elements == null || data.elements.size() == 0) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        } else if (feedbackDataListener != null) {
            feedbackDataListener.feedback(data);
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<ColumnResponse.DataBean> callback) {
        if (columnBeen != null && columnBeen.size() > 0) {
            new APIGetTask<ColumnResponse.DataBean>(callback) {
                @Override
                public void onSetupParams(Object... params) {
                    put("class_name", className);
                    put("start", columnBeen.get(columnBeen.size() - 1).id);
                    put("type", mType);
                }

                @Override
                public String getApi() {
                    return "/api/subscription/column_list";
                }
            }.exe(mType);
        } else {
            mLoadMore.setState(LoadMore.TYPE_IDLE);
        }

    }
}
