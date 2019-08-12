package com.daily.news.subscription.my;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.constants.Constants;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.zjrb.core.base.toolbar.TopBarFactory;

import cn.daily.news.biz.core.DailyActivity;

public class MyColumnActivity extends DailyActivity {
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity_my_subscription);

        String type = getIntent().getData().getQueryParameter(Constants.Name.COLUMN_TYPE);
        mType = Integer.parseInt(type);
        MyColumnFragment fragment = new MyColumnFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.Name.COLUMN_TYPE, mType);
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.my_subscription_container, fragment)
                .commit();
        new ColumnPresenter(fragment, new MyStore());

    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        String title = mType == 1 ? "我的关注" : "我的订阅";
        return TopBarFactory.createDefault(view, this, title).getView();
    }
}
