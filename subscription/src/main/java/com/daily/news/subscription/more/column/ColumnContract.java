package com.daily.news.subscription.more.column;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.BaseView;

import java.util.List;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface ColumnContract {
    interface Presenter extends BasePresenter {
        void setItemId(String itemId);
    }

    interface View extends BaseView<Presenter> {

        void showProgressBar();

        void updateValue(List<Column> columnsBeen);

        void hideProgressBar();

        void showError(String message);
    }

    interface Store<T> extends BaseStore<T> {

    }
}
