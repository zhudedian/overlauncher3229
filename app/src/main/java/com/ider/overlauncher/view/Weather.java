package com.ider.overlauncher.view;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ider.overlauncher.R;
import com.ider.overlauncher.utils.NetUtil;
import com.ider.overlauncher.utils.PreferenceManager;
import com.ider.overlauncher.utils.WeatherUtil;

import java.util.Calendar;

public class Weather extends RelativeLayout {

	WeatherUtil weatherUtil;
	static final int WEATEHR_FINISHED = 1;
	String city;
	PreferenceManager pm;
	TextView localtext;
	ImageView weather_image;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case WEATEHR_FINISHED:
//				String highTmp = weatherUtil.getHighTem();
//				String lowTmp = weatherUtil.getLowTem();
//				setText(lowTmp+"~"+highTmp+"��");
				displayWeather(msg.getData());
				localtext.setText(city);
				break;

			default:
				break;
			}

		}

	};

	public Weather(Context context, AttributeSet attrs) {
		super(context, attrs);
		weatherUtil = new WeatherUtil(context);
		pm = PreferenceManager.getInstance(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.weather_ui, this);
		localtext = (TextView) findViewById(R.id.localtext);
		weather_image = (ImageView) findViewById(R.id.localweather);
//		 Typeface typeFace =Typeface.createFromAsset(context.getAssets(),"right.otf");
//		 localtext.setTypeface(typeFace);
		if(NetUtil.isNetworkAvailable(context)){
			new GetWeather().start();
		}
	}


	class GetWeather extends Thread {

		@Override
		public void run() {
			city = pm.getManuCity();
			if(city == null) {
				city = weatherUtil.getCity();
			}
			if(city != null) {
				weatherUtil.getweather(city, WeatherUtil.TODAY);
				String weatherState = weatherUtil.getWeatherStatus() == null ? weatherUtil
						.getWeatherStatus() : weatherUtil
						.getWeatherStatus2();
						Bundle bundle = new Bundle();
						bundle.putString(WeatherUtil.STATUS, weatherState);
						bundle.putString("date",WeatherUtil.getDate());
//						bundle.putString(WeatherUtil.TEMPERATURE, temperature);
						Message msg = new Message();
						msg.what = WEATEHR_FINISHED;
						msg.setData(bundle);
						handler.sendMessage(msg); // ����������Ϣ
//				handler.sendEmptyMessage(WEATEHR_FINISHED);

			}
		}
	}

	/**
	 * ��ȡ��ǰ����
	 * @return
	 */
	public String getCurrentCity() {
		return city;
	}



	/**
	 * ��������
	 */
	public void refresh(Context context) {

		new GetWeather().start();

	}
	/* package */ private void setDate(Context context, int year, int month, int day) {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		long when = c.getTimeInMillis();

		if (when / 1000 < Integer.MAX_VALUE) {
			((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
		}
		context.sendBroadcast(new Intent("com.ider.date"));
	}


	public void setTextColor(int color) {
		localtext.setTextColor(color);
	}
	public void displayWeather(Bundle bundle) {
//		String date = bundle.getString("date");
//		try {
//			int year = Integer.parseInt(date.substring(0, 4));
//			int month = Integer.parseInt(date.substring(5, 7))-1;
//			int day = Integer.parseInt(date.substring(8, 10));
//			Log.i("WEATHER", "displayWeather: year=" + year + "mon=" + month + "day=" + day);
//			setDate(getContext(), year, month, day);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
		String str_weather_status = bundle.getString(WeatherUtil.STATUS);
//		String temperature = bundle.getString(WeatherUtil.TEMPERATURE);
//		if (temperature != null) {
//			this.temperature.setText(temperature);
//			this.temperaturetext.setText(str_weather_status);
//		}
		if (str_weather_status != null) {
			if (str_weather_status.contains(getContext().getString(
					R.string.str_cloudy))) {
				weather_image.setImageResource(R.mipmap.weather_cloudy);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_sunny))) {
				weather_image.setImageResource(R.mipmap.weather_sunny);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_shade))) {
				weather_image.setImageResource(R.mipmap.weather_dust);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_smoke))) {
				weather_image.setImageResource(R.mipmap.weather_fog);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_h_sand_storm))) {
				weather_image.setImageResource(R.mipmap.weather_wind);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_sand_storm))) {
				weather_image.setImageResource(R.mipmap.weather_wind);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_sand_blowing))) {
				weather_image.setImageResource(R.mipmap.weather_wind);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_s_snow))) {
				weather_image.setImageResource(R.mipmap.weather_snow);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_m_snow))) {
				weather_image.setImageResource(R.mipmap.weather_snow);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_l_snow))) {
				weather_image.setImageResource(R.mipmap.weather_snow);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_h_snow))) {
				weather_image.setImageResource(R.mipmap.weather_snow);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_snow_shower))) {
				weather_image.setImageResource(R.mipmap.weather_snow);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_s_rain))) {
				weather_image.setImageResource(R.mipmap.weather_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_m_rain))) {
				weather_image.setImageResource(R.mipmap.weather_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_l_rain))) {
				weather_image.setImageResource(R.mipmap.weather_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_h_rain))) {
				weather_image.setImageResource(R.mipmap.weather_heavy_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_hh_rain))) {
				weather_image.setImageResource(R.mipmap.weather_heavy_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_hhh_rain))) {
				weather_image.setImageResource(R.mipmap.weather_heavy_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_shower))) {
				weather_image.setImageResource(R.mipmap.weather_shower);
			} else if (str_weather_status.equals(getContext().getString(
					R.string.str_thunder_shower))) {
				weather_image.setImageResource(R.mipmap.weather_thunderstorm);
			}
		}
	}
}
