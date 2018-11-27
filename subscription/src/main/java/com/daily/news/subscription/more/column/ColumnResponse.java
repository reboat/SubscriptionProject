package com.daily.news.subscription.more.column;

import android.os.Parcel;
import android.os.Parcelable;

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
        public List<ColumnBean> elements;

        public static class ColumnBean implements Parcelable {
            public long id;
            public String name;
            public String pic_url;
            public int subscribe_count;
            public int article_count;
            public String subscribe_count_general;
            public String article_count_general;
            public boolean subscribed = false;
            public boolean red_boat_column = false;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(this.id);
                dest.writeString(this.name);
                dest.writeString(this.pic_url);
                dest.writeInt(this.subscribe_count);
                dest.writeInt(this.article_count);
                dest.writeString(this.subscribe_count_general);
                dest.writeString(this.article_count_general);
                dest.writeByte(this.subscribed ? (byte) 1 : (byte) 0);
                dest.writeByte(this.red_boat_column ? (byte) 1 : (byte) 0);
            }

            public ColumnBean() {
            }

            protected ColumnBean(Parcel in) {
                this.id = in.readLong();
                this.name = in.readString();
                this.pic_url = in.readString();
                this.subscribe_count = in.readInt();
                this.article_count = in.readInt();
                this.subscribe_count_general = in.readString();
                this.article_count_general = in.readString();
                this.subscribed = in.readByte() != 0;
                this.red_boat_column = in.readByte() != 0;
            }

            public static final Parcelable.Creator<ColumnBean> CREATOR = new Parcelable.Creator<ColumnBean>() {
                @Override
                public ColumnBean createFromParcel(Parcel source) {
                    return new ColumnBean(source);
                }

                @Override
                public ColumnBean[] newArray(int size) {
                    return new ColumnBean[size];
                }
            };
        }
    }
}
