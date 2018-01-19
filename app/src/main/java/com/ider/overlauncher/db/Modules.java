package com.ider.overlauncher.db;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class Modules {
    private String index;
   @SerializedName("items")
    private List<Items> items;
    private String module_type;
    public String getIndex(){return index;}
    public List<Items> getItems(){return items;}
    public String getModule_type(){return module_type;}
    public void setIndex(String index){this.index = index;}
    public void setItems(List<Items> items){this.items = items;}
    public void setModule_type(String module_type){this.module_type = module_type;}

}
