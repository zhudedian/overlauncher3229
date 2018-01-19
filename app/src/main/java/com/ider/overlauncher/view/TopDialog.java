package com.ider.overlauncher.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ider.overlauncher.HomeActivity;
import com.ider.overlauncher.R;
import com.ider.overlauncher.utils.FocusScaleUtils;
import com.ider.overlauncher.utils.FocusUtils;

/**
 * Created by ider-eric on 2016/4/12.
 */
public class TopDialog extends FrameLayout implements View.OnFocusChangeListener{

    LinearLayout topLine;
    public ImageView topSetting, topCleaner, topFile;
    HomeActivity home;
    ImageView arrow;
    private ImageView[] items;
    public static final int DIRECTION_UP = 0;
    public static final int DIRECTION_DOWN = 1;

    public TopDialog(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.top_dialog, this);
        initViews();

    }

    public TopDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.top_dialog, this);
        initViews();
    }

    public void initViews() {
        home = (HomeActivity) getContext();
        arrow = (ImageView) findViewById(R.id.top_arrow);
        items = new ImageView[9];
        items[0] = (ImageView) findViewById(R.id.item1);
        items[1] = (ImageView) findViewById(R.id.item2);
        items[2] = (ImageView) findViewById(R.id.item3);
        items[3] = (ImageView) findViewById(R.id.item4);
        items[4] = (ImageView) findViewById(R.id.item5);
        items[5] = (ImageView) findViewById(R.id.item6);
        items[6] = (ImageView) findViewById(R.id.item7);
        items[7] = (ImageView) findViewById(R.id.item8);
        items[8] = (ImageView) findViewById(R.id.item9);
        for (int i=0;i<items.length;i++){
            items[i].setOnFocusChangeListener(this);
        }
    }





    public boolean isVisibable() {
        return topLine.getVisibility() == View.VISIBLE;
    }

    public void setVisiable(boolean visiable) {
        if (visiable) {
            topLine.setVisibility(View.VISIBLE);
        } else {
            topLine.setVisibility(View.INVISIBLE);
        }
    }

    /*
     * direction  0: up
     *            1: down
     */
    public void setArrowDirection(int direction) {

        if (direction == DIRECTION_UP) {
            //arrow.setImageResource(R.mipmap.button_iv_up);
            arrow.setVisibility(View.GONE);
        } else if (direction == DIRECTION_DOWN) {
           // arrow.setImageResource(R.mipmap.button_iv);
            arrow.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            ((HomeActivity)getContext()).showTopdialog();
        }
    }


}
