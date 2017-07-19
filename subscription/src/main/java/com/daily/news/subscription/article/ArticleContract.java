package com.daily.news.subscription.article;

import com.daily.news.subscription.Article;
import com.daily.news.subscription.BasePresenter;
import com.daily.news.subscription.BaseStore;
import com.daily.news.subscription.BaseView;
import com.daily.news.subscription.detail.DetailColumn;

import java.util.List;

/**
 * Created by lixinke on 2017/7/17.
 */

public interface ArticleContract {
    interface Presenter extends BasePresenter {
        void setItemId(String itemId);
    }

    interface View extends BaseView<Presenter> {

        void showProgressBar();

        void updateValue(List<Article> articles);

        void hideProgressBar();

        void showError(String message);
    }

    interface Store<T> extends BaseStore<T> {

        void setArticles(List<Article> articles);
    }
}
