package com.ider.overlauncher.services;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2016/11/22.
 */

public class JsonParser {

    public static ArrayList<TagConfig> parseConfigResult(String result) {
        if(result == null) {
            return null;
        }
        ArrayList<TagConfig> configs = new ArrayList<TagConfig>();
        ArrayList<String> packs = new ArrayList<String>();
        try {
            JSONArray arrays = new JSONArray(result);
            for(int i = 0; i < arrays.length(); i++) {
                JSONObject tagConfig = arrays.getJSONObject(i);
                int status = getIntIgnoreNull(tagConfig.get("statues"));
                if(status == 1) {
                    ConfigApp configApp = new ConfigApp();
                    configApp.tag = tagConfig.getString("tag");
                    configApp.status = status;
                    configApp.forced = tagConfig.getBoolean("force");
                    configApp.verCode = getIntIgnoreNull(tagConfig.get("vercode"));
                    configApp.apkUrl = createUrl(tagConfig.getString("down"));
                    configApp.iconUrl = createUrl(tagConfig.getString("icon"));
                    configApp.description = tagConfig.getString("descriptions");
                    configApp.label = tagConfig.getString("label");
                    configApp.md5 = tagConfig.getString("vername");
                    configApp.pkgName = tagConfig.getString("package");
                    configApp.summary = tagConfig.getString("summary");
                    if(!packs.contains(configApp.pkgName)) {
                        Log.i("ConfigService", "parseConfigResult: add to TagConfig");
                        packs.add(configApp.pkgName);
                        configs.add(configApp);
                    }else{
                        Log.i("ConfigService", "parseConfigResult:not  add to TagConfig"+configApp.pkgName);
                    }

                } else if (status == 2) {
                    ConfigPic configPic = new ConfigPic();
                    configPic.tag = tagConfig.getString("tag");
                    configPic.status = status;
                    configPic.imageUrl = createUrl(tagConfig.getString("images"));
                    configPic.miniUrl = createUrl(tagConfig.getString("miniimg"));
                    configs.add(configPic);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return configs;

    }

    private static String createUrl(String url) {
        if(url == null) {
            return null;
        }
        if(url.toLowerCase().startsWith("http")) {
            return url;
        } else {
            return ServicePresenter.SERVER_DOMAIN + url;
        }
    }

    private static int getIntIgnoreNull(Object object) {
        if(object == null || object.toString().equals("null") || object.toString().length() == 0) {
            return 0;
        } else {
            try {
                return Integer.parseInt(object.toString());
            } catch(Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

}
