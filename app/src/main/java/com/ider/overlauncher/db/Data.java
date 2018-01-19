package com.ider.overlauncher.db;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class Data {
    @SerializedName("channel_contents")
    private List<Channel_contents> channel_contents;
    private String req_interval_time;

    public Data(long ld) {

    }

    public List<Channel_contents> getChannel_contents(){return channel_contents;}
    public String getReq_interval_time(){return req_interval_time;}
    public void setChannel_contentses(List<Channel_contents> channel_contents){
        this.channel_contents = channel_contents;
    }
    public void setReq_interval_time(String req_interval_time){this.req_interval_time = req_interval_time;}
}
