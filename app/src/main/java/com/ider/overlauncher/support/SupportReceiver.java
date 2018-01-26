package com.ider.overlauncher.support;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ider.overlauncher.utils.PreferenceManager;

public class SupportReceiver extends BroadcastReceiver {

    private PreferenceManager pmanager;
    public SupportReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        pmanager =  PreferenceManager.getInstance(context);
        pmanager.putString("support_package_name",intent.getStringExtra("packageName"));
        Intent service = new Intent(context, SupportService.class);
        service.setAction(intent.getStringExtra("packageName"));
        context.startService(service);
    }
}
