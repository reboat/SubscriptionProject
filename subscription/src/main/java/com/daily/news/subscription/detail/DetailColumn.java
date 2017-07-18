package com.daily.news.subscription.detail;

import com.daily.news.subscription.Article;

import java.util.List;

/**
 * Created by lixinke on 2017/7/18.
 */

public class DetailColumn {
    private String uid;
    private String name;
    private String pic_url;
    private int subscribe_count;
    private int article_count;
    private boolean subscribed;
    private String background_url;
    private String description;
    private List<Article> elements;
}
