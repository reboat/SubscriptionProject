package com.daily.news.subscription.detail;

import com.daily.news.subscription.subscribe.SubscribePresenter;
import com.zjrb.core.common.manager.APICallManager;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by lixinke on 2017/7/17.
 */

public class DetailPresenter extends SubscribePresenter implements DetailContract.Presenter {
    private DetailContract.View mDetailView;
    private DetailContract.Store mDetailStore;
    private CompositeDisposable mDisposable;

    public DetailPresenter(DetailContract.View detailView, DetailContract.Store detailStore) {
        super(detailView,detailStore);
        mDetailView = detailView;
        mDetailView.setPresenter(this);
        mDetailStore = detailStore;
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe(String... params) {
        mDetailView.showProgressBar();
        Disposable disposable = mDetailStore.getFlowable("")
                .subscribe(new Consumer<DetailColumn>() {
                    @Override
                    public void accept(@NonNull DetailColumn columnsBeen) throws Exception {
                        mDetailView.updateValue(columnsBeen);
                        mDetailView.hideProgressBar();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mDetailView.hideProgressBar();
                        mDetailView.showError(throwable.getMessage());
                    }
                });
        mDisposable.add(disposable);

//       mDetailStore.getTask(new APICallBack<DetailColumn>() {
//           @Override
//           public void onSuccess(DetailColumn data) {
//               mDetailView.updateValue(data);
//           }
//
//           @Override
//           public void onError(String errMsg, int errCode) {
//               super.onError(errMsg, errCode);
//               mDetailView.showError(errMsg);
//           }
//       }).setTag(this).exe(params[0],params[1]);


    }

    @Override
    public void unsubscribe() {
        APICallManager.get().cancel(this);
    }


}
