package oversettings.ider.com.cust;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import oversettings.ider.com.deskcust.DeskConstant;
import oversettings.ider.com.deskcust.IDeskService;
import oversettings.ider.com.deskcust.PageEntry;


public class DeskService extends Service {
    private static String TAG = "launchrcust";

    private IDeskService service;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        IDeskServiceImpl service = new IDeskServiceImpl();
        this.service = service;
        return service;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


//
    class IDeskServiceImpl extends IDeskService.Stub {
        public IDeskServiceImpl() {
            super();
        }

        @Override
        public void basicTypes(int i, long l, boolean b, float v, double v1, String s) throws RemoteException {

        }


        @Override
        public void loadPageDetail(String detailurl) throws RemoteException {
//            final String navId = detailurl.substring(74,detailurl.lastIndexOf("&vender"));
            new AsyncTask<String, Object, String>() {
                @Override
                protected void onPostExecute(String result) {
                    Intent intent = new Intent();
                    intent.setAction(DeskConstant.ACTION_UPDATE_PAGE_DETAIL);
                    if(result == null) {
                        intent.putExtra(DeskConstant.PAGE_DETAIL_STATE, false);
                    } else {
                        try {
                            JSONObject mainjson = new JSONObject(result);
                            PageEntry pageEntry = new PageEntry(mainjson);
                            intent.putExtra(DeskConstant.PAGE_DETAIL_STATE, true);
                            intent.putExtra(DeskConstant.PAGE_DETAIL_ENTRIES, pageEntry);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            intent.putExtra(DeskConstant.PAGE_DETAIL_STATE, false);
                        }
                    }
                    sendBroadcast(intent);
                }
                @Override
                protected String doInBackground(String... strings) {

                    try {
                        String result = OKhttpManager.exuteFromServer(strings[0]);
                        return result;
                    } catch (IOException e) {
                        return null;
                    }
                }
            }.execute(detailurl);
        }

        @Override
        public void loadPages(String url) throws RemoteException {
            Log.i(TAG, "loadPages: url=="+url);
            new AsyncTask<String, Object, String>() {
                @Override
                protected void onPostExecute(String s) {
                    Intent intent = new Intent();
                    intent.setAction(DeskConstant.ACTION_UPDATE_PAGE_COUNT);
                    if(s == null) {
                        intent.putExtra(DeskConstant.PAGE_COUNT_STATE, false);
                        sendBroadcast(intent);
                        return;
                    }
                }

                @Override
                protected String doInBackground(String... strings) {
                    try{
                        return OKhttpManager.exuteFromServer(strings[0]);
                    } catch (Exception e) {
                        return null;
                    }
                }
            }.execute(url);
        }

    }

}
