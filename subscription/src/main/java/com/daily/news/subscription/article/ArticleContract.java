package com.daily.news.subscription.article;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface ArticleContract {
    interface Presenter extends BasePresenter {
        void loadMore(long sort_number, int pageSize);
    }

    interface View extends UIBaseView<Presenter> {
        void updateValue(ArticleResponse response);

        void loadMoreComplete(ArticleResponse response);

        void loadMoreError(String message);
    }

    interface Store extends BaseStore<ArticleResponse> {
        Flowable<ArticleResponse> loadMoreFlowable(long sort_number, int pageSize);
    }
}
