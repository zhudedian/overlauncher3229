package com.ider.overlauncher.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


import com.ider.overlauncher.databases.DBManager;
import com.ider.overlauncher.utils.NetUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by ider-eric on 2016/11/22.
 */

public class ConfigService extends Service implements IService {

    private boolean DEBUG = true;
    private String TAG = "ConfigService";

    private void LOG(String log) {
        if (DEBUG) Log.i(TAG, log);
    }
    public boolean isBinded = false;

    private static Context mContext;
    private MBinder binder;
    private DBManager dbManager;

    public static final String ACTION_CONFIG_UPDATE = "com.ider.action.config_update";

    private IServicePresenter servicePresenter;

    private static Handler mHandler = new Handler();
    private static ServerRunnable serverRunnable;

    private static class ServerRunnable implements Runnable {
        WeakReference<ConfigService> weakReference;

        private ServerRunnable(ConfigService service) {
            weakReference = new WeakReference<ConfigService>(service);
        }

        @Override
        public void run() {
            ConfigService service = weakReference.get();
            if(service != null) {
                service.checkUpdate(false);
            }
        }
    }


    public void checkUpdate(boolean force) {
        if (NetUtil.isNetworkAvailable(getApplicationContext())) {
            servicePresenter.checkUpdate(force);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        binder = new MBinder();
        serverRunnable = new ServerRunnable(ConfigService.this);
        servicePresenter = new ServicePresenter(this);
        dbManager = DBManager.getInstance(mContext);
    }


    @Override
    public IBinder onBind(Intent intent) {
        isBinded = true;
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isBinded = false;
        return super.onUnbind(intent);
    }

    public class MBinder extends Binder {
        public ConfigService getService() {
            return ConfigService.this;
        }
    }

    @Override
    public void requestSuccess(ArrayList<TagConfig> configs) {
        dbManager.insertConfigs(configs);
        Intent intent = new Intent();
        intent.setAction(ACTION_CONFIG_UPDATE);
        intent.putParcelableArrayListExtra("configs", configs);
        Log.i(TAG, "requestSuccess: configsize=="+configs.size());
        sendBroadcast(intent);
    }

    @Override
    public void requestLater(int delay) {
        mHandler.removeCallbacks(serverRunnable);
        mHandler.postDelayed(serverRunnable, delay);
    }



}
