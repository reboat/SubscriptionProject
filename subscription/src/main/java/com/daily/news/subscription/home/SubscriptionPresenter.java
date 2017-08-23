package com.daily.news.subscription.home;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionPresenter implements SubscriptionContract.Presenter {
    private SubscriptionContract.View mView;
    private SubscriptionContract.Store mStore;
    private CompositeDisposable mCompositeDisposable;

    public SubscriptionPresenter(SubscriptionContract.View view, SubscriptionContract.Store store) {
        mView = view;
        mView.setPresenter(this);
        mStore = store;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        mView.showProgressBar();
        mStore.getFlowable("").subscribe(new Consumer<SubscriptionResponse>() {

            @Override
            public void accept(@NonNull SubscriptionResponse subscriptionResponse) throws Exception {
                mView.updateValue(subscriptionResponse);
                mView.hideProgressBar();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                mView.hideProgressBar();
                mView.showError(throwable.getMessage());
            }
        });
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void onRefresh() {
        mStore.getRefreshFlowable("url").subscribe(new Consumer<SubscriptionResponse>() {
            @Override
            public void accept(@NonNull SubscriptionResponse subscriptionResponse) throws Exception {
                mView.onRefreshComplete(subscriptionResponse);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                mView.onRefreshError(throwable.getMessage());
            }
        });
    }
}
