package com.daily.news.subscription.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.daily.news.subscription.R;

import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;

public class DetailActivity extends DailyActivity {

    private Analytics mAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity_detail);

        String id = "";
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
        } else {

            Uri data = intent.getData();
            id = data.getQueryParameter("id");
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailFragment detailFragment = DetailFragment.newInstance(id);
        transaction.add(R.id.detail_container, detailFragment);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAnalytics = new Analytics.AnalyticsBuilder(this, "A0010", "ColumnDetailPageStay", true)
                .name("页面停留时长")
                .pageType("订阅号详情页")
                .build();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAnalytics != null) {
            mAnalytics.sendWithDuration();
        }
    }
}
