package com.ider.overlauncher.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ider.overlauncher.AppContext;
import com.ider.overlauncher.applist.Application;
import com.ider.overlauncher.applist.ApplicationUtil;
import com.ider.overlauncher.services.ConfigApp;
import com.ider.overlauncher.services.ConfigPic;
import com.ider.overlauncher.services.TagConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ider-eric on 2016/11/23.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    private static DBManager manager;

    private String NULL = "null";
    private DBManager(Context context) {

        helper = new DBHelper(context);
        db = helper.getWritableDatabase();

    }

    public static DBManager getInstance(Context context) {
        if (manager == null) {
            manager = new DBManager(context);
        }
        return manager;
    }

    private ContentValues createContentValue(TagConfig config, boolean forInsert) {
        ContentValues cv = new ContentValues();
        if (forInsert) {
            cv.put("tag", config.tag);
        }
        cv.put("status", config.status);
        if (config instanceof ConfigApp) {
            ConfigApp app = (ConfigApp) config;
            cv.put("pkgname", app.pkgName);
            cv.put("label", app.label);
            cv.put("md5", app.md5);
            cv.put("summary", app.summary);
            cv.put("description", app.description);
            cv.put("iconurl", app.iconUrl);
            cv.put("apkurl", app.apkUrl);
            cv.put("vercode", app.verCode);
            cv.put("force", app.forced);
            cv.put("miniimage", NULL);
            cv.put("image", NULL);
        } else {
            ConfigPic pic = (ConfigPic) config;
            cv.put("pkgname", NULL);
            cv.put("label", NULL);
            cv.put("md5", NULL);
            cv.put("summary", NULL);
            cv.put("description", NULL);
            cv.put("iconurl", NULL);
            cv.put("apkurl", NULL);
            cv.put("vercode", 0);
            cv.put("force", false);
            cv.put("miniimage", pic.miniUrl);
            cv.put("image", pic.imageUrl);
        }

        return cv;
    }


    private void updateConfig(TagConfig config) {
        db.beginTransaction();
        ContentValues cv = createContentValue(config, false);
        String[] values = new String[]{ config.tag };
        String where = "tag=?";
        db.update("configs", cv, where, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    private void insertConfig(TagConfig config) {
        db.beginTransaction();
        ContentValues cv = createContentValue(config, true);
        db.insert("configs", null, cv);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void checkAndInsertApp(TagConfig config) {
        TagConfig existsConfig = queryConfig(config.tag);
        if(existsConfig == null) {
            insertConfig(config);
        } else if (!existsConfig.equals(config)) {
            updateConfig(config);
        }
    }

    public void insertConfigs(ArrayList<TagConfig> configs) {
        deleteAllApps();
        for (TagConfig config : configs) {
            if(config.isAvailable()) {
                insertConfig(config);
            }
        }
    }

    public TagConfig queryConfig(String tag) {
        db.beginTransaction();
        TagConfig config = null;
        Cursor cursor = db.rawQuery("select * from configs where tag=?", new String[]{tag});
        if (cursor.moveToNext()) {
            Log.i("dbmanager", "move to next");
            int status = cursor.getInt(2);
            if(status == TagConfig.STATUS_APP) {
                config = new ConfigApp();
                config.tag = tag;
                config.status = status;
                ((ConfigApp)config).pkgName = cursor.getString(3);
                ((ConfigApp)config).label = cursor.getString(4);
                ((ConfigApp)config).md5 = cursor.getString(5);
                ((ConfigApp)config).summary = cursor.getString(6);
                ((ConfigApp)config).description = cursor.getString(7);
                ((ConfigApp)config).iconUrl = cursor.getString(8);
                ((ConfigApp)config).apkUrl = cursor.getString(9);
                ((ConfigApp)config).verCode = cursor.getShort(10);
                ((ConfigApp)config).forced = Boolean.parseBoolean(cursor.getString(11));

            } else {
                config = new ConfigPic();
                config.tag = tag;
                config.status = status;
                ((ConfigPic)config).miniUrl = cursor.getString(12);
                ((ConfigPic)config).imageUrl = cursor.getString(13);
            }

        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return config;
    }


    public void deleteAllApps() {
        db.beginTransaction();
        db.execSQL("delete from configs");
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<TagConfig> findAllConfig(){
        ArrayList<TagConfig> LocalList = null;
        TagConfig config = null;
        Cursor cursor = db.query("configs",null,null,null,null,null,null);
        if(cursor != null) {
            LocalList = new ArrayList<TagConfig>();
            while(cursor.moveToNext()){
                int status = cursor.getInt(2);
                if(status == TagConfig.STATUS_APP) {
                    config = new ConfigApp();
                    config.tag = cursor.getString(1);
                    config.status = status;
                    ((ConfigApp)config).pkgName = cursor.getString(3);
                    ((ConfigApp)config).label = cursor.getString(4);
                    ((ConfigApp)config).md5 = cursor.getString(5);
                    ((ConfigApp)config).summary = cursor.getString(6);
                    ((ConfigApp)config).description = cursor.getString(7);
                    ((ConfigApp)config).iconUrl = cursor.getString(8);
                    ((ConfigApp)config).apkUrl = cursor.getString(9);
                    ((ConfigApp)config).verCode = cursor.getShort(10);
                    ((ConfigApp)config).forced = Boolean.parseBoolean(cursor.getString(11));
                }else {
                    config = new ConfigPic();
                    config.tag = cursor.getString(1);
                    config.status = status;
                    ((ConfigPic)config).miniUrl = cursor.getString(12);
                    ((ConfigPic)config).imageUrl = cursor.getString(13);
                }
                LocalList.add(config);
            }
        }
        return LocalList;
    }


    //app downaload
    public void insertApp(String packageName){
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("pkgname", packageName);
        db.insert("downloads", null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteApp(String packageName) {
        db.beginTransaction();
        db.delete("downloads","pkgname=?",new String[]{packageName});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public List<Application> FindAllApp(){
        Cursor cursor = db.query("downloads",null,null,null,null,null,null);
        List<Application> apps  = new ArrayList<Application>();
        apps.add(ApplicationUtil.doAddApplication(AppContext.getAppContext()));
        if(cursor != null) {
            while(cursor.moveToNext()){
                String packageName = cursor.getString(1);
                Application app = ApplicationUtil.doApplication(AppContext.getAppContext(),packageName);
                if(app!=null) {
                    apps.add(app);
                }else{
                   // deleteApp(packageName);
                }
            }
            return  apps;
        }
        return  apps;
    }


}
