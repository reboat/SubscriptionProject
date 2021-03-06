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
        new Analytics.AnalyticsBuilder(getContext(),"200007","200007")
                .setEvenName("新闻列表点击")
                .setPageType("订阅首页")
                .setObjectID(article.getMlf_id())
                .setObjectName(article.getDoc_title())
                .setSelfObjectID(article.getId())
                .setObjectType(ObjectType.NewsType)
                .setOtherInfo(otherInfoStr)
                .build()
                .send();
    }
}
