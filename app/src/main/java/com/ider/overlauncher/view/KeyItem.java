package com.ider.overlauncher.view;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ider.overlauncher.R;
import com.ider.overlauncher.applist.Application;
import com.ider.overlauncher.applist.ApplicationUtil;
import com.ider.overlauncher.utils.PreferenceManager;


@SuppressLint("Recycle") public class KeyItem extends LinearLayout {
	public int num_src;
	ImageView key;
	int keycode;  // �������ݱ����key
	PreferenceManager preManager;
	
	public KeyItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		preManager = PreferenceManager.getInstance(context);
		LayoutInflater.from(context).inflate(R.layout.key_item, this);
		key = (ImageView) findViewById(R.id.key_num);

		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.KeyItem);
		 num_src = array.getResourceId(R.styleable.KeyItem_key_num, 0);
		key.setImageResource(num_src);
	}
	
	public void setKeycode(int keycode) {
		this.keycode = keycode;
		String pkg = preManager.getKeyPackage(keycode);
		if(pkg != null) {
			Application app = ApplicationUtil.doApplication(getContext(), pkg);
			if(app != null) {
				setApplication(app);
			}
		}
	}
	
	public void setApplication(Application app) {
		key.setImageDrawable(app.getIcon());
		
	}
	public void setImage(int res) {
		key.setImageResource(res);

	}
	public int getKeycode() {
		return this.keycode;
	}
	
	
	public void removeApplication() {
		key.setImageResource(R.mipmap.key_add);
		preManager.removeKeyPackage(keycode);
	}
	
	

}
