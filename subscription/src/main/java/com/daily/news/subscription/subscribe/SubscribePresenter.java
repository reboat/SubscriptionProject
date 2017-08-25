package com.daily.news.subscription.subscribe;

import com.daily.news.subscription.more.column.Column;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

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
    public void submitSubscribe(final Column column) {
        mStore.getSubmitSubscribeFlowable(column).subscribe(new Consumer<Column>() {
            @Override
            public void accept(@NonNull Column column) throws Exception {
                mView.subscribeSuc(column);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                mView.subscribeFail(column, throwable.getMessage());
            }
        });
    }
}
