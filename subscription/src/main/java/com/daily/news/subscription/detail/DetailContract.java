package com.daily.news.subscription.detail;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;
import com.daily.news.subscription.subscribe.SubscribeContract;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface DetailContract {
    interface Presenter extends BasePresenter, SubscribeContract.Presenter {
    }

    interface View extends UIBaseView<Presenter>, SubscribeContract.View {
        void updateValue(DetailResponse.DataBean dataBean);
    }

    interface Store extends BaseStore, SubscribeContract.Store {
        APIBaseTask getTask(APICallBack<DetailResponse.DataBean> callBack);

        Flowable<DetailResponse.DataBean> getDetailResponse(Object... params);
    }
}
