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
* @author zhengy
* create at 2018/3/22 上午10:30
**/
public class MoreActivity extends BaseActivity{

    private static final int REQUEST_CODE_TO_DETAIL = 1110;

    @BindView(R2.id.more_key_word)
    EditText mKeywordView;
    @BindView(R2.id.more_search)
    View mSearchView;

    @BindView(R2.id.more_key_word_delete)
    View mDeletedView;
    @BindView(R2.id.tab_red_sub)
    FrameLayout tabRedSub;
    @BindView(R2.id.tab_my_sub)
    FrameLayout tabMySub;
    @BindView(R2.id.iv_top_bar_back)
    ImageView ivTopBarBack;
    @BindView(R2.id.iv_top_bar_search)
    ImageView ivTopBarSearch;
    @BindView(R2.id.more_container)
    FrameLayout moreContainer;

    private FragmentManager mFragmentManager;

    CategoryRedFragment red_fragment;
    CategoryFragment sub_fragment;

    Unbinder unbinder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity_more);

        unbinder = ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();
        red_fragment = new CategoryRedFragment();
        sub_fragment = new CategoryFragment();

        mFragmentManager.beginTransaction().add(R.id.more_container, red_fragment, "category_red").commit();
        mFragmentManager.beginTransaction().add(R.id.more_container, sub_fragment, "category").commit();


        onViewClicked(tabRedSub);

    }


    @OnClick({R2.id.tab_red_sub, R2.id.tab_my_sub, R2.id.iv_top_bar_back, R2.id.iv_top_bar_search})
    public void onViewClicked(View view) {
        view.setSelected(true);
        if (view.getId() == R.id.tab_red_sub) {
            tabMySub.setSelected(false);
            switchFragment(view, red_fragment, sub_fragment);
            new Analytics.AnalyticsBuilder(MoreActivity.this, "500008", "500008")
                    .setPageType("订阅更多页面")
                    .setEvenName("点击\"红船号\"tab")
                    .build()
                    .send();
        } else if (view.getId() == R.id.tab_my_sub) {
            tabRedSub.setSelected(false);
            switchFragment(view, sub_fragment, red_fragment);
            new Analytics.AnalyticsBuilder(MoreActivity.this, "500009", "500009")
                    .setPageType("订阅更多页面")
                    .setEvenName("点击\"栏目号\"tab")
                    .build()
                    .send();
        } else if (view.getId() == R.id.iv_top_bar_back) {
            finish();
        } else if (view.getId() == R.id.iv_top_bar_search) {

//            Nav.with(this).to("http://www.8531.cn/subscription/more/search");
            Nav.with(this).to(Uri.parse("http://www.8531.cn/subscription/more/search")
                    .buildUpon()
                    .build()
                    .toString(),REQUEST_CODE_TO_DETAIL);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        red_fragment.onActivityResult(requestCode, resultCode, data);
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
