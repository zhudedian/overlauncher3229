package com.ider.overlauncher.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class TvUtil {
	private final static String TAG = "TvUtil";

	public final static int SCREEN_1280 = 1280, SCREEN_1920 = 1920,
			SCREEN_2560 = 2560, SCREEN_3840 = 3840;

	public static int startId = 1000001;
	public static int freeId = 1000001;

	public static int buildId() {
		freeId++;
		return freeId;
	}

	private Map<String, SoftReference<Bitmap>> imageCache;
	private String cachedDir;

	public TvUtil(Context context, Map<String, SoftReference<Bitmap>> imageCache) {
		this.imageCache = imageCache;
		this.cachedDir = "";
	}



	/**
	 * MD5 ����
	 */
	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
			// System.out.println("result: " + buf.toString());// 32λ�ļ���
			// System.out.println("result: " + buf.toString().substring(8,
			// 24));// 16λ�ļ���
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}

	public static String readFile(String filePath) {
		String str = "";
		FileInputStream inStream = null;
		ByteArrayOutputStream stream = null;
		try {
			File readFile = new File(filePath);
			if (!readFile.exists()) {
				return null;
			}
			inStream = new FileInputStream(readFile);
			stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			str = stream.toString();
			stream.close();
			inStream.close();
			return str;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
