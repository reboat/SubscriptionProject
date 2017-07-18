package com.daily.news.subscription.more.category;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by lixinke on 2017/7/17.
 */

public class CategoryPresenter implements CategoryContract.Presenter {
    private CategoryContract.View mMoreView;
    private CategoryContract.Store mMoreStore;
    private CompositeDisposable mDisposable;

    public CategoryPresenter(CategoryContract.View moreView, CategoryContract.Store moreStore) {
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
