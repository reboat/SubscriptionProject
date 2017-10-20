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

import java.util.List;

/**
 * Created by lixinke on 2017/10/20.
 */

public class CategoryColumnFragment extends ColumnFragment {
    public CategoryColumnFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        List<ColumnResponse.DataBean.ColumnBean> columnBeen=getArguments().getParcelableArrayList("columns");
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
}
