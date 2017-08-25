package com.daily.news.subscription.more.column;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixinke on 2017/7/21.
 */

public class LocalColumnStore implements ColumnContract.Store {
    private List<Column> mColumns;

    public LocalColumnStore(List<Column> columns) {
        mColumns = columns;
    }

    @Override
    public Flowable getFlowable(String url) {
        return Flowable.just(mColumns);
    }

    @Override
    public Flowable getSubmitSubscribeFlowable(final Column s) {
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
