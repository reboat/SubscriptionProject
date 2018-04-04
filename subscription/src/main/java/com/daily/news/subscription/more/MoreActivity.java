package com.daily.news.subscription.more;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.category.CategoryFragment;
import com.daily.news.subscription.more.category.CategoryRedFragment;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.nav.Nav;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.daily.news.analytics.Analytics;

/**
 * framelayout + fragmentManager  hide(),show()的方式加载fragment，不适合滑动切换
 *
 * @author zhengy
 *         create at 2018/3/22 上午10:30
 **/
public class MoreActivity extends BaseActivity {

    private static final int REQUEST_CODE_TO_DETAIL = 1110;

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


    @OnClick({R2.id.iv_top_bar_back, R2.id.iv_top_bar_search})
    public void onViewClicked(View view) {
        view.setSelected(true);
        if (view.getId() == R.id.iv_top_bar_back) {
            finish();
        } else if (view.getId() == R.id.iv_top_bar_search) {

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
        sub_fragment.onActivityResult(requestCode, resultCode, data);

    }

    private void switchFragment(View v, Fragment show, Fragment hide) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.show(show);
        if (hide != null) {
            transaction.hide(hide);
        }
        transaction.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
