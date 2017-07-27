package com.daily.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.daily.news.subscription.home.SubscriptionFragment;
import com.daily.news.subscription.home.SubscriptionPresenter;
import com.daily.news.subscription.home.SubscriptionStore;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment recommend = fragmentManager.findFragmentByTag("subscription");
            FragmentTransaction transaction = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.title_home);
                    if (recommend != null) {
                        transaction = fragmentManager.beginTransaction();
                        transaction.hide(recommend);
                        transaction.commit();
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if (recommend == null) {
                        SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
                        transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.content, subscriptionFragment, "subscription");
                        transaction.commit();
                        new SubscriptionPresenter(subscriptionFragment, new SubscriptionStore());

                    } else {
                        transaction = fragmentManager.beginTransaction();
                        transaction.show(recommend);
                        transaction.commit();
                    }
                    mTextMessage.setVisibility(View.GONE);

                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.title_notifications);
                    if (recommend != null) {
                        transaction = fragmentManager.beginTransaction();
                        transaction.hide(recommend);
                        transaction.commit();
                    }
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
