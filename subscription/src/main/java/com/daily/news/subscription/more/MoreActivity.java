package com.daily.news.subscription.more;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.category.CategoryFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R2.id.more_key_word)
    EditText mKeyWordView;
    @BindView(R2.id.more_search)
    View mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        mSearchView.setOnClickListener(this);

        CategoryFragment fragment = new CategoryFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.more_container, fragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        String keyword = mKeyWordView.getText().toString();
        Bundle arguments = new Bundle();
        arguments.putString(MoreDetailFragment.ARG_ITEM_ID, keyword);
        MoreDetailFragment fragment = new SearchDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.more_container, fragment)
                .commit();
    }
}
