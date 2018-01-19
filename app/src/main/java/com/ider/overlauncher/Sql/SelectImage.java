package com.ider.overlauncher.Sql;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.ider.overlauncher.R;
import com.ider.overlauncher.view.SinatvFolder;


/**
 * Created by Eric on 2017/3/13.
 */

public class SelectImage {


    public static void selectImage(Context context,ImageView imageView, String packageName){
        if (packageName.equals("com.android.browser")){
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.apk_browser));
            return;
        }
        if (packageName.equals("com.android.settings")){
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.apk_setting));
            return;
        }
        if (packageName.equals("com.android.music")){
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.apk_music));
            return;
        }
        if (packageName.equals("com.android.rockchip")){
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.apk_filemanager));
            return;
        }
        if (packageName.equals("com.android.vending")){
               imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.apk_google_play));
                return;
        }
        if (packageName.equals("net.fptplay.ottbox")){
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.apk_fpt_play));
            return;
        }
        if (packageName.equals("com.epi.baomoi.touch")){
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.apk_baomoi));
            return;
        }
        if (packageName.equals("org.xbmc.kodi")){
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.apk_xbmc));
            return;
        }
        if (packageName.equals("com.android.providers.downloads.ui")){
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.apk_download));
            return;
        }
        if (packageName.equals("com.google.android.ogyoutube")){
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.apk_youtube));
            return;
        }
        if (packageName.equals("com.android.gallery3d")){
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.apk_gallery));
            return;
        }
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo appInfo = null;
        try {
            appInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setImageDrawable(appInfo.loadIcon(packageManager));
    }
}
