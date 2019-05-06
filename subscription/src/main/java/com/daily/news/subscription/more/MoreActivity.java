package com.daily.news.subscription.more;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.category.CategoryFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.nav.Nav;

public class MoreActivity extends DailyActivity {

    private static final int REQUEST_CODE_TO_DETAIL = 1110;
    @BindView(R2.id.txt_input)
    TextView txtInput;
    CategoryFragment mCategoryFragment;
    Unbinder mUnbinder;
    private String mChannelName;
    private String mChannelId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity_more);
        mUnbinder = ButterKnife.bind(this);

         mChannelName = null;
         mChannelId = null;
        if (getIntent().getData() != null) {
            mChannelName = getIntent().getData().getQueryParameter("channel_name");
            mChannelId = getIntent().getData().getQueryParameter("channel_id");
        }
        mCategoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("channel_name", mChannelName);
        bundle.putString("channel_id", mChannelId);
        mCategoryFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.more_container, mCategoryFragment, "category").commit();
    }


    @OnClick({R2.id.iv_top_bar_back, R2.id.txt_input})
    public void onViewClicked(View view) {
        view.setSelected(true);
        if (view.getId() == R.id.iv_top_bar_back) {
            finish();
        } else if (view.getId() == R.id.txt_input) {
            Uri.Builder builder = new Uri.Builder().path("/subscription/more/search").appendQueryParameter("type", "more");
            Bundle args = new Bundle();
            args.putString("channel_name", mChannelName);
            args.putString("channel_id", mChannelId);
            Nav.with(this).setExtras(args).toPath(builder.build().toString(), REQUEST_CODE_TO_DETAIL);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            mCategoryFragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
