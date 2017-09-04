package com.daily.news.subscription.more.category;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface CategoryContract {
    interface View extends UIBaseView<Presenter> {
        void updateValues(CategoryResponse.DataBean items);
    }

    interface Presenter extends BasePresenter {
    }

    interface Store<T> extends BaseStore<T> {
        String getUrl();

        APIBaseTask<CategoryResponse.DataBean> getTask(APICallBack<CategoryResponse.DataBean> apiCallBack);
    }
}
