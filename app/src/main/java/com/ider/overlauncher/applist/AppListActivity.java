package com.ider.overlauncher.applist;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ider.overlauncher.AppContext;
import com.ider.overlauncher.AppInfo;
import com.ider.overlauncher.FolderActivity;
import com.ider.overlauncher.R;
import com.ider.overlauncher.gson.DataApp;
import com.ider.overlauncher.utils.ActivityAnimationTool;


public class AppListActivity extends Activity implements AppLicationViewDao.updateView {
    private static final long DEFAULT_TRAN_DUR_BIG_ANIM = 150;
    private static final long DEFAULT_TRAN_DUR_SMALL_ANIM = 50;
    private static final String TAG = "AppListActivity";
    public GridView appGrid;
    ProgressBar progress, waitProgress;
    TextView appCount, appSize;
    AppLicationViewDao.MyPresenter myPresenter;
    View lastSelected = null;
    int lastPostion=0;
    FlyImageView fly;
    boolean isLongClick = false;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist);
        myPresenter = new MyPresenterImpl(this, handler);
        registerReceivers();
        initView();
        setListener();
        ActivityAnimationTool.prepareAnimation(this);
        ActivityAnimationTool.animate(this, 1000);
    }

    int currentSelectedLine = 0;

    private void setListener() {
        appGrid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view == null) {
                    return;
                }
                if (lastSelected != null) {
                    lastSelected.animate().scaleX(1f).scaleY(1f).setDuration(DEFAULT_TRAN_DUR_SMALL_ANIM).start();
                } else {
                    appGrid.getChildAt(0).animate().scaleX(1f).scaleY(1f).setDuration(DEFAULT_TRAN_DUR_SMALL_ANIM).start();
                }
                int[] locations = new int[2];
                view.getLocationOnScreen(locations);

                if (locations[1] <= dip2px(getApplicationContext(), 258 + 26)) {
                    currentSelectedLine = 0;
                } else {
                    currentSelectedLine = 1;
                }
//               view.animate().scaleX(1.11f).scaleY(1.11f).setDuration(DEFAULT_TRAN_DUR_BIG_ANIM).start();
                fly.flyTo(fly.getWidth(), fly.getHeight(), getChildLocation(position, currentSelectedLine)[0]+80, getChildLocation(position, currentSelectedLine)[1]+20);
                lastSelected = view;
                lastPostion = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        appGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myPresenter.OnApplicationClick(position);
            }
        });
        appGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isLongClick = true;
                myPresenter.OnApplicationLongClick(position, view);
                return true;
            }
        });
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

    public int[] getChildLocation(int position, int currentSelectedLine) {
        int x, y;

        if (currentSelectedLine == 0) {
            y = dip2px(getApplicationContext(), 189);
            x = dip2px(getApplicationContext(), 212 * (position % 5) + 38);
        } else {
            y = dip2px(getApplicationContext(), 192) + dip2px(getApplicationContext(), 151 );
            x = dip2px(getApplicationContext(), 212 * (position % 5) + 38);
        }
        return new int[]{x, y};
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getDimen(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    private void initView() {
        progress = (ProgressBar) findViewById(R.id.size_pro);
        appGrid = (GridView) findViewById(R.id.recyclerView);
        appCount = (TextView) findViewById(R.id.title_apps);
        appSize = (TextView) findViewById(R.id.totalsize);
        fly = (FlyImageView) findViewById(R.id.mainUpView1);
        waitProgress = (ProgressBar) findViewById(R.id.progress);
        waitProgress.setVisibility(View.GONE);
        myPresenter.getApps();


    }

    private void registerReceivers() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addDataScheme("package");
        registerReceiver(packageReceiver, filter);
        registerReceiver(mHomeKeyEventReceiver,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
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
                    AppListActivity.this.finish();
                }else if(TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)){
                    //表示长按home键,显示最近使用的程序列表
                    AppListActivity.this.finish();
                }
            }
        }
    };
    @Override
    public void showAppList(BaseAdapter adapter) {
        appGrid.setAdapter(adapter);
        fly.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (appGrid.getChildAt(0) != null) {
//                    appGrid.getChildAt(0).animate().scaleX(1.11f).scaleY(1.11f).setDuration(DEFAULT_TRAN_DUR_BIG_ANIM).start();
                }
            }
        }, 100);
    }

    @Override
    public void showProgressbarandAppcount(int[] progressAndcount) {
        progress.setProgress(progressAndcount[1]);
        appCount.setText(String.format(getResources().getString(R.string.applist_title), progressAndcount[0]));
        appSize.setText(String.format(getResources().getString(R.string.applist_availabel_space), progressAndcount[2]));
    }

    @Override
    public void showProgressbar() {
        waitProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        waitProgress.setVisibility(View.GONE);
    }


    BroadcastReceiver packageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                myPresenter.updateAfterUninstall();
            } else if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                String data = intent.getDataString();
                String packgename = data.substring(data.indexOf(":") + 1,
                        data.length());
                myPresenter.updateAfterInstall(packgename);
            }

        }
    };

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(packageReceiver);
            unregisterReceiver(mHomeKeyEventReceiver);
        } catch (Exception e) {

        }
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isLongClick) {
                myPresenter.hidenLongClick();
                isLongClick = false;
                return true;
            }
        }if(keyCode == KeyEvent.KEYCODE_MENU) {
            if (!isLongClick) {
                isLongClick = true;
                if(lastSelected==null){
                    lastSelected = appGrid.getChildAt(0);
                }

                if(lastSelected!=null)
                myPresenter.OnApplicationLongClick(lastPostion, lastSelected);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
