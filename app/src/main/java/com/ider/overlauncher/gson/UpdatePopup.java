package com.ider.overlauncher.gson;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ider.overlauncher.R;

/**
 * Created by Eric on 2018/1/8.
 */

public class UpdatePopup implements View.OnKeyListener{
    private String TAG = "UpdatePopup";
    private Context context;
    private PopupWindow popupWindow;
    private RelativeLayout outsideRelative;
    private LinearLayout innerLinear;
    private TextView title,label,checkText;
    private ImageView imageView;
    private Button ok,cancel;
    private OnOkListener listener;
    private boolean outsideTouchable;
    private boolean cancelable = true;

    public UpdatePopup(Context context,String titleStr,String noticeStr,String iconUrl,boolean outsideTouchable,UpdatePopup.OnOkListener listener){
        this.context = context;
        this.outsideTouchable = outsideTouchable;
        this.listener = listener;
        View view = getView();
        title.setText(titleStr);
        Glide.with(context).load(iconUrl).into(imageView);
        label.setText(noticeStr);
        popupWindow = new PopupWindow(view,-1,-1);
    }
    public UpdatePopup(Context context,String titleStr,String noticeStr,String checkStr,String okStr,
                        String cancelStr,boolean outsideTouchable,UpdatePopup.OnOkListener listener){
        this.context = context;
        this.outsideTouchable = outsideTouchable;
        this.listener = listener;
        View view = getView();
        view.setOnKeyListener(this);
        title.setText(titleStr);
        label.setText(noticeStr);
        ok.setText(okStr);
        cancel.setText(cancelStr);
        popupWindow = new PopupWindow(view,-1,-1,true);
    }
    private View getView(){
        View view = View.inflate(context, R.layout.update_popup, null);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG,"view KEYCODE"+event.getKeyCode());
                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.i(TAG,"view KEYCODE_BACK");
                    listener.onOkClick(false);
                    PopupUtil.forceDismissPopup();
                    return true;
                }
                return false;
            }
        });
        outsideRelative = (RelativeLayout)view.findViewById(R.id.outside_relative);
        innerLinear = (LinearLayout) view.findViewById(R.id.inner_linear);
        title = (TextView)view.findViewById(R.id.title);
        imageView = (ImageView)view.findViewById(R.id.image) ;
        label = (TextView)view.findViewById(R.id.label);
        cancel = (Button)view.findViewById(R.id.cancel_action);
        ok = (Button)view.findViewById(R.id.ok_action);
//        cancel.setOnKeyListener(this);
//        ok.setOnKeyListener(this);
        cancel.setFocusable(true);
        cancel.setFocusableInTouchMode(true);
        cancel.requestFocus();
        outsideRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outsideTouchable){
                    PopupUtil.dismissPopup();
                }
            }
        });
        innerLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOkClick(false);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOkClick(true);
            }
        });
        return view;
    }
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        Log.i(TAG,"KEYCODE"+keyEvent.getKeyCode());
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i(TAG,"KEYCODE_BACK");
            listener.onOkClick(false);
            PopupUtil.forceDismissPopup();
            return true;
        }
        return false;
    }
    public UpdatePopup setCancelable(boolean cancelable){
        this.cancelable = cancelable;
        return this;
    }

    public boolean isCancelable(){
        return this.cancelable;
    }
    public boolean isShowing(){
        return popupWindow.isShowing();
    }
    public void dismiss(){
        if (popupWindow!=null&&popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
    public void show(View parent){
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
    }


    public interface OnOkListener{
        void onOkClick(boolean isOk);
    }
}
