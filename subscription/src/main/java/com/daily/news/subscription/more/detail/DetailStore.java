package com.daily.news.subscription.more.detail;

import com.daily.news.subscription.more.CategoryBean;

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

public class DetailStore implements DetailContract.Store<List<CategoryBean.DataBean.ElementsBean.ColumnsBean>> {
    @Override
    public Flowable<List<CategoryBean.DataBean.ElementsBean.ColumnsBean>> getFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, Publisher<List<CategoryBean.DataBean.ElementsBean.ColumnsBean>>>() {
                    @Override
                    public Publisher<List<CategoryBean.DataBean.ElementsBean.ColumnsBean>> apply(@NonNull Long aLong) throws Exception {
                        Random random = new Random();
                        List<CategoryBean.DataBean.ElementsBean.ColumnsBean> columns = new ArrayList<>();
                        for (int i = 0; i < 30; i++) {
                            CategoryBean.DataBean.ElementsBean.ColumnsBean columnsBean = new CategoryBean.DataBean.ElementsBean.ColumnsBean();
                            columnsBean.article_count = random.nextInt();
                            columnsBean.subscribe_count = random.nextInt();
                            columnsBean.name = "文化中国" + random.nextInt();
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
