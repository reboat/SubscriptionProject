package com.daily.news.subscription.detail;

import com.daily.news.subscription.article.ArticleFragment;
import com.daily.news.subscription.article.ArticleResponse;
import com.trs.tasdk.entity.ObjectType;

import cn.daily.news.analytics.Analytics;

/**
 * Created by lixinke on 2017/11/6.
 */

public class DetailArticleFragment extends ArticleFragment {
    @Override
    protected void onItemClick(ArticleResponse.DataBean.Article article) {
        super.onItemClick(article);
        new Analytics.AnalyticsBuilder(getContext(),"200007","200007")
                .setEvenName("新闻列表点击")
                .setPageType("栏目详情页")
                .setObjectID(article.getColumn_id())
                .setObjectName(article.getColumn_name())
                .setClassifyID(article.getChannel_id())
                .setClassifyName(article.getChannel_name())
                .setObjectType(ObjectType.NewsType)
                .build()
                .send();
    }
}
