package com.ider.overlauncher.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ider.overlauncher.AppContext;
import com.ider.overlauncher.db.Data;
import com.ider.overlauncher.gson.DataApp;
import com.ider.overlauncher.gson.GsonUtil;

import java.util.List;

import static android.R.attr.versionCode;


/**
 * Created by Eric on 2018/1/8.
 */

public class AppUtil {
    public static DataApp isNeedUpdate(String packageName, List<DataApp> list){
        if (list==null){
            return null;
        }
        for (DataApp app:list){
            Log.i("AppUtil","app.getVercode()="+app.getVercode()+app.getPackageName()+"packageName="+packageName);
            if (app.getPackageName().equals(packageName)){
                Log.i("AppUtil","app.getVercode()="+app.getVercode()+"getAppVerCode(packageName)="+getAppVerCode(packageName));
                if (app.getVercode()>getAppVerCode(packageName)){
                    return app;
                }
            }
        }
        return null;
    }
    public static int getAppVerCode(String packageName){
        int versionCode = 0;
        if (packageName != null) {
            PackageInfo packageInfo;
            try {
                packageInfo = AppContext.getAppContext().getPackageManager().getPackageInfo(packageName,0);
            }catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
                packageInfo = null;
            }
            if (packageInfo!=null) {
                versionCode = packageInfo.versionCode;
            }else {
                versionCode = 0;
            }
        }
        return versionCode;
    }
}
