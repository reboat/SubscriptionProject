package com.daily.news.subscription.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.article.ArticlePresenter;
import com.daily.news.subscription.article.ArticleStore;
import com.daily.news.subscription.home.my.MySubscriptionFragment;
import com.daily.news.subscription.home.no.NoSubscriptionFragment;
import com.daily.news.subscription.more.column.ColumnPresenter;
import com.daily.news.subscription.more.column.LocalColumnStore;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SubscriptionFragment extends Fragment implements SubscriptionContract.View {

    @BindView(R2.id.subscription_progress_container)
    View mProgressContainer;
    SubscriptionContract.Presenter mPresenter;

    public SubscriptionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.subscribe();
    }

    @Override
    public void setPresenter(SubscriptionContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void hideProgressBar() {
        mProgressContainer.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        mProgressContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateValue(SubscriptionResponse subscriptionBean) {
        if (subscriptionBean.data.has_subscribe) {
            MySubscriptionFragment fragment = new MySubscriptionFragment();
            getChildFragmentManager().beginTransaction().add(R.id.subscription_container, fragment).commit();
            ArticlePresenter articlePresenter = new ArticlePresenter(fragment, new ArticleStore());
            articlePresenter.setArticles(subscriptionBean.data.article_list);

        } else if (!subscriptionBean.data.has_subscribe) {
            NoSubscriptionFragment fragment = NoSubscriptionFragment.newInstance(subscriptionBean.data);
            getChildFragmentManager().beginTransaction().add(R.id.subscription_container, fragment).commit();
            new ColumnPresenter(fragment, new LocalColumnStore(subscriptionBean.data.recommend_list));
        }
    }

    @Override
    public void showError(String message) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.subscription_title);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
