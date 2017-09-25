package com.daily.news.subscription.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.daily.news.subscription.article.ArticleResponse;
import com.daily.news.subscription.more.column.ColumnResponse;

import java.util.List;

public class SubscriptionResponse {
    public int code;
    public String message;
    public String request_id;
    public DataBean data;

    public static class DataBean {
        public boolean has_subscribe;
        public List<Focus> focus_list;
        public List<ColumnResponse.DataBean.ColumnBean> recommend_list;
        public List<ArticleResponse.DataBean.Article> article_list;
    }
    public static class Focus implements Parcelable {
        public String doc_title;
        public String doc_url;
        public String pic_url;
        public long sort_number;
        public int channel_article_id;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.doc_title);
            dest.writeString(this.doc_url);
            dest.writeString(this.pic_url);
            dest.writeLong(this.sort_number);
            dest.writeInt(this.channel_article_id);
        }

        public Focus() {
        }

        protected Focus(Parcel in) {
            this.doc_title = in.readString();
            this.doc_url = in.readString();
            this.pic_url = in.readString();
            this.sort_number = in.readLong();
            this.channel_article_id = in.readInt();
        }

        public static final Parcelable.Creator<Focus> CREATOR = new Parcelable.Creator<Focus>() {
            @Override
            public Focus createFromParcel(Parcel source) {
                return new Focus(source);
            }

            @Override
            public Focus[] newArray(int size) {
                return new Focus[size];
            }
        };
    }
}
