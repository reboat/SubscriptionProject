package com.daily.news.subscription.detail;

import com.daily.news.subscription.article.ArticleResponse;
import com.daily.news.subscription.more.column.ColumnResponse;

import java.util.List;

/**
 * Created by lixinke on 2017/7/18.
 */

public class DetailColumn extends ColumnResponse.DataBean.ColumnBean {
    public String background_url;
    public String description;
    public List<ArticleResponse.DataBean.Article> elements;
}
