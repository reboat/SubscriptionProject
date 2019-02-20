package com.daily.news.subscription.my;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.zjrb.core.base.toolbar.TopBarFactory;

import cn.daily.news.biz.core.DailyActivity;

public class MyColumnActivity extends DailyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity_my_subscription);

        MyColumnFragment fragment = new MyColumnFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.my_subscription_container, fragment)
                .commit();
        new ColumnPresenter(fragment, new MyStore());

    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view,this,"我的订阅").getView();
    }
}
