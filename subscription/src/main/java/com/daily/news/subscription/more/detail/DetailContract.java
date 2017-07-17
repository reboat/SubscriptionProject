package com.daily.news.subscription.more.detail;

import com.daily.news.subscription.BasePresenter;
import com.daily.news.subscription.BaseStore;
import com.daily.news.subscription.BaseView;
import com.daily.news.subscription.more.CategoryContent;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface DetailContract {
    interface Presenter extends BasePresenter{

    }
    interface View extends BaseView<Presenter>{

        void showProgressBar();

        void updateValue(CategoryContent.CategoryItem categoryItem);

        void hideProgressBar();

        void showError(String message);
    }
    interface Store<T> extends BaseStore<T>{

    }
}
