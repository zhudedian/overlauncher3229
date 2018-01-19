package oversettings.ider.com.deskcust;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by ider-eric on 2016/5/23.
 * 此类实现了可序列化Parcelable接口
 */
public class PageEntry implements Parcelable {
    public TileEntry[] tileEntrys;



    public PageEntry() {
    }

    public PageEntry(JSONObject json) {
        try {
            JSONArray jarray = json.getJSONObject("data").getJSONArray("channel_contents").getJSONObject(0).getJSONArray("modules");
            tileEntrys = new TileEntry[jarray.length()];
            for(int i = 0;i<jarray.length();i++){
                tileEntrys[i]=new TileEntry(jarray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void writeToParcel(Parcel parcel, int flag) {

        parcel.writeParcelableArray(tileEntrys, flag);
    }


    public static final Creator<PageEntry> CREATOR = new Creator<PageEntry>() {
        @Override
        public PageEntry createFromParcel(Parcel parcel) {
            PageEntry entry = new PageEntry();
            Parcelable[] pars = parcel.readParcelableArray(TileEntry.class.getClassLoader());
            if(pars!=null) {
                entry.tileEntrys = Arrays.copyOf(pars, pars.length, TileEntry[].class);
            }
            return entry;
        }

        @Override
        public PageEntry[] newArray(int i) {
            return new PageEntry[i];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }
}
