package com.daily.news.subscription.home;

import com.daily.news.subscription.mock.MockResponse;
import com.daily.news.subscription.more.column.Column;

import org.reactivestreams.Publisher;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.umeng.qq.handler.a.s;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionStore implements SubscriptionContract.Store<SubscriptionResponse> {
    @Override
    public Flowable<SubscriptionResponse> getFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS).flatMap(new Function<Long, Publisher<SubscriptionResponse>>() {
            @Override
            public Publisher<SubscriptionResponse> apply(@NonNull Long aLong) throws Exception {
                return Flowable.just(MockResponse.getInstance().getSubscriptionResponse());
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<SubscriptionResponse> getRefreshFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS).flatMap(new Function<Long, Publisher<SubscriptionResponse>>() {
            @Override
            public Publisher<SubscriptionResponse> apply(@NonNull Long aLong) throws Exception {
                return Flowable.just(MockResponse.getInstance().getRefreshSubscriptionResponse());
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable getSubmitSubscribeFlowable(Column bean) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS).flatMap(new Function<Long, Publisher<?>>() {
            @Override
            public Publisher<?> apply(@NonNull Long aLong) throws Exception {
                Random random = new Random();
                if (random.nextBoolean()) {
                    return Flowable.just(s);
                } else {
                    return Flowable.error(new Throwable("提交失败"));
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
