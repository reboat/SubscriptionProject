package com.daily.news.subscription.more.category;

import com.zjrb.core.api.callback.APICallBack;

import io.reactivex.disposables.CompositeDisposable;

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
    public void subscribe(Object... params) {
        mMoreView.showProgressBar();
//        Disposable disposable = mMoreStore.getFlowable("")
//                .subscribe(new Consumer<CategoryResponse>() {
//                    @Override
//                    public void accept(@NonNull CategoryResponse response) throws Exception {
//                        mMoreView.updateValues(response);
//                        mMoreView.hideProgressBar();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        mMoreView.showError(throwable.getMessage());
//                        mMoreView.hideProgressBar();
//                    }
//                });
//        mDisposable.add(disposable);


        mMoreStore.getTask(new APICallBack<CategoryResponse.DataBean>() {
            @Override
            public void onSuccess(CategoryResponse.DataBean data) {
                mMoreView.updateValues(data);
                mMoreView.hideProgressBar();
            }

            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                mMoreView.showError(errMsg);
                mMoreView.hideProgressBar();
            }
        }).exe(params);
    }


    @Override
    public void unsubscribe() {
        mDisposable.clear();
    }
}
