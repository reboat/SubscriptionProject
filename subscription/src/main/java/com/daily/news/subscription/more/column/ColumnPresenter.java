package com.daily.news.subscription.more.column;

import com.daily.news.subscription.detail.RxException;
import com.daily.news.subscription.subscribe.SubscribePresenter;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;
import com.zjrb.core.common.manager.APICallManager;

import java.util.List;

/**
 * Created by lixinke on 2017/7/17.
 */

public class ColumnPresenter extends SubscribePresenter implements ColumnContract.Presenter {
    private static final String COLUMN_TASK_TAG = "column_task";
    private ColumnContract.View mView;
    private ColumnContract.Store mStore;

    public ColumnPresenter(ColumnContract.View view, ColumnContract.Store store) {
        super(view, store);
        mView = view;
        mView.setPresenter(this);
        mStore = store;
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
                mView.showError(new RxException(errMsg, errCode));
            }
        });
        if (task != null) {
            task.bindLoadViewHolder(mView.getProgressBar()).exe(params);
            task.setTag(COLUMN_TASK_TAG);
        }
    }


    @Override
    public void unsubscribe() {
        APICallManager.get().cancel(COLUMN_TASK_TAG);
    }

    public void refreshData(List<ColumnResponse.DataBean.ColumnBean> recommend_list) {
        ColumnResponse.DataBean dataBean = new ColumnResponse.DataBean();
        dataBean.elements = recommend_list;
        mView.updateValue(dataBean);
    }
}
