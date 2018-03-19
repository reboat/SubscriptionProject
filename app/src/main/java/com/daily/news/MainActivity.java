package com.daily.news;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.daily.news.subscription.home.SubscriptionFragment;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.nav.Nav;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.night_model);
        button.setText(ThemeMode.isNightMode()?"夜间模式":"正常模式");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeMode.setUiMode(!ThemeMode.isNightMode());
                button.setText(ThemeMode.isNightMode()?"夜间模式":"正常模式");
            }
        });

        RadioGroup group = (RadioGroup) findViewById(R.id.radio_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.home:
                        Nav.with(MainActivity.this).to("http://www.8531.cn/subscription/more");
                        break;
                    case R.id.sub:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, SubscriptionFragment.newInstance()).commitAllowingStateLoss();
                        break;
                    case R.id.develop:
                        break;
                }
            }
        });
    }


}
