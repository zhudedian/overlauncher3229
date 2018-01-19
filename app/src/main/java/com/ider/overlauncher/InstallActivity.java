package com.ider.overlauncher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ider.overlauncher.applist.ApplicationUtil;
import com.ider.overlauncher.utils.InstallUtil;
import com.ider.overlauncher.utils.PreferenceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("DefaultLocale")
public class InstallActivity extends Activity {
    private TextView name;
    int finishedCount = 0; // 记录快速安装个数
    int installingIndex = 1; // 正在安装的个数索引
    int totalNum = 0; // APK总数
    private TextView installAgain;
    private List<File> appfiles;
    private TextView cancel;
    String path = "/system/ider"; // 安装路径
    ApplicationUtil appUtil;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtil = new ApplicationUtil();
        preferenceManager = PreferenceManager.getInstance(this);

        Intent inGet = getIntent();
        path = inGet.getStringExtra("path");

        // PreferenceManager默认返回true
        if (preferenceManager.getBoolean("not_installed")) {
            setContentView(R.layout.install_dialog);
            initInstallingView();
            // 开始安装
            beginInstall();
        } else {
            setContentView(R.layout.installed_layout);
            initInstalledView();
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");
        registerReceiver(media, filter);
        filter = new IntentFilter();
        filter.addAction(Constant.ACTION_INSTALLED);
        registerReceiver(installReceiver, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(installReceiver);
        unregisterReceiver(media);
    }

    // 正在安装 初始化界面
    public void initInstallingView() {
        name = (TextView) findViewById(R.id.fileName);
        name.setText("初始化安装环境");

    }

    // 已安装 初始化界面
    public void initInstalledView() {
        installAgain = (TextView) findViewById(R.id.installAgain);
        cancel = (TextView) findViewById(R.id.cancel);
        installAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InstallActivity.this.setContentView(R.layout.install_dialog);
                initInstallingView();

                beginInstall(); // 开始安装
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InstallActivity.this.finish();
            }
        });
    }

    /**
     * 开始安装
     *
     */
    public void beginInstall() {
       appfiles = initApkFiles();
        if(appfiles.size() == 0) {
            this.finish();
            return;
        }
        name.setText(appfiles.get(0).getName() + "\t" + 1 + "/" + totalNum);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i<appfiles.size();i++){
                    InstallUtil.installSlient(appfiles.get(i).getAbsolutePath(),InstallActivity.this,i);
                }
            }
        }).start();


    }

    /**
     * 读取APK文件
     */
    @SuppressLint("DefaultLocale")
    public List<File> initApkFiles() {
        List<File> files = new ArrayList<File>();
        File[] fTmps = new File(path).listFiles();

        if(fTmps == null || fTmps.length == 0) {
            return null;
        }
        String key;
        String value;
        for (int i = 0; i < fTmps.length; i++) {
            String name = fTmps[i].getName().toLowerCase();
            System.out.println("-------------------88"+name);
            String typefile = name.substring(name.lastIndexOf(".")+1, name.length());
            if(typefile.equals("apk")){
                System.out.println("----------name"+name+"filetype="+typefile);
                key = name.substring(0, name.length() - 4);
                value = getApkPackageName(fTmps[i]);
                    if (preferenceManager.getPackage(key) == null)
                        preferenceManager.putString(key, value);
                files.add(fTmps[i]);
            }
        }
        totalNum = files.size(); // APK总个数
        System.out.println("'------------total= "+totalNum);
        return files;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    BroadcastReceiver media = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Intent.ACTION_MEDIA_UNMOUNTED.equals(action)){
                Log.i("Install", "onReceive: ACTION_MEDIA_UNMOUNTED");
                preferenceManager.putBoolean("not_installed", true);
                Intent resultIntent = new Intent();
                InstallActivity.this.setResult(RESULT_CANCELED, resultIntent);
                InstallActivity.this.finish();
            }
        }
    };

    /**
     * 接收安装完一个APK的广播
     */
    BroadcastReceiver installReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
                int count = intent.getIntExtra("installed_count", -1);
                int result = intent.getIntExtra("installed_result", 0);
                Log.i("xxx", "onReceive: ---------------count==" + count);
                String apkName = null;
                if (count != appfiles.size()) {
                    apkName = appfiles.get(count).getName();
                }
                if (apkName != null) {
                    name.setText(apkName + "\t" + (count + 1) + "/" + totalNum);
                    if (result == 0) {
                        Toast.makeText(InstallActivity.this, appfiles.get(count).getName() + "安装成功", Toast.LENGTH_SHORT).show();
                    } else if (result == 2) {
                        Toast.makeText(InstallActivity.this, appfiles.get(count).getName() + "安装失败", Toast.LENGTH_SHORT).show();
                    }
                }
                if (count == totalNum - 1) {
                    preferenceManager.putBoolean("not_installed", false);
                    Intent resultIntent = new Intent();
                    InstallActivity.this.setResult(RESULT_OK, resultIntent);
                    InstallActivity.this.finish();
                }
        }
    };


    /**
     * 从APK文件中读取包名
     *
     * @return
     */
    public String getApkPackageName(File apkFile) {
        PackageInfo info = getPackageManager().getPackageArchiveInfo(
                apkFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
        return info.packageName;
    }

}