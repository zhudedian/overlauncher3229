package com.ider.overlauncher.utils;

import android.app.Service;
import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;

import com.ider.overlauncher.AppContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Eric on 2018/1/20.
 */

public class UsbUtil {
    private static String TAG = "UsbUtil";
    public static boolean checkAmlogic6Usb() {
        StorageManager mStorageManager = (StorageManager) AppContext.getAppContext().getSystemService(Service.STORAGE_SERVICE);
        try {
            Class StorageManager = Class.forName("android.os.storage.StorageManager");
            Class VolumeInfo = Class.forName("android.os.storage.VolumeInfo");
            Class DiskInfo = Class.forName("android.os.storage.DiskInfo");

            Method getVolumes = StorageManager.getMethod("getVolumes");
            Method isMountedReadable = VolumeInfo.getMethod("isMountedReadable");
            Method getType = VolumeInfo.getMethod("getType");
            Method getDisk = VolumeInfo.getMethod("getDisk");


            Method isUsb = DiskInfo.getMethod("isUsb");
            Method getDescription = DiskInfo.getMethod("getDescription");
            List volumes = (List) getVolumes.invoke(mStorageManager);
            for(int i = 0; i < volumes.size(); i++) {
                if(volumes.get(i) != null && (boolean) isMountedReadable.invoke(volumes.get(i))
                        && (int) getType.invoke(volumes.get(i)) == 0) {
                    Object diskInfo = getDisk.invoke(volumes.get(i));
                    boolean usbExists = (boolean) isUsb.invoke(diskInfo);
                    String description = (String) getDescription.invoke(diskInfo);
                    Log.i(TAG, "isUsbExists: " + usbExists + ":" + description);
                    if(usbExists) {
                        return true;
                    }
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }
    public static boolean checkAmlogic6Sd(){
        StorageManager mStorageManager = (StorageManager) AppContext.getAppContext().getSystemService(Service.STORAGE_SERVICE);
        try {
            Class StorageManager = Class.forName("android.os.storage.StorageManager");
            Class VolumeInfo = Class.forName("android.os.storage.VolumeInfo");
            Class DiskInfo = Class.forName("android.os.storage.DiskInfo");

            Method getVolumes = StorageManager.getMethod("getVolumes");
            Method isMountedReadable = VolumeInfo.getMethod("isMountedReadable");
            Method getType = VolumeInfo.getMethod("getType");
            Method getDisk = VolumeInfo.getMethod("getDisk");


            Method isSd = DiskInfo.getMethod("isSd");
            Method getDescription = DiskInfo.getMethod("getDescription");
            List volumes = (List) getVolumes.invoke(mStorageManager);
            Log.i(TAG, "isSdExists: " );
            for(int i = 0; i < volumes.size(); i++) {
                if(volumes.get(i) != null && (boolean) isMountedReadable.invoke(volumes.get(i))
                        && (int) getType.invoke(volumes.get(i)) == 0) {
                    Object diskInfo = getDisk.invoke(volumes.get(i));

                    boolean sdExists = (boolean) isSd.invoke(diskInfo);
                    String description = (String) getDescription.invoke(diskInfo);
                    Log.i(TAG, "isSdExists: " + sdExists + ":" + description);
                    if(sdExists) {
                        return true;
                    }
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
