package com.daily.news.subscription.home;

import com.daily.news.subscription.article.ArticleContract;
import com.daily.news.subscription.article.ArticleResponse;
import com.zjrb.core.api.base.APIBaseTask;
import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by lixinke on 2017/9/22.
 */

public class SubscribeArticleStore implements ArticleContract.Store {
    public List<ArticleResponse.DataBean.Article> mArticles;

    public SubscribeArticleStore(List<ArticleResponse.DataBean.Article> articles) {
        mArticles = articles;
    }

    @Override
    public Flowable<ArticleResponse> getFlowable(String url) {
        ArticleResponse response = new ArticleResponse();
        response.data = new ArticleResponse.DataBean();
        response.data.elements = mArticles;
        return Flowable.just(response);
    }

    @Override
    public APIBaseTask<ArticleResponse.DataBean> getLoadMoreTask(LoadingCallBack callBack) {
        APIGetTask task = new APIGetTask<ArticleResponse.DataBean>(callBack) {
            @Override
            protected void onSetupParams(Object... params) {
                put("start", params[0]);
                put("size", params[1]);
            }

            @Override
            protected String getApi() {
                return "/api/column/my_article_list";
            }
        };
        return task;
    }
}
