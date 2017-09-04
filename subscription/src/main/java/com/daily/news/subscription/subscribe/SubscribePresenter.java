package com.daily.news.subscription.subscribe;

import com.daily.news.subscription.more.column.ColumnResponse;
import com.zjrb.core.api.callback.APICallBack;

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
//        mStore.getSubmitSubscribeFlowable(column).subscribe(new Consumer<Column>() {
//            @Override
//            public void accept(@NonNull Column column) throws Exception {
//                mView.subscribeSuc(column);
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@NonNull Throwable throwable) throws Exception {
//                mView.subscribeFail(column, throwable.getMessage());
//            }
//        });

        mStore.getSubmitSubscribeTask(new APICallBack<ColumnResponse.DataBean.ColumnBean>() {
            @Override
            public void onSuccess(ColumnResponse.DataBean.ColumnBean data) {
            }

            @Override
            public void onEmpty() {
                mView.subscribeSuc(column);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                mView.subscribeFail(column,errMsg);
            }
        }).exe(column.id,!column.subscribed);
    }
}
