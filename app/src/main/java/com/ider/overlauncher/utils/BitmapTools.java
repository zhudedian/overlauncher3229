package com.ider.overlauncher.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.GridLayout;
import android.widget.ImageView;


import com.ider.overlauncher.Constant;
import com.ider.overlauncher.R;
import com.ider.overlauncher.db.ServerApp;
import com.ider.overlauncher.model.ItemEntry;
import com.ider.overlauncher.model.PackageHolder;

import java.util.List;

public class BitmapTools {

    private static BitmapTools INSTANCE;
    private BitmapTools() {

    }

    public static BitmapTools getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new BitmapTools();
        }
        return INSTANCE;
    }

    public Bitmap getResourcecBitmap(Context mContext, int resourceId) {
        Drawable drawable = mContext.getResources().getDrawable(resourceId);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }

    public Bitmap getFolderThumbnailBitmap(Context mContext, List<ServerApp> packages, int width, int height, String tag) {
        // 创建一个grid视图
        View view = View.inflate(mContext, R.layout.folder_thumbnail_grid, null);
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        view.layout(0, 0, width, height);

        GridLayout gridLayout = (GridLayout) view;
        setupThumbnailGrid(gridLayout, packages,tag);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    private void setupThumbnailGrid(GridLayout gridLayout, List<ServerApp> list,String tag) {
        Log.i("zzz", "list.size() : =="+list.size() );
        if(list.size() >= 3) {
            list = list.subList(0, 3);
        }
        else if(list.size()==1){
            list.add(new PackageHolder("add",null));
            list.add(new PackageHolder("add",null));
        }else if(list.size()==2){
            list.add(new PackageHolder("add",null));
        }else if(list.size()==0){
            list.add(new PackageHolder("add",null));
            list.add(new PackageHolder("add",null));
            list.add(new PackageHolder("add",null));
        }
        for(int i = 0; i < 3; i++) {
            Drawable drawable;
            if (list.get(i).getPackageName().equals("add")) {
                if("01".equals(tag)){
                    drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.addone);
                }else  if("02".equals(tag)){
                    drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.addtwo);
                } else  if("03".equals(tag)){
                    drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.addthree);
                }else  if("04".equals(tag)){
                    drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.addfor);
                } else{
                    drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.addfive);
                }
            }else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_APPS)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.apps);
            }else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_SEARCH)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.search);
            }else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_HISTORY)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.hisfav);
            }else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_CLEAN)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.clean);
            }else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_FASTKEY)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.my_key);
            }else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_WEATHER)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.weather);
            }else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_SPEED)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.meter_icon);
            }else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_NETSET)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.zxy_net);
            }else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_DATE)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.zxy_date);
            }
            else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_SOUND)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.zxy_sound);
            }
            else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_DISPLAY)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.zxy_diplay);
            }
            else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_FLASH)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.zxy_flash);
            }
            else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_IME)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.zxy_ime);
            }else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_INFO)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.zxy_info);
            }
            else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_FACTORY)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.zxy_factory);
            }
            else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_APPMANAGER)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.zxy_appmanager);
            }
            else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_DEVELOPE)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.zxy_developer);
            }
            else if(list.get(i).getPackageName().equals(Constant.PACKAGE_TOOL_MORE)){
                drawable = gridLayout.getContext().getResources().getDrawable(R.mipmap.zxy_more);
            }

            else {
                drawable = ItemEntry.loadImage(gridLayout.getContext(), list.get(i).getPackageName());
            }
            ((ImageView) gridLayout.getChildAt(i)).setImageDrawable(drawable);
        }


    }



}
