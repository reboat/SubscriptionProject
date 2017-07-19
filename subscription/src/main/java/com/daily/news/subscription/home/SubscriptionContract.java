package com.daily.news.subscription.home;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.BaseView;
import com.daily.news.subscription.base.UIBaseView;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionContract {

    interface Presenter extends BasePresenter {
    }

    interface View extends UIBaseView<Presenter> {
        void updateValue(SubscriptionResponse subscriptionResponse);
    }

    interface Store<T> extends BaseStore<T> {
    }
}
