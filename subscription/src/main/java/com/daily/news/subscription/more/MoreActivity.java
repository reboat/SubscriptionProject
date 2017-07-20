package com.daily.news.subscription.more;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.category.CategoryFragment;
import com.daily.news.subscription.more.category.CategoryPresenter;
import com.daily.news.subscription.more.category.CategoryStore;
import com.daily.news.subscription.more.column.ColumnFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.search.SearchColumnFragment;
import com.daily.news.subscription.more.search.SearchStore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R2.id.more_key_word)
    EditText mKeywordView;
    @BindView(R2.id.more_search)
    View mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        getSupportActionBar().setTitle("订阅更多");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        mSearchView.setOnClickListener(this);
        mKeywordView.clearFocus();

        CategoryFragment fragment = new CategoryFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.more_container, fragment)
                .commit();
        new CategoryPresenter(fragment, new CategoryStore());
    }

    @Override
    public void onClick(View v) {
        String keyword = mKeywordView.getText().toString();
        Bundle arguments = new Bundle();
        arguments.putString(ColumnFragment.ARG_ITEM_ID, keyword);
        ColumnFragment fragment = new SearchColumnFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.more_container, fragment)
                .commit();
        new ColumnPresenter(fragment, new SearchStore());

        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
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
