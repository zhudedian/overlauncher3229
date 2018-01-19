package com.ider.overlauncher.db;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/18.
 */

public class Items {
    @SerializedName("comm_item")
    private Comm_item comm_item;
    private String has_special;
    private String index;

    private String getHas_special(){return has_special;}
    private String getIndex(){return index;}
    public Comm_item getComm_item(){return comm_item;}
    public void setComm_items(Comm_item comm_items){this.comm_item = comm_items;}
    public void setHas_special(String has_special){this.has_special = has_special;}
    public void setIndex(String index){this.index = index;}

}
