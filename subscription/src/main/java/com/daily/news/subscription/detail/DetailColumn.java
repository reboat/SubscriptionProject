package com.daily.news.subscription.detail;

import com.daily.news.subscription.article.ArticleResponse;
import com.daily.news.subscription.more.column.Column;

import java.util.List;

/**
 * Created by lixinke on 2017/7/18.
 */

public class DetailColumn extends Column{
    public String background_url;
    public String description;
    public List<ArticleResponse.DataBean.Article> elements;
}
