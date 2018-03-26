package com.daily.news.subscription.more.search;

import android.os.Parcel;
import android.os.Parcelable;


import com.daily.news.subscription.more.column.ColumnResponse;

import java.util.List;

/**
 * Created by gaoyangzhen on 2018/3/14.
 */

public class SearchResponse {
    public int code;
    public String message;
    public SearchResponse.DataBean data;


    public static class DataBean {
        public List<SearchResponse.DataBean.ColumnBean> red_boat_columns;
        public List<SearchResponse.DataBean.ColumnBean> general_columns;


        public static class ColumnBean extends ColumnResponse.DataBean.ColumnBean{

        }

    }
}

