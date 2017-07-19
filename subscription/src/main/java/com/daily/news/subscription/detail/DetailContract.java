package com.daily.news.subscription.detail;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.BaseView;
import com.daily.news.subscription.base.UIBaseView;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface DetailContract {
    interface Presenter extends BasePresenter {
        void setItemId(String itemId);
    }

    interface View extends UIBaseView<Presenter> {
        void updateValue(DetailColumn detailColumn);
    }

    interface Store<T> extends BaseStore<T> {
    }
}
