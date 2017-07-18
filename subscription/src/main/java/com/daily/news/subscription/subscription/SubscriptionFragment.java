package com.daily.news.subscription.subscription;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.mock.MockResponse;
import com.daily.news.subscription.subscription.my.MySubscriptionFragment;
import com.daily.news.subscription.subscription.recommend.RecommendFragment;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class SubscriptionFragment extends Fragment {

    @BindView(R2.id.subscription_progress_container)
    View mProgressContainer;

    public SubscriptionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendRequest();
    }

    private void sendRequest() {
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, ObservableSource<SubscriptionResponse>>() {
                    @Override
                    public ObservableSource<SubscriptionResponse> apply(@NonNull Long aLong) throws Exception {
                        return Observable.just(MockResponse.getInstance().getSubscriptionResponse());
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SubscriptionResponse>() {
                    @Override
                    public void accept(@NonNull SubscriptionResponse subscriptionBean) throws Exception {
                        mProgressContainer.setVisibility(View.GONE);
                        if (subscriptionBean.data.has_subscribe) {
                            FragmentManager fragmentManager = getChildFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.subscription_container, MySubscriptionFragment.newInstance("",""));
                            transaction.commit();
                        } else if (!subscriptionBean.data.has_subscribe) {
                            FragmentManager fragmentManager = getChildFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.subscription_container, RecommendFragment.newInstance(subscriptionBean.data));
                            transaction.commit();
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subscription, container, false);
        ButterKnife.bind(this, rootView);
        mProgressContainer.setVisibility(View.VISIBLE);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
