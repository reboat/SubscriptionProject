package com.daily.news.subscription.article;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.UIBaseView;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface ArticleContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends UIBaseView<Presenter> {
        void updateValue(ArticleResponse response);
    }

    interface Store extends BaseStore<ArticleResponse> {
    }
}
