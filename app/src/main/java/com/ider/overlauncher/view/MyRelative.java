package com.ider.overlauncher.view;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ider.overlauncher.R;
//import com.ider.overlauncher.utils.EntryAnimation;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */

public class MyRelative extends RelativeLayout {
    private Context context;
    private Animator animator;
    private TextView textView;
    private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private FucosListener listener;
    public MyRelative(Context context){
        super(context,null);
    }
    public MyRelative(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.my_relative, this);
        textView = (TextView)findViewById(R.id.textdata);
        imageView = (ImageView)findViewById(R.id.imagedata);
        imageView1 = (ImageView)findViewById(R.id.imagedata1);
        imageView2 = (ImageView)findViewById(R.id.imagedata2);
        initView();
    }
    private void initView(){
        if (getTag().equals("1")){

            textView.setText(R.string.film);
            imageView.setImageResource(R.mipmap.bt_film);
        }
        if (getTag().equals("2")){

            textView.setText(R.string.cartoon);
            imageView.setImageResource(R.mipmap.bt_cartoon);
        }
        if (getTag().equals("3")){

            imageView.setImageResource(R.mipmap.bt_news);
            textView.setText(R.string.news);
        }
        if (getTag().equals("4")){

            imageView.setImageResource(R.mipmap.bt_live);
            textView.setText(R.string.live);
        }
        if (getTag().equals("5")){

            imageView.setImageResource(R.mipmap.bt_variety);
            textView.setText(R.string.variety);
        }
        if (getTag().equals("6")){

            imageView.setImageResource(R.mipmap.bt_sportsevents);
            textView.setText(R.string.sportsevents);
        }
        if (getTag().equals("8")){

            imageView2.setImageResource(R.mipmap.searchmyrelative);
        }
        if (getTag().equals("9")){

            imageView2.setImageResource(R.mipmap.historymyrelative);
        }
//        }else if (getTag().equals("2")){
//
//        textView.setText(R.string.video);
    }
    public void setTitle(String titleStr){
        textView.setText(titleStr);
    }
    public ImageView getImageView(){
        return imageView;
    }
    public void setImage(Drawable drawable){
        imageView.setImageDrawable(drawable);
    }
    public void setListener(FucosListener listener){
        this.listener = listener;
    }
    @Override
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
//        animator.start();
        if (listener!=null){
            listener.fucosChange(this,gainFocus);
        }
    }
    public interface FucosListener{
        void fucosChange(View view,boolean gainFocus);
    }
    private void handleIntent(Intent intent) {
        // 隐式调用的方式startActivity
        //intent.setAction("com.tencent.qqlivetv.open");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.yidian.calendar");//设置视频包名，要先确认包名
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager
                .queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "未安装腾讯视频 ， 无法跳转", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWithHomePageUri(String url) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        handleIntent(intent);
    }
}



