package com.ider.overlauncher;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ider.overlauncher.adapter.AppAdapter;
import com.ider.overlauncher.applist.AppListActivity;
import com.ider.overlauncher.db.DbManager;
import com.ider.overlauncher.db.ServerApp;
import com.ider.overlauncher.gson.DataApp;
import com.ider.overlauncher.gson.GsonUtil;
import com.ider.overlauncher.gson.PopupUtil;
import com.ider.overlauncher.gson.UpdatePopup;
import com.ider.overlauncher.model.PackageHolder;
import com.ider.overlauncher.speedTest.SpeedActivity;
import com.ider.overlauncher.utils.ActivityAnimationTool;
import com.ider.overlauncher.utils.AppUtil;
import com.ider.overlauncher.utils.FoldingEffect;
import com.ider.overlauncher.utils.NetUtil;
import com.ider.overlauncher.utils.PreferenceManager;
import com.ider.overlauncher.view.AppSelectWindow;
import com.ider.overlauncher.view.RocketView;

import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * Created by ider-eric on 2017/1/4.
 */

public class FolderActivity extends FullscreenActivity  {

    private static final String TAG = "FolderActivity";
    private Handler mHandler;
    private GridView gridView;
    private AppAdapter appAdapter;
    private TextView title;
    private List<ServerApp> packages;
    private String packagesName;


    String tag;
//    private FocusScaleUtils focusScaleUtils;
//    private FocusUtils focusUtils;
    public boolean isNeedInitMoveHideFocus = true;
    private View view,firstfocus;
    private PreferenceManager pm;
    boolean first = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(this).inflate(R.layout.activity_folder,null);
        setContentView(view);
        mHandler = new Handler();
        pm = PreferenceManager.getInstance(this);

        ActivityAnimationTool.init(new FoldingEffect());
        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        Glide.with(FolderActivity.this).load(R.mipmap.bbb).crossFade(500).into(((ImageView)findViewById(R.id.bg_iv)));
        gridView = (GridView) findViewById(R.id.folder_grid);
        title = (TextView) findViewById(R.id.folder_title);
        firstfocus = findViewById(R.id.firstfocus);

//        focusScaleUtils = new FocusScaleUtils();
//        focusUtils = new FocusUtils(this, view, R.drawable.focusiv, R.drawable.function_back_focus, this.isNeedInitMoveHideFocus);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ServerApp serverApp = (ServerApp) packages.get(i);

                if (serverApp.getType()==2){
                    startDownloadPage(serverApp,false);
                    return;
                }
                final PackageHolder holder = ((PackageHolder) packages.get(i));
                if(holder.getPackageName().equals("add")) {
                    showAppSelectWindow();
                }else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_APPS)){
                    Intent intent = new Intent(FolderActivity.this,AppListActivity.class);
                    ActivityAnimationTool.startActivity(FolderActivity.this,intent);
                }else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_SEARCH)){
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(Constant.ACTION_SEARCH));
                    handleIntent(intent);
                }else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_HISTORY)){
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(Constant.ACTION_HIS));
                    handleIntent(intent);
                }else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_CLEAN)){
                    RocketView Rocketview = new RocketView(FolderActivity.this.getApplicationContext());
                    Rocketview.CreateView();
                    Rocketview = null;
                }else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_FASTKEY)){
                    Intent intent = new Intent(FolderActivity.this,FastKey.class);
                    ActivityAnimationTool.startActivity(FolderActivity.this,intent);
                }else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_WEATHER)){
                    Intent intent = new Intent(FolderActivity.this,CityPicker.class);
                    ActivityAnimationTool.startActivity(FolderActivity.this,intent);
                }else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_SPEED)){
                    Intent intent = new Intent(FolderActivity.this,SpeedActivity.class);
                    ActivityAnimationTool.startActivity(FolderActivity.this,intent);
                }else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_NETSET)){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.zxy.idersettings","com.rk_itvui.settings.network_settingnew"));
                   startActivity(intent);
                }else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_DATE)){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.zxy.idersettings","com.rk_itvui.settings.datetime.DateTimeSetting"));
                    startActivity(intent);
                }
                else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_SOUND)){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.zxy.idersettings","com.rk_itvui.settings.sound.SoundSetting"));
                    startActivity(intent);
                }
                else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_DISPLAY)){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.zxy.idersettings","com.rk_itvui.settings.ScreensSettings"));
                    startActivity(intent);
                }
                else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_FLASH)){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.zxy.idersettings","com.rk_itvui.settings.storeinfo.Storage"));
                    startActivity(intent);
                }
                else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_IME)){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.zxy.idersettings","com.rk_itvui.settings.language.LanguageInputmethod"));
                    startActivity(intent);
                }
                else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_INFO)){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.zxy.idersettings","com.rk_itvui.settings.deviceversion.Deviceversion"));
                    startActivity(intent);
                }
                else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_FACTORY)){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.zxy.idersettings","com.rk_itvui.settings.factoryreset.Factoryreset"));
                    startActivity(intent);
                }
                else if(holder.getPackageName().equals(Constant.ACTION_LOGIN)){
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(Constant.ACTION_LOGIN));
                    handleIntent(intent);

                }
                else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_APPMANAGER)){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setComponent(new ComponentName("com.zxy.idersettings","com.rk_itvui.settings.dialog.ManageApplications"));
                    startActivity(intent);
                }
                else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_DEVELOPE)){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.zxy.idersettings","com.rk_itvui.settings.developer.DevelopmentSettings"));
                    startActivity(intent);
                } else if(holder.getPackageName().equals(Constant.PACKAGE_TOOL_MORE)){
                    Intent intent = new Intent();
                    Intent intent_more = new Intent("android.settings.SETTINGS");
                    startActivity(intent_more);
                }

                else {
                    if (NetUtil.isNetworkAvailable(AppContext.getAppContext())) {
                        GsonUtil.getDataApps(new GsonUtil.HandleResult() {
                            @Override
                            public void resultHandle(List<DataApp> list) {
                                final DataApp app = AppUtil.isNeedUpdate(holder.getPackageName(), list);
                                if (app == null) {
                                    Intent intent = getPackageManager().getLaunchIntentForPackage(holder.getPackageName());
                                    Log.i(TAG, "onItemClick: " + holder.getPackageName());
                                    if (intent != null) {
                                        startActivity(intent);
                                    }
                                }else {
                                    PopupUtil.getUpdatePopup(FolderActivity.this, app.getLabel(), app.getIconUrl(), new UpdatePopup.OnOkListener() {
                                        @Override
                                        public void onOkClick(boolean isOk) {
                                            if (isOk){
                                                startDownloadPage(app);
                                            }else {
                                                Intent intent = getPackageManager().getLaunchIntentForPackage(holder.getPackageName());
                                                Log.i(TAG, "onItemClick: " + holder.getPackageName());
                                                if (intent != null) {
                                                    startActivity(intent);
                                                }
                                            }
                                            PopupUtil.forceDismissPopup();
                                        }
                                    }).show(gridView);
                                }
                            }
                        });
                    }else {
                        Intent intent = getPackageManager().getLaunchIntentForPackage(holder.getPackageName());
                        Log.i(TAG, "onItemClick: " + holder.getPackageName());
                        if (intent != null) {
                            startActivity(intent);
                        }
                    }

                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position >= packages.size()-1) {
                    return true;
                }
                if(tag.equals("01")){
                    if(position <= 8) {
                        return true;
                    }
                }
                ServerApp serverApp = (ServerApp) packages.get(position);

                if (serverApp.getType()==2){
                    return true;
                }
//                if(tag.equals("02")){
//                    return true;
//                }
//
//                if(tag.equals("04")){
//                    if(position <= 4) {
//                        return true;
//                    }
//                }
//                if(tag.equals("05")){
//                        return true;
//                }
                // 数据库操作类对象
                DbManager.getInstance(getApplicationContext()).removePackage((PackageHolder) packages.get(position));
                packages.remove(position);
                appAdapter.notifyDataSetChanged();
                return true;
            }
        });
//

        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
//                    v.bringToFront();
//                    if(v!=null) {
//                        focusUtils.startMoveFocus(v, true, 1.2F);
//                        focusScaleUtils.scaleToLarge(v);
//                        firstfocus.setVisibility(View.INVISIBLE);
//                        Log.i("zzz", "onItemSelected: 0000");
//                    }
                if(tag.equals("02")||tag.equals("03")){
                    if(i<0){
                        findViewById(R.id.tps).setVisibility(View.GONE);
                    }else{
                        if(!"add".equals(packages.get(i).getPackageName())&&packages.get(i).getType()!=2) {
                            findViewById(R.id.tps).setVisibility(View.VISIBLE);
                        }else{
                            findViewById(R.id.tps).setVisibility(View.INVISIBLE);
                        }
                    }
                }else if (tag.equals("01")) {
                        if (i<9) {
                        findViewById(R.id.tps).setVisibility(View.INVISIBLE);
                    } else {
                        if (!"add".equals(packages.get(i).getPackageName())) {
                            findViewById(R.id.tps).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.tps).setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                   //focusScaleUtils.scaleToNormal();
            }
        });

        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
//                    focusUtils.startMoveFocus(view, true, 1.2F);
//                    focusScaleUtils.scaleToLarge(view);
                    firstfocus.setVisibility(View.VISIBLE);
                }else{
                    firstfocus.setVisibility(View.GONE);
                }
            }
        });
        title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//               if (!tag.equals("01")) {
//                    showRenameTabDialog();
//                }
//                if (!tag.equals("02")) {
//                    showRenameTabDialog();
//                }
                return true;
            }
        });

        registerReceiver(mHomeKeyEventReceiver,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
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
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);

    }
    @Override
    public void onResume(){
        super.onResume();
        setTitleName(tag);
    }
    public void startDownloadPage(ServerApp serverApp, boolean isCustom) {
        Intent intent = new Intent(AppContext.getAppContext(), AppInfo.class);
        Bundle bundle = new Bundle();
        bundle.putString("icon", serverApp.getIconUrl());
        bundle.putString("apk",serverApp.getApkUrl());
        bundle.putString("description", serverApp.getDescription());
        bundle.putString("label", serverApp.getLabel());
        bundle.putString("pkg", serverApp.getPackageName());
        bundle.putString("tag", tag);
        bundle.putInt("verCode", serverApp.getVerCode());
        bundle.putString("md5", serverApp.getMd5());
        bundle.putBoolean("isCustom",isCustom);
        intent.putExtras(bundle);
        if(isCustom){
            startActivityForResult(intent,2);
        }else {
            startActivityForResult(intent, 1);
        }
    }
    @Override
    protected void onDestroy() {
        try{
            unregisterReceiver(mHomeKeyEventReceiver);
        }catch (Exception e){

        }
        super.onDestroy();
    }
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
                    FolderActivity.this.finish();
                }else if(TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)){
                    //表示长按home键,显示最近使用的程序列表
                    FolderActivity.this.finish();
                }
            }
        }
    };

    private void handleIntent(Intent intent) {
        // 隐式调用的方式startActivity
        //intent.setAction("com.tencent.qqlivetv.open");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.yidian.calendar");//设置视频包名，要先确认包名
        PackageManager packageManager = FolderActivity.this.getPackageManager();
        List<ResolveInfo> activities = packageManager
                .queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            FolderActivity.this.startActivity(intent);
        } else {
            Toast.makeText(FolderActivity.this, "未安装腾讯视频 ， 无法跳转", Toast.LENGTH_SHORT).show();
        }
    }
    public void setTitleName(String tag ){
        this.tag = tag;
        String currentTitle = pm.getPackage(tag+"zxy");
        if(currentTitle==null) {
            if (tag.equals("01")) {
                title.setText(getResources().getString(R.string.common));
            }
            if (tag.equals("02")) {
                title.setText("电视");
            }
            if (tag.equals("03")) {
                title.setText("影视");
            }

        }else{
            title.setText(currentTitle);
        }
        packages = DbManager.getInstance(getApplicationContext()).queryPackages(tag);
        if(tag.equals("01")){
            packages.add(0,new PackageHolder(Constant.PACKAGE_TOOL_SETTING,"01"));
            packages.add(1,new PackageHolder(Constant.ACTION_HROCKCHIP,"01"));
            packages.add(2,new PackageHolder(Constant.PACKAGE_TOOL_APPS,"01"));
            packages.add(3,new PackageHolder(Constant.PACKAGE_TOOL_CLEAN,"01"));
            packages.add(4,new PackageHolder(Constant.PACKAGE_TOOL_SPEED,"01"));
            packages.add(5,new PackageHolder(Constant.ACTION_LOGIN,"01"));
            packages.add(6,new PackageHolder(Constant.PACKAGE_TOOL_FASTKEY,"01"));
          packages.add(7,new PackageHolder("com.ider.mouse","01"));
          packages.add(8,new PackageHolder("com.ider.update","01"));
//          packages.add(9,new PackageHolder(Constant.PACKAGE_TOOL_DEVELOPE,"01"));
//          packages.add(10,new PackageHolder(Constant.PACKAGE_TOOL_MORE,"01"));
//          packages.add(11,new PackageHolder(Constant.PACKAGE_TOOL_WEATHER,"01"));
//          packages.add(12,new PackageHolder(Constant.PACKAGE_TOOL_FASTKEY,"01"));
        }
        if (tag.equals("02")){
            boolean isDataSave = pm.isDataSave();
            if (isDataSave){
                List<ServerApp> serverApps = DataSupport.where("tag = ?","tv").find(ServerApp.class);
                for (ServerApp app:serverApps) {
                    if (!app.containsPackageName(packages)) {
                        packages.add(app);
                    }
                }
            }
        }else if (tag.equals("03")){
            boolean isDataSave = pm.isDataSave();
            if (isDataSave){
                List<ServerApp> serverApps = DataSupport.where("tag = ?","movie").find(ServerApp.class);
                for (ServerApp app:serverApps) {
                    if (!app.containsPackageName(packages)) {
                        packages.add(app);
                    }
                }
            }
        }

        packages.add(new PackageHolder(0L, "add", tag));


        appAdapter = null;
        appAdapter = new AppAdapter(this, packages);
        gridView.setAdapter(appAdapter);
        mHandler.postDelayed(showApp, 300);
    }
    Runnable showApp = new Runnable() {
        @Override
        public void run() {
            //gridView.setAdapter(appAdapter);
            if(gridView.getChildAt(0)!=null) {
                gridView.getChildAt(0).requestFocus();
//                focusUtils.startMoveFocus(gridView.getChildAt(0), true, 1.2F);
//                focusScaleUtils.scaleToLarge(gridView.getChildAt(0));
            }
        }
    };

    Runnable hideApp = new Runnable() {
        @Override
        public void run() {
            packages.clear();
            appAdapter.notifyDataSetChanged();
            FolderActivity.super.onBackPressed();
        }
    };

    @Override
    public void onBackPressed() {
        mHandler.postDelayed(hideApp, 0);
    }

    public void showAppSelectWindow() {
        AppSelectWindow appSelectWindow = AppSelectWindow.getInstance(this);
        appSelectWindow.setData(packages);
        appSelectWindow.setOnAppSelectListener(new AppSelectWindow.OnAppSelectListener() {
            @Override
            public void onAppSelected(PackageHolder holder) {
                if(!holder.getPackageName().equals("add")) {
                    packages.add(packages.size() - 1, holder);
                    appAdapter.notifyDataSetChanged();
                    holder.setTag(tag);
                    Log.i("zzz", "onAppSelected: " + holder.getPackageName());
                    DbManager.getInstance(getApplicationContext()).insertPackage(holder);
                }
            }
        });
        appSelectWindow.showAppPopWindow(gridView);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
//                    View view = gridView.getSelectedView();
//            if(view!=null) {
//                if (view.getNextFocusRightId() == -1 && view.getId() != -1) {
//                    //right
//                    int tagnext = Integer.parseInt(tag.substring(1, 2)) + 1;
//                    Log.i("zzz", "onKeyDown: tagnext==" + tagnext);
//                    if (tagnext == 6) {
//                        tagnext = 1;
//                    }
//                    setTitleName("0" + tagnext);
//                }
//            }
//        }
//        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
//            View view = gridView.getSelectedView();
//            if(view!=null) {
//                if (view.getNextFocusLeftId() == -1 && view.getId() != -1) {
//                    //left
//                    int tagnext = Integer.parseInt(tag.substring(1, 2)) - 1;
//                    if (tagnext == 0) {
//                        tagnext = 5;
//                    }
//                    setTitleName("0" + tagnext);
//                }
//            }
//        }

        return super.onKeyDown(keyCode, event);
    }

    public void showRenameTabDialog() {
        final EditText editText = new EditText(FolderActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(editText);
        builder.setTitle(R.string.Rename);
        builder.setPositiveButton(R.string.Ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        Editable text = editText.getText();
                        if (text != null) {
                            title.setText(text
                                    .toString());
                            pm.putString(tag+"zxy",text.toString());

                        } else
                            Toast.makeText(FolderActivity.this,
                                    R.string.NullText, Toast.LENGTH_SHORT)
                                    .show();
                    }
                });
        builder.setNegativeButton(R.string.Cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        builder.create().show();
    }
}