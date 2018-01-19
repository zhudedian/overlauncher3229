package com.ider.overlauncher.services;

import android.content.Context;


import com.ider.overlauncher.R;
import com.ider.overlauncher.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import oversettings.ider.com.cust.OKhttpManager;


/**
 * Created by ider-eric on 2016/11/22.
 */

public class LocationUtil {

    private static final String URL_CITY_LOCATE = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json";

    private static String networkLocation() {
        String city = null;
        try {
            String result = OKhttpManager.exuteFromServer(URL_CITY_LOCATE);
            if(result != null) {
                JSONObject jo = new JSONObject(result);
                city = jo.getString("city");
                return city;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getCity(Context context) {
        String city;
        city = networkLocation();
        if(city == null) {
            city = PreferenceManager.getInstance(context).getConfigLocation();
            if(city == null) {
                city = context.getResources().getString(R.string.default_city);
            }
        }
        return city;
    }
}
