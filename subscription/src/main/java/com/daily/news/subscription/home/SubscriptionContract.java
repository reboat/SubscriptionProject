package com.daily.news.subscription.home;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionContract {

    interface Presenter extends BasePresenter {
        void onRefresh(Object... params);
    }

    interface View extends UIBaseView<Presenter> {
        void updateValue(SubscriptionResponse.DataBean subscriptionResponse);

        void onRefreshComplete(SubscriptionResponse.DataBean dataBean);

        void onRefreshError(String message);

    }

    interface Store<T> extends BaseStore<T> {
        Flowable<T> getRefreshFlowable(String url);

        APIBaseTask getTask(APICallBack<SubscriptionResponse.DataBean> apiCallBack);
    }
}
