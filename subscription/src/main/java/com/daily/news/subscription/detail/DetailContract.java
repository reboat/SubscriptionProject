package com.daily.news.subscription.detail;

import com.daily.news.subscription.BasePresenter;
import com.daily.news.subscription.BaseStore;
import com.daily.news.subscription.BaseView;
import com.daily.news.subscription.more.column.Column;

import java.util.List;

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
