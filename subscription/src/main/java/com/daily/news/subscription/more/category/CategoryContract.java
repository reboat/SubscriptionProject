package com.daily.news.subscription.more.category;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.BaseView;
import com.daily.news.subscription.base.UIBaseView;

import java.util.List;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface CategoryContract {
    interface View extends UIBaseView<Presenter> {
        void updateValues(List<Category> items);
    }

    interface Presenter extends BasePresenter {
    }

    interface Store<T> extends BaseStore<T> {
        String getUrl();
    }
}
