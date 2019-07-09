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
        public boolean has_more;

        public static class ColumnBean implements Parcelable {
            public long id;
            public String name;
            public String pic_url;
            public String background_url;
            public int subscribe_count;
            public String subscribe_count_general;
            public int article_count;
            public String article_count_general;
            public String description;
            public boolean subscribed;
            public long sort_number;
            public boolean recommended;
            public String share_url;
            public int priority_level;
            public boolean normal_column;
            public boolean red_boat_column;
            public String card_url;
            public boolean rank_hited;
            public String rank_share_url;
            public String rank_card_url;
            public String hit_rank_count_general;
            public int hit_rank_count;


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(this.id);
                dest.writeString(this.name);
                dest.writeString(this.pic_url);
                dest.writeString(this.background_url);
                dest.writeInt(this.subscribe_count);
                dest.writeString(this.subscribe_count_general);
                dest.writeInt(this.article_count);
                dest.writeString(this.article_count_general);
                dest.writeString(this.description);
                dest.writeByte(this.subscribed ? (byte) 1 : (byte) 0);
                dest.writeLong(this.sort_number);
                dest.writeByte(this.recommended ? (byte) 1 : (byte) 0);
                dest.writeString(this.share_url);
                dest.writeInt(this.priority_level);
                dest.writeByte(this.normal_column ? (byte) 1 : (byte) 0);
                dest.writeByte(this.red_boat_column ? (byte) 1 : (byte) 0);
                dest.writeString(this.card_url);
                dest.writeByte(this.rank_hited ? (byte) 1 : (byte) 0);
                dest.writeString(this.rank_share_url);
                dest.writeString(this.rank_card_url);
                dest.writeString(this.hit_rank_count_general);
                dest.writeInt(this.hit_rank_count);
            }

            public ColumnBean() {
            }

            protected ColumnBean(Parcel in) {
                this.id = in.readLong();
                this.name = in.readString();
                this.pic_url = in.readString();
                this.background_url = in.readString();
                this.subscribe_count = in.readInt();
                this.subscribe_count_general = in.readString();
                this.article_count = in.readInt();
                this.article_count_general = in.readString();
                this.description = in.readString();
                this.subscribed = in.readByte() != 0;
                this.sort_number = in.readLong();
                this.recommended = in.readByte() != 0;
                this.share_url = in.readString();
                this.priority_level = in.readInt();
                this.normal_column = in.readByte() != 0;
                this.red_boat_column = in.readByte() != 0;
                this.card_url = in.readString();
                this.rank_hited = in.readByte() != 0;
                this.rank_share_url = in.readString();
                this.rank_card_url = in.readString();
                this.hit_rank_count_general = in.readString();
                this.hit_rank_count = in.readInt();
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
