package com.daily.news.subscription.home;

import com.daily.news.subscription.article.ArticleResponse;
import com.daily.news.subscription.more.column.Column;

import java.util.List;

public class SubscriptionResponse {
    public int code;
    public String message;
    public String request_id;
    public DataBean data;

    public static class DataBean {
        public boolean has_subscribe;
        public List<Focus> focus_list;
        public List<Column> recommend_list;
        public List<ArticleResponse.DataBean.Article> article_list;
    }
    public static class Focus{
        public String doc_title;
        public String doc_url;
        public String pic_url;
        public int sort_number;
    }
}
