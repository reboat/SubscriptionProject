package com.daily.news.subscription.detail;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.article.ArticleFragment;
import com.daily.news.subscription.article.ArticlePresenter;
import com.daily.news.subscription.constants.Constants;
import com.daily.news.subscription.listener.AppBarStateChangeListener;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.recycleView.HeaderRefresh;
import com.zjrb.core.utils.JsonUtils;
import com.zjrb.core.utils.L;
import com.zjrb.core.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.network.compatible.LoadViewHolder;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;

/**
 * Created by gaoyangzhen on 2018/4/16.
 */

public class DetailFragment extends Fragment implements DetailContract.View, HeaderRefresh.OnRefreshListener {
    private static final String UID = "id";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int CODE_ALREADY_OFF_THE_SHELF = 50604;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.main)
    CoordinatorLayout main;
    @BindView(R2.id.appbar)
    AppBarLayout appbar;
    @BindView(R2.id.toolbar_detail_back)
    ImageView toolbarDetailBack;
    @BindView(R2.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R2.id.toolbar_detail_column_sub_btn)
    TextView toolbarDetailColumnSubBtn;
    @BindView(R2.id.toolbar_subscribe_container)
    LinearLayout toolbarSubscribeContainer;
    @BindView(R2.id.toolbar_rel)
    RelativeLayout toolbarRel;
    private String mUid;
    private DetailContract.Presenter mPresenter;

    @BindView(R2.id.detail_column_imageView)
    ImageView mImageView;
    @BindView(R2.id.detail_column_title)
    TextView mTitleView;
    @BindView(R2.id.detail_column_info)
    TextView mInfoView;
    @BindView(R2.id.detail_column_description)
    TextView mDescriptionView;
    @BindView(R2.id.detail_column_sub_btn)
    TextView mSubscriptionView;
    @BindView(R2.id.subscribe_container)
    ViewGroup mSubscribeContainer;
    @BindView(R2.id.detail_column_header_imageView)
    ImageView mHeaderImageView;
    @BindView(R2.id.detail_empty_error_container)
    View mEmptyErrorContainer;

    private DetailResponse.DataBean.DetailBean mDetailColumn;
    private ArticleFragment mArticleFragment;
    private ArticlePresenter mArticlePresenter;


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.Action.SUBSCRIBE_SUCCESS.equals(intent.getAction())) {
                long id = intent.getLongExtra(Constants.Name.ID, 0);
                boolean subscribe = intent.getBooleanExtra(Constants.Name.SUBSCRIBE, false);

                if (String.valueOf(id).equals(mUid)) {
                    String subscriptionText = subscribe ? "已订阅" : "订阅";
                    mSubscriptionView.setText(subscriptionText);
                    mSubscribeContainer.setSelected(subscribe);
                    toolbarDetailColumnSubBtn.setText(subscriptionText);
                    toolbarSubscribeContainer.setSelected(subscribe);
                }


            }
        }
    };


    public DetailFragment() {
        new DetailPresenter(this, new DetailStore());
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter(Constants.Action.SUBSCRIBE_SUCCESS));
    }

    public static DetailFragment newInstance(String uid) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUid = getArguments().getString(UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscription_fragment_detail_column_new, container, false);
        ButterKnife.bind(this, rootView);
        mArticleFragment = (ArticleFragment) getChildFragmentManager().findFragmentById(R.id.detail_article_fragment);
        mArticleFragment.setOnRefreshListener(this);

        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {

                    //展开状态
                    mArticleFragment.canRefresh(true);
                    toolbar.setVisibility(View.GONE);

                } else if (state == State.COLLAPSED) {

                    //折叠状态
                    mArticleFragment.canRefresh(false);
                    toolbar.setVisibility(View.VISIBLE);

                } else {

                    //中间状态
                    mArticleFragment.canRefresh(false);
                    toolbar.setVisibility(View.GONE);

                }

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.subscribe(mUid);
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {
    }

    @Override
    public void onRefresh() {
        mArticleFragment.setRefreshing(true);
        mPresenter.onRefresh(mUid);
    }

    @Override
    public void updateValue(DetailResponse response) {
        if (mArticleFragment != null) {
            mArticleFragment.setRefreshing(false);
        }
        if (response.code == 200) {
            DetailResponse.DataBean data = response.data;
            mDetailColumn = data.detail;
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(R.drawable.column_placeholder_big);
            Glide.with(this).load(data.detail.pic_url).apply(options).into(mImageView);
            mTitleView.setText(data.detail.name);
            toolbarTitle.setText(data.detail.name);

            String info = TextUtils.isEmpty(data.detail.subscribe_count_general) ? "" : data.detail.subscribe_count_general + "订阅  ";
            info += TextUtils.isEmpty(data.detail.article_count_general) ? "" : data.detail.article_count_general + "份稿件";
            mInfoView.setText(info);
            mDescriptionView.setText(data.detail.description);
            String subscriptionText = data.detail.subscribed ? "已订阅" : "订阅";
            mSubscriptionView.setText(subscriptionText);
            mSubscribeContainer.setSelected(data.detail.subscribed);
            toolbarDetailColumnSubBtn.setText(subscriptionText);
            toolbarSubscribeContainer.setSelected(data.detail.subscribed);

            options.placeholder(R.drawable.detail_column_default);
            Glide.with(this).load(data.detail.background_url).apply(options).into(mHeaderImageView);
            if (mArticlePresenter != null) {
                mArticlePresenter.refreshData(data.elements);
            } else {
                mArticlePresenter = new ArticlePresenter(mArticleFragment, new DetailArticleStore(mUid, data.elements));
                mArticlePresenter.refreshData(data.elements);
            }
        } else if (response.code == CODE_ALREADY_OFF_THE_SHELF) {
            appbar.setVisibility(View.GONE);
            mEmptyErrorContainer.setVisibility(View.VISIBLE);
            L.e("栏目下线");
        }
    }

    @Override
    public void onRefreshComplete() {
        if (mArticleFragment != null) {
            mArticleFragment.setRefreshing(false);
        }
    }

    @Override
    public void hideProgressBar() {
    }

    @Override
    public void showError(Throwable message) {
    }

    @Override
    public LoadViewHolder getProgressBar() {
        return new LoadViewHolder(main, (ViewGroup) main.getParent());
    }

    @OnClick({R2.id.subscribe_container, R2.id.toolbar_subscribe_container})
    public void submitSubscribe() {
        if (mDetailColumn.subscribed) {
            String pageType = "栏目详情页";
            String eventName = "“取消订阅”栏目";
            if(mDetailColumn.red_boat_column){
                pageType = "之江号详情页";
                eventName = "之江号取消订阅";
            }
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new Analytics.AnalyticsBuilder(getContext(), "A0114", "A0114", "SubColumn", false)
                    .setObjectID(String.valueOf(mDetailColumn.id))
                    .setObjectName(mDetailColumn.name)
                    .setEvenName(eventName)
                    .setPageType(pageType)
                    .setObjectType(ObjectType.NewsType)
                    .setOtherInfo(otherInfoStr)
                    .pageType(pageType)
                    .columnID(String.valueOf(mDetailColumn.id))
                    .columnName(mDetailColumn.name)
                    .operationType("取消订阅")
                    .build()
                    .send();
        }
        mPresenter.submitSubscribe(mDetailColumn);
        modifySubscribeBtnState(!mDetailColumn.subscribed);


    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        Intent intent = new Intent(Constants.Action.SUBSCRIBE_SUCCESS);
        intent.putExtra(Constants.Name.SUBSCRIBE, bean.subscribed);
        intent.putExtra(Constants.Name.ID, bean.id);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        getActivity().setResult(Activity.RESULT_OK, intent);

        if (bean.subscribed) {
            String pageType = "栏目详情页";
            String eventName = "“订阅”栏目";
            if(bean.red_boat_column){
                pageType = "之江号详情页";
                eventName = "之江号订阅";
            }
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new Analytics.AnalyticsBuilder(getContext(), "A0014", "A0014", "SubColumn", false)
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setObjectType(ObjectType.NewsType)
                    .setPageType(pageType)
                    .setEvenName(eventName)
                    .setOtherInfo(otherInfoStr)
                    .pageType(pageType)
                    .columnID(String.valueOf(bean.id))
                    .columnName(bean.name)
                    .operationType("订阅")
                    .build()
                    .send();
        }
    }

    @Override
    public void subscribeFail(ColumnResponse.DataBean.ColumnBean bean, String message) {
        modifySubscribeBtnState(!mDetailColumn.subscribed);
        Toast.makeText(getContext(), mDetailColumn.subscribed ? "取消订阅失败!" : "订阅失败!", Toast.LENGTH_SHORT).show();
    }

    private void modifySubscribeBtnState(boolean subscribe) {
        mDetailColumn.subscribed = subscribe;
        String subscriptionText = mDetailColumn.subscribed ? "已订阅" : "订阅";
        mSubscriptionView.setText(subscriptionText);
        mSubscribeContainer.setSelected(subscribe);
        toolbarDetailColumnSubBtn.setText(subscriptionText);
        toolbarSubscribeContainer.setSelected(subscribe);


    }

    @OnClick({R2.id.detail_empty_back, R2.id.detail_back, R2.id.toolbar_detail_back})
    public void onBack() {
        getActivity().finish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }


    @OnClick({R2.id.detail_share, R2.id.toolbar_detail_share})
    public void onViewClicked(View view) {

        if (mDetailColumn != null) {
            String shareName = StringUtils.isEmpty(mDetailColumn.name) ? "浙江新闻" : mDetailColumn.name ;
            String shareDes = StringUtils.isEmpty(mDetailColumn.description) ?"下载浙江新闻，查看更多身边新闻" :  mDetailColumn.description;
            String shareUrl = StringUtils.isEmpty(mDetailColumn.share_url) ? "https://zj.zjol.com.cn/":  mDetailColumn.share_url;
            //        //分享专用bean
            OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                    .setObjectID(mDetailColumn.id + "")
                    .setObjectName(shareName)
                    .setObjectType(ObjectType.ColumnType)
                    .setPageType("栏目详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", mDetailColumn.id + "")
                            .put("subject", "")
                            .toString())
                    .setSelfobjectID(mDetailColumn.id + "");

            if (!StringUtils.isEmpty(mDetailColumn.share_url)) {
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setTitle(shareName)
                        .setTextContent(shareDes)
                        .setImgUri(mDetailColumn.pic_url)
                        .setTargetUrl(shareUrl)
                        .setAnalyticsBean(bean)
                        .setEventName("PageShare")
                        .setShareContentID(mDetailColumn.id + "")
                        .setShareType("栏目")
                );
            } else {
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setTitle(shareName)
                        .setTextContent(shareDes)
                        .setPicId(R.mipmap.ic_launcher)
                        .setTargetUrl(shareUrl)
                        .setAnalyticsBean(bean)
                        .setEventName("PageShare")
                        .setShareContentID(mDetailColumn.id + "")
                        .setShareType("栏目")
                );
            }
        }


        //点击分享操作
        new Analytics.AnalyticsBuilder(getContext(), "800018", "800018", "AppTabClick", false)
                .setObjectID(mDetailColumn.id + "")
                .setObjectName(mDetailColumn.name)
                .setObjectType(ObjectType.ColumnType)
                .setPageType("栏目详情页")
                .setEvenName("点击“分享”")
                .pageType("栏目详情页")
                .clickTabName("分享")
                .build()
                .send();

    }
}
