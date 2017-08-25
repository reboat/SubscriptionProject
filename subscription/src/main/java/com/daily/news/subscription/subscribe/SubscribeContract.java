package com.daily.news.subscription.subscribe;

import com.daily.news.subscription.more.column.Column;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/8/25.
 */

public interface SubscribeContract {
    interface Presenter {
        void submitSubscribe(Column column);
    }

    interface View {
        void subscribeSuc(Column bean);

        void subscribeFail(Column bean, String message);
    }

    interface Store {
        Flowable<Column> getSubmitSubscribeFlowable(Column column);
    }
}
