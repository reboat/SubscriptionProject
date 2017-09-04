package com.daily.news.subscription.more.column;

import com.daily.news.subscription.subscribe.SubscribeStore;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;

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

public class ColumnStore extends SubscribeStore implements ColumnContract.Store<List<ColumnResponse.DataBean.ElementsBean>> {
    @Override
    public Flowable<List<ColumnResponse.DataBean.ElementsBean>> getFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, Publisher<List<ColumnResponse.DataBean.ElementsBean>>>() {
                    @Override
                    public Publisher<List<ColumnResponse.DataBean.ElementsBean>> apply(@NonNull Long aLong) throws Exception {
                        Random random = new Random();
                        List<ColumnResponse.DataBean.ElementsBean> columns = new ArrayList<>();
                        for (int i = 0; i < 30; i++) {
                            ColumnResponse.DataBean.ElementsBean column = new ColumnResponse.DataBean.ElementsBean();
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
    public APIBaseTask getTask(APICallBack<ColumnResponse.DataBean> apiCallBack) {
        return null;
    }
}
