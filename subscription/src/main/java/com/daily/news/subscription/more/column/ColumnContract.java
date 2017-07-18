package com.daily.news.subscription.more.column;

import com.daily.news.subscription.BasePresenter;
import com.daily.news.subscription.BaseStore;
import com.daily.news.subscription.BaseView;
import com.daily.news.subscription.more.SubscriptionColumn;

import java.util.List;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface ColumnContract {
    interface Presenter extends BasePresenter {
        void setItemId(String itemId);
    }

    interface View extends BaseView<Presenter> {

        void showProgressBar();

        void updateValue(List<SubscriptionColumn.DataBean.ElementsBean.ColumnsBean> columnsBeen);

        void hideProgressBar();

        void showError(String message);
    }

    interface Store<T> extends BaseStore<T> {

    }
}
