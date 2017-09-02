package com.daily.news.subscription.more.column;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;
import com.daily.news.subscription.subscribe.SubscribeContract;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface ColumnContract {
    interface Presenter extends BasePresenter ,SubscribeContract.Presenter{
        void setItemId(String itemId);

        void submitSubscribe(Column bean);
    }

    interface View extends UIBaseView<Presenter> ,SubscribeContract.View{
        void updateValue(ColumnResponse.DataBean columnsBeen);
    }

    interface Store<T> extends BaseStore<T>,SubscribeContract.Store {
        APIBaseTask getTask(APICallBack<ColumnResponse.DataBean> apiCallBack);
    }
}
