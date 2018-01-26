package com.ider.overlauncher.applist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.overlauncher.AppContext;
import com.ider.overlauncher.R;
import com.ider.overlauncher.gson.DataApp;
import com.ider.overlauncher.gson.GsonUtil;
import com.ider.overlauncher.gson.PopupUtil;
import com.ider.overlauncher.gson.UpdatePopup;
import com.ider.overlauncher.utils.AppUtil;
import com.ider.overlauncher.utils.NetUtil;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */

public class MyPresenterImpl implements AppLicationViewDao.MyPresenter {
    AppLicationViewDao.updateView activity;
    ApplicationAdapter adapter;
    List<Application> apps;
    Context context;
    AppListActivity appListActivity;
    Handler mHandler;
    View oldView;
    int position = 0;

    public MyPresenterImpl(AppLicationViewDao.updateView activity, Handler mHandler) {
        this.context = AppContext.getAppContext();
        this.activity = activity;
        this.appListActivity = (AppListActivity) activity;
        this.mHandler = mHandler;
    }

    @Override
    public void getApps() {
        activity.showProgressbar();
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                apps = ApplicationUtil.loadAllApplication(context);

                adapter = new ApplicationAdapter(apps);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        activity.showAppList(adapter);
                        activity.showProgressbarandAppcount(getProgressAndAppcount());
                        activity.hideProgressbar();
                    }
                });
                Looper.loop();
            }
        }.start();

    }

    @Override
    public int[] getProgressAndAppcount() {

        int count = apps.size();
        int totalMb = getSDTotalSize();
        int availableMb = getSDAvailableSize();
        int progress = (totalMb - getSDAvailableSize()) * 100 / totalMb;
        int[] datas = new int[]{count, progress, availableMb};
        Log.i("Launcher", "getProgressAndAppcount: count=="+count+"***progress=="+progress+"**availableMb=="+availableMb);
        return datas;
    }

    @Override
    public void OnApplicationLongClick(int position, View view) {
        this.position = position;
        Application app = apps.get(position);
        Button btn = (Button) view.findViewById(R.id.btn_uninstall);
        final Button btnopen = (Button) view.findViewById(R.id.btn_open);
        btn.setFocusable(true);
        btnopen.setFocusable(true);
        btn.setClickable(true);
        btnopen.setClickable(true);
        if (ApplicationUtil.isSystemApp(context, app)) {
            btn.setVisibility(View.INVISIBLE);
            btn.setFocusable(false);
            Log.i("dimen", "h_45_dp = " + context.getResources().getDimensionPixelSize(R.dimen.h_45));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    context.getResources().getDimensionPixelSize(R.dimen.h_45));
            params.gravity = Gravity.CENTER;
            btnopen.setLayoutParams(params);
            btnopen.setNextFocusDownId(btnopen.getId());
            btnopen.setNextFocusDownId(btnopen.getId());
            btnopen.setNextFocusLeftId(btnopen.getId());
            btnopen.setNextFocusRightId(btnopen.getId());
        }
        btnopen.setOnClickListener(clicker);
        btn.setOnClickListener(clicker);
        view.findViewById(R.id.select_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.select_view).animate().alpha(1f).setDuration(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                btnopen.requestFocus();
            }
        });
        oldView = view;
    }

    View.OnClickListener clicker = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_open) {
                appListActivity.isLongClick = false;
                ApplicationUtil.startApp(context, apps.get(position));
            } else if (v.getId() == R.id.btn_uninstall) {
                hidenLongClick();
                appListActivity.isLongClick = false;
                ApplicationUtil.uninstallApp(context, apps.get(position));
            }
        }
    };

    @Override
    public void OnApplicationClick(final int position) {
        Log.i("presenter", "OnApplicationClick: " + position);
        if (NetUtil.isNetworkAvailable(AppContext.getAppContext())) {
            GsonUtil.getDataApps(new GsonUtil.HandleResult() {
                @Override
                public void resultHandle(List<DataApp> list) {
                    final DataApp app = AppUtil.isNeedUpdate(apps.get(position).getPackageName(), list);
                    if (app == null) {
                        Intent intent = context.getPackageManager().getLaunchIntentForPackage(apps.get(position).getPackageName());
//                        Log.i(TAG, "onItemClick: " + holder.getPackageName());
                        if (intent != null) {
                            context.startActivity(intent);
                        }
                    }else {
                        PopupUtil.getUpdatePopup(context, app.getLabel(), app.getIconUrl(), new UpdatePopup.OnOkListener() {
                            @Override
                            public void onOkClick(boolean isOk) {
                                if (isOk){
                                    appListActivity.startDownloadPage(app);
                                }else {
                                    ApplicationUtil.startApp(context, apps.get(position));
                                }
                                PopupUtil.forceDismissPopup();
                            }
                        }).show(appListActivity.appGrid);
                    }
                }
            });
        }else {
            ApplicationUtil.startApp(context, apps.get(position));
        }

}

    @Override
    public void hidenLongClick() {
        if(oldView!=null){
            oldView.findViewById(R.id.select_view).setVisibility(View.GONE);
        oldView.findViewById(R.id.select_view).animate().alpha(0f).setDuration(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Button btn = (Button) oldView.findViewById(R.id.btn_uninstall);
                final Button btnopen = (Button) oldView.findViewById(R.id.btn_open);
                btn.setFocusable(false);
                btnopen.setFocusable(false);
                btn.setClickable(false);
                btnopen.setClickable(false);
            }
        });
        }
    }

    @Override
    public void updateAfterUninstall() {
        List<Application> applications = ApplicationUtil.loadAllApplication(context);
        apps.clear();
        apps.addAll(applications);
        adapter.notifyDataSetChanged();
        activity.showProgressbarandAppcount(getProgressAndAppcount());
    }

    @Override
    public void updateAfterInstall(String packageName) {
        List<Application> applications = ApplicationUtil.loadAllApplication(context);
        apps.clear();
        apps.addAll(applications);
        adapter.notifyDataSetChanged();
        activity.showProgressbarandAppcount(getProgressAndAppcount());
    }

    class ApplicationAdapter extends BaseAdapter {
        List<Application> apps;

        public ApplicationAdapter(List<Application> apps) {
            this.apps = apps;
        }

        @Override
        public int getCount() {
            return apps.size();
        }

        @Override
        public Object getItem(int position) {
            return apps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new GridViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applist, parent, false);
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.app_icon);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.textView);
//                viewHolder.head_tv = (TextView) convertView.findViewById(R.id.app_size);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GridViewHolder) convertView.getTag();
            }
            Application app = apps.get(position);
            viewHolder.iv.setImageDrawable(app.getIcon());
            viewHolder.tv.setText(app.getLabel());
//            viewHolder.head_tv.setText(app.getSize() + "M");
            return convertView;
        }
    }

    class GridViewHolder {
        public ImageView iv;
        public TextView tv;
        public TextView head_tv;
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    private int getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        int totalMb;

        if(Build.VERSION.SDK_INT < 18) {
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            totalMb = (int) (blockSize * totalBlocks / 1024 / 1024);
        } else {
            long totalBytes = stat.getTotalBytes();
            totalMb = (int) (totalBytes / 1024 / 1024);
        }

        return totalMb;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    private int getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        int availableMb;
        if (Build.VERSION.SDK_INT < 18) {
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            availableMb = (int) (blockSize * availableBlocks / 1024 / 1024);
        } else {
            long availableBytes = stat.getAvailableBytes();
            availableMb = (int) (availableBytes / 1024 / 1024);
        }
        return availableMb;
    }
}
