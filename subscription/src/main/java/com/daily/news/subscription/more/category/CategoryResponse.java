package com.daily.news.subscription.more.category;

import com.daily.news.subscription.more.column.ColumnResponse;

import java.util.List;

/**
 * 订阅栏目数据结构
 */

public class CategoryResponse {
    public int code;
    public String message;
    public String request_id;
    public DataBean data;
    public static class DataBean {
        public List<CategoryBean> elements;
        public static class CategoryBean {
            public String class_name;
            public int class_id;
            public long class_sort_number;
            public boolean is_selected = false;
            public List<ColumnResponse.DataBean.ColumnBean> columns;
        }
    }
}

