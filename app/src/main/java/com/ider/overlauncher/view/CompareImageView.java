package com.ider.overlauncher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ider.overlauncher.R;

public class CompareImageView extends FrameLayout {
	public Integer orderNum;
	public String summary;
	public String title;
	ImageView image;
	private String url;
	private Context context;

	public CompareImageView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.image_layout, this);
		initViews();
	}

	public void initViews() {
		image = (ImageView) findViewById(R.id.image);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public CompareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CompareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	
	public void setSummary(String summary) {
		this.summary = summary;
	}


	public String getSummary() {
		return summary;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}


	public void setImageResource(int imgRes) {
		Glide.with(context).load(imgRes).into(image);
	}

	public void setImageBitmap(Bitmap bitmap) {
		image.setImageBitmap(bitmap);
	}



}
