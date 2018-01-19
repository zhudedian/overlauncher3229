package com.ider.overlauncher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.ider.overlauncher.adapter.SpinnerAdapter;
import com.ider.overlauncher.utils.ActivityAnimationTool;
import com.ider.overlauncher.utils.PreferenceManager;
import com.ider.overlauncher.utils.WeatherUtil;
import com.ider.overlauncher.view.Weather;


public class CityPicker extends Activity {
	JSONArray array;
	List<String> provinces;
	List<String> cities;
	String city;
	Spinner spinner1;
	Spinner spinner2;
	TextView bigCity;
	GridView hotGrid;
	CheckBox autoCheck;
	PreferenceManager preManager;
	ImageView weatherimg;
	TextView weathertext;
	WeatherUtil wutil;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 100:
					displayWeather(msg.getData());
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_picker);
		preManager = PreferenceManager.getInstance(this);
		wutil = new WeatherUtil(this);
		findViews();
		showViews();
		setListeners();

		registerReceiver(mHomeKeyEventReceiver,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
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
					CityPicker.this.finish();
				}else if(TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)){
					//表示长按home键,显示最近使用的程序列表
					CityPicker.this.finish();
				}
			}
		}
	};


	public void findViews() {
		bigCity = (TextView) findViewById(R.id.city_big);
		spinner1 = (Spinner) findViewById(R.id.province_spinner);
		spinner2 = (Spinner) findViewById(R.id.city_spinner);
		hotGrid = (GridView) findViewById(R.id.hot_grid);
		autoCheck = (CheckBox) findViewById(R.id.auto_check);
		weatherimg = (ImageView) findViewById(R.id.weatherimg);
		weathertext = (TextView) findViewById(R.id.weathertext);
	}


	public void showViews() {

		// 获取传入的城市，显示在BigCity上
		Intent in = getIntent();
		city = in.getStringExtra("currentCity");

		if (city != null) {
			bigCity.setText(city);
		} else {
			bigCity.setText(R.string.auto);
		}

		provinces = readData();
		Log.i("Launcher", "showViews: provinces=="+provinces.size());
		hotGrid.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
				R.layout.hot_city_item, hotCities()));

		// 设置checkBox状态
		if(preManager.getManuCity() == null) {
			autoCheck.setChecked(true);
		} else {
			autoCheck.setChecked(false);
		}

	}

	public void setListeners() {
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				cities = getCities(position);
				Log.i("Launcher", "onItemSelected: cities="+cities.size()+"***"+position);
				spinner2.setAdapter(new SpinnerAdapter(getApplicationContext(),
						cities));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				if (!cities.get(position).equals("城市")) {
					// 手动城市
					setManuCity(cities.get(position));
					weatherthread = new GetWeather();
					weatherthread.start();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		hotGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				setManuCity(hotCities().get(position));
				weatherthread = new GetWeather();
				weatherthread.start();
			}
		});

		autoCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				if (isChecked) {
					setAutoCity();
				} else {
					setManuCity(null);
				}
			}
		});
	}
	GetWeather weatherthread=null;
	class GetWeather extends Thread {

		@Override
		public void run() {
			if(city != null) {
				wutil.getweather(city, WeatherUtil.TODAY);
				String weatherState = wutil.getWeatherStatus() == null ? wutil
						.getWeatherStatus() : wutil
						.getWeatherStatus2();
				Bundle bundle = new Bundle();
				String high = wutil.getHighTem();
				String low = wutil.getLowTem();
				String data =low+"℃~"+high+"℃";
				bundle.putString(WeatherUtil.STATUS, weatherState);
				bundle.putString(WeatherUtil.TEMPERATURE, data);
				Message msg = new Message();
				msg.what = 100;
				msg.setData(bundle);
				handler.sendMessage(msg); // ����������Ϣ
//				handler.sendEmptyMessage(WEATEHR_FINISHED);

			}
		}
	}
	/**
	 * 写入手动城市
	 * @param city
	 */
	public void setManuCity(String city) {
		this.city = city;
		if (city != null) {
			preManager.setManuCity(city);
			bigCity.setText(city);
		}
	}

	/**
	 * 设置自动定位（删除保存的城市）
	 */
	public void setAutoCity() {
		this.city = null;
		preManager.removeCity();
		bigCity.setText(R.string.auto);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	public List<String> readData() {
//		final String data = null;
		final List<String> provinces = new ArrayList<String>();
		// ========== 通过输入流获取JSON格式的省市数据
		new AsyncTask<String , Object, String>(){
			@Override
			protected String doInBackground(String... arg0) {
				try {
					InputStream is = getAssets().open("city.json");
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int count;
					while ((count = is.read()) != -1) {
						baos.write(count);
					}
					is.close();
					baos.close();
					byte[] buffer = baos.toByteArray();
					String result = new String(buffer, "UTF-8");
					return result;
				} catch (IOException e) {
					Log.i("Launcher", "doInBackground:IOException ");
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(String result) {
				Log.i("Launcher", "doInBackground:result== "+result);
				if(result!=null){
					try {
						array = new JSONArray(result);
						for (int i = 0; i < array.length(); i++) {
							JSONObject jo = array.getJSONObject(i);
							provinces.add(jo.getString("name"));
						}
						Log.i("Launcher", "doInBackground:result== "+provinces.size());
						spinner1.setAdapter(new SpinnerAdapter(getApplicationContext(),
								provinces));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

		}.execute(new String[]{});
//		try {
//			InputStream is = getAssets().open("city.json");
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			int count;
//			while ((count = is.read()) != -1) {
//				baos.write(count);
//			}
//			is.close();
//			baos.close();
//			byte[] buffer = baos.toByteArray();
//			data = new String(buffer, "gb2312");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return provinces;
	}

	public List<String> getProvinces() {
		List<String> provinces = new ArrayList<String>();
		try {
			array = new JSONArray(readData());
			for (int i = 0; i < array.length(); i++) {
				JSONObject jo = array.getJSONObject(i);
				provinces.add(jo.getString("name"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return provinces;
	}

	public List<String> getCities(int index) {
		List<String> cities = new ArrayList<String>();
		try {
			JSONObject jo = array.getJSONObject(index);
			JSONArray ja = jo.getJSONArray("sub");
			for (int i = 1; i < ja.length() - 1; i++) {
				JSONObject object = ja.getJSONObject(i);
				cities.add(object.getString("name"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("Launcher", "getCities: "+cities.size());
		return cities;
	}

	public List<String> hotCities() {
		List<String> list = new ArrayList<String>();
		list.add("北京");
		list.add("上海");
		list.add("广州");
		list.add("深圳");
		list.add("武汉");
		list.add("南京");
		list.add("西安");
		list.add("成都");
		list.add("郑州");
		list.add("杭州");
		list.add("东莞");
		list.add("重庆");
		list.add("长沙");
		list.add("天津");
		list.add("苏州");
		list.add("沈阳");
		list.add("福州");
		list.add("无锡");
		list.add("哈尔滨");
		list.add("厦门");
		list.add("石家庄");
		return list;
	}

	@Override
	protected void onDestroy() {
		sendBroadcast(new Intent(Constant.PACKAGE_TOOL_WEATHER));
		if(weatherthread!=null){
			handler.removeCallbacks(weatherthread);
		}
		try{
			unregisterReceiver(mHomeKeyEventReceiver);
		}catch (Exception e){
			e.printStackTrace();
		}
		super.onDestroy();
	}

		public void displayWeather(Bundle bundle) {
			String str_weather_status = bundle.getString(WeatherUtil.STATUS);
			String temperature = bundle.getString(WeatherUtil.TEMPERATURE);
		if (temperature != null) {
			this.weathertext.setText(str_weather_status+"  "+temperature);
		}
			if (str_weather_status != null) {
				if (str_weather_status.contains(getResources().getString(
						R.string.str_cloudy))) {
					weatherimg.setImageResource(R.mipmap.weather_cloudy);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_sunny))) {
					weatherimg.setImageResource(R.mipmap.weather_sunny);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_shade))) {
					weatherimg.setImageResource(R.mipmap.weather_dust);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_smoke))) {
					weatherimg.setImageResource(R.mipmap.weather_fog);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_h_sand_storm))) {
					weatherimg.setImageResource(R.mipmap.weather_wind);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_sand_storm))) {
					weatherimg.setImageResource(R.mipmap.weather_wind);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_sand_blowing))) {
					weatherimg.setImageResource(R.mipmap.weather_wind);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_s_snow))) {
					weatherimg.setImageResource(R.mipmap.weather_snow);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_m_snow))) {
					weatherimg.setImageResource(R.mipmap.weather_snow);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_l_snow))) {
					weatherimg.setImageResource(R.mipmap.weather_snow);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_h_snow))) {
					weatherimg.setImageResource(R.mipmap.weather_snow);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_snow_shower))) {
					weatherimg.setImageResource(R.mipmap.weather_snow);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_s_rain))) {
					weatherimg.setImageResource(R.mipmap.weather_rain);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_m_rain))) {
					weatherimg.setImageResource(R.mipmap.weather_rain);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_l_rain))) {
					weatherimg.setImageResource(R.mipmap.weather_rain);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_h_rain))) {
					weatherimg.setImageResource(R.mipmap.weather_heavy_rain);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_hh_rain))) {
					weatherimg.setImageResource(R.mipmap.weather_heavy_rain);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_hhh_rain))) {
					weatherimg.setImageResource(R.mipmap.weather_heavy_rain);
				} else if (str_weather_status.contains(getResources().getString(
						R.string.str_shower))) {
					weatherimg.setImageResource(R.mipmap.weather_shower);
				} else if (str_weather_status.equals(getResources().getString(
						R.string.str_thunder_shower))) {
					weatherimg.setImageResource(R.mipmap.weather_thunderstorm);
				}
			}
		}
}
