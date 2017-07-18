package com.daily.news.subscription.more.column;

import android.os.Parcel;
import android.os.Parcelable;

public class Column implements Parcelable {
    public String uid;
    public String name;
    public String pic_url;
    public int subscribe_count;
    public int article_count;
    public boolean subscribed;
    public int sort_number;

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
        dest.writeByte(this.subscribed ? (byte) 1 : (byte) 0);
        dest.writeInt(this.sort_number);
    }

    public Column() {
    }

    protected Column(Parcel in) {
        this.uid = in.readString();
        this.name = in.readString();
        this.pic_url = in.readString();
        this.subscribe_count = in.readInt();
        this.article_count = in.readInt();
        this.subscribed = in.readByte() != 0;
        this.sort_number = in.readInt();
    }

    public static final Parcelable.Creator<Column> CREATOR = new Parcelable.Creator<Column>() {
        @Override
        public Column createFromParcel(Parcel source) {
            return new Column(source);
        }

        @Override
        public Column[] newArray(int size) {
            return new Column[size];
        }
    };
}
