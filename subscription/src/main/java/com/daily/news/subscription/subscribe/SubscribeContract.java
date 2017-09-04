package com.daily.news.subscription.subscribe;

import com.daily.news.subscription.more.column.Column;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/8/25.
 */

public interface SubscribeContract {
    interface Presenter {
        void submitSubscribe(ColumnResponse.DataBean.ElementsBean column);
    }

    interface View {
        void subscribeSuc(ColumnResponse.DataBean.ElementsBean bean);

        void subscribeFail(ColumnResponse.DataBean.ElementsBean bean, String message);
    }

    interface Store {
        Flowable<Column> getSubmitSubscribeFlowable(ColumnResponse.DataBean.ElementsBean column);

        APIBaseTask getSubmitSubscribeTask(APICallBack<String> apiCallBack);
    }
}
