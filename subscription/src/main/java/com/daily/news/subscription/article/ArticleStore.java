package com.daily.news.subscription.article;

import com.zjrb.core.load.LoadingCallBack;

import cn.daily.news.biz.core.network.compatible.APIPostTask;
import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/7/17.
 */

public class ArticleStore implements ArticleContract.Store {

    @Override
    public Flowable<ArticleResponse> getFlowable(String url) {
        return null;
    }

    @Override
    public APIPostTask getLoadMoreTask(LoadingCallBack callBack) {
        return null;
    }
}
