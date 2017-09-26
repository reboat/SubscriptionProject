package com.daily.news;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.RadioGroup;

import com.daily.news.subscription.home.SubscriptionFragment;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.db.ThemeMode;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.night_model).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeMode.setUiMode(!ThemeMode.isNightMode());
            }
        });

        RadioGroup group = (RadioGroup) findViewById(R.id.radio_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.home:
                        break;
                    case R.id.sub:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, SubscriptionFragment.newInstance()).commit();
                        break;
                    case R.id.develop:
                        break;
                }
            }
        });
    }


}
