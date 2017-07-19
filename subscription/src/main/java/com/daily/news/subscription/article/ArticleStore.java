package com.daily.news.subscription.article;

import android.text.TextUtils;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/17.
 */

public class ArticleStore implements ArticleContract.Store<List<Article>> {
    private List<Article> mArticles;

    @Override
    public Flowable<List<Article>> getFlowable(String url) {
        if (TextUtils.isEmpty(url)) {
            return Flowable.just(mArticles);
        }
        return null;
    }

    @Override
    public void setArticles(List<Article> articles) {
        mArticles = articles;
    }
}
