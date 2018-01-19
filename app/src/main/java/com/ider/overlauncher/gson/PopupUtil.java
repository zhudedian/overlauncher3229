package com.ider.overlauncher.gson;

import android.content.Context;

import com.ider.overlauncher.R;

/**
 * Created by Eric on 2018/1/8.
 */

public class PopupUtil {


    private static UpdatePopup updatePopup;


    public static UpdatePopup getUpdatePopup(Context context, String noticeStr,String iconUrl, UpdatePopup.OnOkListener listener){
        updatePopup = new UpdatePopup(context, "发现新版本！",
                noticeStr,iconUrl, true,listener);
        return updatePopup;
    }

    public static void dismissPopup() {
        if(updatePopup!=null&& updatePopup.isShowing()){
            if (updatePopup.isCancelable()) {
                updatePopup.dismiss();
                updatePopup= null;
            }
        }
    }
    public static void forceDismissPopup() {
        if(updatePopup!=null&& updatePopup.isShowing()){
            updatePopup.dismiss();
            updatePopup= null;
        }

    }
}
