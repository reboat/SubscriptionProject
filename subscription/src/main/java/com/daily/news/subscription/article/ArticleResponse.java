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

        public static class Article extends ArticleItemBean {
            //TODO ArticleItemBean 后面会添加，注意删除该字段
            private String mlf_id;
            public String getMlf_id() {
                return mlf_id;
            }

            public void setMlf_id(String mlf_id) {
                this.mlf_id = mlf_id;
            }
        }
    }
}
