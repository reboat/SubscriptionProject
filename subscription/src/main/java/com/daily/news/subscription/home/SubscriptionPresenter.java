package com.daily.news.subscription.home;

import com.daily.news.subscription.detail.RxException;
import com.zjrb.core.api.callback.APICallBack;
import com.zjrb.core.common.manager.APICallManager;
import com.zjrb.core.utils.SettingManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionPresenter  implements SubscriptionContract.Presenter {
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
    public void subscribe(Object... params) {
        mView.showProgressBar();
        mStore.getTask(new APICallBack<SubscriptionResponse.DataBean>() {
            @Override
            public void onSuccess(SubscriptionResponse.DataBean data) {
                mView.updateValue(data);
                mView.hideProgressBar();
            }

            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                mView.showError(new RxException(errMsg,errCode));
                mView.hideProgressBar();
            }
        }).bindLoadViewHolder(mView.getProgressBar()).exe(params[0]);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void onRefresh(Object... params) {
        APICallManager.get().cancel(this);
        mStore.getTask(new APICallBack<SubscriptionResponse.DataBean>() {
            @Override
            public void onSuccess(SubscriptionResponse.DataBean data) {
                mView.onRefreshComplete(data);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                mView.onRefreshError(errMsg);}
        }).setTag(this).exe(params[0]);
        SettingManager.getInstance().setSubscriptionRefreshTime(System.currentTimeMillis());
    }
}
