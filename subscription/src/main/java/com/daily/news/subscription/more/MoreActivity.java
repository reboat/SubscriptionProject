package com.daily.news.subscription.more;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.category.CategoryFragment;
import com.daily.news.subscription.more.category.CategoryPresenter;
import com.daily.news.subscription.more.category.CategoryStore;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.search.SearchColumnFragment;
import com.daily.news.subscription.more.search.SearchStore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    @BindView(R2.id.more_key_word)
    EditText mKeywordView;
    @BindView(R2.id.more_search)
    View mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.more_title);
        }

        ButterKnife.bind(this);
        mSearchView.setOnClickListener(this);
        mKeywordView.clearFocus();
        mKeywordView.setOnEditorActionListener(this);

        CategoryFragment fragment = new CategoryFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.more_container, fragment)
                .commit();
        new CategoryPresenter(fragment, new CategoryStore());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
            searchCategory();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        searchCategory();
    }

    private void searchCategory() {
        String keyword = mKeywordView.getText().toString();
        if (!checkValid(keyword)) {
            return;
        }

        SearchColumnFragment fragment= (SearchColumnFragment) getSupportFragmentManager().findFragmentByTag("search");
        if (fragment == null) {
            fragment = new SearchColumnFragment();
            Bundle args=new Bundle();
            args.putString("keyword",keyword);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.more_container, fragment,"search")
                    .addToBackStack("search")
                    .commit();
            new ColumnPresenter(fragment, new SearchStore());
        }else{
            fragment.sendRequest(new Object[]{keyword});
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean checkValid(String keyword) {
        return true;
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
