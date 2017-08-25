package com.daily.news.subscription.more.column;

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

public class ColumnStore implements ColumnContract.Store<List<Column>> {
    @Override
    public Flowable<List<Column>> getFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, Publisher<List<Column>>>() {
                    @Override
                    public Publisher<List<Column>> apply(@NonNull Long aLong) throws Exception {
                        Random random = new Random();
                        List<Column> columns = new ArrayList<>();
                        for (int i = 0; i < 30; i++) {
                            Column column = new Column();
                            column.article_count = random.nextInt(1000);
                            column.subscribe_count = random.nextInt(1000);
                            column.name = "文化中国" + random.nextInt(1000);
                            column.subscribed = random.nextBoolean();
                            columns.add(column);
                        }

                        return Flowable.just(columns);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
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
