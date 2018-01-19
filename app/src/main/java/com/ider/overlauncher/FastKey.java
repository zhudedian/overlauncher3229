package com.ider.overlauncher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;


import com.ider.overlauncher.applist.Application;
import com.ider.overlauncher.applist.ApplicationUtil;
import com.ider.overlauncher.db.ServerApp;
import com.ider.overlauncher.model.PackageHolder;
import com.ider.overlauncher.utils.FocusScaleUtils;
import com.ider.overlauncher.utils.FocusUtils;
import com.ider.overlauncher.utils.PreferenceManager;
import com.ider.overlauncher.view.AppSelectWindow;
import com.ider.overlauncher.view.KeyItem;

import java.util.ArrayList;
import java.util.List;


public class FastKey extends Activity implements OnFocusChangeListener, OnClickListener, OnLongClickListener {
	
	KeyItem[] keys = new KeyItem[12];
//	FlyImageView selecter;
	public static PreferenceManager preManager;
	private TextView select_tip;
	private View baseview;
	private FocusScaleUtils focusScaleUtils;
	private FocusUtils focusUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseview = LayoutInflater.from(this).inflate(R.layout.fast_key,null);
		setContentView(baseview);
		focusScaleUtils = new FocusScaleUtils();
		focusUtils = new FocusUtils(this, baseview, R.drawable.focusiv, R.drawable.function_back_focus,true);

		preManager = PreferenceManager.getInstance(this);
//		selecter = (FlyImageView) findViewById(R.id.key_selector);
		keys[0] = (KeyItem) findViewById(R.id.key_1);
		keys[1] = (KeyItem) findViewById(R.id.key_2);
		keys[2] = (KeyItem) findViewById(R.id.key_3);
		keys[3] = (KeyItem) findViewById(R.id.key_4);
		keys[4] = (KeyItem) findViewById(R.id.key_5);
		keys[5] = (KeyItem) findViewById(R.id.key_6);
		keys[6] = (KeyItem) findViewById(R.id.key_7);
		keys[7] = (KeyItem) findViewById(R.id.key_8);
		keys[8] = (KeyItem) findViewById(R.id.key_9);
		keys[9] = (KeyItem) findViewById(R.id.key_0);
		keys[10] = (KeyItem) findViewById(R.id.key_tv);
		keys[11] = (KeyItem) findViewById(R.id.key_vod);
		keys[0].setKeycode(KeyEvent.KEYCODE_1);
		keys[1].setKeycode(KeyEvent.KEYCODE_2);
		keys[2].setKeycode(KeyEvent.KEYCODE_3);
		keys[3].setKeycode(KeyEvent.KEYCODE_4);
		keys[4].setKeycode(KeyEvent.KEYCODE_5);
		keys[5].setKeycode(KeyEvent.KEYCODE_6);
		keys[6].setKeycode(KeyEvent.KEYCODE_7);
		keys[7].setKeycode(KeyEvent.KEYCODE_8);
		keys[8].setKeycode(KeyEvent.KEYCODE_9);
		keys[9].setKeycode(KeyEvent.KEYCODE_0);
		keys[10].setKeycode(KeyEvent.KEYCODE_TV_ANTENNA_CABLE);
		keys[11].setKeycode(KeyEvent.KEYCODE_DVR);
		select_tip = (TextView) findViewById(R.id.select_tip);

		for(int i = 0; i < keys.length; i++) {
			keys[i].setOnFocusChangeListener(this);
			keys[i].setOnClickListener(this);
			keys[i].setOnLongClickListener(this);
		}
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				selecter.setVisibility(View.VISIBLE);
//			}
//		},300);

		registerReceiver(mHomeKeyEventReceiver,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
	}

	@Override
	protected void onDestroy() {
		try{
			unregisterReceiver(mHomeKeyEventReceiver);
		}catch (Exception e){

		}
		super.onDestroy();
	}

	private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
		String SYSTEM_REASON = "reason";
		String SYSTEM_HOME_KEY = "homekey";
		String SYSTEM_HOME_KEY_LONG = "recentapps";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_REASON);
				if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
					//表示按了home键,程序到了后台
					FastKey.this.finish();
				}else if(TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)){
					//表示长按home键,显示最近使用的程序列表
					FastKey.this.finish();
				}
			}
		}
	};
	



	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		KeyItem item = (KeyItem) v;
		String pgk = preManager.getKeyPackage(item.getKeycode());
		if(hasFocus) {
			v.bringToFront();
			focusUtils.showFocus();
			focusUtils.startMoveFocus(v,true,1.2F);
			focusScaleUtils.scaleToLarge(v);
			if(pgk==null){
				item.setImage(R.mipmap.key_add);
				select_tip.setText(getResources().getString(R.string.add_keytip));
			}else{
				select_tip.setText(getResources().getString(R.string.remove_keytip));
			}
		}else{
			if(pgk==null) {
				item.setImage(item.num_src);
			}
			focusScaleUtils.scaleToNormal();
		}
	}
	@Override
	public void onClick(View view) {
		showAppSelectWindow((KeyItem)view);
	}


	public List<ServerApp> getcurrentPhs(){
		List<ServerApp> phs = new ArrayList<>();
		for (KeyItem item:keys
			 ) {
			String pgk  = preManager.getKeyPackage(item.getKeycode());
			if(pgk!=null){
				phs.add(new PackageHolder(0L,pgk,null));
			}
		}
		return phs;
	}
	public void showAppSelectWindow(final KeyItem v) {
		List<ServerApp> phs  = getcurrentPhs();
		AppSelectWindow appSelectWindow = AppSelectWindow.getInstance(this);
		appSelectWindow.setData(phs);
		appSelectWindow.setOnAppSelectListener(new AppSelectWindow.OnAppSelectListener() {
			@Override
			public void onAppSelected(PackageHolder holder) {
				if(!holder.getPackageName().equals("add")) {
					v.setApplication(ApplicationUtil.doApplication(FastKey.this, holder.getPackageName()));
					preManager.setKeyPackage(v.getKeycode(), holder.getPackageName());
				}
			}
		});
		appSelectWindow.showAppPopWindow(baseview);
	}

	public static void setTvKey(String packg){
		preManager.setKeyPackage(KeyEvent.KEYCODE_TV_ANTENNA_CABLE, packg);
	}
	@Override
	public boolean onLongClick(View view) {

		((KeyItem) view).removeApplication();

		return true;
	}
	


	
	public List<String> findall(){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < keys.length; i++) {
			//Application app =keys[i].getContext().getApplicationInfo()
			String pkgname = preManager.getKeyPackage(keys[i].getKeycode());
			if(pkgname!=null&&!"".equals(pkgname)){
				list.add(pkgname);
			}
		}
		return list;
	}
	
	//ȥ������ӵ�apk	
		public List<Application> insectApp(List<Application> app1, List<String> app2){
			for (int i = 0; i < app2.size(); i++) {
				for (int j = 0; j < app1.size(); j++) {
					if(app2.get(i).equals(app1.get(j).getPackageName())){
						app1.remove(app1.get(j));
					}
				}
			}
			return app1;
		}
	
}
