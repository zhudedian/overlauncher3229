package com.ider.overlauncher.db;

import com.ider.overlauncher.model.PackageHolder;

import org.greenrobot.greendao.annotation.Generated;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Eric on 2018/1/6.
 */

public class ServerApp extends DataSupport {

    private Long id;
    private int type;
    private String tag;
    private String iconUrl;
    private String packageName;
    private String label;
    private String apkUrl;
    private String description;
    private int verCode;
    private String md5;



    public ServerApp(){}

    public ServerApp(String packageName){
        this.packageName = packageName;
    }
    public ServerApp(int type,String packageName,String label,String iconUrl,String apkUrl,String description,int verCode,String md5,String tag){
        this.type = type;
        this.packageName = packageName;
        this.label = label;
        this.iconUrl = iconUrl;
        this.apkUrl = apkUrl;
        this.description = description;
        this.verCode = verCode;
        this.md5 = md5;
        this.tag = tag;
    }
//    @Generated(hash = 1602000662)
//    public ServerApp(Long id, String packageName, String tag) {
//        this.id = id;
//        this.packageName = packageName;
//        this.tag = tag;
//    }

    public int getType(){
        return type;
    }
    public void setIconUrl(String iconUrl){
        this.iconUrl = iconUrl;
    }
    public String getIconUrl(){
        return iconUrl;
    }
    public void setPackageName(String packageName){
        this.packageName = packageName;
    }
    public String getPackageName(){
        return packageName;
    }
    public void setLabel(String label){
        this.label = label;
    }
    public String getLabel(){
        return label;
    }
    public void setApkUrl(String apkUrl){
        this.apkUrl = apkUrl;
    }
    public String getApkUrl(){
        return apkUrl;
    }
    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return id;
    }
    public void setTag(String tag){
        this.tag = tag;
    }
    public String getTag(){
        return tag;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
    public void setVerCode(int verCode){
        this.verCode = verCode;
    }
    public int getVerCode(){
        return verCode;
    }
    public void setMd5(String md5){
        this.md5 = md5;
    }
    public String getMd5(){
        return md5;
    }

    public boolean containsPackageName(List<ServerApp> list){
        for (ServerApp serverApp:list){
            if (this.packageName.equals(serverApp.getPackageName())){
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean equals(Object obj) {
        return this.getPackageName().equals(((ServerApp)obj).getPackageName());
    }
    @Override
    public int hashCode(){
        return 2;
    }
}
