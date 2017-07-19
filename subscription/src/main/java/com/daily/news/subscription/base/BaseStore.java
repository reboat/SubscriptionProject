package com.daily.news.subscription.base;

import io.reactivex.Flowable;

public interface BaseStore<T> {
    Flowable<T> getFlowable(String url);
}
