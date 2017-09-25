package com.daily.news.subscription.detail;

import com.daily.news.subscription.subscribe.SubscribePresenter;
import com.zjrb.core.common.manager.APICallManager;

import io.reactivex.disposables.CompositeDisposable;
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
    public void subscribe(Object... params) {
        mDetailView.showProgressBar();
//       mDetailStore.getTask(new APICallBack<DetailResponse.DataBean>() {
//           @Override
//           public void onSuccess(DetailResponse.DataBean data) {
//               mDetailView.updateValue(data);
//               mDetailView.hideProgressBar();
//           }
//
//           @Override
//           public void onError(String errMsg, int errCode) {
//               super.onError(errMsg, errCode);
//               mDetailView.showError(errMsg);
//               mDetailView.hideProgressBar();
//           }
//       }).setTag(this).exe(params);

        mDetailStore.getDetailResponse(params).subscribe(new Consumer<DetailResponse.DataBean>() {
            @Override
            public void accept(DetailResponse.DataBean dataBean) throws Exception {
                mDetailView.updateValue(dataBean);
                mDetailView.hideProgressBar();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mDetailView.showError(throwable);
                mDetailView.hideProgressBar();
            }
        });


    }

    @Override
    public void unsubscribe() {
        APICallManager.get().cancel(this);
    }


}
