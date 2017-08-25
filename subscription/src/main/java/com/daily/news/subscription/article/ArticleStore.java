package com.daily.news.subscription.article;

import com.daily.news.subscription.mock.MockResponse;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/17.
 */

public class ArticleStore implements ArticleContract.Store {

    @Override
    public Flowable<ArticleResponse> getFlowable(String url) {
        return Flowable.just(MockResponse.getInstance().getArticleResponse());
    }
}
