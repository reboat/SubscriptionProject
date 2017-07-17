package com.daily.news.subscription;

import io.reactivex.Flowable;

public interface BaseStore<T> {
    Flowable<T> getFlowable(String url);
}
