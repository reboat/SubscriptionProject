package com.daily.news.subscription.more.category;

import com.daily.news.subscription.detail.RxException;

import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.network.compatible.APICallManager;

/**
 * Created by lixinke on 2017/7/17.
 */

public class CategoryPresenter implements CategoryContract.Presenter {
    private CategoryContract.View mMoreView;
    private CategoryContract.Store mMoreStore;

    public CategoryPresenter(CategoryContract.View moreView, CategoryContract.Store moreStore) {
        mMoreView = moreView;
        mMoreView.setPresenter(this);
        mMoreStore = moreStore;
    }

    @Override
    public void subscribe(Object... params) {
        mMoreView.showProgressBar();
        mMoreStore.getTask(new APICallBack<CategoryResponse.DataBean>() {
            @Override
            public void onSuccess(CategoryResponse.DataBean data) {
                mMoreView.updateValues(data);
                mMoreView.hideProgressBar();
            }

            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                mMoreView.showError(new RxException(errMsg,errCode));
                mMoreView.hideProgressBar();
            }
        }).bindLoadViewHolder(mMoreView.getProgressBar()).setTag(this).exe(params);
    }


    @Override
    public void unsubscribe() {
        APICallManager.get().cancel(this);
    }
}
