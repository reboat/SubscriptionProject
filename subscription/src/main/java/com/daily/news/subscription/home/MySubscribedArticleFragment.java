package com.daily.news.subscription.home;

import com.daily.news.subscription.article.ArticleFragment;
import com.daily.news.subscription.article.ArticleResponse;
import com.trs.tasdk.entity.ObjectType;

import cn.daily.news.analytics.Analytics;

/**
 * Created by lixinke on 2017/11/6.
 * 用于埋点
 */

public class MySubscribedArticleFragment extends ArticleFragment {
    @Override
    public void onItemClick(ArticleResponse.DataBean.Article article) {
        super.onItemClick(article);
        new Analytics.AnalyticsBuilder(getContext(),"200007","200007")
                .setEvenName("新闻列表点击")
                .setPageType("订阅首页")
                .setObjectID(article.getColumn_id())
                .setObjectName(article.getColumn_name())
                .setClassifyID(article.getChannel_id())
                .setClassifyName(article.getChannel_name())
                .setObjectType(ObjectType.NewsType)
                .build()
                .send();
    }
}
