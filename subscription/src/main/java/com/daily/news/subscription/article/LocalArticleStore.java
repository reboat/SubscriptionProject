package com.daily.news.subscription.article;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/8/25.
 */

public class LocalArticleStore implements ArticleContract.Store {
    public List<ArticleResponse.DataBean.Article> mArticles;

    public LocalArticleStore(List<ArticleResponse.DataBean.Article> articles) {
        mArticles = articles;
    }

    @Override
    public Flowable<ArticleResponse> getFlowable(String url) {
        ArticleResponse response = new ArticleResponse();
        response.code = 200;
        response.data = new ArticleResponse.DataBean();
        response.data.elements = mArticles;
        return Flowable.just(response);
    }
}
