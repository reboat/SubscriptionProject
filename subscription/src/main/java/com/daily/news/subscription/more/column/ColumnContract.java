package com.daily.news.subscription.more.column;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface ColumnContract {
    interface Presenter extends BasePresenter {
        void setItemId(String itemId);

        void submitSubscribe(Column bean);
    }

    interface View extends UIBaseView<Presenter> {
        void updateValue(List<Column> columnsBeen);

        void subscribeSuc(Column bean);

        void subscribeFail(Column bean, String message);
    }

    interface Store<T> extends BaseStore<T> {
        Flowable getSubscribeFlowable(Column s);
    }
}
