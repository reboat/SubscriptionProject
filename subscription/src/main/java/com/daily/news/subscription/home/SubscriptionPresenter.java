package com.daily.news.subscription.home;

import com.daily.news.subscription.detail.RxException;

import cn.daily.news.biz.core.db.SettingManager;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.network.compatible.APICallManager;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lixinke on 2017/7/18.
 */

public class SubscriptionPresenter  implements SubscriptionContract.Presenter {
    private static final String TAG_DETAIL="tag_detail";
    private static final String TAG_REFRESH="tag_refresh";

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
        }).setTag(TAG_DETAIL).bindLoadViewHolder(mView.getProgressBar()).exe();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
        APICallManager.get().cancel(TAG_DETAIL);
        APICallManager.get().cancel(TAG_REFRESH);
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
        }).setTag(TAG_REFRESH).exe();
        SettingManager.getInstance().setSubscriptionRefreshTime(System.currentTimeMillis());
    }
}
