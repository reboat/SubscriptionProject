package com.daily.news.subscription.subscription;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionBean implements Parcelable {
    public int code;
    public String message;
    public String request_id;
    public DataBean data;

    public static class DataBean implements Parcelable {
        public boolean has_subscribe;
        public List<FocusBean> focus_list;
        public List<RecommendBean> recommend_list;
        public List<ArticleBean> article_list;

        public static class FocusBean implements Parcelable {
            public String doc_title;
            public String doc_url;
            public String pic_url;
            public int sort_number;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.doc_title);
                dest.writeString(this.doc_url);
                dest.writeString(this.pic_url);
                dest.writeInt(this.sort_number);
            }

            public FocusBean() {
            }

            protected FocusBean(Parcel in) {
                this.doc_title = in.readString();
                this.doc_url = in.readString();
                this.pic_url = in.readString();
                this.sort_number = in.readInt();
            }

            public static final Creator<FocusBean> CREATOR = new Creator<FocusBean>() {
                @Override
                public FocusBean createFromParcel(Parcel source) {
                    return new FocusBean(source);
                }

                @Override
                public FocusBean[] newArray(int size) {
                    return new FocusBean[size];
                }
            };
        }

        public static class RecommendBean implements Parcelable {
            public String uid;
            public String name="";
            public String pic_url;
            public int subscribe_count;
            public int article_count;

            public RecommendBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.uid);
                dest.writeString(this.name);
                dest.writeString(this.pic_url);
                dest.writeInt(this.subscribe_count);
                dest.writeInt(this.article_count);
            }

            protected RecommendBean(Parcel in) {
                this.uid = in.readString();
                this.name = in.readString();
                this.pic_url = in.readString();
                this.subscribe_count = in.readInt();
                this.article_count = in.readInt();
            }

            public static final Creator<RecommendBean> CREATOR = new Creator<RecommendBean>() {
                @Override
                public RecommendBean createFromParcel(Parcel source) {
                    return new RecommendBean(source);
                }

                @Override
                public RecommendBean[] newArray(int size) {
                    return new RecommendBean[size];
                }
            };
        }

        public static class ArticleBean implements Parcelable {
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

            public ArticleBean() {
            }

            protected ArticleBean(Parcel in) {
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

            public static final Creator<ArticleBean> CREATOR = new Creator<ArticleBean>() {
                @Override
                public ArticleBean createFromParcel(Parcel source) {
                    return new ArticleBean(source);
                }

                @Override
                public ArticleBean[] newArray(int size) {
                    return new ArticleBean[size];
                }
            };
        }

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
            this.focus_list = new ArrayList<FocusBean>();
            in.readList(this.focus_list, FocusBean.class.getClassLoader());
            this.recommend_list = new ArrayList<RecommendBean>();
            in.readList(this.recommend_list, RecommendBean.class.getClassLoader());
            this.article_list = new ArrayList<ArticleBean>();
            in.readList(this.article_list, ArticleBean.class.getClassLoader());
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.message);
        dest.writeString(this.request_id);
        dest.writeParcelable(this.data, flags);
    }

    public SubscriptionBean() {
    }

    protected SubscriptionBean(Parcel in) {
        this.code = in.readInt();
        this.message = in.readString();
        this.request_id = in.readString();
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<SubscriptionBean> CREATOR = new Parcelable.Creator<SubscriptionBean>() {
        @Override
        public SubscriptionBean createFromParcel(Parcel source) {
            return new SubscriptionBean(source);
        }

        @Override
        public SubscriptionBean[] newArray(int size) {
            return new SubscriptionBean[size];
        }
    };
}
