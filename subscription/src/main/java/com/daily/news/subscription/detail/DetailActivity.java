package com.daily.news.subscription.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.daily.news.subscription.R;
import com.zjrb.core.common.base.BaseActivity;

public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity_detail);

        Intent intent = getIntent();
        Uri data = intent.getData();
        String id = data.getQueryParameter("id");
        Log.e("TAG",id);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailFragment_new detailFragment = DetailFragment_new.newInstance(data.getQueryParameter("id"));
        transaction.add(R.id.detail_container, detailFragment);
        transaction.commit();
    }
}
