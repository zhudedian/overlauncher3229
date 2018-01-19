package com.ider.overlauncher.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ider.overlauncher.Constant;
import com.ider.overlauncher.R;
import com.ider.overlauncher.db.ServerApp;
import com.ider.overlauncher.model.ItemEntry;

import java.util.List;

/**
 * Created by ider-eric on 2016/12/26.
 */

public class AppAdapter extends BaseAdapter {

    private static final String TAG = "AppAdapter";

    private Context mContext;
    private int layoutId = R.layout.app_select_item;
    private List<ServerApp> data;

    public AppAdapter(Context mContext, List<ServerApp> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public AppAdapter(Context mContext, int layoutId, List<ServerApp> data) {
        this(mContext, data);
        this.layoutId = layoutId;
    }




    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        ServerApp serverApp = data.get(i);
        if(view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
            holder.image = (ImageView) view.findViewById(R.id.app_item_image);
            holder.text = (TextView) view.findViewById(R.id.app_item_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String pkgname = serverApp.getPackageName();
        if (serverApp.getType()==2){
            holder.text.setText(serverApp.getLabel());
            Glide.with(mContext).load(serverApp.getIconUrl()).into(holder.image);
        }else {
            holder.image.setImageDrawable(getPackageDrawable(pkgname));
            holder.text.setText(getPackageText(pkgname));
        }
        view.setId(i);
        return view;
    }

    private Drawable getPackageDrawable(String pkgname) {
        if(pkgname.equals("add")) {
            return mContext.getResources().getDrawable(R.mipmap.add_item_white);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_APPS)){
            return mContext.getResources().getDrawable(R.mipmap.apps);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_SEARCH)){
            return mContext.getResources().getDrawable(R.mipmap.search);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_HISTORY)){
            return mContext.getResources().getDrawable(R.mipmap.hisfav);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_CLEAN)){
            return mContext.getResources().getDrawable(R.mipmap.clean);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_FASTKEY)){
            return mContext.getResources().getDrawable(R.mipmap.my_key);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_WEATHER)){
            return mContext.getResources().getDrawable(R.mipmap.weather);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_SPEED)){
            return mContext.getResources().getDrawable(R.mipmap.meter_icon);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_NETSET)){
            return mContext.getResources().getDrawable(R.mipmap.zxy_net);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_DATE)){
            return mContext.getResources().getDrawable(R.mipmap.zxy_date);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_SOUND)){
            return mContext.getResources().getDrawable(R.mipmap.zxy_sound);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_DISPLAY)){
            return mContext.getResources().getDrawable(R.mipmap.zxy_diplay);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_FLASH)){
            return mContext.getResources().getDrawable(R.mipmap.zxy_flash);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_IME)){
            return mContext.getResources().getDrawable(R.mipmap.zxy_ime);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_INFO)){
            return mContext.getResources().getDrawable(R.mipmap.zxy_info);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_FACTORY)){
            return mContext.getResources().getDrawable(R.mipmap.zxy_factory);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_APPMANAGER)){
            return mContext.getResources().getDrawable(R.mipmap.zxy_appmanager);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_DEVELOPE)){
            return mContext.getResources().getDrawable(R.mipmap.zxy_developer);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_MORE)){
            return mContext.getResources().getDrawable(R.mipmap.zxy_more);
        }
        else if(pkgname.equals(Constant.ACTION_HROCKCHIP)){
            return mContext.getResources().getDrawable(R.mipmap.files);
        }
        else if(pkgname.equals(Constant.ACTION_LOGIN)){
            return mContext.getResources().getDrawable(R.mipmap.user);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_SETTING)){
            return mContext.getResources().getDrawable(R.mipmap.settings);
        }

        else {
            return ItemEntry.loadImage(mContext, pkgname);
        }
    }

    private String getPackageText(String pkgname) {
        if(pkgname.equals("add")) {
            return mContext.getResources().getString(R.string.add);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_APPS)){
            return mContext.getResources().getString(R.string.apps);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_SEARCH)){
            return mContext.getResources().getString(R.string.search);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_HISTORY)){
            return mContext.getResources().getString(R.string.history);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_CLEAN)){
            return mContext.getResources().getString(R.string.clean);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_FASTKEY)){
            return mContext.getResources().getString(R.string.fasykey);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_WEATHER)){
            return mContext.getResources().getString(R.string.weather);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_SPEED)){
            return mContext.getResources().getString(R.string.speed);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_NETSET)){
            return mContext.getResources().getString(R.string.netset);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_DATE)){
            return mContext.getResources().getString(R.string.dateset);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_SOUND)){
            return mContext.getResources().getString(R.string.soundset);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_FASTKEY)){
            return mContext.getResources().getString(R.string.flashset);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_IME)){
            return mContext.getResources().getString(R.string.imeset);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_INFO)){
            return mContext.getResources().getString(R.string.infoset);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_FACTORY)){
            return mContext.getResources().getString(R.string.factoryset);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_APPMANAGER)){
            return mContext.getResources().getString(R.string.appset);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_DEVELOPE)){
            return mContext.getResources().getString(R.string.develpoeset);
        }else if(pkgname.equals(Constant.PACKAGE_TOOL_MORE)){
            return mContext.getResources().getString(R.string.moreset);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_DISPLAY)){
            return mContext.getResources().getString(R.string.disset);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_FLASH)){
            return mContext.getResources().getString(R.string.flashset);
        }
        else if(pkgname.equals(Constant.ACTION_HROCKCHIP)){
            return mContext.getResources().getString(R.string.filemanager);
        }
        else if(pkgname.equals(Constant.ACTION_LOGIN)){
            return mContext.getResources().getString(R.string.login);
        }
        else if(pkgname.equals(Constant.PACKAGE_TOOL_SETTING)){
            return mContext.getResources().getString(R.string.settings);
        }
        else {
            return ItemEntry.loadLabel(mContext, pkgname);
        }

    }

    class ViewHolder {
        private ImageView image;
        private TextView text;
    }
}
