package com.daily.news.subscription.more.column;

import com.daily.news.subscription.subscribe.SubscribePresenter;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lixinke on 2017/7/17.
 */

public class ColumnPresenter extends SubscribePresenter implements ColumnContract.Presenter {
    private ColumnContract.View mDetailView;
    private ColumnContract.Store mDetailStore;
    private CompositeDisposable mDisposable;

    public ColumnPresenter(ColumnContract.View detailView, ColumnContract.Store detailStore) {
        super(detailView, detailStore);
        mDetailView = detailView;
        mDetailView.setPresenter(this);
        mDetailStore = detailStore;
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void setItemId(String itemId) {

    }

    @Override
    public void subscribe(Object... params) {
        mDetailView.showProgressBar();
//        Disposable disposable = mDetailStore.getFlowable("")
//                .subscribe(new Consumer<List<Column>>() {
//                    @Override
//                    public void accept(@NonNull List<Column> columnsBeen) throws Exception {
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
        APIBaseTask task = mDetailStore.getTask(new APICallBack<ColumnResponse.DataBean>() {
            @Override
            public void onSuccess(ColumnResponse.DataBean data) {
                mDetailView.updateValue(data);
                mDetailView.hideProgressBar();
            }

            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                mDetailView.hideProgressBar();
                mDetailView.showError(errMsg);
            }
        });
        if (task != null) {
            task.exe(params);
        }
    }


    @Override
    public void unsubscribe() {
        mDisposable.clear();
    }

    public void refreshData(List<ColumnResponse.DataBean.ColumnBean> recommend_list) {
        ColumnResponse.DataBean dataBean=new ColumnResponse.DataBean();
        dataBean.elements=recommend_list;
        mDetailView.updateValue(dataBean);
    }
}
