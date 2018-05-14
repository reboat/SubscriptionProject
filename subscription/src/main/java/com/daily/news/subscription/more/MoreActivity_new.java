package com.daily.news.subscription.more;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.category.CategoryFragment;
import com.daily.news.subscription.more.category.CategoryRedFragment;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.biz.ResourceBiz;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.nav.Nav;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.daily.news.analytics.Analytics;

/**
 * viewPager实现加载fragment，包含点击tab和滑动切换
 * Created by gaoyangzhen on 2018/3/21.
 */

public class MoreActivity_new extends BaseActivity {


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
    ViewPager moreContainer;


    List<Fragment> fragmentList = new ArrayList<>();
    CategoryRedFragment red_fragment;
    CategoryFragment sub_fragment;

    Unbinder unbinder;
    @BindView(R2.id.tab_red_sub_txt)
    TextView tabRedSubTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_activity_more_new);

        unbinder = ButterKnife.bind(this);

        initView();

        red_fragment = new CategoryRedFragment();
        sub_fragment = new CategoryFragment();

        fragmentList.add(red_fragment);
        fragmentList.add(sub_fragment);

        moreContainer.setAdapter(new MoreFragmentAdapter(getSupportFragmentManager(), fragmentList));
        moreContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Do Nothing
            }

            @Override
            public void onPageSelected(int position) {
                onViewClicked(position == 0 ? tabRedSub : tabMySub);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Do Nothing
            }
        });

        onViewClicked(tabRedSub);

    }

    //动态获取tab的名字，最长4个字符
    private void initView() {
        ResourceBiz biz = SPHelper.get().getObject(SPHelper.Key.INITIALIZATION_RESOURCES);
        if (biz != null && biz.feature_list != null) {
            for (ResourceBiz.FeatureListBean bean : biz.feature_list) {
                if (bean.name.equals("hch")) {
                    String text = bean.desc;
                    if (text != null && text != "") {

                        if(text.length() > 4)
                        {
                            text = text.substring(0, 4) + "...";
                        }
                        tabRedSubTxt.setText(text);
                    }
                    break;
                }
            }
        }
    }


    @OnClick({R2.id.tab_red_sub, R2.id.tab_my_sub, R2.id.iv_top_bar_back, R2.id.iv_top_bar_search})
    public void onViewClicked(View view) {
        view.setSelected(true);
        if (view.getId() == R.id.tab_red_sub) {
            tabMySub.setSelected(false);
            moreContainer.setCurrentItem(0);
            new Analytics.AnalyticsBuilder(MoreActivity_new.this, "500008", "500008")
                    .setPageType("订阅更多页面")
                    .setEvenName("点击\"红船号\"tab")
                    .build()
                    .send();
        } else if (view.getId() == R.id.tab_my_sub) {
            tabRedSub.setSelected(false);
            moreContainer.setCurrentItem(1);
            new Analytics.AnalyticsBuilder(MoreActivity_new.this, "500009", "500009")
                    .setPageType("订阅更多页面")
                    .setEvenName("点击\"栏目号\"tab")
                    .build()
                    .send();
        } else if (view.getId() == R.id.iv_top_bar_back) {
            finish();
        } else if (view.getId() == R.id.iv_top_bar_search) {

            Nav.with(this).to(Uri.parse("http://www.8531.cn/subscription/more/search")
                    .buildUpon()
                    .appendQueryParameter("type", "more_new")
                    .build()
                    .toString(), REQUEST_CODE_TO_DETAIL);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            red_fragment.onActivityResult(requestCode, resultCode, data);
            sub_fragment.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
