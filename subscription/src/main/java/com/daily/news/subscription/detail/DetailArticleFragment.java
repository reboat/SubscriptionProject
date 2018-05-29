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
        //红船号稿件
        if(article.getDoc_type() == 10) {
            new Analytics.AnalyticsBuilder(getContext(),"200007","200007")
                    .setEvenName("新闻列表点击")
                    .setPageType("栏目详情页")
                    .setObjectID(String.valueOf(article.guid))
                    .setObjectName(article.getList_title())
                    .setClassifyID(article.getColumn_id())
                    .setClassifyName(article.getColumn_name())
                    .setSelfObjectID(String.valueOf(article.getId()))
                    .setObjectType(ObjectType.NewsType)
                    .build()
                    .send();
        }
        else
        {//普通稿件
            new Analytics.AnalyticsBuilder(getContext(),"200007","200007")
                    .setEvenName("新闻列表点击")
                    .setPageType("栏目详情页")
                    .setObjectID(article.getMlf_id())
                    .setObjectName(article.getList_title())
                    .setClassifyID(article.getColumn_id())
                    .setClassifyName(article.getColumn_name())
                    .setSelfObjectID(String.valueOf(article.getId()))
                    .setObjectType(ObjectType.NewsType)
                    .build()
                    .send();
        }
    }

}
