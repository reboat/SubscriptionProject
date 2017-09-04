package com.daily.news.subscription.more.column;

import java.util.List;

/**
 * Created by lixinke on 2017/9/2.
 */

public class ColumnResponse {
    public int code;
    public String message;
    public String request_id;
    public DataBean data;


    public static class DataBean {
        public List<ElementsBean> elements;

        public static class ElementsBean {
            public long id;
            public String name;
            public String pic_url;
            public int subscribe_count;
            public int article_count;
            public String subscribe_count_general;
            public String article_count_general;
            public boolean subscribed;
        }
    }
}
