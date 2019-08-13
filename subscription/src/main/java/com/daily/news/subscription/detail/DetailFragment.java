package com.daily.news.subscription.detail;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.core.network.callback.ApiCallback;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.article.ArticleFragment;
import com.daily.news.subscription.article.ArticlePresenter;
import com.daily.news.subscription.bitmap.BlurTransformation;
import com.daily.news.subscription.constants.Constants;
import com.daily.news.subscription.detail.task.PromoteResponse;
import com.daily.news.subscription.detail.task.PromoteTask;
import com.daily.news.subscription.listener.AppBarStateChangeListener;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.recycleView.HeaderRefresh;
import com.zjrb.core.ui.widget.CircleImageView;
import com.zjrb.core.utils.L;
import com.zjrb.core.utils.StringUtils;
import com.zjrb.core.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.model.ResourceBiz;
import cn.daily.news.biz.core.network.compatible.LoadViewHolder;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;
import cn.daily.news.biz.core.ui.dialog.RankTipDialog;
import cn.daily.news.biz.core.ui.toast.ZBToast;
import cn.daily.news.biz.core.utils.TypeFaceUtils;

/**
 * Created by gaoyangzhen on 2018/4/16.
 */

public class DetailFragment extends Fragment implements DetailContract.View, HeaderRefresh.OnRefreshListener {
    public static final int CODE_ALREADY_OFF_THE_SHELF = 50604;
    private static final String UID = "id";
    private static final int DEFAULT_PAGE_SIZE = 10;
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
    @BindView(R2.id.toolbar_rel)
    RelativeLayout toolbarRel;
    private String mUid;
    private DetailContract.Presenter mPresenter;

    @BindView(R2.id.detail_column_imageView)
    CircleImageView mImageView;
    @BindView(R2.id.detail_column_title)
    TextView mTitleView;
    @BindView(R2.id.detail_column_description)
    TextView mDescriptionView;
    @BindView(R2.id.subscribe_container)
    ImageView mSubscribeContainer;
    @BindView(R2.id.detail_column_header_imageView)
    ImageView mHeaderImageView;
    @BindView(R2.id.detail_empty_error_container)
    View mEmptyErrorContainer;
    @BindView(R2.id.toobar_icon)
    ImageView mToolbarIcon;
    @BindView(R2.id.toolbar_detail_sub)
    ImageView mToolbarSub;
    @BindView(R2.id.detail_column_num)
    TextView mArticleNumView;
    @BindView(R2.id.detail_column_mark)
    TextView mTypeTagView;
    @BindView(R2.id.rank_hit_count)
    TextView mHitCountView;
    @BindView(R2.id.rank_action)
    TextView mActionView;
    @BindView(R2.id.rank_score)
    TextView mScoreView;
    @BindView(R2.id.rank_action_container)
    View mActionViewContainer;

    @BindView(R2.id.detail_loading_container)
    ViewGroup mLoadingContainer;
    @BindView(R2.id.loading_temp)
    View mLoadingTemp;


    private DetailResponse.DataBean.DetailBean mDetailColumn;
    private ArticleFragment mArticleFragment;
    private ArticlePresenter mArticlePresenter;

    private BroadcastReceiver mSubscribeStateReceiver = new SubscribeStateReceiver();
    private BroadcastReceiver mRankStateReceiver = new RankStateReceiver();

    public DetailFragment() {
        new DetailPresenter(this, new DetailStore());
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
        View rootView = inflater.inflate(R.layout.subscription_fragment_detail_column, container, false);
        ButterKnife.bind(this, rootView);
        mArticleFragment = (ArticleFragment) getChildFragmentManager().findFragmentById(R.id.detail_article_fragment);
        mArticleFragment.setOnRefreshListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            int height = UIUtils.dip2px(24);
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                height = getResources().getDimensionPixelSize(resourceId);
            }
            toolbar.setPadding(0, height, 0, 0);
            mLoadingContainer.setPadding(0, height, 0, 0);
        }

        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int i) {
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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mSubscribeStateReceiver, new IntentFilter(Constants.Action.SUBSCRIBE_SUCCESS));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRankStateReceiver, new IntentFilter(Constants.Action.HIT_RANK_SUCCESS));
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
        mLoadingContainer.setVisibility(View.VISIBLE);
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
            main.setVisibility(View.VISIBLE);
            DetailResponse.DataBean data = response.data;
            mDetailColumn = data.detail;
            mArticleFragment.setColumnId(mDetailColumn);
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(R.drawable.detail_icon_holder);
            Glide.with(this).load(data.detail.pic_url).apply(options).into(mImageView);
            Glide.with(this).load(data.detail.pic_url).apply(options).into(mToolbarIcon);
            mTitleView.setText(data.detail.name);
            toolbarTitle.setText(data.detail.name);

            if (!data.detail.normal_column) {
                mTypeTagView.setVisibility(View.VISIBLE);
                mActionViewContainer.setVisibility(isRankEnable() ? View.VISIBLE : View.GONE);
            } else {
                mTypeTagView.setVisibility(View.GONE);
                mActionViewContainer.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.detail.article_count_general)) {
                mArticleNumView.setText(String.format("文章数 %s篇", data.detail.article_count_general));
                TypeFaceUtils.formatNumToDin(mArticleNumView);
            }

            mDescriptionView.setText(data.detail.description);
            mSubscribeContainer.setSelected(data.detail.subscribed);
            int id = mDetailColumn.subscribed ? R.drawable.zjnews_detail_navigation_subscribed_icon : R.drawable.zjnews_detail_navigation_subscribe_icon;
            mToolbarSub.setImageResource(id);

            RequestOptions options1 = RequestOptions.bitmapTransform(new BlurTransformation(getContext(), 25f))
                    .placeholder(R.drawable.detail_column_default);
            GlideApp.with(this).load(data.detail.background_url).apply(options1).into(mHeaderImageView);
            if (mArticlePresenter != null) {
                mArticlePresenter.refreshData(data.elements);
            } else {
                mArticlePresenter = new ArticlePresenter(mArticleFragment, new DetailArticleStore(mUid, data.elements));
                mArticlePresenter.refreshData(data.elements);
            }

            mHitCountView.setText(getHitRankCount(mDetailColumn));

            if (mDetailColumn.rank_hited) {
                mActionView.setText("拉票");
            } else {
                mActionView.setText("打榜");
            }

        } else if (response.code == CODE_ALREADY_OFF_THE_SHELF) {
            appbar.setVisibility(View.GONE);
            mEmptyErrorContainer.setVisibility(View.VISIBLE);
            L.e("栏目下线");
        }
    }

    /**
     * 返回打榜人气值 优先使用服务端返回的字符串，为空超过1w显示1万+
     *
     * @param bean
     * @return
     */
    private String getHitRankCount(DetailResponse.DataBean.DetailBean bean) {
        return TextUtils.isEmpty(bean.hit_rank_count_general)
                ? (bean.hit_rank_count > Constants.MAX_COUNT ? "1万+" : String.valueOf(bean.hit_rank_count))
                : bean.hit_rank_count_general;
    }

    /**
     * 榜单是否开启,默认开启
     *
     * @return
     */
    private boolean isRankEnable() {
        boolean isRankEnable = true;
        ResourceBiz resourceBiz = SPHelper.get().getObject(cn.daily.news.biz.core.constant.Constants.Key.INITIALIZATION_RESOURCES);
        if (resourceBiz != null && resourceBiz.feature_list != null && resourceBiz.feature_list.size() > 0) {
            for (ResourceBiz.FeatureListBean bean : resourceBiz.feature_list) {
                if ("columns_rank".equals(bean.name)) {
                    isRankEnable = bean.enabled;
                    break;
                }
            }
        }
        return isRankEnable;
    }

    @OnClick(R2.id.rank_action_container)
    public void onActionClick() {
        if (!mDetailColumn.rank_hited) {
            if (!mDetailColumn.subscribed) {
                RankTipDialog.Builder builder = new RankTipDialog.Builder()
                        .setLeftText("取消")
                        .setRightText("继续打榜")
                        .setMessage("打榜需要先完成关注")
                        .setOnLeftClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new Analytics.AnalyticsBuilder(v.getContext(), "200037", "", false)
                                        .name("点击取消打榜")
                                        .pageType("弹框").build().send();
                            }
                        })
                        .setOnRightClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendActionRequest(mDetailColumn.id);
                                new Analytics.AnalyticsBuilder(v.getContext(), "200038", "", false)
                                        .name("点击继续打榜")
                                        .pageType("弹框").build().send();

                            }
                        });
                RankTipDialog dialog = new RankTipDialog(getContext());
                dialog.setBuilder(builder);
                dialog.show();
            } else {
                sendActionRequest(mDetailColumn.id);
            }

            new Analytics.AnalyticsBuilder(getContext(), "A0061", "", false)
                    .name("点击打榜")
                    .pageType("订阅号详情页")
                    .columnID(String.valueOf(mDetailColumn.id))
                    .columnName(mDetailColumn.name)
                    .seObjectType(ObjectType.C90)
                    .build()
                    .send();
        } else {
            shareSail(mDetailColumn);
            new Analytics.AnalyticsBuilder(getContext(), "A0062", "", false)
                    .name("点击拉票")
                    .pageType("订阅号详情页")
                    .columnID(String.valueOf(mDetailColumn.id))
                    .columnName(mDetailColumn.name)
                    .seObjectType(ObjectType.C90)
                    .build()
                    .send();
        }
    }

    private void shareSail(DetailResponse.DataBean.DetailBean bean) {
        String shareName = String.format("我正在为起航号“%s”拉赞助力，快来和我一起为它加油！", bean.name);
        String shareDes = TextUtils.isEmpty(bean.description) ? null : String.format("点击查看起航号“%s”榜上排名", bean.name);
        String shareUrl = "https://zj.zjol.com.cn/";


        UmengShareBean shareBean = UmengShareBean.getInstance()
                .setSingle(false)
                .setTitle(shareName)
                .setAnalyticsBean(OutSizeAnalyticsBean.getInstance()
                        .setPageType("订阅号详情页")
                        .setColumn_id(String.valueOf(mDetailColumn.id))
                        .setColumn_name(mDetailColumn.name)
                        .setObjectType(ObjectType.C90))
                .setTextContent(shareDes)
                .setTargetUrl(TextUtils.isEmpty(bean.rank_share_url) ? shareUrl : bean.rank_share_url)
                .setShareType("栏目")
                .setCardPageType("卡片详情页")
                .setNewsCard(false)
                .setCardUrl(bean.rank_card_url);
        if (!StringUtils.isEmpty(bean.pic_url)) {
            shareBean.setImgUri(bean.pic_url);
        } else {
            shareBean.setPicId(R.mipmap.ic_launcher);
        }
        shareBean.setPicId(R.mipmap.ic_launcher);
        UmengShareUtils.getInstance().startShare(shareBean);
    }

    private void sendActionRequest(long id) {
        new PromoteTask(new PromoteCallback()).exe(id);
    }

    private void makeAnimation() {
        mScoreView.setVisibility(View.VISIBLE);
        ObjectAnimator translate = ObjectAnimator.ofFloat(mScoreView, "translationY", 0, -50);
        translate.setDuration(1000);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mScoreView, "alpha", 0, 1);
        alpha.setDuration(1000);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mScoreView, "scaleX", 1, 1.2f);
        scaleX.setDuration(1000);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mScoreView, "scaleY", 1, 1.2f);
        scaleY.setDuration(1000);

        AnimatorSet step1 = new AnimatorSet();
        step1.playTogether(translate, alpha, scaleX, scaleY);

        ObjectAnimator translate1 = ObjectAnimator.ofFloat(mScoreView, "translationY", -50, -70);
        translate1.setDuration(600);
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(mScoreView, "alpha", 1, 0);
        alpha1.setDuration(600);


        AnimatorSet step2 = new AnimatorSet();
        step2.playTogether(translate1, alpha1);

        AnimatorSet allStep = new AnimatorSet();
        allStep.play(step1).before(step2);
        allStep.start();
    }

    @Override
    public void onRefreshComplete() {
        if (mArticleFragment != null) {
            mArticleFragment.setRefreshing(false);
        }
    }

    @Override
    public void hideProgressBar() {
        mLoadingContainer.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable message) {
    }

    @Override
    public LoadViewHolder getProgressBar() {
        return new LoadViewHolder(mLoadingTemp, (ViewGroup) mLoadingTemp.getParent());
    }

    @OnClick({R2.id.subscribe_container, R2.id.toolbar_detail_sub})
    public void submitSubscribe() {
        new Analytics.AnalyticsBuilder(getContext(), mDetailColumn.subscribed ? "A0114" : "A0014", "SubColumn", false)
                .name(mDetailColumn.subscribed ? "订阅号取消订阅" : "订阅号订阅")
                .pageType("订阅号详情页")
                .columnID(String.valueOf(mDetailColumn.id))
                .seObjectType(ObjectType.C90)
                .columnName(mDetailColumn.name)
                .operationType(mDetailColumn.subscribed ? "取消订阅" : "订阅")
                .build()
                .send();
        mDetailColumn.subscribed = !mDetailColumn.subscribed;
        mPresenter.submitSubscribe(mDetailColumn);
    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        Intent intent = new Intent(Constants.Action.SUBSCRIBE_SUCCESS);
        intent.putExtra(Constants.Name.SUBSCRIBE, bean.subscribed);
        intent.putExtra(Constants.Name.ID, bean.id);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void subscribeFail(ColumnResponse.DataBean.ColumnBean bean, String message) {
        modifySubscribeBtnState(mDetailColumn.subscribed);
        ZBToast.showByType(getContext(), "操作失败!", ZBToast.TYPE_1);
    }

    private void modifySubscribeBtnState(boolean subscribe) {
        mDetailColumn.subscribed = subscribe;
        mSubscribeContainer.setSelected(subscribe);
        int id = mDetailColumn.subscribed ? R.drawable.zjnews_detail_navigation_subscribed_icon : R.drawable.zjnews_detail_navigation_subscribe_icon;
        mToolbarSub.setImageResource(id);


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
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mSubscribeStateReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mRankStateReceiver);
    }


    @OnClick({R2.id.detail_share, R2.id.toolbar_detail_share})
    public void onViewClicked(View view) {

        if (mDetailColumn != null) {
            String shareName = StringUtils.isEmpty(mDetailColumn.name) ? "浙江新闻" : mDetailColumn.name;
            String shareDes = StringUtils.isEmpty(mDetailColumn.description) ? "下载浙江新闻，查看更多身边新闻" : mDetailColumn.description;
            String shareUrl = StringUtils.isEmpty(mDetailColumn.share_url) ? "https://zj.zjol.com.cn/" : mDetailColumn.share_url;
            //        //分享专用bean
            //TODO 分享ObjectType
            OutSizeAnalyticsBean analyticsBean = OutSizeAnalyticsBean.getInstance()
                    .setObjectType(ObjectType.C90)
                    .setPageType("订阅号详情页")
                    .setSeName("点击分享栏目卡片")
                    .setColumn_id(String.valueOf(mDetailColumn.id))
                    .setColumn_name(mDetailColumn.name);

            UmengShareBean shareBean = UmengShareBean.getInstance()
                    .setSingle(false)
                    .setTitle(shareName)
                    .setEventName("PageShare")
                    .setTextContent(shareDes).setTargetUrl(shareUrl)
                    .setAnalyticsBean(analyticsBean)
                    .setShareContentID(mDetailColumn.id + "")
                    .setShareType("栏目")
                    .setNewsCard(false)
                    .setCardUrl(mDetailColumn.card_url);

            if (!StringUtils.isEmpty(mDetailColumn.share_url)) {
                shareBean.setImgUri(mDetailColumn.pic_url);
            } else {
                shareBean.setPicId(R.mipmap.ic_launcher);
            }
            UmengShareUtils.getInstance().startShare(shareBean);
        }


        //点击分享操作
        new Analytics.AnalyticsBuilder(getContext(), "800018", "AppTabClick", false)
                .name("点击分享")
                .pageType("订阅号详情页")
                .clickTabName("分享")
                .build()
                .send();
    }

    public void syncRankState(Context context, long column_id, int score) {
        Intent intent = new Intent("hit_rank_success");
        intent.putExtra("id", column_id);
        intent.putExtra("score", score);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 同步拉票状态
     *
     * @param bean
     */
    private void rankState(DetailResponse.DataBean.DetailBean bean) {
        mActionView.setText(bean.rank_hited ? "拉票" : "打榜");
        mHitCountView.setText(getHitRankCount(bean));
    }


    /**
     * 打榜回调接口
     */
    private class PromoteCallback implements ApiCallback<PromoteResponse> {
        @Override
        public void onCancel() {

        }

        @Override
        public void onError(String errMsg, int errCode) {
            if (errCode == 53003) {
                ZBToast.showShort(getContext(), errMsg);
            } else {
                ZBToast.showShort(getContext(), "打榜失败");
            }
        }

        @Override
        public void onSuccess(PromoteResponse data) {
            ZBToast.showShort(getContext(), data.toast);
            final DetailResponse.DataBean.DetailBean bean = mDetailColumn;
            makeAnimation();
            mDetailColumn.subscribed = true;
            subscribeSuc(mDetailColumn);
            syncRankState(getContext(), mDetailColumn.id, data.delta_count);
        }
    }

    /**
     * 打榜状态同步
     */
    private class RankStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (Constants.Action.HIT_RANK_SUCCESS.equals(intent.getAction())) {
                long id = intent.getLongExtra(Constants.Name.ID, 0);
                int score = intent.getIntExtra(Constants.Name.SCORE, 0);
                if (id == mDetailColumn.id) {
                    DetailResponse.DataBean.DetailBean bean = mDetailColumn;
                    bean.rank_hited = true;
                    bean.hit_rank_count += score;
                    rankState(bean);
                }
            }
        }
    }


    /**
     * 订阅状态同步
     */
    private class SubscribeStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.Action.SUBSCRIBE_SUCCESS.equals(intent.getAction())) {
                long id = intent.getLongExtra(Constants.Name.ID, 0);
                boolean subscribe = intent.getBooleanExtra(Constants.Name.SUBSCRIBE, false);

                if (String.valueOf(id).equals(mUid)) {
                    mSubscribeContainer.setSelected(subscribe);
                    int mId = mDetailColumn.subscribed ? R.drawable.zjnews_detail_navigation_subscribed_icon : R.drawable.zjnews_detail_navigation_subscribe_icon;
                    mToolbarSub.setImageResource(mId);
                }
            }
        }
    }
}
