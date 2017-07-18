package com.daily.news.subscription.more.my;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.daily.news.subscription.R;
import com.daily.news.subscription.more.column.ColumnPresenter;

public class MyColumnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_column);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("我的订阅");

        MyColumnFragment fragment = new MyColumnFragment();
        Bundle bundle=new Bundle();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.my_subscription_container, fragment)
                .commit();
        new ColumnPresenter(fragment, new MyStore());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
