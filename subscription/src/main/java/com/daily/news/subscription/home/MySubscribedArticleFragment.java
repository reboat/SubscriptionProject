package com.daily.news.subscription.home;

import com.daily.news.subscription.article.ArticleFragment;
import com.daily.news.subscription.article.ArticleResponse;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

import cn.daily.news.analytics.Analytics;

/**
 * Created by lixinke on 2017/11/6.
 * 用于埋点
 */

public class MySubscribedArticleFragment extends ArticleFragment {
    @Override
    public void onItemClick(ArticleResponse.DataBean.Article article) {
        super.onItemClick(article);
        Map<String, String> otherInfo = new HashMap<>();
        otherInfo.put("relatedColumn", String.valueOf(article.getColumn_id()));
        String otherInfoStr = JsonUtils.toJsonString(otherInfo);
        //红船号稿件
        if(article.getDoc_type() == 10)
        {
            new Analytics.AnalyticsBuilder(getContext(), "200007", "200007", "AppContentClick", false)
                    .setEvenName("新闻列表点击")
                    .setPageType("订阅首页")
                    .setObjectID(String.valueOf(article.guid))
                    .setObjectName(article.getDoc_title())
                    .setSelfObjectID(article.getId())
                    .setObjectType(ObjectType.NewsType)
                    .setOtherInfo(otherInfoStr)
                    .newsID(String.valueOf(article.guid))
                    .selfNewsID(String.valueOf(article.getId()))
                    .newsTitle(article.getDoc_title())
                    .pageType("订阅首页")
                    .objectType("订阅新闻列表")
                    .pubUrl(article.getUrl())
                    .build()
                    .send();
        }
        else {
            //普通稿件
            new Analytics.AnalyticsBuilder(getContext(), "200007", "200007", "AppContentClick", false)
                    .setEvenName("新闻列表点击")
                    .setPageType("订阅首页")
                    .setObjectID(article.getMlf_id())
                    .setObjectName(article.getDoc_title())
                    .setSelfObjectID(article.getId())
                    .setObjectType(ObjectType.NewsType)
                    .setOtherInfo(otherInfoStr)
                    .newsID(String.valueOf(article.getMlf_id()))
                    .selfNewsID(String.valueOf(article.getId()))
                    .newsTitle(article.getDoc_title())
                    .pageType("订阅首页")
                    .objectType("订阅新闻列表")
                    .pubUrl(article.getUrl())
                    .build()
                    .send();
        }
    }
}
