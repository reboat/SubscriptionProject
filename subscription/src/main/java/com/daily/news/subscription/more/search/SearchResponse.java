package com.daily.news.subscription.more.search;

import com.daily.news.subscription.more.column.ColumnResponse;

import java.util.List;

/**
 * Created by gaoyangzhen on 2018/3/14.
 */

public class SearchResponse {
    public int code;
    public DataBean data;
    public static class DataBean {
        public boolean has_more;
        public List<ColumnBean> elements;
        public static class ColumnBean extends ColumnResponse.DataBean.ColumnBean {
        }
    }
}

