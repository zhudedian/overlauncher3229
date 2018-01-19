package com.ider.overlauncher.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.overlauncher.Constant;
import com.ider.overlauncher.FolderActivity;
import com.ider.overlauncher.HomeActivity;
import com.ider.overlauncher.R;
import com.ider.overlauncher.db.DbManager;
import com.ider.overlauncher.db.ServerApp;
import com.ider.overlauncher.model.PackageHolder;
import com.ider.overlauncher.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ider-eric on 2016/12/29.
 */

public class ShortcutFolder extends BaseEntryView {
    private static final String TAG = "ShortcutFolder";
    // 数据库操作类
    private DbManager dbManager;
    // 用于保存用户存储的package信息
    private List<ServerApp> packages;
    private ImageView thumbnailGrid;
    private ImageView thumbnailGrid1;
    private ImageView image;
    private com.ider.overlauncher.utils.PreferenceManager pm ;
    private TextView title;
    private MyRelative.FucosListener listener;
    private Context context;
    public ShortcutFolder(Context context) {

        this(context, null);
    }
    public void setListener(MyRelative.FucosListener listener){
        this.listener = listener;
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {

//        if (gainFocus) {
//            animator = EntryAnimation.createFocusAnimator(this);
//        } else {
//
//            if (animator != null && animator.isRunning()) {
//                animator.cancel();
//            }
//            animator = EntryAnimation.createLoseFocusAnimator(this);
//
//        }
//       animator.start();
       if (listener!=null){
            listener.fucosChange(this,gainFocus);
       }
    }
    public ShortcutFolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.shortcut_folder_small, this);
        thumbnailGrid = (ImageView) findViewById(R.id.shortcut_folder_thumbnail_grid);
        thumbnailGrid1 = (ImageView) findViewById(R.id.shortcut_folder_thumbnail_grid1);
        image = (ImageView)findViewById(R.id.image);
        title = (TextView) findViewById(R.id.shortcut_folder_text);
        pm = PreferenceManager.getInstance(context);
        updateTitle(context);
    }


    public void  updateTitle(Context context){
        String tag = (String) getTag();
        String currentTitle = pm.getPackage(tag+"zxy");
        if(currentTitle==null) {
            if (tag.equals("01")) {
                title.setText(context.getResources().getString(R.string.common));

                thumbnailGrid.setImageResource(R.drawable.commonfolder);
            }
            if (tag.equals("02")) {
                title.setText("电视");
                image.setImageResource(R.mipmap.btn_live);
                thumbnailGrid1.setImageResource(R.drawable.livefolder);
            }
            if (tag.equals("03")) {
                title.setText("影视");
                image.setImageResource(R.mipmap.btn_film);
                thumbnailGrid1.setImageResource(R.drawable.moviefolder);
            }
//            if (tag.equals("03")) {
//                title.setText(context.getResources().getString(R.string.music));
//            }
//            if (tag.equals("04")) {
//                title.setText(context.getResources().getString(R.string.Tools));
//            }
//            if (tag.equals("05")) {
//                title.setText(context.getResources().getString(R.string.settings));
//            }
        }else{
            title.setText(currentTitle);
        }

    }

    @Override
    public void updateSelf() {
        dbManager = DbManager.getInstance(getContext());
        packages = dbManager.queryPackages((String) getTag());


        if("01".equals(getTag())){
            packages.add(0,new PackageHolder(Constant.PACKAGE_TOOL_SETTING,"01"));
            packages.add(1,new PackageHolder(Constant.ACTION_HROCKCHIP,"01"));
            packages.add(2,new PackageHolder(Constant.PACKAGE_TOOL_APPS,"01"));
            packages.add(3,new PackageHolder(Constant.PACKAGE_TOOL_CLEAN,"01"));
            packages.add(4,new PackageHolder(Constant.PACKAGE_TOOL_SPEED,"01"));
            packages.add(5,new PackageHolder(Constant.ACTION_LOGIN,"01"));
            packages.add(6,new PackageHolder(Constant.PACKAGE_TOOL_FASTKEY,"01"));

        }
        Bitmap bitmap = mBitmapTools.getFolderThumbnailBitmap(getContext(), packages, thumbnailGrid.getWidth(), thumbnailGrid.getHeight(),(String)getTag());
      thumbnailGrid.setImageBitmap(bitmap);
        updateTitle(getContext());

    }

    @Override
    public void setDefault() {
        ArrayList<PackageHolder> list = new ArrayList<>();
        Log.i("zzz", "setDefault: getTag="+getTag());
        for (String packageName:Constant.demandPackageNames){
            if (HomeActivity.isDownload(packageName)){
                list.add(new PackageHolder(packageName,"03"));
            }
        }
        for (String packageName:Constant.tvPackageNames){
            if (HomeActivity.isDownload(packageName)){
                list.add(new PackageHolder(packageName,"02"));
            }
        }
        for (String packageName:Constant.storePackageNames){
            if (HomeActivity.isDownload(packageName)){
                list.add(new PackageHolder(packageName,"01"));
            }
        }

//        if("05".equals(getTag())){
//            list.add(0,new PackageHolder(Constant.PACKAGE_TOOL_WEATHER,"05"));
//            list.add(1,new PackageHolder(Constant.PACKAGE_TOOL_FASTKEY,"05"));
//            list.add(2,new PackageHolder(Constant.PACKAGE_TOOL_NETSET,"05"));
//            list.add(3,new PackageHolder("com.zxy.idersettings","05"));
//            Log.i("zzz", "setDefault: list="+list.size());
//        }else if("04".equals(getTag())){
//            list.add(0,new PackageHolder(Constant.PACKAGE_TOOL_APPS,"04"));
//            list.add(1,new PackageHolder(Constant.PACKAGE_TOOL_SEARCH,"04"));
//            list.add(2,new PackageHolder(Constant.PACKAGE_TOOL_CLEAN,"04"));
//            list.add(3,new PackageHolder(Constant.PACKAGE_TOOL_HISTORY,"04"));
//            list.add(4,new PackageHolder(Constant.PACKAGE_TOOL_SPEED,"04"));
//        }
//      Bitmap bitmap = mBitmapTools.getFolderThumbnailBitmap(getContext(), list, thumbnailGrid.getWidth(), thumbnailGrid.getHeight());thumbnailGrid.setImageBitmap(bitmap);
//        list.add(new PackageHolder("com.ider.help","02"));
//        list.add(new PackageHolder("com.ider.help","01"));
        DbManager.getInstance(getContext()).insertPackages(list);
    }
    @Override
    public void onClick() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext());
        Intent intent = new Intent(getContext(), FolderActivity.class);
        intent.putExtra("tag", (String) getTag());
        ActivityCompat.startActivity(getContext(), intent, compat.toBundle());
    }

    @Override
    public void onLongClick() {

    }

}
