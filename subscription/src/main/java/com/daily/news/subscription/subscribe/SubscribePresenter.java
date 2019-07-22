package com.daily.news.subscription.subscribe;

import com.daily.news.subscription.more.column.ColumnResponse;

import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.network.compatible.APICallManager;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lixinke on 2017/8/25.
 */

public class SubscribePresenter implements SubscribeContract.Presenter {
    private SubscribeContract.View mView;
    private SubscribeContract.Store mStore;
    private CompositeDisposable mDisposable;

    public SubscribePresenter(SubscribeContract.View view, SubscribeContract.Store store) {
        mView = view;
        mStore = store;
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void submitSubscribe(final ColumnResponse.DataBean.ColumnBean column) {
        mStore.getSubmitSubscribeTask(new APICallBack<ColumnResponse.DataBean.ColumnBean>() {
            @Override
            public void onSuccess(ColumnResponse.DataBean.ColumnBean data) {
                column.subscribed=!column.subscribed;
                mView.subscribeSuc(column);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                mView.subscribeFail(column,errMsg);
            }
        })
                .setTag(this)
                .exe(column.id,!column.subscribed);
    }

    @Override
    public void unsubscribe() {
        APICallManager.get().cancel(this);
    }
}
