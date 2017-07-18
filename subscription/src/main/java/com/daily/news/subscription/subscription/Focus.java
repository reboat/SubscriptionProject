package com.daily.news.subscription.subscription;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lixinke on 2017/7/18.
 */

public class Focus implements Parcelable {
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

    public Focus() {
    }

    protected Focus(Parcel in) {
        this.doc_title = in.readString();
        this.doc_url = in.readString();
        this.pic_url = in.readString();
        this.sort_number = in.readInt();
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
