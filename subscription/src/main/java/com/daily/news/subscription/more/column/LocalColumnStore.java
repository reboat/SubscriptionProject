package com.daily.news.subscription.more.column;

import com.daily.news.subscription.subscribe.SubscribeStore;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.callback.APICallBack;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/21.
 */

public class LocalColumnStore extends SubscribeStore implements ColumnContract.Store {
    private List<ColumnResponse.DataBean.ColumnBean> mColumns;

    public LocalColumnStore(List<ColumnResponse.DataBean.ColumnBean> columns) {
        mColumns = columns;
    }

    @Override
    public Flowable getFlowable(String url) {
        return Flowable.just(mColumns);
    }


    @Override
    public APIBaseTask getTask(APICallBack apiCallBack) {
        ColumnResponse.DataBean dataBean=new ColumnResponse.DataBean();
        dataBean.elements=mColumns;
        apiCallBack.onSuccess(dataBean);
        return null;
    }
}
