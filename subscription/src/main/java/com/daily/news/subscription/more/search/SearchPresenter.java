package com.daily.news.subscription.more.search;

import com.daily.news.subscription.detail.RxException;
import com.daily.news.subscription.subscribe.SubscribePresenter;

import java.util.List;

import cn.daily.news.biz.core.network.compatible.APIBaseTask;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.network.compatible.APICallManager;

/**
 * Created by gaoyangzhen on 2018/3/14.
 */

public class SearchPresenter extends SubscribePresenter implements SearchContract.Presenter {
    private static final String COLUMN_TASK_TAG = "column_task";
    private SearchContract.View mView;
    private SearchContract.Store mStore;

    public SearchPresenter(SearchContract.View view, SearchContract.Store store) {
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
        APIBaseTask task = mStore.getTask(new APICallBack<SearchResponse.DataBean>() {
            @Override
            public void onSuccess(SearchResponse.DataBean data) {
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
            task.bindLoadViewHolder(mView.getProgressBar())
                    .setTag(COLUMN_TASK_TAG)
                    .exe(params);
        }
    }


    @Override
    public void unsubscribe() {
        super.unsubscribe();
        APICallManager.get().cancel(COLUMN_TASK_TAG);
    }

    public void refreshData(SearchResponse.DataBean recommend_list) {
        SearchResponse.DataBean dataBean = recommend_list;
        mView.updateValue(dataBean);
    }
}
