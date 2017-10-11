package com.daily.news.subscription.more.column;

import com.daily.news.subscription.detail.RxException;
import com.daily.news.subscription.subscribe.SubscribePresenter;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lixinke on 2017/7/17.
 */

public class ColumnPresenter extends SubscribePresenter implements ColumnContract.Presenter {
    private ColumnContract.View mView;
    private ColumnContract.Store mStore;
    private CompositeDisposable mDisposable;

    public ColumnPresenter(ColumnContract.View view, ColumnContract.Store store) {
        super(view, store);
        mView = view;
        mView.setPresenter(this);
        mStore = store;
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void setItemId(String itemId) {

    }

    @Override
    public void subscribe(Object... params) {
        mView.showProgressBar();
        APIBaseTask task = mStore.getTask(new APICallBack<ColumnResponse.DataBean>() {
            @Override
            public void onSuccess(ColumnResponse.DataBean data) {
                mView.updateValue(data);
                mView.hideProgressBar();
            }

            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                mView.hideProgressBar();
                mView.showError(new RxException(errMsg,errCode));
            }
        });
        if (task != null) {
            task.bindLoadViewHolder(mView.getProgressBar()).exe(params);
        }
    }


    @Override
    public void unsubscribe() {
        mDisposable.clear();
    }

    public void refreshData(List<ColumnResponse.DataBean.ColumnBean> recommend_list) {
        ColumnResponse.DataBean dataBean=new ColumnResponse.DataBean();
        dataBean.elements=recommend_list;
        mView.updateValue(dataBean);
    }
}
