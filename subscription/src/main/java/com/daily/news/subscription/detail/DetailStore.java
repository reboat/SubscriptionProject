package com.daily.news.subscription.detail;

import com.daily.news.subscription.mock.MockResponse;
import com.daily.news.subscription.more.column.Column;
import com.daily.news.subscription.more.column.ColumnContract;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixinke on 2017/7/17.
 */

public class DetailStore implements DetailContract.Store<DetailColumn> {
    @Override
    public Flowable<DetailColumn> getFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, Publisher<DetailColumn>>() {
                    @Override
                    public Publisher<DetailColumn> apply(@NonNull Long aLong) throws Exception {
                        return Flowable.just(MockResponse.getInstance().getDetail(""));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }
}
