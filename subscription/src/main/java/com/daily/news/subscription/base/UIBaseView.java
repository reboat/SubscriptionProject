package com.daily.news.subscription.base;

/**
 * Created by lixinke on 2017/7/19.
 */

public interface UIBaseView<T> extends BaseView<T> {
    void showProgressBar();

    void hideProgressBar();

    void showError(String message);
}
