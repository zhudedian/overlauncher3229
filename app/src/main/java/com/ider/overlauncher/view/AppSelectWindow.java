package com.ider.overlauncher.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.ider.overlauncher.R;
import com.ider.overlauncher.adapter.AppAdapter;
import com.ider.overlauncher.db.ServerApp;
import com.ider.overlauncher.model.PackageHolder;

import java.util.ArrayList;
import java.util.List;



public class AppSelectWindow implements View.OnKeyListener, AdapterView.OnItemClickListener{

    private Context mContext;
    private List<ServerApp> allApps;
    private View baseView;
    private PopupWindow popWindow;
    private static AppSelectWindow INSTANCE;
    private OnAppSelectListener onAppSelectListener;
    private List<ServerApp> locals = null;




    public interface OnAppSelectListener{
        void onAppSelected(PackageHolder holder);
    }
    public void setOnAppSelectListener(OnAppSelectListener onAppSelectListener) {
        this.onAppSelectListener = onAppSelectListener;
    }


    private AppSelectWindow(Context context) {
        this.mContext = context;
    }

    public static AppSelectWindow getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new AppSelectWindow(context);
        }
        return INSTANCE;
    }


    public void showAppPopWindow(View baseView) {
        this.baseView = baseView;
        View view = View.inflate(mContext, R.layout.app_select_window, null);
        view.setOnKeyListener(this);

        GridView gridView = (GridView) view.findViewById(R.id.app_select_grid);
        gridView.setOnKeyListener(this);
        gridView.setOnItemClickListener(this);
        setupAppGrid(gridView);

        this.popWindow = new PopupWindow(view, -1, -1, true);
        this.popWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        this.popWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);
    }


    private void setupAppGrid(GridView gridView) {
        allApps =queryApplication(mContext);
        if(allApps.size()==0) {
            allApps.add(new PackageHolder(0L, "add", null));
        }
            AppAdapter adapter = new AppAdapter(mContext, R.layout.app_select_item, allApps);
            gridView.setAdapter(adapter);

    }
    public void setData(List<ServerApp> locals){
        this.locals = locals;
    }
    private  List<ServerApp> queryApplication(Context context) {
        List<ServerApp> enties = new ArrayList<>();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String packageName = resolveInfo.activityInfo.applicationInfo.packageName;
            if(!packageName.equals("com.yidian.calendar")&&!packageName.equals("com.android.settings")){
                if(locals==null){
                    enties.add(new PackageHolder(packageName, null));
                }else if(!new ServerApp(packageName).containsPackageName(locals)) {
                    enties.add(new PackageHolder(packageName, null));
                }
            }
        }
        return enties;
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            this.popWindow.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        if(baseView instanceof ShortcutView) {
//            ((ShortcutView) baseView).mItemEntry = new ItemEntry(allApps.get(i).getPackageName());
//            ((ShortcutView) baseView).updateSelf();
//            ((ShortcutView) baseView).save();
//        } else
//        if(baseView instanceof GridView) {
        try {
            if(onAppSelectListener != null) {
                    onAppSelectListener.onAppSelected((PackageHolder) allApps.get(i));
            }

//        }

            popWindow.dismiss();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
