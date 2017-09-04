package com.daily.news.subscription.my;

import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.more.column.ColumnStore;

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

public class MyStore extends ColumnStore {
    @Override
    public Flowable<List<ColumnResponse.DataBean.ColumnBean>> getFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, Publisher<List<ColumnResponse.DataBean.ColumnBean>>>() {
                    @Override
                    public Publisher<List<ColumnResponse.DataBean.ColumnBean>> apply(@NonNull Long aLong) throws Exception {
                        Random random = new Random();
                        List<ColumnResponse.DataBean.ColumnBean> columns = new ArrayList<>();
                        for (int i = 0; i < 30; i++) {
                            ColumnResponse.DataBean.ColumnBean column = new ColumnResponse.DataBean.ColumnBean();
                            column.article_count = random.nextInt();
                            column.subscribe_count = random.nextInt();
                            column.name = "搜索数据" + random.nextInt();
                            column.subscribed = random.nextBoolean();
                            columns.add(column);
                        }

                        return Flowable.just(columns);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }
}
