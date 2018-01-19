package com.ider.overlauncher.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.ider.overlauncher.HomeActivity;
import com.ider.overlauncher.utils.BitmapTools;


public abstract class BaseEntryView extends RelativeLayout implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "BaseEntryView";
    
    public BitmapTools mBitmapTools;
    public Context mContext;
    private ObjectAnimator animator = null;

    public BaseEntryView(Context context) {
        this(context, null);
    }

    public BaseEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnClickListener(this);
        setOnLongClickListener(this);
        mBitmapTools = BitmapTools.getInstance();
    }

    // 第一次进入程序时调用
    public abstract void setDefault();

    // 非第一次进入程序时调用，与setDefault只能调用其中给一个
    public abstract void updateSelf();

    public abstract void onClick();

    public abstract void onLongClick();

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        //Log.i(TAG,"onWindowFocusChanged");
        if (HomeActivity.isFirstIn(getContext())) {
            setDefault();
        }
        updateSelf();

    }

//    @Override
//    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
//
//        if(gainFocus) {
//            animator = EntryAnimation.createFocusAnimator(this);
//        } else {
//
//            if(animator != null && animator.isRunning()) {
//                animator.cancel();
//            }
//            animator = EntryAnimation.createLoseFocusAnimator(this);
//
//        }
//        animator.start();
//    }

    @Override
    public void onClick(View view) {
        onClick();
    }

    @Override
    public boolean onLongClick(View view) {
        onLongClick();
        return true;
    }


}
