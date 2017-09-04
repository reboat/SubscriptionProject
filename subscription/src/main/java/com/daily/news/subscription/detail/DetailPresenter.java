package com.daily.news.subscription.detail;

import com.daily.news.subscription.subscribe.SubscribePresenter;
import com.zjrb.core.api.callback.APICallBack;
import com.zjrb.core.common.manager.APICallManager;

import io.reactivex.disposables.CompositeDisposable;

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
    public void subscribe(Object... params) {
        mDetailView.showProgressBar();
//        Disposable disposable = mDetailStore.getFlowable("")
//                .subscribe(new Consumer<DetailColumn>() {
//                    @Override
//                    public void accept(@NonNull DetailColumn columnsBeen) throws Exception {
//                        mDetailView.updateValue(columnsBeen);
//                        mDetailView.hideProgressBar();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        mDetailView.hideProgressBar();
//                        mDetailView.showError(throwable.getMessage());
//                    }
//                });
//        mDisposable.add(disposable);

       mDetailStore.getTask(new APICallBack<DetailResponse.DataBean>() {
           @Override
           public void onSuccess(DetailResponse.DataBean data) {
               mDetailView.updateValue(data);
               mDetailView.hideProgressBar();
           }

           @Override
           public void onError(String errMsg, int errCode) {
               super.onError(errMsg, errCode);
               mDetailView.showError(errMsg);
               mDetailView.hideProgressBar();
           }
       }).setTag(this).exe(params);


    }

    @Override
    public void unsubscribe() {
        APICallManager.get().cancel(this);
    }


}
