package com.daily.news.subscription.home;

import com.daily.news.subscription.subscribe.SubscribePresenter;
import com.zjrb.core.api.callback.APICallBack;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionPresenter extends SubscribePresenter implements SubscriptionContract.Presenter {
    private SubscriptionContract.View mView;
    private SubscriptionContract.Store mStore;
    private CompositeDisposable mCompositeDisposable;

    public SubscriptionPresenter(SubscriptionContract.View view, SubscriptionContract.Store store) {
        super(view, store);
        mView = view;
        mView.setPresenter(this);
        mStore = store;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe(String... params) {
        mView.showProgressBar();
//        mStore.getFlowable("").subscribe(new Consumer<SubscriptionResponse>() {
//
//            @Override
//            public void accept(@NonNull SubscriptionResponse subscriptionResponse) throws Exception {
//                mView.updateValue(subscriptionResponse.data);
//                mView.hideProgressBar();
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@NonNull Throwable throwable) throws Exception {
//                mView.hideProgressBar();
//                mView.showError(throwable.getMessage());
//            }
//        });

        mStore.getTask(new APICallBack<SubscriptionResponse.DataBean>() {
            @Override
            public void onSuccess(SubscriptionResponse.DataBean data) {
                mView.updateValue(data);
                mView.hideProgressBar();
            }

            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                mView.showError(errMsg);
                mView.hideProgressBar();
            }
        }).exe(params[0]);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void onRefresh(Object... params) {
//        mStore.getRefreshFlowable("url").subscribe(new Consumer<SubscriptionResponse>() {
//            @Override
//            public void accept(@NonNull SubscriptionResponse subscriptionResponse) throws Exception {
//                mView.onRefreshComplete(subscriptionResponse);
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@NonNull Throwable throwable) throws Exception {
//                mView.onRefreshError(throwable.getMessage());
//            }
//        });

        mStore.getTask(new APICallBack<SubscriptionResponse.DataBean>() {
            @Override
            public void onSuccess(SubscriptionResponse.DataBean data) {
                mView.onRefreshComplete(data);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                mView.onRefreshError(errMsg);}
        }).exe(params[0]);

    }
}
