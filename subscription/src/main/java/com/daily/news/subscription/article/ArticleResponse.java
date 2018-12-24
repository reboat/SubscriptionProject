package com.daily.news.subscription.article;

import com.zjrb.daily.news.bean.ArticleItemBean;

import java.util.List;

/**
 * Created by lixinke on 2017/8/25.
 */

public class ArticleResponse {
    public int code;
    public String message;
    public String request_id;
    public DataBean data;


    public static class DataBean {
        public List<Article> elements;
        public List<Article> adBeans;

        public static class Article extends ArticleItemBean {

        }
    }
}
