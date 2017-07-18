package com.daily.news.subscription.more;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by lixinke on 2017/7/17.
 */

public class MorePresenter implements MoreContract.Presenter {
    private MoreContract.View mMoreView;
    private MoreContract.Store mMoreStore;
    private CompositeDisposable mDisposable;

    public MorePresenter(MoreContract.View moreView, MoreContract.Store moreStore) {
        mMoreView = moreView;
        mMoreView.setPresenter(this);
        mMoreStore = moreStore;
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        mMoreView.showProgressBar();
        Disposable disposable = mMoreStore.getFlowable(mMoreStore.getUrl())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(@NonNull List<Category> items) throws Exception {
                        mMoreView.updateValues(items);
                        mMoreView.hideProgressBar();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mMoreView.showError(throwable.getMessage());
                        mMoreView.hideProgressBar();
                    }
                });
        mDisposable.add(disposable);
    }

    @Override
    public void unsubscribe() {
        mDisposable.clear();
    }
}
