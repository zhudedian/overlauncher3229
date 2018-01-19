package com.ider.overlauncher.model;

import android.util.Log;

import com.ider.overlauncher.db.ServerApp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by ider-eric on 2017/2/22.
 */
@Entity
public class PackageHolder extends ServerApp{

    @Id(autoincrement = true)
    private Long id;
    private String packageName;
    private String tag;

    @Generated(hash = 1602000662)
    public PackageHolder(Long id, String packageName, String tag) {
        this.id = id;
        this.packageName = packageName;
        this.tag = tag;
    }

    public PackageHolder(String packageName, String tag) {
        this.packageName = packageName;
        this.tag = tag;
    }

    @Generated(hash = 448922367)
    public PackageHolder(){

    }

    public PackageHolder(String packageName) {
        this.packageName = packageName;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getPackageName().equals(((PackageHolder)obj).getPackageName());
    }
}
