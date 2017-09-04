package com.daily.news.subscription.my;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnResponse;

/**
 * Created by lixinke on 2017/7/17.
 */

public class MyColumnFragment extends ColumnFragment {
    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        removeItem(bean);
    }

    @Override
    public View emptyView(LayoutInflater inflater, ViewGroup parent){
        return inflater.inflate(R.layout.my_subscription_empty,parent,false);
    }
}
