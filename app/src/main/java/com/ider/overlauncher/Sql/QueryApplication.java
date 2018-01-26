package com.ider.overlauncher.Sql;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.ider.overlauncher.db.ServerApp;
import com.ider.overlauncher.model.PackageHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2017/3/13.
 */

public class QueryApplication {
    public static List<ServerApp> query(Context context) {
        List<ServerApp> enties = new ArrayList<>();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String packageName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (!packageName.equals("com.ider.tools")&&!packageName.equals("com.android.settings")
            &&!packageName.equals("com.yidian.calendar")) {
                enties.add(new PackageHolder(packageName, null));
            }

        }
        return enties;
    }
}
