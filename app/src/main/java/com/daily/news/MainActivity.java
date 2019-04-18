package com.daily.news;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatDelegate;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daily.news.subscription.home.SubscriptionFragment;

import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.db.ThemeMode;
import cn.daily.news.biz.core.nav.Nav;

public class MainActivity extends DailyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
