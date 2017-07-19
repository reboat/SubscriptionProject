package com.daily.news.subscription.article;

import com.daily.news.subscription.base.BasePresenter;
import com.daily.news.subscription.base.BaseStore;
import com.daily.news.subscription.base.BaseView;
import com.daily.news.subscription.base.UIBaseView;

import java.util.List;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface ArticleContract<T> {
    interface Presenter extends BasePresenter {
        void setItemId(String itemId);
    }

    interface View extends UIBaseView<Presenter> {
        void updateValue(List<Article> articles);
    }

    interface Store<T> extends BaseStore<T> {
        void setArticles(List<Article> articles);
    }
}
