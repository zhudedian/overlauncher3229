package com.ider.overlauncher;

import android.content.Context;

import org.litepal.LitePalApplication;


/**
 * Created by zxy on 2017/3/22.
 */

public class AppContext extends android.app.Application{
    private static Context context;

    public void onCreate() {
        super.onCreate();
        AppContext.context = getApplicationContext();
        LitePalApplication.initialize(context);
    }
    public static Context getAppContext() {
        return AppContext.context;
    }
}
