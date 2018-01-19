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
public class TileEntry implements Parcelable {

    public String index;
    public String module_type;
    public TileEntryItem[] items;
    public TileEntry() {
    }

    public TileEntry(JSONObject json) {
        try {
            index = json.getString("index");
            module_type = json.getString("module_type");
            JSONArray jarray = json.getJSONArray("items");
            items = new TileEntryItem[jarray.length()];
            for(int i = 0;i<jarray.length();i++){
                items[i]=new TileEntryItem(jarray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void writeToParcel(Parcel parcel, int flag) {

        parcel.writeString(index);
        parcel.writeString(module_type);
        parcel.writeParcelableArray(items, flag);
    }


    public static final Parcelable.Creator<TileEntry> CREATOR = new Parcelable.Creator<TileEntry>() {
        @Override
        public TileEntry createFromParcel(Parcel parcel) {
            TileEntry entry = new TileEntry();
            entry.index = parcel.readString();
            entry.index = parcel.readString();
            Parcelable[] pars = parcel.readParcelableArray(TileEntry.class.getClassLoader());
            if(pars!=null) {
                entry.items = Arrays.copyOf(pars, pars.length, TileEntryItem[].class);
            }
            return entry;
        }

        @Override
        public TileEntry[] newArray(int i) {
            return new TileEntry[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
