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

import com.daily.news.subscription.ui.RecommendFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.title_home);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentByTag("recommend");
                    if (fragment != null) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.hide(fragment);
                        transaction.commit();
                    }


                    return true;
                case R.id.navigation_dashboard:
                    fragmentManager = getSupportFragmentManager();
                    fragment = fragmentManager.findFragmentByTag("recommend");
                    if (fragment == null) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.content, new RecommendFragment(), "recommend");
                        transaction.commit();
                    } else {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.show(fragment);
                        transaction.commit();
                    }
                    mTextMessage.setVisibility(View.GONE);

                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.title_notifications);
                    fragmentManager = getSupportFragmentManager();
                    fragment = fragmentManager.findFragmentByTag("recommend");
                    if (fragment != null) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction = fragmentManager.beginTransaction();
                        transaction.hide(fragment);
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
