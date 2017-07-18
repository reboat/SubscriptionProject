package com.daily.news.subscription.more;

import java.util.List;

/**
 * 订阅栏目数据结构
 */

public class SubscriptionColumn {
    public int code;
    public String message;
    public String request_id;
    public DataBean data;

    public static class DataBean {
        public List<ElementsBean> elements;

        public static class ElementsBean {
            public String class_name;
            public int class_id;
            public int class_sort_number;
            public List<ColumnsBean> columns;

            public static class ColumnsBean {
                public String uid;
                public String name;
                public String pic_url;
                public int subscribe_count;
                public int article_count;
                public boolean subscribed;
                public int sort_number;
            }
        }
    }
}
