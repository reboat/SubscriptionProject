package com.daily.news.subscription;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lixinke on 2017/7/18.
 */

public class Article implements Parcelable {
    public int id;
    public String list_title;
    public String list_style;
    public String list_tag;
    public int doc_type;
    public int read_count;
    public int like_count;
    public int comment_count;
    public String channel_id;
    public String channel_name;
    public String column_id;
    public String column_name;
    public long sort_number;
    public String uri_scheme;
    public boolean is_followed;
    public boolean is_column_subscribed;
    public int comment_level;
    public boolean is_like_enabled;
    public String web_link;
    public int album_count;
    public String activity_status;
    public long activity_date_begin;
    public long activity_date_end;
    public int activity_register_count;
    public boolean activity_announced;
    public String live_type;
    public String live_status;
    public String live_url;
    public String video_url;
    public int video_duration;
    public long topic_date_begin;
    public long topic_date_end;
    public List<String> list_pics;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.list_title);
        dest.writeString(this.list_style);
        dest.writeString(this.list_tag);
        dest.writeInt(this.doc_type);
        dest.writeInt(this.read_count);
        dest.writeInt(this.like_count);
        dest.writeInt(this.comment_count);
        dest.writeString(this.channel_id);
        dest.writeString(this.channel_name);
        dest.writeString(this.column_id);
        dest.writeString(this.column_name);
        dest.writeLong(this.sort_number);
        dest.writeString(this.uri_scheme);
        dest.writeByte(this.is_followed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_column_subscribed ? (byte) 1 : (byte) 0);
        dest.writeInt(this.comment_level);
        dest.writeByte(this.is_like_enabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.web_link);
        dest.writeInt(this.album_count);
        dest.writeString(this.activity_status);
        dest.writeLong(this.activity_date_begin);
        dest.writeLong(this.activity_date_end);
        dest.writeInt(this.activity_register_count);
        dest.writeByte(this.activity_announced ? (byte) 1 : (byte) 0);
        dest.writeString(this.live_type);
        dest.writeString(this.live_status);
        dest.writeString(this.live_url);
        dest.writeString(this.video_url);
        dest.writeInt(this.video_duration);
        dest.writeLong(this.topic_date_begin);
        dest.writeLong(this.topic_date_end);
        dest.writeStringList(this.list_pics);
    }

    public Article() {
    }

    protected Article(Parcel in) {
        this.id = in.readInt();
        this.list_title = in.readString();
        this.list_style = in.readString();
        this.list_tag = in.readString();
        this.doc_type = in.readInt();
        this.read_count = in.readInt();
        this.like_count = in.readInt();
        this.comment_count = in.readInt();
        this.channel_id = in.readString();
        this.channel_name = in.readString();
        this.column_id = in.readString();
        this.column_name = in.readString();
        this.sort_number = in.readLong();
        this.uri_scheme = in.readString();
        this.is_followed = in.readByte() != 0;
        this.is_column_subscribed = in.readByte() != 0;
        this.comment_level = in.readInt();
        this.is_like_enabled = in.readByte() != 0;
        this.web_link = in.readString();
        this.album_count = in.readInt();
        this.activity_status = in.readString();
        this.activity_date_begin = in.readLong();
        this.activity_date_end = in.readLong();
        this.activity_register_count = in.readInt();
        this.activity_announced = in.readByte() != 0;
        this.live_type = in.readString();
        this.live_status = in.readString();
        this.live_url = in.readString();
        this.video_url = in.readString();
        this.video_duration = in.readInt();
        this.topic_date_begin = in.readLong();
        this.topic_date_end = in.readLong();
        this.list_pics = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
