package com.ider.overlauncher.support;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.ider.overlauncher.AppContext;
import com.ider.overlauncher.AppInfo;
import com.ider.overlauncher.FolderActivity;
import com.ider.overlauncher.gson.DataApp;
import com.ider.overlauncher.gson.GsonUtil;
import com.ider.overlauncher.gson.PopupUtil;
import com.ider.overlauncher.gson.UpdatePopup;
import com.ider.overlauncher.utils.AppUtil;
import com.ider.overlauncher.utils.NetUtil;
import com.ider.overlauncher.utils.PreferenceManager;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

import static android.R.attr.start;
import static android.content.ContentValues.TAG;

public class SupportService extends Service {

    private String packageName;
    private PreferenceManager pmanager;
    private SupportView supportView;
    public SupportService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        pmanager =  PreferenceManager.getInstance(this);
        if (intent!=null) {
            packageName = intent.getAction();
        }
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onCreate() {
        super.onCreate();

        supportView = new SupportView(this);
        supportView.createView(new SupportView.ClickListener() {
            @Override
            public void okClick(boolean isOk) {
                if (NetUtil.isNetworkAvailable(AppContext.getAppContext())) {
                    GsonUtil.getDataApps(new GsonUtil.HandleResult() {
                        @Override
                        public void resultHandle(List<DataApp> list) {
                            final DataApp app = AppUtil.getAppForName(pmanager.getPackage("support_package_name"), list);
                            if (app == null) {
                                if (list!=null&&list.size()>0){
                                    startDownloadPage(list.get(0));
                                }
                            }else {
                                startDownloadPage(app);
                            }
                        }
                    });
                };
                supportView.removeView();
                stopSelf();
            }
        });
        registerReceiver();
    }
    private void registerReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("finish_support_service");
        registerReceiver(myReceiver, filter);
    }
    @Override
    public void onDestroy(){
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }
    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
    public void startDownloadPage(DataApp dataApp) {
        Intent intent = new Intent(AppContext.getAppContext(), AppInfo.class);
        Bundle bundle = new Bundle();
        bundle.putString("icon", dataApp.getIconUrl());
        bundle.putString("apk", dataApp.getApkUrl());
        bundle.putString("description", dataApp.getDescriptions());
        bundle.putString("label", dataApp.getLabel());
        bundle.putString("pkg", dataApp.getPackageName());
        bundle.putString("tag", dataApp.getTag());
        bundle.putInt("verCode", dataApp.getVercode());
        bundle.putString("md5", dataApp.getMd5());
        bundle.putBoolean("isCustom",false);
        bundle.putBoolean("isForceDown",true);
        bundle.putBoolean("isOpen6Key",true);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
