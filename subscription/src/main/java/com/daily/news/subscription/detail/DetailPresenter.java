package com.daily.news.subscription.detail;

import com.daily.news.subscription.more.column.Column;
import com.daily.news.subscription.more.column.ColumnContract;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by lixinke on 2017/7/17.
 */

public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View mDetailView;
    private DetailContract.Store mDetailStore;
    private CompositeDisposable mDisposable;

    public DetailPresenter(DetailContract.View detailView, DetailContract.Store detailStore) {
        mDetailView = detailView;
        mDetailView.setPresenter(this);
        mDetailStore = detailStore;
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void setItemId(String itemId) {

    }

    @Override
    public void subscribe() {
        mDetailView.showProgressBar();
        Disposable disposable = mDetailStore.getFlowable("")
                .subscribe(new Consumer<DetailColumn>() {
                    @Override
                    public void accept(@NonNull DetailColumn columnsBeen) throws Exception {
                        mDetailView.updateValue(columnsBeen);
                        mDetailView.hideProgressBar();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mDetailView.hideProgressBar();
                        mDetailView.showError(throwable.getMessage());
                    }
                });
        mDisposable.add(disposable);
    }

    @Override
    public void unsubscribe() {
        mDisposable.clear();
    }
}