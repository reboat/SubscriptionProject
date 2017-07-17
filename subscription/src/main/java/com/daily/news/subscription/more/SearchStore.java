package com.daily.news.subscription.more;

import com.daily.news.subscription.more.detail.DetailContract;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixinke on 2017/7/17.
 */

public class SearchStore implements DetailContract.Store {
    @Override
    public Flowable getFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, Publisher<CategoryContent.CategoryItem>>() {
                    @Override
                    public Publisher<CategoryContent.CategoryItem> apply(@NonNull Long aLong) throws Exception {
                        CategoryContent.CategoryItem item = new CategoryContent.CategoryItem("", "Search content", "Search Detail");
                        return Flowable.just(item);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }
}
