package com.daily.news.subscription.more.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.constants.Constants;
import com.zjrb.core.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;

/**
 * Created by gaoyangzhen on 2018/3/13.
 */

public class SearchActivity extends DailyActivity implements TextView.OnEditorActionListener, TextWatcher {


    @BindView(R2.id.et_input)
    EditText etInput;
    @BindView(R2.id.iv_cross)
    ImageView ivCross;
    @BindView(R2.id.tv_cancel)
    TextView tvCancel;
    @BindView(R2.id.more_container)
    FrameLayout moreContainer;

    private Unbinder mUnbinder;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity_search);
        mUnbinder = ButterKnife.bind(this);
        etInput.setOnEditorActionListener(this);
        etInput.addTextChangedListener(this);

        String type = getIntent().getData().getQueryParameter(Constants.Name.COLUMN_TYPE);
        mType = Integer.parseInt(type);

        String hint = mType == 1 ? "搜索启航号" : "搜索栏目";
        etInput.setHint(hint);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null) {
            ivCross.setVisibility(s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
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

    private void searchCategory() {
        String keyword = etInput.getText().toString();
        if (!checkValid(keyword)) {
            return;
        }

        Fragment fragment = new SearchColumnFragment();
        Bundle args = new Bundle();
        args.putString("keyword", keyword);
        args.putInt(Constants.Name.COLUMN_TYPE, mType);
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.more_container, fragment, "search")
                .addToBackStack("search")
                .commitAllowingStateLoss();


        new Analytics.AnalyticsBuilder(this, "A0013", "Search", false)
                .name("订阅号搜索")
                .pageType("订阅号分类检索页面")
                .searchWord(keyword)
                .isHotWordUsed(false)
                .isHistoryWordUsed(false)
                .build()
                .send();
        UIUtils.hideSoftInput(etInput);
    }

    private boolean checkValid(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, "请输入查询内容!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @OnClick({R2.id.iv_cross, R2.id.tv_cancel})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.iv_cross) {
            SearchColumnFragment fragment = (SearchColumnFragment) getSupportFragmentManager().findFragmentByTag("search");
            if (fragment != null) {
                fragment.clear();
            }
            etInput.setText("");

        } else if (view.getId() == R.id.tv_cancel) {
            UIUtils.hideSoftInput(etInput); // 手动隐藏软键盘
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
