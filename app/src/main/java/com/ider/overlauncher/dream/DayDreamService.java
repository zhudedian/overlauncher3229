package com.ider.overlauncher.dream;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.service.dreams.DreamService;
import android.util.Log;

import com.ider.overlauncher.R;


public class DayDreamService extends DreamService {

    private final static String TAG = "DayDreamService";



    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.i(TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    //退出的时候干掉自己哈哈哈哈
    @Override
    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();

        Log.i(TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
//            viewPager.setCurrentItem(currentItem);
        };
    };



    @Override
    public void onDreamingStarted() {
        Log.i(TAG,Thread.currentThread().getStackTrace()[2].getMethodName());

    }

    @Override
    public void onDreamingStopped(){
        Log.i(TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
    }

}
