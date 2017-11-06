package com.daily.news.subscription.more;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.category.CategoryFragment;
import com.daily.news.subscription.more.search.SearchColumnFragment;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics.AnalyticsBuilder;

public class MoreActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    @BindView(R2.id.more_key_word)
    EditText mKeywordView;
    @BindView(R2.id.more_search)
    View mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity_more);

        ButterKnife.bind(this);
        mSearchView.setOnClickListener(this);
        mKeywordView.clearFocus();
        mKeywordView.setOnEditorActionListener(this);

        if (getSupportFragmentManager().findFragmentByTag("category") == null) {
            CategoryFragment fragment = new CategoryFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.more_container, fragment, "category")
                    .commit();
        }
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

        SearchColumnFragment fragment = (SearchColumnFragment) getSupportFragmentManager().findFragmentByTag("search");
        if (fragment == null) {
            fragment = new SearchColumnFragment();
            Bundle args = new Bundle();
            args.putString("keyword", keyword);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.more_container, fragment, "search")
                    .addToBackStack("search")
                    .commit();
        } else {
            fragment.sendRequest(new Object[]{keyword});
        }

        new AnalyticsBuilder(this, "A0013", "A0013")
                .setPageType("订阅更多页面")
                .setEvenName("关键词搜索")
                .setSearch(keyword)
                .build()
                .send();
        UIUtils.hideSoftInput(mKeywordView);
    }

    private boolean checkValid(String keyword) {
        return true;
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, "订阅更多").getView();
    }
}
