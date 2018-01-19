package com.ider.overlauncher.Sql;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/12/22.
 */

public class Book extends DataSupport{
    private  int id;
    private  String author;
    private  int pages;
    private String tag;
    private String packageName;
    private String name;
    public Book(String packageName,String tag){
        this.packageName = packageName;
        this.tag = tag ;
    }
    public int getId()
    {
        return id;
    }
    public void setId(int id){

        this.id = id;
    }
    public String getAuthor(){

        return author;
    }
    public void setAuthor(String author){

        this.author = author;
    }
    public int getPages(){

        return pages;
    }
    public void setPages(int pages){

        this.pages = pages;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name){

        this.name = name;
    }
public String getPackageName(){
    return packageName;
}
    public void setPackageName(String packageName){
        this.packageName = packageName;
    }
}
