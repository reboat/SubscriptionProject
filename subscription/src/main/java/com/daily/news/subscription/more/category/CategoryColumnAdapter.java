package com.daily.news.subscription.more.category;

import android.view.ViewGroup;

import com.daily.news.subscription.more.column.ColumnAdapter;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by lixinke on 2017/10/20.
 */

public class CategoryColumnAdapter extends ColumnAdapter {
    public CategoryColumnAdapter(List<ColumnResponse.DataBean.ColumnBean> columnBeen) {
        super(columnBeen);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        ColumnViewHolder holder= (ColumnViewHolder) super.onAbsCreateViewHolder(parent, viewType);
        holder.hidePadingView();
        return holder;
    }
}
