package com.ider.overlauncher.support;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ider.overlauncher.AppContext;
import com.ider.overlauncher.R;

/**
 * Created by Eric on 2018/1/26.
 */

public class SupportView {
    private Context context ;
    private static View mFloatView;
    private Button okButton;
    private WindowManager.LayoutParams mLayoutParams;
    private static Instrumentation instrumentation = new Instrumentation();
    private WindowManager mWindowManager;
    private Bitmap mMouseBitmap;
    private int displayWidth;
    private int displayHeight;
    private int mCurrentX;
    private int mCurrentY;

    public SupportView(Context context){
        this.context = context;
    }
    public void createView(final ClickListener listener) {

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayHeight = mWindowManager.getDefaultDisplay().getHeight();
        displayWidth = mWindowManager.getDefaultDisplay().getWidth();

        mFloatView = View.inflate(context, R.layout.support_view, null);
        okButton = (Button)mFloatView.findViewById(R.id.ok_action);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.okClick(true);
            }
        });
        mLayoutParams = new WindowManager.LayoutParams();
        //设置View默认的摆放位置
        mLayoutParams.gravity = Gravity.LEFT|Gravity.TOP ;
        //设置window type
        mLayoutParams.type = WindowManager.LayoutParams.LAST_SYSTEM_WINDOW;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //设置背景为透明
        mLayoutParams.format = PixelFormat.RGBA_8888;
        //注意该属性的设置很重要，FLAG_NOT_FOCUSABLE使浮动窗口不获取焦点,若不设置该属性，屏幕的其它位置点击无效，应为它们无法获取焦点
        mLayoutParams.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        //设置视图的显示位置，通过WindowManager更新视图的位置其实就是改变(x,y)的值
        mCurrentX = mLayoutParams.x = 0;
        mCurrentY = mLayoutParams.y = 0;
        //设置视图的宽、高
        mLayoutParams.width = displayWidth;
        mLayoutParams.height = displayHeight;
        //将视图添加到Window中
        mWindowManager.addView(mFloatView, mLayoutParams);
    }

    public void removeView(){
        mWindowManager.removeView(mFloatView);
    }
    public interface ClickListener{
        void okClick(boolean isOk);
    }


}
