package com.daily.news.subscription.subscribe;

import com.daily.news.subscription.more.column.ColumnResponse;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/8/25.
 */

public interface SubscribeContract {
    interface Presenter {
        void submitSubscribe(ColumnResponse.DataBean.ColumnBean column);
        void unsubscribe();
    }

    interface View {
        void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean);

        void subscribeFail(ColumnResponse.DataBean.ColumnBean bean, String message);
    }

    interface Store {
        Flowable<ColumnResponse.DataBean.ColumnBean> getSubmitSubscribeFlowable(ColumnResponse.DataBean.ColumnBean column);

        APIBaseTask getSubmitSubscribeTask(APICallBack<ColumnResponse.DataBean.ColumnBean> apiCallBack);
    }
}
