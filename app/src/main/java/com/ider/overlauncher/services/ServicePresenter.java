package com.ider.overlauncher.services;

import android.os.Build;
import android.util.Log;


import com.ider.overlauncher.AppContext;
import com.ider.overlauncher.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import oversettings.ider.com.cust.OKhttpManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


/**
 * Created by ider-eric on 2016/11/23.
 */

public class ServicePresenter implements IServicePresenter {

    private void LOG(String log) {
        Log.i("ServicePresenter", log);
    }

    public static final String URL_SERVER_VERSION = "http://szider.net/searchinfo.aspx?type=0";
    public static final String URL_TAG_CONFIG = "http://szider.net/searchinfo.aspx?type=1&vendorID=%d&model=%s&city=%s";
    public static final String SERVER_DOMAIN = "http://szider.net";

    private int normalTaskDelay = 60 * 60 * 1000;
    private int errorTaskDelay = 60 * 1000;
    private long lastRequestTimeMills = 0;
    private int unNecessaryDelay = 3 * 60 * 6000;
    private IService service;

    public ServicePresenter(IService service) {
        this.service = service;
    }


    @Override
    public void checkUpdate(boolean force) {
        Observable
                .just(URL_SERVER_VERSION)
                .filter(new CheckunNecessaryReqeust(force))
                .observeOn(Schedulers.io())
                .map(getServerVersion)
                .filter(checkServiceVersion)
                .map(buildConfigUpdateUrl)
                .filter(checkUrlAvailable)
                .map(requestUpdateData)
                .filter(checkDataAvailable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ConfigSubscriber());
    }


    private class CheckunNecessaryReqeust implements Func1<String, Boolean> {
        boolean forced;

        CheckunNecessaryReqeust(boolean forced) {
            this.forced = forced;
        }

        @Override
        public Boolean call(String s) {
            if (forced) {
                return true;
            }

            if (lastRequestTimeMills == 0) {
                lastRequestTimeMills = System.currentTimeMillis();
                return true;
            } else {
                long currentTimeMills = System.currentTimeMillis();
                if (currentTimeMills - lastRequestTimeMills > unNecessaryDelay) {
                    lastRequestTimeMills = System.currentTimeMillis();
                    return true;
                }
            }
            LOG("request too ofen, ignore this request");
            return false;
        }
    }

    private Func1<String, Integer> getServerVersion = new Func1<String, Integer>() {
        @Override
        public Integer call(String url) {
            LOG("getting Server Version...");
            try {
                String result = OKhttpManager.exuteFromServer(URL_SERVER_VERSION);
                LOG("result = " + result);
                JSONArray array = new JSONArray(result);
                return array.getJSONObject(0).getInt("count");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return -1;
        }
    };


    private Func1<Integer, Boolean> checkServiceVersion = new Func1<Integer, Boolean>() {
        @Override
        public Boolean call(Integer version) {
            if (version > getLastestLocalVersion()) {
                PreferenceManager.getInstance(AppContext.getAppContext()).setLocalServiceVersion(version);
                return true;
            }

            return false;
        }
    };

    private Func1<Integer, String> buildConfigUpdateUrl = new Func1<Integer, String>() {
        @Override
        public String call(Integer integer) {
            LOG("build config update url");
            String city = LocationUtil.getCity(AppContext.getAppContext());
//            int vendorId = Product.getVendorId(Appcontext.getAppContext());
            String model = Build.MODEL;
            int vendorId = 0;
           // String model = "IDER_BBA51";//getmodel();
            String url = null;
            try {
                url = String.format(URL_TAG_CONFIG, new Object[]{vendorId, model, URLEncoder.encode(city, "utf-8")});
                LOG(url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return url;


        }
    };

    private Func1<String, Boolean> checkUrlAvailable = new Func1<String, Boolean>() {
        @Override
        public Boolean call(String url) {
            return url != null && !url.equals("");
        }
    };

    private Func1<String, ArrayList<TagConfig>> requestUpdateData = new Func1<String, ArrayList<TagConfig>>() {
        @Override
        public ArrayList<TagConfig> call(String url) {
            try {
                String result = OKhttpManager.exuteFromServer(url);
                return JsonParser.parseConfigResult(result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    };


    private Func1<ArrayList<TagConfig>, Boolean> checkDataAvailable = new Func1<ArrayList<TagConfig>, Boolean>() {
        @Override
        public Boolean call(ArrayList<TagConfig> configs) {
            LOG("check update data available");
            return configs != null;
        }
    };

    private class ConfigSubscriber extends Subscriber<ArrayList<TagConfig>> {
        @Override
        public void onCompleted() {
            LOG("subscriber onComplete");
            // check 1 hour later
            // service.requestLater(normalTaskDelay);

        }

        @Override
        public void onNext(ArrayList<TagConfig> tagConfigs) {
            LOG("onNext");
            service.requestSuccess(tagConfigs);
        }

        @Override
        public void onError(Throwable e) {
            LOG("subscriber onError, " + e.getMessage());
            // check 1 minute later
            // service.requestLater(errorTaskDelay);
        }
    }


    private int getLastestLocalVersion() {
        return PreferenceManager.getInstance(AppContext.getAppContext())
                .getLocalServiceVersion();
    }

    private String getmodel() {
//        String result = TvUtil.readFile("/proc/cmdline");
//        if (result != null) {
//            String s = result.split("inside_model=")[1];
//            String model = s.substring(0, s.lastIndexOf("business_model")).trim();
//            return model;
//        }
        return null;
    }

}
