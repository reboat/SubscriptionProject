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
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.FooterLoadMore;
import com.zjrb.core.recycleView.LoadMore;
import com.zjrb.core.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.model.ResourceBiz;
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
    int type;
    int id;
    boolean has_more;

    public CategoryColumnFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        columnBeen = getArguments().getParcelableArrayList("columns");
        type = getArguments().getInt("type");
        id = getArguments().getInt("id");
        has_more = getArguments().getBoolean("has_more");
        new ColumnPresenter(this, new LocalColumnStore(columnBeen, type, id, has_more));

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
        if (bean.subscribed) {
            String eventName = "“订阅”栏目";
            String pageType = "栏目号分类检索页面";
            if(bean.red_boat_column){
                eventName = "之江号订阅";
                pageType = "之江号分类检索页面";

            }
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new Analytics.AnalyticsBuilder(getContext(), "A0014", "A0014","SubColumn", false)
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setPageType(pageType)
                    .setEvenName(eventName)
                    .setOtherInfo(otherInfoStr)
                    .pageType(pageType)
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
            String eventName = "点击栏目条目（头像+标题）";
            String pageType = "栏目号分类检索页面";
            if(bean.red_boat_column){
                eventName = "点击之江号条目（头像+标题）";
                pageType = "之江号分类检索页面";
            }
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new Analytics.AnalyticsBuilder(getContext(), "500003", "500003", "ToDetailColumn", false)
                    .setEvenName(eventName)
                    .setPageType(pageType)
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setOtherInfo(otherInfoStr)
                    .pageType(pageType)
                    .columnID(String.valueOf(bean.id))
                    .columnName(bean.name)
                    .build()
                    .send();
        }
    }

    @Override
    public void onSubscribe(ColumnResponse.DataBean.ColumnBean bean) {
        if (bean.subscribed) {
            String eventName = "“取消订阅”栏目";
            String pageType = "栏目号分类检索页面";
            if(bean.red_boat_column){
                eventName = "之江号取消订阅";
                pageType = "之江号分类检索页面";
            }
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new Analytics.AnalyticsBuilder(getContext(), "A0114", "A0114","SubColumn", false)
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setEvenName(eventName)
                    .setPageType(pageType)
                    .setOtherInfo(otherInfoStr)
                    .pageType(pageType)
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
    public void updateValue(ColumnResponse.DataBean dataBean) {
        if(columnBeen == null)
        {
            columnBeen = new ArrayList<>();
        }
        if(dataBean != null && dataBean.elements != null){
            columnBeen.addAll(dataBean.elements);
        }
        if(type == 1){
            tvTips.setText("暂无"+ getRedName() +"上线\n    敬请期待");
        }else {
            tvTips.setText("暂无栏目上线\n    敬请期待");
        }
        super.updateValue(dataBean);
        if(dataBean.elements == null || dataBean.elements.size() == 0){
            mLoadMore.setState(LoadMore.TYPE_IDLE);
        }else if(!dataBean.has_more){
            mLoadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    private String getRedName(){
        ResourceBiz biz = SPHelper.get().getObject(cn.daily.news.biz.core.constant.Constants.Key.INITIALIZATION_RESOURCES);
        if (biz != null && biz.feature_list != null) {
            for (ResourceBiz.FeatureListBean bean : biz.feature_list) {
                if (bean.name.equals("hch")) {
                    String text = bean.desc;
                    if (text != null && text != "") {

                        if(text.length() > 4)
                        {
                            text = text.substring(0, 4) + "...";
                        }
                        return text;
                    }
                    break;
                }
            }
        }
        return "栏目";
    }

    @Override
    public void onLoadMoreSuccess(ColumnResponse.DataBean data, LoadMore loadMore) {
        mColumnAdapter.addData(data.elements, true);
        if (data.elements == null || data.elements.size() == 0) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }else if(feedbackDataListener != null){
            feedbackDataListener.feedback(data);
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<ColumnResponse.DataBean> callback) {
        if(columnBeen != null && columnBeen.size() > 0) {
            new APIGetTask<ColumnResponse.DataBean>(callback) {
                @Override
                public void onSetupParams(Object... params) {
                    put("type", type);
                    put("class_id", id);
                    put("start", columnBeen.get(columnBeen.size() - 1).id);
                }

                @Override
                public String getApi() {
                    return "/api/red_boat/column_list";
                }
            }.exe();
        }
        else {
            mLoadMore.setState(LoadMore.TYPE_IDLE);
        }

    }
}
