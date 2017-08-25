package com.daily.news.subscription.article;

import com.zjrb.coreprojectlibrary.api.base.APIPostTask;
import com.zjrb.coreprojectlibrary.api.callback.LoadingCallBack;

/**
 * Created by lixinke on 2017/8/25.
 */

public class LoadMoreArticleTask extends APIPostTask {

    public LoadMoreArticleTask(LoadingCallBack callBack) {
        super(callBack);
    }

    @Override
    protected void onSetupParams(Object... params) {
        put("start", params[0]);
        put("size", params[1]);
    }

    @Override
    protected String getApi() {
        return "/api/column/my_article_list";
    }
}
