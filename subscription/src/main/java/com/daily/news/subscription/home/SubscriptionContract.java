package com.daily.news.subscription.home;

import com.daily.news.subscription.BasePresenter;
import com.daily.news.subscription.BaseStore;
import com.daily.news.subscription.BaseView;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionContract {

    interface Presenter extends BasePresenter{

    }

    interface View extends BaseView<Presenter>{

        void hideProgressBar();

        void showProgressBar();

        void updateValue(SubscriptionResponse subscriptionResponse);

        void showError(String message);
    }
    interface Store<T> extends BaseStore<T>{

    }
}
