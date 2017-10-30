package com.daily.news.subscription.detail;

import com.daily.news.subscription.subscribe.SubscribePresenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by lixinke on 2017/7/17.
 */

public class DetailPresenter extends SubscribePresenter implements DetailContract.Presenter {
    private DetailContract.View mDetailView;
    private DetailContract.Store mDetailStore;
    private CompositeDisposable mDisposable;

    public DetailPresenter(DetailContract.View detailView, DetailContract.Store detailStore) {
        super(detailView, detailStore);
        mDetailView = detailView;
        mDetailView.setPresenter(this);
        mDetailStore = detailStore;
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe(Object... params) {
        mDetailView.showProgressBar();
        Disposable disposable = mDetailStore.getDetailResponse(mDetailView.getProgressBar(), params).subscribe(new Consumer<DetailResponse>() {
            @Override
            public void accept(DetailResponse response) throws Exception {
                mDetailView.updateValue(response);
                mDetailView.hideProgressBar();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mDetailView.showError(throwable);
                mDetailView.hideProgressBar();
            }
        });

        mDisposable.add(disposable);
    }

    @Override
    public void unsubscribe() {
        mDisposable.clear();
    }

    @Override
    public void onRefresh(String uid) {
        mDetailStore.getDetailResponse(null,uid).subscribe(new Consumer<DetailResponse>() {
            @Override
            public void accept(DetailResponse detailResponse) throws Exception {
                mDetailView.updateValue(detailResponse);
                mDetailView.onRefreshComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mDetailView.showError(throwable);
            }
        });
    }
}
