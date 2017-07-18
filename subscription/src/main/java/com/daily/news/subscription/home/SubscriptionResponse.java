package com.daily.news.subscription.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.daily.news.subscription.Article;
import com.daily.news.subscription.more.column.Column;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionResponse {
    public int code;
    public String message;
    public String request_id;
    public DataBean data;

    public static class DataBean implements Parcelable {
        public boolean has_subscribe;
        public List<Focus> focus_list;
        public List<Column> recommend_list;
        public List<Article> article_list;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.has_subscribe ? (byte) 1 : (byte) 0);
            dest.writeList(this.focus_list);
            dest.writeList(this.recommend_list);
            dest.writeList(this.article_list);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.has_subscribe = in.readByte() != 0;
            this.focus_list = new ArrayList<>();
            in.readList(this.focus_list, Focus.class.getClassLoader());
            this.recommend_list = new ArrayList<>();
            in.readList(this.recommend_list, Column.class.getClassLoader());
            this.article_list = new ArrayList<>();
            in.readList(this.article_list, Article.class.getClassLoader());
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
