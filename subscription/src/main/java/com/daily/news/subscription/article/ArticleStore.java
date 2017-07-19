package com.daily.news.subscription.article;

import android.text.TextUtils;

import com.daily.news.subscription.Article;
import com.daily.news.subscription.detail.DetailColumn;
import com.daily.news.subscription.detail.DetailContract;
import com.daily.news.subscription.mock.MockResponse;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
