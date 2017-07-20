package com.daily.news.subscription.article;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by lixinke on 2017/7/17.
 */

public class ArticlePresenter implements ArticleContract.Presenter {
    private ArticleContract.View mArticleView;
    private ArticleContract.Store mArticleStore;
    private CompositeDisposable mDisposable;

    public ArticlePresenter(ArticleContract.View articleView, ArticleContract.Store articleStore) {
        mArticleView = articleView;
        mArticleView.setPresenter(this);
        mArticleStore = articleStore;
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void setItemId(String itemId) {

    }

    public void setArticles(List<Article> articles){
        mArticleStore.setArticles(articles);
    }

    @Override
    public void subscribe() {
        mArticleView.showProgressBar();
        Disposable disposable = mArticleStore.getFlowable("")
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(@NonNull List<Article> articles) throws Exception {
                        mArticleView.updateValue(articles);
                        mArticleView.hideProgressBar();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mArticleView.hideProgressBar();
                        mArticleView.showError(throwable.getMessage());
                    }
                });
        mDisposable.add(disposable);
    }

    @Override
    public void unsubscribe() {
        mDisposable.clear();
    }
}