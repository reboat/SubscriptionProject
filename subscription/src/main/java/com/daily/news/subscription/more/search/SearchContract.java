package com.daily.news.subscription.more.search;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.daily.news.subscription.subscribe.SubscribeContract;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;

/**
 * Created by gaoyangzhen on 2018/3/14.
 */

public interface SearchContract {
    interface Presenter extends BasePresenter,SubscribeContract.Presenter{
        void setItemId(String itemId);

        void submitSubscribe(ColumnResponse.DataBean.ColumnBean bean);
    }

    interface View extends UIBaseView<SearchContract.Presenter>,SubscribeContract.View{
        void updateValue(SearchResponse.DataBean columnsBeen);
    }

    interface Store<T> extends BaseStore<T>,SubscribeContract.Store {
        APIBaseTask getTask(APICallBack<SearchResponse.DataBean> apiCallBack);
    }
}

