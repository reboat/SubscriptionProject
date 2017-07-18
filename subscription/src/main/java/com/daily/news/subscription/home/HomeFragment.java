package com.daily.news.subscription.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.Article;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.home.my.SubscriptionFragment;
import com.daily.news.subscription.home.no.NoSubscriptionFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment implements SubscriptionContract.View {

    @BindView(R2.id.subscription_progress_container)
    View mProgressContainer;
    SubscriptionContract.Presenter mPresenter;

    public HomeFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_subscription, container, false);
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
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.subscription_container, SubscriptionFragment.newInstance((ArrayList<Article>) subscriptionBean.data.article_list));
            transaction.commit();
        } else if (!subscriptionBean.data.has_subscribe) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.subscription_container, NoSubscriptionFragment.newInstance(subscriptionBean.data));
            transaction.commit();
        }
    }

    @Override
    public void showError(String message) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("订阅");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
