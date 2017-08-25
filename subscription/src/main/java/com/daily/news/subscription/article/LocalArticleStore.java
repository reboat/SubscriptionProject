package com.daily.news.subscription.article;

import com.daily.news.subscription.mock.MockResponse;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

    @Override
    public Flowable<ArticleResponse> loadMoreFlowable(long sort_number, int pageSize) {
        return Flowable.timer(500, TimeUnit.MILLISECONDS).flatMap(new Function<Long, Publisher<ArticleResponse>>() {
            @Override
            public Publisher<ArticleResponse> apply(@NonNull Long aLong) throws Exception {
                ArticleResponse response = MockResponse.getInstance().getArticleResponse();
                if (new Random().nextBoolean()) {
                    response.data.elements.clear();
                }
                return Flowable.just(response);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
