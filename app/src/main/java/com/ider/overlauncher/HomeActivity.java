package com.ider.overlauncher;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.ider.overlauncher.adapter.TvGridAdapter;
import com.ider.overlauncher.adapter.TvGridDownloadAdapter;
import com.ider.overlauncher.applist.AppListActivity;
import com.ider.overlauncher.applist.Application;
import com.ider.overlauncher.applist.ApplicationUtil;
import com.ider.overlauncher.databases.DBManager;
import com.ider.overlauncher.db.DbManager;
import com.ider.overlauncher.db.ServerApp;
import com.ider.overlauncher.gson.DataApp;
import com.ider.overlauncher.gson.FileCopy;
import com.ider.overlauncher.gson.GsonUtil;
import com.ider.overlauncher.gson.PopupUtil;
import com.ider.overlauncher.gson.UpdatePopup;
import com.ider.overlauncher.model.PackageHolder;
import com.ider.overlauncher.services.ConfigApp;
import com.ider.overlauncher.services.ConfigService;
import com.ider.overlauncher.services.TagConfig;
import com.ider.overlauncher.utils.ActivityAnimationTool;
import com.ider.overlauncher.utils.AppUtil;
import com.ider.overlauncher.utils.DownloadUtil;
import com.ider.overlauncher.utils.FocusScaleUtils;
import com.ider.overlauncher.utils.FocusUtils;
import com.ider.overlauncher.utils.NetUtil;
import com.ider.overlauncher.utils.PreferenceManager;
import com.ider.overlauncher.view.AppSelectWindow;
import com.ider.overlauncher.view.CompareImageView;
import com.ider.overlauncher.view.MyRelative;
import com.ider.overlauncher.view.RocketView;
import com.ider.overlauncher.view.ShortcutFolder;
import com.ider.overlauncher.view.SinatvFolder;
import com.ider.overlauncher.view.StateBar;
import com.ider.overlauncher.view.TopDialog;
import com.ider.overlauncher.view.TvHorizontalGridView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import oversettings.ider.com.cust.DeskService;
import oversettings.ider.com.deskcust.DeskConstant;
import oversettings.ider.com.deskcust.IDeskService;
import oversettings.ider.com.deskcust.PageEntry;

//import com.ider.overlauncher.view.MyGallery;


public class HomeActivity extends AppCompatActivity implements View.OnFocusChangeListener,GalleryScrollListener,MyRelative.FucosListener {
        private static final String TAG ="HomeActivity" ;
        private static PreferenceManager preferences;
        private View homeView;
         private TextView week;
        public  FocusScaleUtils focusScaleUtils;
        public  FocusUtils focusUtils;
    private View lastFocusView;
        public boolean isNeedInitMoveHideFocus = true;
        public static IDeskService service;
        private boolean isDeskUpdate = false;
    //    private MyGallery gallery;
        private List<CompareImageView> defaultChilds;
    //    private ScrollTextView title_sv;
        private TextView titles;
        private ShortcutFolder[] viewsfolder;
        private ConfigService configService;
        private  List<TagConfig> loaclTag;
        private DBManager dbManager;
        private boolean isIderUpdate = false,isShow=false,isTopShow=false;
        private TvHorizontalGridView downloadGrid,serverGrid;
        private BottomView bottomView;
        private TopDialog view_mytop;
        private TvGridAdapter serverAdater;
        private TvGridDownloadAdapter downloadAdater;
        private List<Application> apps;
        private ImageView blurBackground;
        private PreferenceManager pmanager;
        private ViewPropertyAnimator topDialogAnimator;
        private Handler handler = new Handler();
        private AlertDialog dialog =null;
        private StateBar stateBar;
        private boolean mykeyevent;
        private MyRelative myRelative,myRelative2,myRelative3,myRelative4,myRelative5,myRelative6;
        private SinatvFolder sinatv,sinatv1,sinatv2,sinatv3;
        private TextView mysinatvtext;



        final static private int[] LOG_DB =
                {KeyEvent.KEYCODE_6,KeyEvent.KEYCODE_5,KeyEvent.KEYCODE_4,
                        KeyEvent.KEYCODE_3,KeyEvent.KEYCODE_2,KeyEvent.KEYCODE_1
                };
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            homeView = LayoutInflater.from(this).inflate(R.layout.activity_home,null);
            setContentView(homeView);
            pmanager = PreferenceManager.getInstance(AppContext.getAppContext());
            dbManager = DBManager.getInstance(AppContext.getAppContext());
            pmanager.setLocalServiceVersion(-1);
    //        ActivityAnimationTool.init(new FoldingEffect());
            focusScaleUtils = new FocusScaleUtils();
            focusUtils = new FocusUtils(this, homeView, R.drawable.focusiv, R.drawable.function_back_focus, this.isNeedInitMoveHideFocus);
            if (pmanager.ifConfigServiceOn()) {
                bindServices();
            }
            initView();
            bindMyService();
            registReceivers();
            showData();
    //        startHelp();
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }else {

            }
            FastKey.preManager= PreferenceManager.getInstance(AppContext.getAppContext());
//            FileCopy.installSlient();
//            FileCopy.copy("/storage/9AC4-8BCB/bootanimation.ts","/data/local/misc/bootanimation.ts");
//            try {
//                Runtime.getRuntime().exec("chmod 644 " + "/data/local/misc/bootanimation.ts");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }


        @Override
        protected void onResume() {
          //  handler.postDelayed(help,Constant.START_HELP_TIME);
    //        gallery.changedScroll(true);
    //        stateBar.initClock();
            if(NetUtil.isNetworkAvailable(HomeActivity.this)){
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getNetTime();
                    }
                }.start();
            }
            handler.removeCallbacks(keynumStart);
            handler.removeCallbacks(key6Start);
            if (mykeyevent){
                handler.removeCallbacks(keynumStart);
                if (!pmanager.ifConfigServiceOn()&&NetUtil.isNetworkAvailable(AppContext.getAppContext())) {
                    bindServices();
                    pmanager.setConfigServiceOn();
                }
                // 开启后台配置桌面↑
                //点击发送广播显示APK↓
                Intent intent = new Intent("con.ider.overlauncher.LAUNCHER");
                sendBroadcast(intent);
            }
            mass_pwd=0;
            super.onResume();

        }

        @Override
        protected void onPause() {
    //        gallery.changedScroll(false);
           // handler.removeCallbacks(help);
            super.onPause();
        }

        private void initView() {
            viewsfolder = new ShortcutFolder[3];
            viewsfolder[0] = (ShortcutFolder) findViewById(R.id.folder1);
           viewsfolder[1] = (ShortcutFolder) findViewById(R.id.folder2);
            viewsfolder[2]= (ShortcutFolder)findViewById(R.id.movie_folder);
    //        viewsfolder[2] = (ShortcutFolder) findViewById(R.id.folder3);
    //        viewsfolder[3] = (ShortcutFolder) findViewById(R.id.folder4);
    //        viewsfolder[4] = (ShortcutFolder) findViewById(R.id.folder5);
    //        gallery = (MyGallery)findViewById(R.id.gallery);
    //        defaultBlocks();
    //        gallery.setChilds(defaultChilds);
    //       gallery.setOnGalleryScrollListener(this);
    //        gallery.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    //            @Override
    //            public void onFocusChange(View view, boolean b) {
    //                if(b){
    //                    hiddenGalleryFocus(false);
    //                    if(isShow){
    //                        hideTopDialog(true);
    //                    }
    //                    if(isTopShow){
    //                        hideTopDialog(false);
    //                    }
    //                }
    //            }
    //        });
    //        title_sv = (ScrollTextView) findViewById(R.id.title_sv);
            titles = (TextView) findViewById(R.id.episode_tv);
            bottomView = (BottomView) findViewById(R.id.view_mybottom);
            serverGrid = (TvHorizontalGridView) bottomView.findViewById(R.id.grid_notdownload);
            downloadGrid = (TvHorizontalGridView) bottomView.findViewById(R.id.grid_download);
            blurBackground = (ImageView) findViewById(R.id.blur_bg);
            stateBar = (StateBar) findViewById(R.id.statebar);
            view_mytop = (TopDialog) findViewById(R.id.view_mytop);
            myRelative = (MyRelative)findViewById( R.id.myrelative);
            myRelative2 = (MyRelative)findViewById( R.id.myrelative2);
            myRelative3 = (MyRelative)findViewById( R.id.myrelative3);
            myRelative4 = (MyRelative)findViewById( R.id.myrelative4);
            myRelative5 = (MyRelative)findViewById( R.id.myrelative5);
            myRelative6 = (MyRelative)findViewById( R.id.myrelative6);

            week = (TextView)findViewById(R.id.week);
            sinatv= (SinatvFolder) findViewById(R.id.sinatv);
            sinatv1= (SinatvFolder) findViewById(R.id.sinatv1);
            sinatv2= (SinatvFolder) findViewById(R.id.sinatv2);
            sinatv3= (SinatvFolder) findViewById(R.id.sinatv3);
            mysinatvtext = (TextView) findViewById(R.id.mysinatvtext);
            sinatv.setListener(this);
            sinatv1.setListener(this);
            sinatv2.setListener(this);
            sinatv3.setListener(this);
            viewsfolder[0].setListener(this);
            viewsfolder[1].setListener(this);
            viewsfolder[2].setListener(this);
            myRelative.setListener(this);
            myRelative2.setListener(this);
            myRelative3.setListener(this);
            myRelative4.setListener(this);
            myRelative5.setListener(this);
            myRelative6.setListener(this);

            initData();
            setListener();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   checkpull();
                }
            },2000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewsfolder[0].setFocusable(true);
                    viewsfolder[0].setFocusableInTouchMode(true);
                    viewsfolder[0].requestFocus();
                }
            },3000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stateBar.updateNetState();
                }
            },5000);

        }
        @Override
        public void fucosChange(View view, boolean gainFocus) {
            lastFocusView = view;
            if (gainFocus){
                view.bringToFront();
                focusUtils.showFocus();
                focusUtils.startMoveFocus(view,true,1.2F);
                focusScaleUtils.scaleToLarge(view);
                hiddenGalleryFocus(true);
                if(isShow){
                    hideTopDialog(true);
                }
                if(isTopShow){
                    hideTopDialog(false);
                }
            }else {

                focusScaleUtils.scaleToNormal();
            }
        }
        private void checkpull() {
            if(isShow){
                hideTopDialog(true);
            }
            if(isTopShow){
                hideTopDialog(false);
            }
        }

        private void initData() {
            loaclTag = dbManager.findAllConfig();
            //检测一下载
            apps = dbManager.FindAllApp();
            downloadAdater = new TvGridDownloadAdapter(AppContext.getAppContext(),apps);
            downloadGrid.setAdapter(downloadAdater);
        }

        private void setListeners() {
    //        for (int i =0 ; i< viewsfolder.length;i++){
    //            viewsfolder[i].setOnFocusChangeListener(this);
    //       }
        }

        public static boolean isFirstIn(Context context) {
             preferences = PreferenceManager.getInstance(AppContext.getAppContext());
            boolean firstIn = preferences.getBoolean("first_in");
            if (firstIn) {
                preferences.putBoolean("first_in", false);
            }
            return firstIn;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (hasFocus){
                v.bringToFront();
                focusUtils.showFocus();
                focusUtils.startMoveFocus(v,false,1.2F);
                focusScaleUtils.scaleToLarge(v);
                hiddenGalleryFocus(true);
                if(isShow){
                    hideTopDialog(true);
                }
                if(isTopShow){
                    hideTopDialog(false);
                }
            }else {

                focusScaleUtils.scaleToNormal();
            }
        }

        private void registReceivers() {
            IntentFilter filter;
            filter = new IntentFilter();
            filter.addAction(DeskConstant.ACTION_UPDATE_PAGE_DETAIL);
            filter.addAction(Constant.PACKAGE_TOOL_WEATHER);
            registerReceiver(custreceiver, filter);

            filter = new IntentFilter();
            filter.addAction(NetUtil.CONNECTIVITY_CHANGE);
            filter.addAction(NetUtil.RSSI_CHANGE);
            filter.addAction(NetUtil.WIFI_STATE_CHANGE);
            registerReceiver(netReceiver, filter);

            filter = new IntentFilter();
            filter.addAction(ConfigService.ACTION_CONFIG_UPDATE);
            filter.addAction("com.ider.date");
            filter.addAction("com.ider.forced");
            registerReceiver(sixReceiver, filter);

            filter = new IntentFilter();
            filter.addAction("com.tencent.qqlivetv.login.rsp");
            filter.addAction("com.tencent.qqlivetv.open.result");
            registerReceiver(useRecever, filter);

            filter = new IntentFilter();
            filter.addAction("com.tv.history.add");
            filter.addAction("com.tv.favorite..history.del.tolauncher");

            filter.addAction("com.tv.favorite.del.tolaunche");
            filter.addAction("com.tv.favorite.add");
            registerReceiver(hisRecever, filter);

            filter = new IntentFilter();
            filter.addAction(Intent.ACTION_MEDIA_EJECT);
            filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
            filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            filter.addDataScheme("file");
            registerReceiver(mediaReciever, filter);

            filter = new IntentFilter();
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            filter.addAction(Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
            filter.addDataScheme("package");
            registerReceiver(uninstallRecevier, filter);

            filter = new IntentFilter();
            filter.addAction("com.ider.sinatv");
            registerReceiver(mysinatv,filter);

            filter = new IntentFilter();
            filter.addAction("com.ider.sinatvoff");
            registerReceiver(mysinatvoff,filter);
        }

        @Override
        protected void onDestroy() {
            if (Util.isOnMainThread()) {
                Glide.with(AppContext.getAppContext()).pauseRequests();
            }
            try{
                unregisterReceiver(sixReceiver);
                unregisterReceiver(custreceiver);
                unbindService(connection);
                unbindService(connectionmy);
                unregisterReceiver(useRecever);
                unregisterReceiver(hisRecever);
                unregisterReceiver(mediaReciever);
                unregisterReceiver(uninstallRecevier);
                unregisterReceiver(netReceiver);
                unregisterReceiver(mysinatv);
                unregisterReceiver(mysinatvoff);
            }catch (Exception e){
                e.printStackTrace();
            }
            super.onDestroy();
        }

        private void bindMyService() {
            Intent in = new Intent();
            in.setAction(DeskConstant.DESK_SERVICE_ACTION);
            Intent intent = new Intent(this, DeskService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
        private void bindServices() {
            if (configService == null || !configService.isBinded) {
                Intent intent = new Intent(HomeActivity.this, ConfigService.class);
                this.bindService(intent, connectionmy, BIND_AUTO_CREATE);
            }
        }

        ServiceConnection connectionmy = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ConfigService.MBinder binder = (ConfigService.MBinder) iBinder;
                configService = binder.getService();
                if(NetUtil.isNetworkAvailable(AppContext.getAppContext()))
                {
                    checkUpdate(true);
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                bindServices();
            }
        };

        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.i(TAG, "onServiceConnected: service connected !!!!!!!!!!!");
                service = IDeskService.Stub.asInterface(iBinder);
                //String url = String.format(Constant.SERVER_URL, "2017030817", "ider863327");
    //            String url = String.format(Constant.SERVER_URL, Constant.SERVER_PAGEID, Constant.SERVER_IDER);
                try {
                    if(NetUtil.isNetworkAvailable(AppContext.getAppContext())) {
                        service.loadPageDetail(Constant.DATA_URL);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.i(TAG, "onServiceDisconnected: service ServiceDisconnected !!!!!!!!!!!");
                bindMyService();
            }
        };

        private void checkUpdate(boolean force) {
            if (configService != null) {
                configService.checkUpdate(force);
            }
        }

        BroadcastReceiver custreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(DeskConstant.ACTION_UPDATE_PAGE_DETAIL)){
                    boolean state = intent.getBooleanExtra(DeskConstant.PAGE_DETAIL_STATE, false);
                    isDeskUpdate = state;
                    if(state){
                        PageEntry page = intent.getParcelableExtra(DeskConstant.PAGE_DETAIL_ENTRIES);
    //                    if(page!=null){
    //                        gallery.updateGallery(page);
    //                    }
                    }
                }if(intent.getAction().equals(Constant.PACKAGE_TOOL_WEATHER)){
                    stateBar.getWeather();
                }
            }
        };
        BroadcastReceiver sixReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("com.ider.date")){
                     stateBar.setdata();
                }else if(intent.getAction().equals("com.ider.forced")){
                    apps = dbManager.FindAllApp();
                    downloadAdater = new TvGridDownloadAdapter(AppContext.getAppContext(),apps);
                    downloadGrid.setAdapter(downloadAdater);
                }
                else {
                    loaclTag = intent.getParcelableArrayListExtra("configs");
                    Log.i(TAG, "onReceive: sixReceiver data==" + loaclTag.size());
                    updateConfigService(loaclTag);
                }
            }
        };

        BroadcastReceiver useRecever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int login_status = intent.getIntExtra("login_status",0);
                String login_type = intent.getStringExtra("login_type");
                String action = intent.getAction();
                Log.i("zzz", "onReceive: login_status=="+login_status+"***action="+action+"***login_type="+login_type);
            }
        };

        BroadcastReceiver hisRecever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action  = intent.getAction();
                Log.i("zzz", "onReceive: action=="+action);
                if(action.equals(Constant.ACTION_FAVADD)){
                    //添加
                }else if(action.equals(Constant.ACTION_FAVDELTOLAUNCHER)){
                    //删除
                }
                else  if(action.equals(Constant.ACTION_HISADD)){
                    //添加
                    String srcApp = intent.getStringExtra("srcApp");
                    String videoId = intent.getStringExtra("videoId");
                    String vieoName = intent.getStringExtra("videoName");
                    int videoType = intent.getIntExtra("videoType",0);
                    String videoImgUrl = intent.getStringExtra("videoImgUrl");
                    String episodeId = intent.getStringExtra("episodeId");
                    String episodeName = intent.getStringExtra("episodeName");
                    int episodeCount = intent.getIntExtra("episodeCount",0);
                    int currentPosition = intent.getIntExtra("currentPosition",0);
                    int duration = intent.getIntExtra("duration",0);
                    String definition = intent.getStringExtra("definition");
                    String cmdInfo = intent.getStringExtra("cmdInfo");
                    String userkey = intent.getStringExtra("userkey");

                    Log.i("zzz", "onReceive: videoId="+videoId+"**vieoName=="+vieoName+"**videoImgUrl="+videoImgUrl+"**episodeId=="+episodeId);
                }else if(action.equals(Constant.ACTION_HISDELTOLAUNCHER)){
                    //删除
                }
            }
        };

        BroadcastReceiver mediaReciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                String data = intent.getDataString();
                if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                    stateBar.updateUsbStatetwo(true);
                    String path = data.substring(7,data.length());
                    checkUsbInstall(path);
                }
                if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
                    stateBar.updateUsbStatetwo(false);

                }
            }
        };

        private String delpackage = null;
        private BroadcastReceiver uninstallRecevier = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if(action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                    String data = intent.getData().toString();
                    String packageName = data.replace("package:", "");
                    delpackage = packageName;
                    handler.postDelayed(startcheck, 2000);

                }
                if(action.equals(intent.ACTION_PACKAGE_REPLACED)){
                    String data = intent.getData().toString();
                    String packageName = data.replace("package:", "");
                    Log.i(TAG, "onReceive: REPLACED");
                    if(packageName.equals(delpackage)){
                        handler.removeCallbacks(startcheck);
                    }
                }
                if(action.equals(intent.ACTION_PACKAGE_ADDED)){
                    String data = intent.getData().toString();
                    String packageName = data.replace("package:", "");
                    if (pmanager.isDataSave()){
                        DataSupport.deleteAll(ServerApp.class,"packageName = ?",packageName);
                    }
                    Log.i("zzz", "ACTION_PACKAGE_ADDED: package=="+packageName);
                    for (String demandPackageName:Constant.storePackageNames) {
                        if (demandPackageName.equals(packageName)) {
                            //点播
                            if (!packageName.equals(delpackage)) {
                                DbManager.getInstance(getApplicationContext()).insertPackage(new PackageHolder(packageName, "01"));
                                viewsfolder[1].updateSelf();
                            }
                        }
                    }
                    for (String tvPackageName:Constant.tvPackageNames) {
                        if (tvPackageName.equals(packageName)) {
                            //直播
                            if (!packageName.equals(delpackage)) {
                                DbManager.getInstance(getApplicationContext()).insertPackage(new PackageHolder(packageName, "02"));
                                viewsfolder[0].updateSelf();
                            }
                        }
                    }
                    for (String demandPackageName:Constant.demandPackageNames) {
                        if (demandPackageName.equals(packageName)) {
                            //点播
                            if (!packageName.equals(delpackage)) {
                                DbManager.getInstance(getApplicationContext()).insertPackage(new PackageHolder(packageName, "03"));
                                viewsfolder[1].updateSelf();
                            }
                        }
                    }
                }
            }
        };
        BroadcastReceiver mysinatv =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mysinatvtext.setVisibility(View.VISIBLE);
            }
        };
     BroadcastReceiver mysinatvoff=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mysinatvtext.setVisibility(View.GONE);
        }
    };
        BroadcastReceiver netReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (NetUtil.CONNECTIVITY_CHANGE.equals(action)) {
                    if (NetUtil.isNetworkAvailable(AppContext.getAppContext())) {
                        if (dialog != null) {
                            dialog.cancel();
                            dialog.dismiss();
                        }

//                        JSONUtil.getData(JSONUtil.movie,new JSONUtil.OnCompleteListener() {
//
//                            @Override
//                            public void complete(final Data data) {
//                                Log.i("complete", "complete!!!!!!!!!");
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        String url = data.getChannel_contents().get(0).getModules().get(0).getItems().get(0).getComm_item().getExt_info1().getPic_192x108();
//                                        Log.i("url",url);
//                                        Glide.with( HomeActivity.this).load(url).into(myRelative.getImageView());
//
//                                        myRelative.setTitle(getString(R.string.film));
//                                    }
//                                });
//                            }
//                        });
//                                JSONUtil.getData(JSONUtil.children, new JSONUtil.OnCompleteListener() {
//                                    @Override
//                                    public void complete(final Data data) {
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                String url2 = data.getChannel_contents().get(0).getModules().get(0).getItems().get(0).getComm_item().getExt_info1().getPic_192x108();
//                                                Glide.with( HomeActivity.this).load(url2).into(myRelative2.getImageView());
//                                                myRelative2.setTitle(getString(R.string.cartoon));
//                                            }
//                                        });
//                                    }
//                                });
//                        JSONUtil.getData(JSONUtil.chosen, new JSONUtil.OnCompleteListener() {
//                            @Override
//                            public void complete(final Data data) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        String url3 = data.getChannel_contents().get(0).getModules().get(0).getItems().get(0).getComm_item().getExt_info1().getPic_192x108();
//                                        Glide.with( HomeActivity.this).load(url3).into(myRelative3.getImageView());
//                                        myRelative3.setTitle(getString(R.string.news));
//                                    }
//                                });
//                            }
//                        });
//                        JSONUtil.getData(JSONUtil.tvUrl, new JSONUtil.OnCompleteListener() {
//                            @Override
//                            public void complete(final Data data) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        String url4 = data.getChannel_contents().get(0).getModules().get(0).getItems().get(0).getComm_item().getExt_info1().getPic_192x108();
//                                        Glide.with( HomeActivity.this).load(url4).into(myRelative4.getImageView());
//                                        myRelative4.setTitle(getString(R.string.tvplay));
//                                    }
//                                });
//                            }
//                        });
//                        JSONUtil.getData(JSONUtil.variety, new JSONUtil.OnCompleteListener() {
//                            @Override
//                            public void complete(final Data data) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        String url5 = data.getChannel_contents().get(0).getModules().get(0).getItems().get(0).getComm_item().getExt_info1().getPic_408x230();
//                                        Glide.with( HomeActivity.this).load(url5).into(myRelative5.getImageView());
//                                        myRelative5.setTitle(getString(R.string.variety));
//                                    }
//                                });
//                            }
//                        });
//                        JSONUtil.getData(JSONUtil.physical_pay, new JSONUtil.OnCompleteListener() {
//                            @Override
//                            public void complete(final Data data) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        String url6 = data.getChannel_contents().get(0).getModules().get(1).getItems().get(1).getComm_item().getExt_info1().getPic_192x108();
//                                        Glide.with( HomeActivity.this).load(url6).into(myRelative6.getImageView());
//                                        myRelative6.setTitle(getString(R.string.sportsevents));
//                                    }
//                                });
//                            }
//                        });

    //                }else{
    //                    showNetDialog();
    //                }
    //                //networking
                        if (service != null) {
    //                    if(dialog!=null){
    //                        dialog.cancel();
    //                        dialog.dismiss();
    //                    }
                            try {
                                service.loadPageDetail(Constant.DATA_URL);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            if (!isIderUpdate) {
                                checkUpdate(true);
                            }
                            new Thread(){
                                @Override
                                public void run() {
                                    getNetTime();
                                }
                            }.start();

                        }
                    }
                    stateBar.updateNetState();
                }
            }
        };

        Runnable startcheck = new Runnable() {
            @Override
            public void run() {
                checkUninstall(delpackage);
            }
        };

        public void checkUninstall(String packageName){
            for(int i = 0 ;i <apps.size();i++){
                if(packageName.equals(apps.get(i).getPackageName())){
                    apps.remove(apps.get(i));
                    dbManager.deleteApp(packageName);
                    downloadAdater.notifyDataSetChanged();
                }
            }
        }

        public void showNetDialog(){
            Log.i("zzz", "showNetDialog: ");
            if (dialog==null) {
                AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(HomeActivity.this);
                normalDialog.setTitle(getResources().getString(R.string.neterro_tip));
                normalDialog.setMessage("  " + getResources().getString(R.string.net_tip));
                normalDialog.setPositiveButton(getResources().getString(R.string.btn_enter),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings"));
                                startActivity(intent);
                            }
                        });
                normalDialog.setNegativeButton(getResources().getString(R.string.Cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                dialog.dismiss();
                            }
                        });
                dialog = normalDialog.show();
            }else {
                if (!dialog.isShowing()){
                    dialog.show();
                }
            }

        }

        public void checkUsbInstall(String path){
            String folder = "HDTV_BOX";
            File file = new File(path+ File.separator+folder);
            if(file.exists()&&file.isDirectory()){
                Intent intent = new Intent(HomeActivity.this,InstallActivity.class);
                intent.putExtra("path",file.getAbsolutePath());
                startActivityForResult(intent,3);
            }
        }

        @Override
        public void onScroll() {

        }

        public void hiddenGalleryFocus(boolean isHudden){
            if(isHudden){
                titles.setVisibility(View.INVISIBLE);
                findViewById(R.id.gallery_focus).setVisibility(View.INVISIBLE);
            }else{
                titles.setVisibility(View.VISIBLE);
                findViewById(R.id.gallery_focus).setVisibility(View.VISIBLE);
                focusUtils.hideFocus();
            }
        }
        private int mKeycodeStackIndex = 0;
        int config_pwd = 0;
        int mass_pwd = 0;
       
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            Log.i("zzz", "onKeyDown: keyCode="+keyCode);
    //        handler.removeCallbacks(help);
    //        handler.postDelayed(help,Constant.START_HELP_TIME);

            if(keyCode == KeyEvent.KEYCODE_SOFT_LEFT){
                startSet();
            }
            if(keyCode == KeyEvent.KEYCODE_TV_TELETEXT){
                startApps();
            }
            if(keyCode == KeyEvent.KEYCODE_STB_INPUT){
                startclean();
            }

            

            if(keyCode == KeyEvent.KEYCODE_MENU){
                hideTopDialog(true);
                if(isShow){
                    hideTopDialog(true);
                }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showTopdialog();
                        }
                    },300);
            }
            if(keyCode==KeyEvent.KEYCODE_BACK){
                if(isShow){
                    hideTopDialog(true);
                    if (lastFocusView!=null){
                        lastFocusView.requestFocus();
                    }
                }
                if(isTopShow){
                    hideTopDialog(false);
                    if (lastFocusView!=null){
                        lastFocusView.requestFocus();
                    }
                }
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_0||keyCode == KeyEvent.KEYCODE_1//
                    || keyCode == KeyEvent.KEYCODE_2
                    || keyCode == KeyEvent.KEYCODE_3
                    || keyCode == KeyEvent.KEYCODE_4
                    || keyCode == KeyEvent.KEYCODE_5
                    || keyCode == KeyEvent.KEYCODE_6
                    || keyCode == KeyEvent.KEYCODE_7
                    || keyCode == KeyEvent.KEYCODE_8
                    || keyCode == KeyEvent.KEYCODE_9
                    || keyCode == KeyEvent.KEYCODE_RO
                    || keyCode == KeyEvent.KEYCODE_F11) {
                if (keyCode == LOG_DB[mKeycodeStackIndex])
                {
                    Log.i(TAG, "onKeyDown: mKeycodeStackIndex++"+mKeycodeStackIndex);
                    mKeycodeStackIndex++;
                    handler.removeCallbacks(keynumStart);
                    handler.removeCallbacks(key6Start);
                }
                else{
                    mKeycodeStackIndex = 0;
                }
                if (mKeycodeStackIndex >= LOG_DB.length){
                    mKeycodeStackIndex=0;
                    handler.removeCallbacks(keynumStart);
                    if (!pmanager.ifConfigServiceOn()&&NetUtil.isNetworkAvailable(AppContext.getAppContext())) {
                        bindServices();
                        pmanager.setConfigServiceOn();
                    }
                    return true;
                }
                mass_pwd=0;

                if (keyCode == KeyEvent.KEYCODE_6) {
                    config_pwd++;
                    handler.removeCallbacks(key6Start);
                    handler.postDelayed(key6Start, 1000);
                    if (config_pwd >= 6) {
                        Log.i(TAG, "onKeyDown: mykeyevent++++++++");
                        handler.removeCallbacks(key6Start);
                        if (!pmanager.ifConfigServiceOn()&&NetUtil.isNetworkAvailable(AppContext.getAppContext())) {
                            bindServices();
                            pmanager.setConfigServiceOn();
                        }
                        // 开启后台配置桌面↑
                        //点击发送广播显示APK↓
                        Intent intent = new Intent("con.ider.overlauncher.LAUNCHER");
                         sendBroadcast(intent);
                    }
                }
                else {
                    config_pwd = 0;
                    currentKey = keyCode;
                    handler.postDelayed(keynumStart, 1000);

                }

                return true;
            }



            return super.onKeyDown(keyCode, event);
        }
        Runnable key6Start = new Runnable() {
            @Override
            public void run() {
                config_pwd = 0;
                String pkg = pmanager.getKeyPackage(KeyEvent.KEYCODE_6);
                if (pkg != null) {
                    Application app = ApplicationUtil.doApplication(AppContext.getAppContext(),pkg);
                    if(app!=null) {
                        ApplicationUtil.startApp(HomeActivity.this, app);
                    }
                }else {
                    Toast.makeText(HomeActivity.this, R.string.nullapp,
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(HomeActivity.this, FastKey.class);
                    startActivity(intent);

                }
            }
        };

        int currentKey = 0;
        Runnable keynumStart = new Runnable() {
            @Override
            public void run() {
                String pkg = pmanager.getKeyPackage(currentKey);
                if (pkg != null) {
                    Application app = ApplicationUtil.doApplication(AppContext.getAppContext(), pkg);
                    if (app != null) {
                        ApplicationUtil.startApp(HomeActivity.this, app);
                    }
                } else {
                    Intent intent = null ;
                    if(currentKey == KeyEvent.KEYCODE_RO){
                        intent = HomeActivity.this.getPackageManager().getLaunchIntentForPackage("com.yidian.calendar");
                        pmanager.setKeyPackage(KeyEvent.KEYCODE_RO, "com.yidian.calendar");
                        if (intent!=null) {
                            startActivity(intent);
                        }
                    }
                    else if(currentKey == KeyEvent.KEYCODE_F11){
                        Log.i(TAG,"TVdown");
                        TVdown(AppContext.getAppContext());
                    }else {
                        Toast.makeText(HomeActivity.this, R.string.nullapp,
                                Toast.LENGTH_LONG).show();
                        intent = new Intent(HomeActivity.this, FastKey.class);
                        startActivity(intent);
                    }
                }
            }
        };

        public void TVdown(Context mContext){
            PackageManager pm = mContext.getPackageManager();
            List<PackageInfo> infos = pm.getInstalledPackages(0);

            for(PackageInfo info: infos) {

                    for (String packg:Constant.tvPackageNames){
                        if(info.packageName.equals(packg)){
                            Intent intent = getPackageManager().getLaunchIntentForPackage(packg);
                            if(intent!=null) {
                                pmanager.setKeyPackage(KeyEvent.KEYCODE_TV_ANTENNA_CABLE, packg);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                                return;
                            }
                        }
                    }
                    Intent intent;
                        Toast.makeText(HomeActivity.this, R.string.nullapp,
                                Toast.LENGTH_LONG).show();
                        intent = new Intent(HomeActivity.this, FastKey.class);
                        startActivity(intent);

            }

        }
        private void updateConfigService(List<TagConfig> configs){

            checkFoced(configs);
        }
        public static boolean isDownload(String packageName){
            Application app = ApplicationUtil.doApplication(AppContext.getAppContext(), packageName);
            if (app != null) {
                Log.i(TAG, "checkIsDownload: packageName=" + packageName + app.getLabel());
                return true;
            }
            return false;
        }
        public void checkFoced(List<TagConfig> configs){
            List<TagConfig> forcedconfigs = new ArrayList<TagConfig>();
            for (int i = 0;i<configs.size();i++) {
                ConfigApp config = (ConfigApp) configs.get(i);
                Log.i(TAG, "checkFoced: config.forced=="+config.forced);
                if (config.forced && !isDownload(config.pkgName)) {
                    Log.i(TAG, "checkFoced: ==" + config.pkgName);
                    forcedconfigs.add(config);
    //                configs.remove(config);
                    String tag = config.tag;
                    dbManager.insertApp(config.pkgName);
                    DownloadUtil.getInstance().startForeD(AppContext.getAppContext(), config.apkUrl);

                }
            }
            if(forcedconfigs.size()>0){
                for (int i = 0 ; i<forcedconfigs.size();i++){
                    configs.remove(forcedconfigs.get(i));
                }
            }
    //        List<TagConfig> loaclTagall =new ArrayList<TagConfig>();
            loaclTag = checkIsDownload(configs);
            if(!isIderUpdate) {
                serverGrid.removeAllViews();
                serverAdater = new TvGridAdapter(AppContext.getAppContext(), checkIsDownload(loaclTag));
                serverGrid.setAdapter(serverAdater);
                isIderUpdate=true;
            }

        }

        private List<TagConfig> checkIsDownload(List<TagConfig> locals) {
            List<TagConfig>cons =new ArrayList<TagConfig>();
            for (int i = 0; i < locals.size(); i++
                    ) {
                ConfigApp con = (ConfigApp) locals.get(i);
                String packageName = con.pkgName;
                Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                if (intent != null) {
                    cons.add(con);
                }
            }
            locals.removeAll(cons);
            boolean isDataSave = pmanager.isDataSave();
            for (TagConfig config:locals){
                ConfigApp configApp = (ConfigApp)config;
                if (configApp.containsPackageNameOf(Constant.tvPackageNames)) {
                    ServerApp serverApp = new ServerApp(2, configApp.pkgName, configApp.summary, configApp.iconUrl, configApp.apkUrl,
                            configApp.description, configApp.verCode, configApp.md5,"tv");
                    if (isDataSave){
                        DataSupport.deleteAll(ServerApp.class,"packageName = ?",serverApp.getPackageName());
                    }else {
                        pmanager.setDataSave();
                    }
                    serverApp.save();
                }
                if (configApp.containsPackageNameOf(Constant.demandPackageNames)) {
                    ServerApp serverApp = new ServerApp(2, configApp.pkgName, configApp.summary, configApp.iconUrl, configApp.apkUrl,
                            configApp.description, configApp.verCode, configApp.md5,"movie");
                    if (isDataSave){
                        DataSupport.deleteAll(ServerApp.class,"packageName = ?",serverApp.getPackageName());
                    }else {
                        pmanager.setDataSave();
                    }
                    serverApp.save();
                }
            }
            return locals;
        }

        public void showBottomView(final boolean istop) {
            if(!isShow) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(apps==null||apps.size()==0){
                            apps = dbManager.FindAllApp();
                            downloadAdater = new TvGridDownloadAdapter(AppContext.getAppContext(),apps);
                            downloadGrid.setAdapter(downloadAdater);
                        }
                        isShow = true;
                        showAnimBlurBackground(0);
                        if (pmanager.ifConfigServiceOn()) {
                            pullDownTopDialog(getResources().getDimension(R.dimen.h_210), 1, istop);
                        } else {
                            pullDownTopDialog(getResources().getDimension(R.dimen.h_440), 1, istop);
                        }
                        // pullDownTopDialog(getResources().getDimension(R.dimen.h_210), 1,istop);
    //                    startMarqueen(getResources().getString(R.string.title_tip));
                    }
                },300);


            }
        }

        public void showAnimBlurBackground(int brightness) {
            blurBackground.bringToFront();
            blur(blurBackground, brightness);
            if(blurBackground.getAlpha()==0)
            {
                ObjectAnimator animator = ObjectAnimator.ofFloat(blurBackground, "alpha", 0.0f, 1.0f);
                animator.setDuration(300);
                animator.start();
            }
        }
        public void pullDownTopDialog(Float y,int direction,boolean isTop) {

            if(isTop) {
                bottomView.bringToFront();
                bottomView.setArrowDirection(direction);
                topDialogAnimator = bottomView.animate();
                topDialogAnimator.x(0);
                topDialogAnimator.y(y);
            }else{
                view_mytop.bringToFront();
                view_mytop.setArrowDirection(direction);
                topDialogAnimator = view_mytop.animate();
                topDialogAnimator.x(0);
                topDialogAnimator.y(y);
                if(direction==1) {
                    view_mytop.requestFocus();
                }
            }
            topDialogAnimator.setDuration(300);
            topDialogAnimator.start();

        }

        private void blur(ImageView view, int brightness) {
    //        Glide.with(Launcher.this).load(R.mipmap.blur).crossFade(500).into(view);
    //        view.setImageDrawable(getResources().getDrawable(R.mipmap.blur));
    //        Bitmap  bkg =getScreenBitmapforbg();
            Log.i("zzz", "blur: blur");
    //        if(bkg==null){
            view.setImageResource(R.mipmap.bbb);
    //            Glide.with(AppContext.getAppContext()).load(R.mipmap.bbb).crossFade(500).into(view);
    //        }else {
    ////        Bitmap bkg = BitmapFactory.decodeFile("/sdcard/screenshot.png");
    //            float scaleFactor = 8;
    //            float radius = 8;
    //            if (blurBitmap != null && !blurBitmap.isRecycled()) {
    //                blurBitmap.recycle();
    //            }
    //
    //            blurBitmap = Bitmap.createBitmap(
    //                    (int) (view.getMeasuredWidth() / scaleFactor),
    //                    (int) (view.getMeasuredHeight() / scaleFactor),
    //                    Bitmap.Config.ARGB_8888);
    //            Canvas canvas = new Canvas(blurBitmap);
    //            canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
    //                    / scaleFactor);
    //            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
    //            Paint paint = new Paint();
    //
    //            if (brightness != 0) {
    //                ColorMatrix cMatrix = new ColorMatrix();
    //                cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1,
    //                        0, 0, brightness,// 改变亮度
    //                        0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
    //
    //                paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    //            }
    //            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
    //            canvas.drawBitmap(bkg, 0, 0, paint);
    //            blurBitmap = FastBlur.doBlur(blurBitmap, (int) radius, true);
    //            view.setImageBitmap(blurBitmap);
    //            if (bkg != null && !bkg.isRecycled()) {
    //                bkg.recycle();
    //            }
    //        }
        }
    //    static Bitmap blurBitmap;

    //    public Bitmap getScreenBitmapforbg() {
    //        Bitmap screenBitmap=null;
    //        View decorview = getWindow().getDecorView();
    //        decorview.setDrawingCacheEnabled(true);
    //        if(blurBitmap != null && !blurBitmap.isRecycled()){
    //            blurBitmap=null;
    //        }
    //        if(decorview!=null&&decorview.getDrawingCache()!=null) {
    //            screenBitmap = Bitmap.createBitmap(decorview.getDrawingCache());
    //        }
    //        decorview.destroyDrawingCache();
    //        return screenBitmap;
    //    }

        private void setListener() {
            setListeners();
            downloadGrid.setOnItemSelectListener(new TvHorizontalGridView.OnItemSelectListener() {
                @Override
                public void onItemSelect(View item, int position) {
                    String  text = getResources().getString(R.string.title_tip);;
                    if (position == 0) {
                        text = getResources().getString(R.string.add_tip);
                        item.setNextFocusLeftId(item.getId());
                    }
                    startMarqueen(text);
                    showBottomView(true);
                }
            });
            downloadGrid.setOnItemClickListener(new TvHorizontalGridView.OnItemClickListener() {
                @Override
                public void onItemClick(View item, int position) {
                    final Application app = apps.get(position);
                    if(app!=null&&!app.getPackageName().equals(Constant.ADD_PACKAGE)){
                        if (NetUtil.isNetworkAvailable(AppContext.getAppContext())) {
                            GsonUtil.getDataApps(new GsonUtil.HandleResult() {
                                @Override
                                public void resultHandle(List<DataApp> list) {
                                    final DataApp app2 = AppUtil.isNeedUpdate(app.getPackageName(), list);
                                    if (app2 == null) {
                                        ApplicationUtil.startApp(AppContext.getAppContext(),app);
                                    }else {
                                        PopupUtil.getUpdatePopup(HomeActivity.this, app2.getLabel(), app2.getIconUrl(), new UpdatePopup.OnOkListener() {
                                            @Override
                                            public void onOkClick(boolean isOk) {
                                                if (isOk){
                                                    startDownloadPage(app2);
                                                }else {
                                                    ApplicationUtil.startApp(AppContext.getAppContext(),app);
                                                }
                                                PopupUtil.forceDismissPopup();
                                            }
                                        }).show(downloadGrid);
                                    }
                                }
                            });
                        }else {
                            ApplicationUtil.startApp(AppContext.getAppContext(),app);
                        }

                    }else if(app.getPackageName().equals(Constant.ADD_PACKAGE)){
                       // showAppDialog(1,apps,null);
                        showAppSelectWindow();
                    }
                }
            });
            downloadGrid.setOnItemLongClickListener(new TvHorizontalGridView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(View item, int position) {
                    if (!apps.get(position).getPackageName().equals(Constant.ADD_PACKAGE)) {
    //                    showTitileDialog(apps.get(position));
                        Application app = apps.get(position);
                        apps.remove(app);
                        dbManager.deleteApp(app.getPackageName());
                        isShow=false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isShow=true;
                            }
                        },500);
                        downloadAdater.notifyDataSetChanged();
                    }
                    return true;
                }
            });
            serverGrid.setOnItemSelectListener(new TvHorizontalGridView.OnItemSelectListener() {
                @Override
                public void onItemSelect(View item, int position) {
                    showBottomView(true);
                    startMarqueen(getResources().getString(R.string.downLoad_tip));
                    TextView textView =((TextView)item.findViewById(R.id.tv_title));
                    textView .setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    textView .setSingleLine(true);
                    textView .setMarqueeRepeatLimit(-1);
                    if(position==0){
                        item.setNextFocusLeftId(item.getId());
                    }
                    if(position==loaclTag.size()-1){
                        item.setNextFocusRightId(item.getId());
                    }

                }
            });

            serverGrid.setOnItemClickListener(new TvHorizontalGridView.OnItemClickListener() {
                @Override
                public void onItemClick(View item, int position) {
                    if (position<loaclTag.size()) {
                        ConfigApp config = (ConfigApp) loaclTag.get(position);
                        startDownloadPage(config, false);
                    }
                }
            });
        }

        public void showAppSelectWindow() {
            List<ServerApp> appslocal = new ArrayList<>();
            for (int i =0;i<apps.size();i++){
                appslocal.add(new PackageHolder(apps.get(i).getPackageName(),null));
            }
            AppSelectWindow appSelectWindow = AppSelectWindow.getInstance(this);
            appSelectWindow.setData(appslocal);
            appSelectWindow.setOnAppSelectListener(new AppSelectWindow.OnAppSelectListener() {
                @Override
                public void onAppSelected(PackageHolder holder) {
                    if(!holder.getPackageName().equals("add")) {
                        apps.add(ApplicationUtil.doApplication(HomeActivity.this, holder.getPackageName()));
                        dbManager.insertApp(holder.getPackageName());
                        downloadAdater.notifyDataSetChanged();
                        Log.i("zzz", "onAppSelected: holder.getPackageName()==" + holder.getPackageName());
                    }
                }
            });
            appSelectWindow.showAppPopWindow(homeView);
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
        public void startDownloadPage(ConfigApp config,boolean isCustom) {
            Intent intent = new Intent(AppContext.getAppContext(), AppInfo.class);
            Bundle bundle = new Bundle();
            bundle.putString("icon", config.iconUrl);
            bundle.putString("apk", config.apkUrl);
            bundle.putString("description", config.description);
            bundle.putString("label", config.label);
            bundle.putString("pkg", config.pkgName);
            bundle.putString("tag", config.tag);
            bundle.putInt("verCode", config.verCode);
            bundle.putString("md5", config.md5);
            bundle.putBoolean("isCustom",isCustom);
            intent.putExtras(bundle);
            if(isCustom){
                startActivityForResult(intent,2);
            }else {
                startActivityForResult(intent, 1);
            }
        }

        public  void startMarqueen(String text){
    //        long currentTimeMills = System.currentTimeMillis();
    //        if (lastTimeMills == 0 || currentTimeMills - lastTimeMills > 2000) {
    //            handler.removeCallbacks(stopShowTip);
    //            lastTimeMills = currentTimeMills;
    //            marqueeText.setVisibility(View.VISIBLE);
    //            pullDownTopTip(getResources().getDimension(R.dimen.h_40),0);
    //            marqueeText.bringToFront();
    //            marqueeText.setText(text);
    //            handler.postDelayed(stopShowTip,text.length()*500);
    //        }
        }

        public  void hideTopDialog(final boolean istop) {
            if(istop) {
                if (isShow) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isShow = false;
                            pullDownTopDialog(getResources().getDimension(R.dimen.h_655), 0, istop);
                            hideAnimBlurBackground();
                            //  stopMarqueen();
                        }
                    }, 300);

                }
            }else{
                if (isTopShow) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isTopShow = false;
                            pullDownTopDialog(-250f,0,false);
                            hideAnimBlurBackground();
                            //  stopMarqueen();
                        }
                    }, 300);

                }
            }

        }

        public void showTopdialog(){
            if(!isTopShow) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isTopShow = true;
                        showAnimBlurBackground(0);
                        pullDownTopDialog(Float.intBitsToFloat(0),1,false);
                    }
                }, 300);
            }
        }
        public void hideAnimBlurBackground() {
            if(blurBackground.getAlpha()==1) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(blurBackground, "alpha", 1.0f, 0.0f);
                animator.setDuration(300);
                animator.start();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        blurBackground.setImageBitmap(null);
                    }
                },300);
            }
        }
        public void stopMarqueen(){
            //pullDownTopTip(getResources().getDimension(R.dimen.h_toptip),1);
        }


        //his
        public void startHis(){
            Intent intent = new Intent();
            intent.setData(Uri.parse(Constant.ACTION_HIS));
            handleIntent(intent);
        }

        //fav
        public void startHIS(){
            Intent intent = new Intent();
            intent.setData(Uri.parse(Constant.ACTION_HIS));
            handleIntent(intent);
        }

        public void checkLogin(){
            Intent intent = new Intent("com.tencent.qqlivetv.login.req");
            //intent.putExtra("type",2);
            sendBroadcast(intent);
        }
        //login
        public void startLogin(){
            Intent intent = new Intent();
            intent.setData(Uri.parse(Constant.ACTION_LOGIN));
            handleIntent(intent);
        }
        //apps
        public void startApps(){
            Intent intent = new Intent(HomeActivity.this,AppListActivity.class);
            ActivityAnimationTool.startActivity(HomeActivity.this,intent);
        }

        //city
        public void startCity(){
            Intent intent = new Intent(HomeActivity.this,CityPicker.class);
            ActivityAnimationTool.startActivity(HomeActivity.this,intent);
        }
        //fastkey
        public void startFastKey(){
            Intent intent = new Intent(HomeActivity.this,FastKey.class);
            ActivityAnimationTool.startActivity(HomeActivity.this, intent);

        }
        //file
        public void startFile(){
            Intent intent = HomeActivity.this.getPackageManager().getLaunchIntentForPackage("com.softwinner.TvdFileManager");
            if(intent!=null){
                startActivity(intent);
            }
        }
        //settings
        public void startSet(){
            Intent intent = HomeActivity.this.getPackageManager().getLaunchIntentForPackage("com.zxy.idersettings");
            if(intent!=null){
                startActivity(intent);
            }
        }
    public void startQclive(){
        Intent intent = HomeActivity.this.getPackageManager().getLaunchIntentForPackage("com.qclive.tv");
        if(intent!=null){
            startActivity(intent);
        }
    }
    public void startQQtv(){
        Intent intent = new Intent();
        intent.setData(Uri.parse(Constant.ACTION_CHOSEN));
        handleIntent(intent);
    }
        //clean
        public void startclean(){
            RocketView Rocketview = new RocketView(HomeActivity.this.getApplicationContext());
            Rocketview.CreateView();
            Rocketview = null;
        }
        //search
        public void startSearch(){
            Intent intent = new Intent();
            intent.setData(Uri.parse(Constant.ACTION_SEARCH));
            handleIntent(intent);
        }
        //help
        public void startHelp(){
            Intent intent = HomeActivity.this.getPackageManager().getLaunchIntentForPackage("com.ider.help");
            if(intent!=null){
                startActivity(intent);
            }

        }
        public void startMovie() {
            Intent intent = new Intent();
            intent.setData(Uri.parse(Constant.ACTION_MOVIE));
            handleIntent(intent);
        }
        public void startChildren(){
            Intent intent = new Intent();
            intent.setData(Uri.parse(Constant.ACTION_CHILDREN));
            handleIntent(intent);
        }
        public void startYueshow() {
            Intent intent = new Intent();
            intent.setData(Uri.parse(Constant.ACTION_YUESHOW));
            handleIntent(intent);
        }
        public void startTv() {
            Intent intent = new Intent();
            intent.setData(Uri.parse(Constant.ACTION_TV));
            handleIntent(intent);
        }
        public void startVariety() {
            Intent intent = new Intent();
            intent.setData(Uri.parse(Constant.ACTION_VARIETY));
            handleIntent(intent);
        }
        public void startPhyscalpay(){
            Intent intent = new Intent();
            intent.setData(Uri.parse(Constant.ACTION_PHYSCAL_PAY));
            handleIntent(intent);

        }
        private void handleIntent(Intent intent) {
            // 隐式调用的方式startActivity
            //intent.setAction("com.tencent.qqlivetv.open");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.yidian.calendar");//设置视频包名，要先确认包名
            PackageManager packageManager = HomeActivity.this.getPackageManager();
            List<ResolveInfo> activities = packageManager
                    .queryIntentActivities(intent, 0);
            boolean isIntentSafe = activities.size() > 0;
            if (isIntentSafe) {
                HomeActivity.this.startActivity(intent);
            } else {
                Toast.makeText(HomeActivity.this, "未安装腾讯视频 ， 无法跳转", Toast.LENGTH_SHORT).show();
            }
        }

        private void openWithHomePageUri(String url) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            handleIntent(intent);
        }

        public void TopOnclick(View view){
            switch (view.getId()){
                case R.id.item1:
                    startSearch();
                    break;
                case R.id.item2:
                    startApps();
                    break;
                case R.id.item3:
                    startFile();
                    break;
                case R.id.item4:
                    startSet();
                    break;
                case R.id.item5:
                    startHIS();
                    break;
                case R.id.item6:
                    startLogin();
                    break;
                case R.id.item7:
                    startclean();
                    break;
                case R.id.item8:
                    startCity();
                    break;
                case R.id.item9:
                    startHelp();
                    break;
                case R.id.myrelative:
                    startMovie();
                    break;
                case R.id.myrelative2:
                    startChildren();
                    break;
                case R.id.myrelative3:
                    startYueshow();
                    break;
                case R.id.myrelative4:
                    startTv();
                    break;
                case R.id.myrelative5:
                    startVariety();
                    break;
                case R.id.myrelative6:
                    startPhyscalpay();
                    break;
            }
        }
    //    Runnable help = new Runnable() {
    //        @Override
    //        public void run() {
    //            startHelp();
    //        }
    //    };
    //
    //    public void startHelp(){
    //        Intent intent  = new Intent();
    //        intent.setComponent(new ComponentName("com.ider.help2","com.ider.help.WebActivity"));
    //        ActivityAnimationTool.startActivity(HomeActivity.this, intent);
    //    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == 1&&resultCode == RESULT_OK) {
                String pkgName = data.getStringExtra("package");
                Application app =ApplicationUtil.doApplication(AppContext.getAppContext(),pkgName);
                if(app!=null) {
                    apps.add(app);
                    if(downloadGrid.getChildAt(apps.size()-1)!=null) {
                        downloadGrid.getChildAt(apps.size()-1).requestFocus();
                    }
                    downloadAdater.notifyDataSetChanged();
                    dbManager.insertApp(pkgName);
                    for(int i = 0;i<loaclTag.size();i++){
                        ConfigApp configApp = (ConfigApp)loaclTag.get(i);
                        if(pkgName.equals(configApp.pkgName)){
                            loaclTag.remove(loaclTag.get(i));
                            serverAdater.notifyDataSetChanged();
                        }
                    }
                }
            }

        }
    private void  showData(){
        try {
            Calendar c = Calendar.getInstance();
            String w = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
             week.setText(w);
        } catch (Exception e){

        }
    }
        private void getNetTime() {
            URL url = null;//取得资源对象
            URLConnection uc;
            try {
                url = new URL("http://www.baidu.com");
                //url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
                //url = new URL("http://www.bjtime.cn");
                uc = url.openConnection();//生成连接对象
                uc.connect(); //发出连接
                long ld = uc.getDate(); //取得网站日期时间
                Log.i("zzz", "getNetTime: ld==="+ld);
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(ld);
                if (ld / 1000 < Integer.MAX_VALUE) {
                    ((AlarmManager) HomeActivity.this.getSystemService(Context.ALARM_SERVICE)).setTime(ld);
                    HomeActivity.this.sendBroadcast(new Intent("com.ider.date"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            HomeActivity.this.sendBroadcast(new Intent("com.ider.date"));
        }
  protected void onRestart(){
      SharedPreferences preferencese = getSharedPreferences("settings",MODE_APPEND);
      mykeyevent = preferencese.getBoolean("mykeyevent",false);
      handler.removeCallbacks(keynumStart);
      handler.removeCallbacks(key6Start);
      if (mykeyevent){
          handler.removeCallbacks(keynumStart);
          if (!pmanager.ifConfigServiceOn()&&NetUtil.isNetworkAvailable(AppContext.getAppContext())) {
              bindServices();
              pmanager.setConfigServiceOn();
          }
          // 开启后台配置桌面↑
          //点击发送广播显示APK↓
          Intent intent = new Intent("con.ider.overlauncher.LAUNCHER");
          sendBroadcast(intent);
      }
      mass_pwd=0;
      super.onRestart();
  }
}
