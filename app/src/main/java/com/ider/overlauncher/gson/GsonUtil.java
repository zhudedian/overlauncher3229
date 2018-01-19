package com.ider.overlauncher.gson;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ider.overlauncher.AppContext;
import com.ider.overlauncher.services.LocationUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.ider.overlauncher.services.ServicePresenter.URL_TAG_CONFIG;

/**
 * Created by Eric on 2018/1/8.
 */

public class GsonUtil {
    private static HandleResult handleResult;
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
    public static void getDataApps(HandleResult handleResult){
        GsonUtil.handleResult = handleResult;

        new Thread() {
            @Override
            public void run() {
                try {
                    String city = LocationUtil.getCity(AppContext.getAppContext());
//            int vendorId = Product.getVendorId(Appcontext.getAppContext());
                    String model = Build.MODEL;
                    int vendorId = 0;
                    // String model = "IDER_BBA51";//getmodel();
                    String url = null;
                    try {
                        url = String.format(URL_TAG_CONFIG, new Object[]{vendorId, model, URLEncoder.encode(city, "utf-8")});
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Request request = new Request.Builder()
                            .url(url).build();
                    Call call = okHttpClient.newCall(request);
                    Response response = call.execute();
                    String result = response.body().string();
                    Message message = mHandler.obtainMessage();
                    message.what = 0;
                    Bundle data = new Bundle();
                    data.putString("result",result);
                    message.setData(data);
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public static List<DataApp> handleResponse(String response){
        try {
            return new Gson().fromJson(response,new TypeToken<List<DataApp>>(){}.getType());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 0:
                    Bundle data = msg.getData();
                    if (data == null) {
                        return;
                    }
                    List<DataApp> list = handleResponse(data.getString("result"));
                    handleResult.resultHandle(list);
                    break;
                case 1:

                    break;
            }
        }
    };

    public interface HandleResult{
        void resultHandle(List<DataApp> list);
    }
}
