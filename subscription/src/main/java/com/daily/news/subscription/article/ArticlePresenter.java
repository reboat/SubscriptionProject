package com.daily.news.subscription.article;


import com.zjrb.core.api.callback.LoadingCallBack;

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
    public void subscribe(Object... params) {
        mArticleView.showProgressBar();
        Disposable disposable = mArticleStore.getFlowable("")
                .subscribe(new Consumer<ArticleResponse>() {
                    @Override
                    public void accept(@NonNull ArticleResponse articles) throws Exception {
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
    public void loadMore(long sortNum,int size,LoadingCallBack<ArticleResponse.DataBean> callback) {
        mArticleStore.getLoadMoreTask(callback).exe(sortNum,size);
    }

    @Override
    public void refreshData(List<ArticleResponse.DataBean.Article> article_list) {
        ArticleResponse response=new ArticleResponse();
        response.data=new ArticleResponse.DataBean();
        response.data.elements=article_list;
        mArticleView.updateValue(response);
    }

    @Override
    public void unsubscribe() {
        mDisposable.clear();
    }
}
