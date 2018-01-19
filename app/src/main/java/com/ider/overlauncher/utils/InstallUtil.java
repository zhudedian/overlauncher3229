package com.ider.overlauncher.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.ider.overlauncher.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class InstallUtil {

    public static final String TAG = "InstallUtil";
    public static final int INSTALL_SUCCESS = 0;
    public static final int INSTALL_FILE_NOTFOUND = 1;
    public static final int INSTALL_ERROR = 2;

    private InstallUtil() {
        throw new AssertionError();
    }

    /**
     * @param filePath
     * @return 0 means normal, 1 means file not exist, 2 means other exception error
     */
    public static int installSlient(String filePath, Context context, int i) {
        Log.i("install", "path = " + filePath);
        File file;
        if (filePath == null || filePath.length() == 0 || !(file = new File(filePath)).exists() || file.length() <= 0 || !file.isFile()) {
            Log.i("install", "p1111111");
            return 1;
        }

        String[] args = {"pm", "install", "-i", "com.ider.overlauncher", filePath};
        ProcessBuilder processBuilder = new ProcessBuilder(args);

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        int result;
        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;

            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }

            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            Log.i("install", "22222222222");
            e.printStackTrace();
        }finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        // TODO should add memory is not enough here
        if (successMsg.toString().contains("Success")) {
            result = 0;
        } else {
            result = 2;
        }
        Log.i("install", "44444444 result ="+result);
        if(context!=null) {
            if(i!=100) {
                Log.d("install", "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
                Intent intent = new Intent(Constant.ACTION_INSTALLED);
                intent.putExtra("installed_count", i);
                intent.putExtra("installed_result", result);
                context.sendBroadcast(intent);
            }else{
                Log.d("install", "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
                Intent intent = new Intent("com.ider.forced");
                context.sendBroadcast(intent);
            }
        }
        return result;
    }

}