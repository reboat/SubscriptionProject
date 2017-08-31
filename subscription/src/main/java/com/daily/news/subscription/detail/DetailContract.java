package com.daily.news.subscription.detail;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;
import com.daily.news.subscription.subscribe.SubscribeContract;
import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.APICallBack;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface DetailContract {
    interface Presenter extends BasePresenter, SubscribeContract.Presenter {
    }

    interface View extends UIBaseView<Presenter>, SubscribeContract.View {
        void updateValue(DetailColumn detailColumn);
    }

    interface Store extends BaseStore<DetailColumn>, SubscribeContract.Store {
        APIPostTask getTask(APICallBack<DetailColumn> callBack);
    }
}
