package com.ider.overlauncher.view;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ider.overlauncher.AppContext;
import com.ider.overlauncher.Constant;
import com.ider.overlauncher.HomeActivity;
import com.ider.overlauncher.R;
import com.ider.overlauncher.applist.AppListActivity;
import com.ider.overlauncher.applist.ApplicationUtil;
import com.ider.overlauncher.utils.MyDigitalClock;
import com.ider.overlauncher.utils.NetUtil;
import com.ider.overlauncher.utils.PreferenceManager;

import java.io.File;

@SuppressLint("NewApi")
public class StateBar extends RelativeLayout {
	Context context;
	Weather weather;
			 ImageView netState;
	ImageView usb;
	MyDigitalClock clock;
	PreferenceManager preManager;
	TextView data;
	private ImageView launchsetting;
	private ImageView launchrockble;


	public StateBar(Context context) {
		super(context);
	}

	public StateBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.state_bar, this);
		initViews();
	}

	public void initViews() {
//		weather = (Weather) findViewById(R.id.weather);
		netState = (ImageView) findViewById(R.id.state_bar_icon_network);
		usb =(ImageView) findViewById(R.id.state_bar_icon_usb);
		data = (TextView) findViewById(R.id.data);
		clock = (MyDigitalClock) findViewById(R.id.clock);
		setdata();
		updateUsbState();
	}

	public void setdata() {
		Time t = new Time();
		t.setToNow();
		int month = t.month + 1;
		String mont = null;
		if (month < 10) {
			mont = "0" + month;
		} else {
			mont = "" + month;
		}
		data.setText("" + t.year + "/" + mont + "/" + t.monthDay);
	}

	public void initClock(){
		clock.initClock(context);
	}
	/**
	 * view被绘制到窗体时调用，在onDraw()之前
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

	}

	/**
	 * 更新网络标记
	 */
	public void updateNetState() {

		if (NetUtil.isNetworkAvailable(context)) {
//			weather.refresh(context);
//			setdata();
			if (NetUtil.isEthernetConnect(context)) {
				netState.setImageResource(R.mipmap.eth_on);
			} else if(NetUtil.isWifiConnect(context)){
				netState.setImageResource(R.mipmap.wifi_on);
				//netState.setImageLevel(NetUtil.wifiLevel(context));
			}else{
				netState.setImageResource(R.mipmap.eth_on);
			}
//			setdata();

		} else {
			((HomeActivity)getContext()).showNetDialog();
			netState.setImageResource(0);
		}
	}

	/**
	 * 更新USB状态
	 */
	public void updateUsbState() {
		String path = isUsbExit();
		if (path!= null) {
			usb.setVisibility(View.VISIBLE);
//			usb.setFocusable(true);
			//((HomeActivity)getContext()).checkUsbInstall(path);
		} else {
			usb.setVisibility(View.INVISIBLE);
		}
	}

	public void updateUsbStatetwo(boolean munt) {
		if(munt) {
			usb.setVisibility(View.VISIBLE);
//			usb.setFocusable(true);
		}else{
			usb.setVisibility(View.INVISIBLE);
		}
	}


	/**
	 * 判断USB是否连接，已连接，返回u盘路径，否则返回null
	 *
	 * @return
	 */
	public String isUsbExit() {
		String[] path = { "/mnt/usb_storage/USB_DISK0/udisk0",
				"/mnt/usb_storage/USB_DISK1/udisk0",
				"/mnt/usb_storage/USB_DISK3/udisk0",
				"/mnt/usb_storage/USB_DISK4/udisk0", "/mnt/usbhost/Storage01",
				"/mnt/usbhost/Storage02" };
		for (int i = 0; i < path.length; i++) {
			File file = new File(path[i]);
			if (file != null && file.listFiles() != null) {
				return file.getAbsolutePath();
			}
		}
		return null;
	}

	/**
	 * 更新天气
	 */
	public void getWeather() {
		weather.refresh(getContext());
//		setdata();
	}

	public void cityPick(String currentCity) {
//		Intent in = new Intent(getContext(), CityPicker.class);
//		// 传入当前城市，跳转后显示
//
//		in.putExtra("currentCity", currentCity);
//		getContext().startActivity(in);
	}


}
