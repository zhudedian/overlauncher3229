package com.ider.overlauncher.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ider.overlauncher.AppContext;
import com.ider.overlauncher.AppInfo;
import com.ider.overlauncher.FolderActivity;
import com.ider.overlauncher.R;
import com.ider.overlauncher.Sql.Book;
import com.ider.overlauncher.Sql.QueryApplication;
import com.ider.overlauncher.Sql.SelectImage;
import com.ider.overlauncher.db.DbManager;
import com.ider.overlauncher.db.ServerApp;
import com.ider.overlauncher.gson.DataApp;
import com.ider.overlauncher.gson.GsonUtil;
import com.ider.overlauncher.gson.PopupUtil;
import com.ider.overlauncher.gson.UpdatePopup;
import com.ider.overlauncher.model.PackageHolder;
import com.ider.overlauncher.utils.AppUtil;
import com.ider.overlauncher.utils.NetUtil;
import com.ider.overlauncher.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

//import com.ider.overlauncher.utils.EntryAnimation;

/**
 * Created by Administrator on 2017/12/21.
 */

public class SinatvFolder extends RelativeLayout implements View.OnClickListener, View.OnLongClickListener{
        private static final String TAG = "SinatvFolder";
    private DbManager dbManager;
    private List<ServerApp> packages;
    private ImageView sinatvGrid;
    private RelativeLayout sinatvfolder;
    private PreferenceManager pm;
    private Context context;
    private TextView tpss;
    private TextView title;
    private String packageName;
    private MyRelative.FucosListener listener;



    public SinatvFolder(Context context) {
        this(context, null);
        this.context = getContext();
    }

    public void setListener(MyRelative.FucosListener listener){

        this.listener = listener;
    }
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (context==null){
            context = getContext();
        }
        SharedPreferences preferences = getContext().getSharedPreferences("launcher_prefers", Context.MODE_PRIVATE);
        packageName = preferences.getString((String) getTag(), null);
        if (packageName!=null) {
            List<ServerApp> apps = QueryApplication.query(getContext());
            if (apps.contains(new PackageHolder(packageName))) {
                PackageManager packageManager = getContext().getPackageManager();
                ApplicationInfo appInfo = null;
                try {
                    appInfo = packageManager.getApplicationInfo(packageName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if(gainFocus){
                    Intent intent = new Intent("com.ider.sinatv");
                    AppContext.getAppContext().sendBroadcast(intent);
                }else {
                    Intent intent = new Intent("com.ider.sinatvoff");
                    AppContext.getAppContext().sendBroadcast(intent);
                }

            }
        } if (packageName==null){
            if(gainFocus){
                Intent intent = new Intent("com.ider.sinatvoff");
                AppContext.getAppContext().sendBroadcast(intent);
            }else {
                Intent intent = new Intent("com.ider.sinatvoff");
                AppContext.getAppContext().sendBroadcast(intent);
            }
        }
       if (listener!=null){
          listener.fucosChange(this,gainFocus);
       }
    }
    public interface FucosListener{
        void fucosChange(View view, boolean gainFocus);
    }
    public SinatvFolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.shortcut_folder_sinatv, this);
        sinatvGrid = (ImageView) findViewById(R.id.shortcut_sinatv_thumbnail_grid);
        sinatvfolder = (RelativeLayout) findViewById(R.id.sinatvfolder);
        title = (TextView) findViewById(R.id.shortcut_sinatv_text);
       pm = PreferenceManager.getInstance(context);
        setOnClickListener(this);
        setOnLongClickListener(this);
        updateTitle(context);

    }

public void onWindowFocusChanged(boolean hasWindowFocus) {
    super.onWindowFocusChanged(hasWindowFocus);
    SharedPreferences preferences = getContext().getSharedPreferences("launcher_prefers", Context.MODE_PRIVATE);
    packageName = preferences.getString((String) getTag(), null);
    if (packageName!=null) {
        List<ServerApp> apps = QueryApplication.query(getContext());
        if (apps.contains(new PackageHolder(packageName))) {
            SelectImage.selectImage(getContext(),sinatvGrid, packageName);
            PackageManager packageManager = getContext().getPackageManager();
            ApplicationInfo appInfo = null;
            try {
                appInfo = packageManager.getApplicationInfo(packageName, 0);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            sinatvfolder.setBackgroundResource(R.mipmap.folder_sinatv_paly);
            title.setText(appInfo.loadLabel(packageManager));
        }else {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString((String) getTag(), null);
        editor.apply();
        packageName = null;

        }
    }
        if (packageName==null){
            sinatvGrid.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.add_sinatv));
            sinatvfolder.setBackgroundResource(R.mipmap.folder_sinatv);
            sinatvGrid.setBackground(null);
            title.setText(R.string.add);
        }
    }


    @Override
    public void onClick(View view) {
        onClick();
    }



    public void updateTitle(Context context) {
        String tag = (String) getTag();
        String currentTitle = pm.getPackage(tag + "zxy");
//        if (currentTitle == null) {
//            if (tag.equals("6")) {
//                title.setText(context.getResources().getString(R.string.live));
//            } else {
//                title.setText(currentTitle);
//            }
//        }
    }

        public void updateSelf () {
            dbManager = DbManager.getInstance(getContext());
            packages = dbManager.queryPackages((String) getTag());
    updateTitle(getContext());
        }


    public void onClick() {
        if (packageName==null){
            showAppSelectWindow();
        }else {
            if (NetUtil.isNetworkAvailable(AppContext.getAppContext())) {
                GsonUtil.getDataApps(new GsonUtil.HandleResult() {
                    @Override
                    public void resultHandle(List<DataApp> list) {
                        final DataApp app = AppUtil.isNeedUpdate(packageName, list);
                        if (app == null) {
                            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
                            if (intent != null) {
                                getContext().startActivity(intent);
                            }
                        }else {
                            PopupUtil.getUpdatePopup(getContext(), app.getLabel(), app.getIconUrl(), new UpdatePopup.OnOkListener() {
                                @Override
                                public void onOkClick(boolean isOk) {
                                    if (isOk){
                                        startDownloadPage(app);
                                    }else {
                                        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
                                        if (intent != null) {
                                            getContext().startActivity(intent);
                                        }
                                    }
                                    PopupUtil.forceDismissPopup();
                                }
                            }).show(sinatvfolder);
                        }
                    }
                });
            }else {
                Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
                if (intent != null) {
                    getContext().startActivity(intent);
                }
            }

        }
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
        getContext().startActivity(intent);

    }
    public void showAppSelectWindow() {
        AppSinatvWindow appSinatvWindow = AppSinatvWindow.getInstance(getContext());
        appSinatvWindow.setOnAppSelectListener(new AppSinatvWindow.OnAppSelectListener() {
                @Override
                public void onAppSelected(PackageHolder holder) {
                   Book book = new Book(holder.getPackageName(),(String) getTag());
                    book.save();
                    SharedPreferences preferences = getContext().getSharedPreferences("launcher_prefers",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    if(holder.getPackageName().equals(preferences.getString("001", null))||holder.getPackageName().equals(preferences.getString("002", null)) || holder.getPackageName().equals(preferences.getString("003", null))|| holder.getPackageName().equals(preferences.getString("004", null))){

                        Toast.makeText(getContext(),"App already exists",Toast.LENGTH_SHORT).show();
                    }else {
                        editor.putString((String) getTag(), holder.getPackageName());
                        editor.apply();
                    }
                }
        });
        appSinatvWindow.showAppPopWindow(packages,sinatvfolder);
        sinatvGrid.setBackground(null);
    }

    public boolean onLongClick(View view) {
        sinatvGrid.setImageBitmap(BitmapFactory.decodeResource(AppContext.getAppContext().getResources(), R.mipmap.add_sinatv));
        title.setText(R.string.add);
        SharedPreferences preferences = AppContext.getAppContext().getSharedPreferences("launcher_prefers", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString((String) getTag(), null);
        editor.apply();
        packageName=null;
        sinatvfolder.setBackgroundResource(R.mipmap.folder_sinatv);
        sinatvGrid.setBackground(null);
        return true;

    }
}

