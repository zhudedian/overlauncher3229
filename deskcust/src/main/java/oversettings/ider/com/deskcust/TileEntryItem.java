package oversettings.ider.com.deskcust;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ider-eric on 2016/5/23.
 * 此类实现了可序列化Parcelable接口
 */
public class TileEntryItem implements Parcelable {

    public String index;//下标
    public String episode_updated;//剧集
    public String item_id;
    public String item_type;//类型
    public String uri;//url
    public String pic_470x630;//图片
    public String pic_496x722;//图片替补
    public String title;//剧名


    public TileEntryItem items;
    public TileEntryItem() {
    }

    public TileEntryItem(JSONObject json) {
        try {
            index = json.getString("index");
            pic_496x722 = json.getJSONObject("comm_item").getString("pic_496x722");
            episode_updated = json.getJSONObject("comm_item").getString("episode_updated");
            uri = json.getJSONObject("comm_item").getJSONObject("ext_info1").getString("uri");
            item_id = json.getJSONObject("comm_item").getString("item_id");
            item_type = json.getJSONObject("comm_item").getString("item_type");
            title = json.getJSONObject("comm_item").getString("title");
//            pic_470x630 = json.getJSONObject("comm_item").getJSONObject("ext_info1").getString("pic_470x630");
//            Log.i("launchrcust", "TileEntryItem: index="+index+"**episode_updated="+episode_updated+"**pic_470x630="+pic_470x630
//            +"**+uri="+uri+"**item_id="+item_id+"**item_type="+"**title="+title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void writeToParcel(Parcel parcel, int flag) {

        parcel.writeString(index);
        parcel.writeString(episode_updated);
        parcel.writeString(pic_470x630);
        parcel.writeString(uri);
        parcel.writeString(item_id);
        parcel.writeString(item_type);
        parcel.writeString(title);
        parcel.writeString(pic_496x722);
    }


    public static final Creator<TileEntryItem> CREATOR = new Creator<TileEntryItem>() {
        @Override
        public TileEntryItem createFromParcel(Parcel parcel) {
            TileEntryItem entry = new TileEntryItem();
            entry.index = parcel.readString();
            entry.episode_updated = parcel.readString();
            entry.pic_470x630 = parcel.readString();
            entry.uri = parcel.readString();
            entry.item_id = parcel.readString();
            entry.item_type = parcel.readString();
            entry.title = parcel.readString();
            entry.pic_496x722 = parcel.readString();
            return entry;
        }

        @Override
        public TileEntryItem[] newArray(int i) {
            return new TileEntryItem[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
