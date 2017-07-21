package com.daily.news.subscription.home.my;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.article.ArticleFragment;

public class SubscriptionFragment extends ArticleFragment {
    public SubscriptionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View headerView = inflater.inflate(R.layout.header_my_subscription, container, false);
        addHeaderView(headerView);

        headerView.findViewById(R.id.my_sub_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getString(R.string.daily_intent_action));
                intent.setData(Uri.parse("http://www.8531.cn/subscription/my/subscription"));
                startActivity(intent);
            }
        });

        headerView.findViewById(R.id.my_sub_more_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getString(R.string.daily_intent_action));
                intent.setData(Uri.parse("http://www.8531.cn/subscription/more"));
                startActivity(intent);
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
