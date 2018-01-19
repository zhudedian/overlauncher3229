package com.ider.overlauncher.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WeatherUtil {
	private static final String TAG = "WeatherUtil";
	public static final String TODAY = "0";
	public static final String STATUS = "status";
	public static final String TEMPERATURE = "temperature";

	static String weather = null;
	static String weather2 = null;
	static String high = null;
	static String low = null;
	static String wind = null;
	static String date = null;
	public static final int WEATHER_STATE_FINISHED = 1000;   // ��ȡ�����ɹ�
	public static final int WEATHER_STATE_ERRO = 1001;  // ʧ��
	public static final int WEATHER_STATE_UNSTARTED = 1002;  // δ��ʼ��ȡ����
	public static final int WEATHER_STATE_CHECKING = 1003;  // ���ڻ�ȡ����
	
	public int weatherState = WEATHER_STATE_UNSTARTED;

	public WeatherUtil(Context context) {
	}

	/**
	 * ��ȡ����
	 * 
	 * @param city
	 *            ������
	 * @param day
	 *
	 */
	public void getweather(String city, String day) {
		setWeatherState(WEATHER_STATE_CHECKING);
		URL url;

		try {

			DocumentBuilderFactory domfac = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dombuilder = domfac.newDocumentBuilder();
			Document doc;
			Element root;
			NodeList books;

			url = new URL("http://php.weather.sina.com.cn/xml.php?city="
					+ URLEncoder.encode(city, "gb2312")
					+ "&password=DJOYnieT8234jlsK&day=" + day);
			Log.d(TAG, url.toString());
			doc = dombuilder.parse(url.openStream());

			root = doc.getDocumentElement();
			books = root.getChildNodes();
			if (books == null || books.item(1) == null) {
				weather = null;
				weather2 = null;
				high = null;
				low = null;
				wind = null;

				return;
			}

			for (Node node = books.item(1).getFirstChild(); node != null; node = node
					.getNextSibling()) {

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					if (node.getNodeName().equals("status1"))
						weather = node.getTextContent();
					else if (node.getNodeName().equals("status2"))
						weather2 = node.getTextContent();
					else if (node.getNodeName().equals("udatetime"))
						date = node.getTextContent();
					else if (node.getNodeName().equals("temperature1"))
						high = node.getTextContent();
					else if (node.getNodeName().equals("temperature2"))
						low = node.getTextContent();
					else if (node.getNodeName().equals("direction1"))
						wind = node.getTextContent();
				}
				
			}
			
			setWeatherState(WEATHER_STATE_FINISHED);
			// Log.d("Weather", " "+weather+"/"+weather2 + "  "+low+ "/"+high +
			// "  " + wind);

		} catch (Exception e) {
			setWeatherState(WEATHER_STATE_ERRO);
			Log.i(TAG, "������ȡʧ�ܣ���������");
		}
	}

	public String getCity() {
		String jsonStr = NetUtil
				.getStringFromUrl("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json");
		String city = null;
		if (jsonStr != null) {
			try {
				JSONObject jo = new JSONObject(jsonStr);
				city = jo.getString("city");

			} catch (JSONException e) {
				Log.i(TAG, "���л�ȡʧ�ܣ���������");
			}
		}

		return city;
	}

	/**
	 * ����״̬
	 * @return
	 */
	public String getWeatherStatus() {

		return weather;
	}

	public String getWeatherStatus2() {

		return weather2;
	}

	public static String getDate() {
		return date;
	}

	/**
	 * ����
	 * @return
	 */
	public boolean isWeatherWillChange() {
		if (weather == null) {
			return false;
		}
		return !weather.equals(weather2);
	}

	public String getLowTem() {
		return low;
	}

	public String getHighTem() {
		return high;
	}

	public String getWindDirection() {

		return wind;
	}
	
	public int getWeatherState() {
		return this.weatherState;
	}
	
	public void setWeatherState(int state) {
		this.weatherState = state;
	}
}
