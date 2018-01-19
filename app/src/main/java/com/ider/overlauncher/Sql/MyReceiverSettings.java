package com.ider.overlauncher.Sql;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MyReceiverSettings extends BroadcastReceiver {
    public MyReceiverSettings() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        SharedPreferences preferencese = context.getSharedPreferences("settings",context.MODE_APPEND);
        SharedPreferences.Editor editor = preferencese.edit();
        editor.putBoolean("mykeyevent",true);
        editor.apply();
    }
}
