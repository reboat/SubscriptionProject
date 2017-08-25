package com.daily.news.subscription.home;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;
import com.daily.news.subscription.more.column.Column;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionContract {

    interface Presenter extends BasePresenter {
        void onRefresh();

        void submitSubscribe(Column bean);
    }

    interface View extends UIBaseView<Presenter> {
        void updateValue(SubscriptionResponse subscriptionResponse);

        void onRefreshComplete(SubscriptionResponse subscriptionResponse);

        void onRefreshError(String message);

        void subscribeSuc(Column bean);

        void subscribeFail(Column bean, String message);
    }

    interface Store<T> extends BaseStore<T> {
        Flowable<T> getRefreshFlowable(String url);

        Flowable getSubmitSubscribeFlowable(Column bean);
    }
}
