package com.daily.news.subscription.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailFragment extends Fragment implements DetailContract.View {
    private static final String UID = "id";
    private static final int DEFAULT_PAGE_SIZE = 10;
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
    @BindView(R2.id.detail_column_progressBar_container)
    View mProgressBarContainer;
    @BindView(R2.id.detail_column_progressBar)
    ProgressBar mProgressBar;
    @BindView(R2.id.detail_column_tip_view)
    TextView mTipView;
    @BindView(R2.id.detail_column_header_imageView)
    ImageView mHeaderImageView;
    @BindView(R2.id.detail_content_container)
    View mContentContainer;
    @BindView(R2.id.detail_empty_error_container)
    View mEmptyErrorContainer;

    private DetailResponse.DataBean.DetailBean mDetailColumn;


    public DetailFragment() {
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
        mProgressBarContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTipView.setText("加载中...");
    }

    @Override
    public void updateValue(DetailResponse.DataBean data) {
        mDetailColumn = data.detail;
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.column_placeholder_big);
        Glide.with(this).load(data.detail.pic_url).apply(options).into(mImageView);
        mTitleView.setText(data.detail.name);
        mInfoView.setText(String.format(Locale.getDefault(), "%d万订阅 %d篇稿件", data.detail.subscribe_count, data.detail.article_count));
        mDescriptionView.setText(data.detail.description);
        String subscriptionText = data.detail.subscribed ? "已经订阅" : "订阅";
        mSubscriptionView.setText(subscriptionText);
        mSubscriptionView.setSelected(data.detail.subscribed);

        options.placeholder(R.drawable.detail_column_default);
        Glide.with(this).load(data.detail.background_url).apply(options).into(mHeaderImageView);

        ArticleFragment fragment = new ArticleFragment();
        getChildFragmentManager().beginTransaction().add(R.id.detail_article_container, fragment).commit();
        new ArticlePresenter(fragment, new DetailArticleStore(mUid, data.elements));
    }

    @Override
    public void hideProgressBar() {
        mProgressBarContainer.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable message) {
        mProgressBarContainer.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        if (message instanceof RxException) {
            RxException exception = (RxException) message;
            // TODO 栏目不存在显示页面 errCode 未确定
            if (exception.errCode == 111) {
                mContentContainer.setVisibility(View.GONE);
                mEmptyErrorContainer.setVisibility(View.VISIBLE);
            }
        } else {
            mTipView.setText(message.getMessage());
        }
    }

    @OnClick(R2.id.detail_column_sub_btn)
    public void submitSubscribe() {
        modifySubscribeBtnState(!mDetailColumn.subscribed);
        mPresenter.submitSubscribe(mDetailColumn);
    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {
        modifySubscribeBtnState(bean.subscribed);
        Intent intent = new Intent(Constants.Action.SUBSCRIBE_SUCCESS);
        intent.putExtra(Constants.Name.SUBSCRIBE, bean.subscribed);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void subscribeFail(ColumnResponse.DataBean.ColumnBean bean, String message) {
        modifySubscribeBtnState(!bean.subscribed);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void modifySubscribeBtnState(boolean subscribe) {
        mDetailColumn.subscribed = subscribe;
        mSubscriptionView.setSelected(mDetailColumn.subscribed);
        String subscriptionText = mDetailColumn.subscribed ? "已经订阅" : "订阅";
        mSubscriptionView.setText(subscriptionText);
    }

    @OnClick(R2.id.detail_empty_back)
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
