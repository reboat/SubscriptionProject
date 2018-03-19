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

//        public static class ColumnBean implements Parcelable {
//            public long id;
//            public String name;
//            public String pic_url;
//            public int subscribe_count;
//            public int article_count;
//            public String subscribe_count_general;
//            public String article_count_general;
//            public boolean subscribed = false;
//
//            @Override
//            public int describeContents() {
//                return 0;
//            }
//
//            @Override
//            public void writeToParcel(Parcel dest, int flags) {
//                dest.writeLong(this.id);
//                dest.writeString(this.name);
//                dest.writeString(this.pic_url);
//                dest.writeInt(this.subscribe_count);
//                dest.writeInt(this.article_count);
//                dest.writeString(this.subscribe_count_general);
//                dest.writeString(this.article_count_general);
//                dest.writeByte(this.subscribed ? (byte) 1 : (byte) 0);
//            }
//
//            public ColumnBean() {
//            }
//
//            protected ColumnBean(Parcel in) {
//                this.id = in.readLong();
//                this.name = in.readString();
//                this.pic_url = in.readString();
//                this.subscribe_count = in.readInt();
//                this.article_count = in.readInt();
//                this.subscribe_count_general = in.readString();
//                this.article_count_general = in.readString();
//                this.subscribed = in.readByte() != 0;
//            }
//
//            public static final Parcelable.Creator<SearchResponse.DataBean.ColumnBean> CREATOR = new Parcelable.Creator<SearchResponse.DataBean.ColumnBean>() {
//                @Override
//                public SearchResponse.DataBean.ColumnBean createFromParcel(Parcel source) {
//                    return new SearchResponse.DataBean.ColumnBean(source);
//                }
//
//                @Override
//                public SearchResponse.DataBean.ColumnBean[] newArray(int size) {
//                    return new SearchResponse.DataBean.ColumnBean[size];
//                }
//            };
//        }
    }
}

