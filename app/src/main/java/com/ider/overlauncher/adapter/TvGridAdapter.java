package com.ider.overlauncher.adapter;

import android.content.Context;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ider.overlauncher.R;
import com.ider.overlauncher.services.ConfigApp;
import com.ider.overlauncher.services.TagConfig;

import java.util.List;

public class TvGridAdapter extends TvBaseAdapter {

	
	private List<TagConfig> appList;
	private LayoutInflater inflater;
	private Context context;
//	private int[]colors = {//android.R.color.darker_gray,android.R.color.holo_green_dark,android.R.color.secondary_text_dark_nodisable,
//			android.R.color.holo_orange_dark,android.R.color.holo_orange_light,android.R.color.holo_purple,
//			android.R.color.holo_blue_light,android.R.color.tertiary_text_dark,
//			android.R.color.primary_text_dark_nodisable,android.R.color.holo_green_light,
//	};

	public TvGridAdapter(Context context, List<TagConfig> appList){
		this.inflater= LayoutInflater.from(context);
		this.appList=appList;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return appList.size();
	}

	@Override
	public Object getItem(int position) {
		return appList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View contentView, ViewGroup parent) {
		ViewHolder holder=null;
		if (contentView==null) {
			contentView=inflater.inflate(R.layout.item_grid, null);
			holder=new ViewHolder();
			holder.tv_title=(TextView) contentView.findViewById(R.id.tv_title);
			holder.tiv_icon=(ImageView) contentView.findViewById(R.id.tiv_icon);
			holder.item_bg = contentView.findViewById(R.id.item_bg);
			contentView.setTag(holder);
		}else{
			holder=(ViewHolder) contentView.getTag();
		}

		ConfigApp config =(ConfigApp)appList.get(position);
		holder.tv_title.setText(config.summary);
//		Log.i("TvGridAdapter","config.iconUrl="+config.iconUrl);
		Glide.with(context).load(config.iconUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.tiv_icon);
//		holder.item_bg.setBackgroundColor(context.getResources().getColor(colors[position % colors.length]));
		return contentView;
	}
	
	public void addItem(TagConfig item) {
		appList.add(item);
	}
	public void removeItem(TagConfig item) {
		appList.remove(item);
	}
	public void clear() {
		appList.clear();
	}

	public void flush(List<TagConfig> appListNew) {
		appList = appListNew;
	}

	
	static class ViewHolder{
		TextView tv_title;
		ImageView tiv_icon;
		View item_bg;
	}
}
