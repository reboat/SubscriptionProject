package com.daily.news.subscription.more;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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

    private FragmentManager mFragmentManager;

    CategoryFragment sub_fragment;

    Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity_more);

        unbinder = ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();
        sub_fragment = new CategoryFragment();

        mFragmentManager.beginTransaction().add(R.id.more_container, sub_fragment, "category").commit();


    }


    @OnClick({R2.id.iv_top_bar_back, R2.id.txt_input})
    public void onViewClicked(View view) {
        view.setSelected(true);
        if (view.getId() == R.id.iv_top_bar_back) {
            finish();
        } else if (view.getId() == R.id.txt_input) {

//            Nav.with(this).to("http://www.8531.cn/subscription/more/search");
            Nav.with(this).to(Uri.parse("http://www.8531.cn/subscription/more/search")
                    .buildUpon()
                    .appendQueryParameter("type", "more")
                    .build()
                    .toString(), REQUEST_CODE_TO_DETAIL);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            sub_fragment.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
