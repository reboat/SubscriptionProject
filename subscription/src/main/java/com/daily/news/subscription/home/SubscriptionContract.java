package com.daily.news.subscription.home;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;
import com.daily.news.subscription.subscribe.SubscribeContract;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionContract {

    interface Presenter extends BasePresenter,SubscribeContract.Presenter {
        void onRefresh();
    }

    interface View extends UIBaseView<Presenter> ,SubscribeContract.View{
        void updateValue(SubscriptionResponse subscriptionResponse);

        void onRefreshComplete(SubscriptionResponse subscriptionResponse);

        void onRefreshError(String message);

    }

    interface Store<T> extends BaseStore<T> ,SubscribeContract.Store{
        Flowable<T> getRefreshFlowable(String url);
    }
}
