package com.daily.news.subscription.detail;

import com.daily.news.subscription.article.Article;

import java.util.List;

/**
 * Created by lixinke on 2017/7/18.
 */

public class DetailColumn {
    public String uid;
    public String name;
    public String pic_url;
    public int subscribe_count;
    public int article_count;
    public boolean subscribed;
    public String background_url;
    public String description;
    public List<Article> elements;
}
