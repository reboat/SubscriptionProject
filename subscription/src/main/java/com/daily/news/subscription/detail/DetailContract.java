package com.daily.news.subscription.detail;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.BaseView;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface DetailContract {
    interface Presenter extends BasePresenter {
        void setItemId(String itemId);
    }

    interface View extends BaseView<Presenter> {

        void showProgressBar();

        void updateValue(DetailColumn detailColumn);

        void hideProgressBar();

        void showError(String message);
    }

    interface Store<T> extends BaseStore<T> {

    }
}
