package com.ider.overlauncher.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.ider.overlauncher.R;
import com.ider.overlauncher.Sql.QueryApplication;
import com.ider.overlauncher.adapter.AppAdapter;
import com.ider.overlauncher.db.ServerApp;
import com.ider.overlauncher.model.PackageHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/12/25.
 */

public class AppSinatvWindow implements View.OnKeyListener,AdapterView.OnItemClickListener {
    private Context mContext;
    private List<ServerApp> allApps;
    private View baseView;
    private PopupWindow popWindow;
    private static AppSinatvWindow INSTANCE;
    private OnAppSelectListener onAppSelectListener;

    public interface OnAppSelectListener {
        void onAppSelected(PackageHolder holder);
    }

    public void setOnAppSelectListener(OnAppSelectListener onAppSelectListener) {
        this.onAppSelectListener = onAppSelectListener;
    }

    private AppSinatvWindow(Context context) {

        this.mContext = context;
    }

    public static AppSinatvWindow getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppSinatvWindow(context);
        }
        return INSTANCE;
    }

    public void showAppPopWindow(List<ServerApp> packages, View baseView) {
        this.baseView = baseView;
        View view = View.inflate(mContext, R.layout.app_select_window, null);
        view.setOnKeyListener(this);

        GridView gridView = (GridView) view.findViewById(R.id.app_select_grid);
        gridView.setOnKeyListener(this);
        gridView.setOnItemClickListener(this);
        setupAppGrid(packages, gridView);

        this.popWindow = new PopupWindow(view, -1, -1, true);
        this.popWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        this.popWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);
    }

    private void setupAppGrid(List<ServerApp> packages, GridView gridView) {
        SharedPreferences preferences = mContext.getSharedPreferences("launcher_prefers",Context.MODE_PRIVATE);
        allApps = QueryApplication.query(mContext);

        if (allApps.contains(new PackageHolder(preferences.getString("001", " ")))){
            allApps.remove(new PackageHolder(preferences.getString("001", " ")));
        }
        if (allApps.contains(new PackageHolder(preferences.getString("002", " ")))){
            allApps.remove(new PackageHolder(preferences.getString("002", " ")));
        }
        if (allApps.contains(new PackageHolder(preferences.getString("003", " ")))){
            allApps.remove(new PackageHolder(preferences.getString("003", " ")));
        }
        if (allApps.contains(new PackageHolder(preferences.getString("004", " ")))){
            allApps.remove(new PackageHolder(preferences.getString("004", " ")));
        }
        AppAdapter adapter = new AppAdapter(mContext, R.layout.app_select_item, allApps);
        gridView.setAdapter(adapter);
    }
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            this.popWindow.dismiss();
            return true;
        }
        return false;
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onAppSelectListener.onAppSelected((PackageHolder) allApps.get(i));
                popWindow.dismiss();
    }
}