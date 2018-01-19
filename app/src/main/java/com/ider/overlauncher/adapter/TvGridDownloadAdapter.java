package com.ider.overlauncher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ider.overlauncher.R;
import com.ider.overlauncher.applist.Application;

import java.util.List;

public class TvGridDownloadAdapter extends TvBaseAdapter {


	private List<Application> appList;
	private LayoutInflater inflater;
	private Context context;
//	private int[]colors = {//android.R.color.darker_gray,android.R.color.holo_green_dark,android.R.color.secondary_text_dark_nodisable,
//			android.R.color.holo_blue_light,android.R.color.tertiary_text_dark,
//			android.R.color.primary_text_dark_nodisable,android.R.color.holo_green_light,
//			android.R.color.holo_orange_dark,android.R.color.holo_orange_light,android.R.color.holo_purple
//	};

	public TvGridDownloadAdapter(Context context, List<Application> appList){
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

		Application config = appList.get(position );
		holder.tv_title.setText(config.getLabel());
		holder.tiv_icon.setImageDrawable(config.getIcon());
//		holder.item_bg.setBackgroundColor(context.getResources().getColor(colors[position % colors.length]));
		return contentView;
	}
	
	public void addItem(Application item) {
		appList.add(item);
	}
	public void removeItem(Application item) {
		appList.remove(item);
	}
	public void clear() {
		appList.clear();
	}

	public void flush(List<Application> appListNew) {
		appList = appListNew;
	}

	
	static class ViewHolder{
		TextView tv_title;
		ImageView tiv_icon;
		View item_bg;
	}
}
