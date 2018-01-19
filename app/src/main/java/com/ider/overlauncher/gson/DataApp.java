package com.ider.overlauncher.gson;

import com.google.gson.annotations.SerializedName;
import com.ider.overlauncher.services.ServicePresenter;

/**
 * Created by Eric on 2018/1/8.
 */

public class DataApp {

    private String tag;
    private String statues;
    @SerializedName("icon")
    private String iconUrl;
    @SerializedName("down")
    private String apkUrl;
    @SerializedName("package")
    private String packageName;
    private String label;
    private String summary;
    @SerializedName("vername")
    private String md5;
    private String vercode;
    private String descriptions;
    private String force;


    public String getTag(){
        return tag;
    }
    public int getStatues(){
        return Integer.parseInt(statues);
    }
    public String getIconUrl(){
        return createUrl(iconUrl);
    }
    public String getApkUrl(){
        return createUrl(apkUrl);
    }
    public String getPackageName(){
        return packageName;
    }
    public String getLabel(){
        return label;
    }
    public String getSummary(){
        return summary;
    }
    public String getMd5(){
        return md5;
    }
    public int getVercode(){
        return Integer.parseInt(vercode);
    }
    public String getDescriptions(){
        return descriptions;
    }
    public boolean isForce(){
        return Boolean.parseBoolean(force);
    }
    private String createUrl(String url) {
        if(url == null) {
            return null;
        }
        if(url.toLowerCase().startsWith("http")) {
            return url;
        } else {
            return ServicePresenter.SERVER_DOMAIN + url;
        }
    }
}
