package com.daily.news.subscription.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.article.ArticleFragment;
import com.daily.news.subscription.article.ArticlePresenter;
import com.daily.news.subscription.article.ArticleStore;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment implements DetailContract.View {
    private static final String UID = "uid";
    private String mUid;
    private DetailContract.Presenter mPresenter;

    @BindView(R2.id.detail_column_imageView)
    ImageView mImageView;
    @BindView(R2.id.detail_column_title)
    TextView mTitleView;
    @BindView(R2.id.detail_column_info)
    TextView mInfoView;
    @BindView(R2.id.detail_column_description)
    TextView mDescritpionView;
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
        View rootView = inflater.inflate(R.layout.fragment_detail_column, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.subscribe();
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
    public void updateValue(DetailColumn detailColumn) {
        Glide.with(this).load(detailColumn.pic_url).into(mImageView);
        mTitleView.setText(detailColumn.name);
        mInfoView.setText(String.format(Locale.getDefault(), "%d万订阅 %d篇稿件", detailColumn.subscribe_count, detailColumn.article_count));
        mDescritpionView.setText(detailColumn.description);
        String subscriptionText=detailColumn.subscribed?"已经订阅":"订阅";
        mSubscriptionView.setText(subscriptionText);
        mSubscriptionView.setSelected(detailColumn.subscribed);
        Glide.with(this).load(detailColumn.background_url).into(mHeaderImageView);

        ArticleFragment fragment =new ArticleFragment();
        getChildFragmentManager().beginTransaction().add(R.id.detail_article_container, fragment).commit();
        ArticlePresenter presenter=new ArticlePresenter(fragment,new ArticleStore());
        presenter.setArticles(detailColumn.elements);
    }

    @Override
    public void hideProgressBar() {
        mProgressBarContainer.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        mProgressBarContainer.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mTipView.setText(message);
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
