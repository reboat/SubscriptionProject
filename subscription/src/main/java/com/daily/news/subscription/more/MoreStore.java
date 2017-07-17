package com.daily.news.subscription.more;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixinke on 2017/7/17.
 */

public class MoreStore implements MoreContract.Store<List<CategoryContent.CategoryItem>> {
    private static String MORE_URL = "";

    @Override
    public Flowable<List<CategoryContent.CategoryItem>> getFlowable(String url) {
        return Flowable.timer(400, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, Publisher<List<CategoryContent.CategoryItem>>>() {
                    @Override
                    public Publisher<List<CategoryContent.CategoryItem>> apply(@NonNull Long aLong) throws Exception {

                        List<CategoryContent.CategoryItem> items = new ArrayList<>();
                        for (int i = 0; i < 20; i++) {
                            items.add(new CategoryContent.CategoryItem("" + i, "内容" + i, ""));
                        }
                        return Flowable.just(items);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    @Override
    public String getUrl() {
        return MORE_URL;
    }
}
