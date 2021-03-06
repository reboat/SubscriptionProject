package com.daily.news.subscription.detail;

import com.daily.news.subscription.article.ArticleResponse;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.zjrb.core.domain.base.BaseData;

import java.util.List;

/**
 * Created by lixinke on 2017/7/18.
 */

public class DetailResponse {
    public int code;
    public String message;
    public String request_id;
    public DataBean data;

    public static class DataBean extends BaseData{

        public DetailBean detail;
        public List<ArticleResponse.DataBean.Article> elements;

        public static class DetailBean extends ColumnResponse.DataBean.ColumnBean {
            public String background_url;
            public String description;
            public long sort_number;
        }
    }
}
