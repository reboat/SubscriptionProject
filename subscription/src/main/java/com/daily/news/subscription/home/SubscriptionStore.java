package com.daily.news.subscription.home;

import com.daily.news.subscription.mock.MockResponse;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionStore implements SubscriptionContract.Store<SubscriptionResponse> {
    @Override
    public Flowable<SubscriptionResponse> getFlowable(String url) {
        return Flowable.timer(300, TimeUnit.MILLISECONDS).flatMap(new Function<Long, Publisher<SubscriptionResponse>>() {
            @Override
            public Publisher<SubscriptionResponse> apply(@NonNull Long aLong) throws Exception {
                return Flowable.just(MockResponse.getInstance().getSubscriptionResponse());
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
