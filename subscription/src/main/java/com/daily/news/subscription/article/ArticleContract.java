package com.daily.news.subscription.article;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.LoadingCallBack;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface ArticleContract {
    interface Presenter extends BasePresenter {
        void loadMore(long sortNum,int size,LoadingCallBack<ArticleResponse.DataBean> callback);
    }

    interface View extends UIBaseView<Presenter> {
        void updateValue(ArticleResponse response);
    }

    interface Store extends BaseStore<ArticleResponse> {
        APIBaseTask<ArticleResponse.DataBean> getLoadMoreTask(LoadingCallBack callBack);
    }
}
