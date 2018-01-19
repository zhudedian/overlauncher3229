package com.ider.overlauncher.utils;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ider.overlauncher.db.Data;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/19.
 */

public class JSONUtil {
    public static String tvUrl = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=KK_LAUNCHER&channel_selector=tv&content_selector=all&pictures=pic_648x364%2bpic_408x230%2bpic_260x364%2bpic_192x108&Q-UA=QV%3D1%26PR%3DVIDEO%26PT%3DKONKA%26CHID%3D10052%26RL%3D1920*1080%26VN%3D3.0.0%26VN_CODE%3D120%26SV%3D4.4.2%26DV%3DMiBOX2%26VN_BUILD%3D0";
    public static String movie = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=KK_LAUNCHER&channel_selector=movie&content_selector=all&pictures=pic_648x364%2bpic_408x230%2bpic_260x364%2bpic_192x108&Q-UA=QV%3D1%26PR%3DVIDEO%26PT%3DKONKA%26CHID%3D10052%26RL%3D1920*1080%26VN%3D3.0.0%26VN_CODE%3D120%26SV%3D4.4.2%26DV%3DMiBOX2%26VN_BUILD%3D0";
    public static String variety ="http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=KK_LAUNCHER&channel_selector=variety&content_selector=all&pictures=pic_648x364%2bpic_408x230%2bpic_260x364%2bpic_192x1080&Q-UA=QV%3D1%26PR%3DVIDEO%26PT%3DKONKA%26CHID%3D10052%26RL%3D1920*1080%26VN%3D3.0.0%26VN_CODE%3D120%26SV%3D4.4.2%26DV%3DMiBOX2%26VN_BUILD%3D0";
    public static String children ="http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=KK_LAUNCHER&channel_selector=children&content_selector=all&pictures=pic_648x364%2bpic_408x230%2bpic_260x364%2bpic_192x108&Q-UA=QV%3D1%26PR%3DVIDEO%26PT%3DKONKA%26CHID%3D10052%26RL%3D1920*1080%26VN%3D3.0.0%26VN_CODE%3D120%26SV%3D4.4.2%26DV%3DMiBOX2%26VN_BUILD%3D0";
    public static String physical_pay = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=KK_LAUNCHER&channel_selector=physical_pay&content_selector=all&pictures=pic_648x364%2bpic_408x230%2bpic_260x364%2bpic_192x108&Q-UA=QV%3D1%26PR%3DVIDEO%26PT%3DKONKA%26CHID%3D10052%26RL%3D1920*1080%26VN%3D3.0.0%26VN_CODE%3D120%26SV%3D4.4.2%26DV%3DMiBOX2%26VN_BUILD%3D0";
    public static String chosen = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=KK_LAUNCHER&channel_selector=chosen&content_selector=all&pictures=pic_648x364%2bpic_408x230%2bpic_260x364%2bpic_192x108&Q-UA=QV%3D1%26PR%3DVIDEO%26PT%3DKONKA%26CHID%3D10052%26RL%3D1920*1080%26VN%3D3.0.0%26VN_CODE%3D120%26SV%3D4.4.2%26DV%3DMiBOX2%26VN_BUILD%3D0";


    public static Data data;
    public static void getData(String urlStr,OnCompleteListener listener){
        Log.i("JSONUtil", "getData,urlStr"+urlStr);
        sendRequestWithOkHttp(urlStr,listener);
        Log.i("aaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaa3");
    }
    private static void sendRequestWithOkHttp(final String urlStr, final OnCompleteListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Log.i("JSONUtil", "urlStr"+urlStr);
                    Request request = new Request.Builder().url(urlStr).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i("JSONUtil", "responseData");
                    JSONObject jsonObject = new JSONObject(responseData);
                    String dataJson = jsonObject.getString("data");
                    Data data = new Gson().fromJson(dataJson,Data.class);
                    listener.complete(data);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public interface OnCompleteListener{
        void complete(Data data);
    }
}
