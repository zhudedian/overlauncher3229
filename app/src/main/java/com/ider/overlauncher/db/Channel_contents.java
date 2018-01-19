package com.ider.overlauncher.db;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class Channel_contents {
   private String channel_id;
    @SerializedName("modules")
    private List<Modules> modules;
    private String version;
    public String getChannel_id(){return channel_id;}
    public List<Modules> getModules(){return modules;}
    public String getVersion(){return version;}
    public void setChannel_id(String channel_id){this.channel_id = channel_id;}
    public void setModules(List<Modules> modules){this.modules = modules;}
    public void setVersion(String version) {
        this.version = version;
    }

}
