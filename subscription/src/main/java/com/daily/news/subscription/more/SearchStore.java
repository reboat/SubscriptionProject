package com.daily.news.subscription.more;

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

public class SearchStore implements ColumnContract.Store<List<SubscriptionColumn.DataBean.Category.ColumnsBean>> {
    @Override
    public Flowable<List<SubscriptionColumn.DataBean.Category.ColumnsBean>> getFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, Publisher<List<SubscriptionColumn.DataBean.Category.ColumnsBean>>>() {
                    @Override
                    public Publisher<List<SubscriptionColumn.DataBean.Category.ColumnsBean>> apply(@NonNull Long aLong) throws Exception {
                        Random random = new Random();
                        List<SubscriptionColumn.DataBean.Category.ColumnsBean> columns = new ArrayList<>();
                        for (int i = 0; i < 30; i++) {
                            SubscriptionColumn.DataBean.Category.ColumnsBean columnsBean = new SubscriptionColumn.DataBean.Category.ColumnsBean();
                            columnsBean.article_count = random.nextInt();
                            columnsBean.subscribe_count = random.nextInt();
                            columnsBean.name = "搜索数据" + random.nextInt();
                            columnsBean.subscribed = random.nextBoolean();
                            columns.add(columnsBean);
                        }

                        return Flowable.just(columns);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }
}
