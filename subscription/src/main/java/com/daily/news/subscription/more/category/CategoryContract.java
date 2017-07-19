package com.daily.news.subscription.more.category;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.BaseView;

import java.util.List;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface CategoryContract {
    interface View extends BaseView<Presenter> {

        void updateValues(List<Category> items);

        void showError(String message);

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter extends BasePresenter {

    }

    interface Store<T> extends BaseStore<T> {
       String getUrl();
    }
}
