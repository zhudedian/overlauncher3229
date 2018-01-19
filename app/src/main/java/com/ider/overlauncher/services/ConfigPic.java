package com.ider.overlauncher.services;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ider-eric on 2016/11/22.
 */

public class ConfigPic extends TagConfig {

    public String miniUrl;
    public String imageUrl;

    @Override
    public boolean isAvailable() {
        return miniUrl != null && imageUrl != null;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        bundle.putString("miniUrl", miniUrl);
        bundle.putString("imageUrl", imageUrl);
        parcel.writeBundle(bundle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ConfigPic> CREATOR = new Parcelable.Creator<ConfigPic>() {
        @Override
        public ConfigPic[] newArray(int i) {
            return new ConfigPic[i];
        }

        @Override
        public ConfigPic createFromParcel(Parcel parcel) {
            Bundle bundle = parcel.readBundle(getClass().getClassLoader());
            ConfigPic pic = new ConfigPic();
            pic.tag = bundle.getString("tag", null);
            pic.miniUrl = bundle.getString("miniUrl", null);
            pic.imageUrl = bundle.getString("imageUrl", null);
            return pic;
        }
    };
}
