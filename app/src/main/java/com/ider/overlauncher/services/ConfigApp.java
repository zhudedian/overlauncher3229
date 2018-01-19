package com.ider.overlauncher.services;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ider-eric on 2016/11/22.
 */

public class ConfigApp extends TagConfig {
    public String pkgName;
    public String label;
    public String md5;
    public String summary;
    public String description;
    public String iconUrl;
    public String apkUrl;
    public int verCode;
    public boolean forced;


    @Override
    public boolean isAvailable() {
        return pkgName != null && md5 != null && summary != null && iconUrl != null && apkUrl != null && description != null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        bundle.putString("pkgName", pkgName);
        bundle.putString("label", label);
        bundle.putString("md5", md5);
        bundle.putString("summary", summary);
        bundle.putString("description", description);
        bundle.putString("iconUrl", iconUrl);
        bundle.putString("apkUrl", apkUrl);
        bundle.putInt("verCode", verCode);
        bundle.putBoolean("forced", forced);
        parcel.writeBundle(bundle);
    }

    public static final Parcelable.Creator<ConfigApp> CREATOR = new Parcelable.Creator<ConfigApp>() {
        @Override
        public ConfigApp createFromParcel(Parcel parcel) {
            Bundle bundle = parcel.readBundle(getClass().getClassLoader());
            ConfigApp app = new ConfigApp();
            app.tag = bundle.getString("tag", null);
            app.pkgName = bundle.getString("pkgName", null);
            app.label = bundle.getString("label", null);
            app.md5 = bundle.getString("md5", null);
            app.summary = bundle.getString("summary", null);
            app.description = bundle.getString("description", null);
            app.iconUrl = bundle.getString("iconUrl", null);
            app.apkUrl = bundle.getString("apkUrl", null);
            app.verCode = bundle.getInt("verCode", 0);
            app.forced = bundle.getBoolean("forced", false);

            return app;
        }

        @Override
        public ConfigApp[] newArray(int i) {
            return new ConfigApp[i];
        }
    };


    @Override
    public boolean equals(Object o) {
        if (o instanceof ConfigApp) {
            ConfigApp app = (ConfigApp) o;
            return !(tag == null || pkgName == null || md5 == null ||
                    label == null || summary == null || description == null ||
                    iconUrl == null || apkUrl == null)
                    && tag.equals(app.tag) && pkgName.equals(app.pkgName) &&
                    md5.equals(app.md5) && label.equals(app.label) &&
                    summary.equals(app.summary) && description.equals(app.description) &&
                    iconUrl.equals(app.iconUrl) && apkUrl.equals(app.apkUrl) &&
                    verCode == app.verCode && forced == app.forced;

        } else {
            return false;
        }

    }

    public boolean containsPackageNameOf(String[] packageNames){
        for (String packageName:packageNames){
            if (this.pkgName.equals(packageName)){
                return true;
            }
        }
        return false;
    }
}
