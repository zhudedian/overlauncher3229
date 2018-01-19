package com.ider.overlauncher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/23.
 */

public class BottomView extends RelativeLayout {
//    private AnimationDrawable animationDrawable;
    private TextView animationView;
    private int DIRECTION_UP = 0;
    private final int DIRECTION_DOWN = 1;
    private boolean back = true;
    public BottomView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bottom_layout, this);
        initViews();
    }


    private void initViews() {
        animationView = (TextView)findViewById(R.id.anim_iv2);
//        animationDrawable = (AnimationDrawable) animationView.getDrawable();
//        animationDrawable.start();
    }

    public BottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom_layout, this);
        initViews();
    }

    public BottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.bottom_layout, this);
        initViews();
    }
    public void setArrowDirection(int direction) {

        if (direction == DIRECTION_UP) {
            Drawable drawable = getResources().getDrawable(R.mipmap.button_iv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            animationView.setCompoundDrawables(drawable, null, null, null);
            animationView.setText(getResources().getString(R.string.toast_open_app));
        } else if (direction == DIRECTION_DOWN) {
            Drawable drawable = getResources().getDrawable(R.mipmap.button_iv_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            animationView.setCompoundDrawables(drawable, null, null, null);
            animationView.setText(getResources().getString(R.string.toast_close_app));
        }

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){

        }

        return super.onKeyDown(keyCode, event);
    }


}
