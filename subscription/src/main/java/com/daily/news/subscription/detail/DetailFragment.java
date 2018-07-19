package com.daily.news.subscription.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.article.ArticleFragment;
import com.daily.news.subscription.article.ArticlePresenter;
import com.daily.news.subscription.constants.Constants;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;

public class DetailFragment extends Fragment implements DetailContract.View, HeaderRefresh.OnRefreshListener {
    private static final String UID = "id";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int CODE_ALREADY_OFF_THE_SHELF = 50604;
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
    @BindView(R2.id.detail_content_container)
    View mContentContainer;
    @BindView(R2.id.detail_empty_error_container)
    View mEmptyErrorContainer;

    private DetailResponse.DataBean.DetailBean mDetailColumn;
    private ArticleFragment mArticleFragment;
    private ArticlePresenter mArticlePresenter;


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

            String info = TextUtils.isEmpty(data.detail.subscribe_count_general) ? "" : data.detail.subscribe_count_general + "订阅  ";
            info += TextUtils.isEmpty(data.detail.article_count_general) ? "" : data.detail.article_count_general + "份稿件";
            mInfoView.setText(info);
            mDescriptionView.setText(data.detail.description);
            String subscriptionText = data.detail.subscribed ? "已订阅" : "订阅";
            mSubscriptionView.setText(subscriptionText);
            mSubscribeContainer.setSelected(data.detail.subscribed);

            options.placeholder(R.drawable.detail_column_default);
            Glide.with(this).load(data.detail.background_url).apply(options).into(mHeaderImageView);
            if (mArticlePresenter != null) {
                mArticlePresenter.refreshData(data.elements);
            } else {
                mArticlePresenter = new ArticlePresenter(mArticleFragment, new DetailArticleStore(mUid, data.elements));
                mArticlePresenter.refreshData(data.elements);
            }
        } else if (response.code == CODE_ALREADY_OFF_THE_SHELF) {
            mContentContainer.setVisibility(View.GONE);
            mEmptyErrorContainer.setVisibility(View.VISIBLE);
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
        return new LoadViewHolder(mContentContainer, (ViewGroup) mContentContainer.getParent());
    }

    @OnClick(R2.id.subscribe_container)
    public void submitSubscribe() {
        mPresenter.submitSubscribe(mDetailColumn);
        modifySubscribeBtnState(!mDetailColumn.subscribed);

        if (mDetailColumn.subscribed) {
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new Analytics.AnalyticsBuilder(getContext(), "A0114", "A0114","subColumn", false)
                    .setObjectID(String.valueOf(mDetailColumn.id))
                    .setObjectName(mDetailColumn.name)
                    .setEvenName("“取消订阅”栏目")
                    .setPageType("栏目详情页")
                    .setOtherInfo(otherInfoStr)
                    .pageType("栏目详情页")
                    .columnID(String.valueOf(mDetailColumn.id))
                    .columnName(mDetailColumn.name)
                    .operationType("取消订阅")
                    .build()
                    .send();
        }
    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        Intent intent = new Intent(Constants.Action.SUBSCRIBE_SUCCESS);
        intent.putExtra(Constants.Name.SUBSCRIBE, bean.subscribed);
        intent.putExtra(Constants.Name.ID, bean.id);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        getActivity().setResult(Activity.RESULT_OK,intent);

        if (bean.subscribed) {
            Map<String, String> otherInfo = new HashMap<>();
            otherInfo.put("customObjectType", "RelatedColumnType");
            String otherInfoStr = JsonUtils.toJsonString(otherInfo);
            new Analytics.AnalyticsBuilder(getContext(), "A0014", "A0014","subColumn", false)
                    .setObjectID(String.valueOf(bean.id))
                    .setObjectName(bean.name)
                    .setPageType("栏目详情页")
                    .setEvenName("“订阅”栏目")
                    .setOtherInfo(otherInfoStr)
                    .pageType("栏目详情页")
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
    }

    @OnClick({R2.id.detail_empty_back, R2.id.detail_back})
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

}
