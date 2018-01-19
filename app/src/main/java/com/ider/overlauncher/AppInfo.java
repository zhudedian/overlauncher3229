package com.ider.overlauncher;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ider.overlauncher.utils.DownloadUtil;
import com.ider.overlauncher.utils.InstallUtil;
import com.ider.overlauncher.utils.PreferenceManager;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.ider.overlauncher.utils.InstallUtil.installSlient;


public class AppInfo extends Activity {
	String TAG = "AppInfo";
	ImageView icon;
	TextView label, description;
	Button continueDownload;
	Button reDownload;
	String url;
	String pkgName;
	String tag;
	int verCode;
	String md5;
	boolean downloading;
	DownloadUtil downloadUtil;
	private boolean isCustom=false;
	private boolean isForceDown = false;
	private PreferenceManager pmanager;
	private boolean isDownloadComplete;

	private static final int DOWNLOAD_SUCCESS = 1;
	private static final int DOWNLOAD_FAILED = 2;
	private static final int DOWNLOADING_WAIT = 3;
	private static final int CHECK_ERROR = 4;
	int ErrdownNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_info);
		continueDownload = (Button) findViewById(R.id.download);
		reDownload = (Button) findViewById(R.id.reDownload);
		icon = (ImageView) findViewById(R.id.image);
		label = (TextView) findViewById(R.id.label);
		description = (TextView) findViewById(R.id.description);
		pmanager =  PreferenceManager.getInstance(this);
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		Glide.with(AppContext.getAppContext()).load(data.getString("icon")).error(R.mipmap.fail).into(icon);
		label.setText(data.getString("label"));
		description.setText("简介：" + data.getString("description"));
		this.url = data.getString("apk");
		this.tag = data.getString("tag");
		System.out.println("labelaaa" + data.getString("label"));
		this.pkgName = data.getString("pkg");
		this.verCode = data.getInt("verCode");
		this.md5 = data.getString("md5");
		isCustom = data.getBoolean("isCustom");
		isForceDown= data.getBoolean("isForceDown",false);
		isDownloadComplete = false;
		File apk = new File(Constant.APK_CACHE_PATH + "/"
				+ DownloadUtil.getInstance().getNameFromUrl(url));
		File cacheApk = new File(Constant.APK_CACHE_PATH + "/"
				+ DownloadUtil.getInstance().getCacheNameFromUrl(url));
		if (!apk.exists() && !cacheApk.exists()) {
			showDownloadButton();
		} else {
			continueDownload.setText("继续下载");
		}
		Log.i(TAG, "label = " + label + "/ url = " + url);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addDataScheme("package");
		registerReceiver(packageReceiver, filter);
		reDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				reDownload();
			}
		});
		continueDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				download();
			}
		});
		if (isForceDown){
			reDownload();
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(packageReceiver);
		super.onDestroy();

	}




	public void download() {

		showDownloadButton();

		downloadUtil = DownloadUtil.getInstance();

		if (!downloading) {
			Log.i(TAG, "downloading = " + downloadUtil.downloading());
			if (downloadUtil.downloading()
					&& !downloadUtil.currentPackage().equals(pkgName)) {
				Toast.makeText(AppInfo.this, R.string.downloading_wait,
						Toast.LENGTH_SHORT).show();
			} else {
				downloading = true;
				continueDownload.setText("正在下载…");
				new DownloadThread(url).start();
				handler.post(percent);
			}
		}
	}

	public void reDownload() {
		showDownloadButton();
		File file = new File(Constant.APK_CACHE_PATH + "/"
				+ downloadUtil.getNameFromUrl(url));
		File cacheFile = new File(Constant.APK_CACHE_PATH + "/"
				+ downloadUtil.getCacheNameFromUrl(url));
		file.delete();
		cacheFile.delete();
		if (!downloading) {
			Log.i(TAG, "downloading = " + downloadUtil.downloading());
			if (downloadUtil.downloading()
					&& !downloadUtil.currentPackage().equals(pkgName)) {
				Toast.makeText(AppInfo.this, R.string.downloading_wait,
						Toast.LENGTH_SHORT).show();
			} else {
				downloading = true;
				continueDownload.setText("正在下载…");
				new DownloadThread(url).start();
				handler.post(percent);
			}
		}
	}

	public void showDownloadButton() {
		reDownload.setVisibility(View.GONE);
		LayoutParams lp = (LayoutParams) continueDownload.getLayoutParams();
		lp.width = 300;
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		continueDownload.setLayoutParams(lp);
		downloadUtil = DownloadUtil.getInstance();

	}

	Runnable percent = new Runnable() {
		@Override
		public void run() {
			String percent = downloadUtil.getPercent();
			if (percent == null) {
				percent = "";
			}
			continueDownload.setText(String.format(
					getResources().getString(R.string.Downloading),
					downloadUtil.getPercent()));
			handler.postDelayed(this, 1000);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_SUCCESS:
				handler.removeCallbacks(percent);
				isDownloadComplete = true;
				continueDownload.setText(R.string.Installing);
				final String path = (String) msg.obj;
				new Thread(){
					@Override
					public void run(){
						InstallUtil.installSlient(path,null,0);
					}
				}.start();
				break;
			case DOWNLOAD_FAILED:
				downloading = false;
				handler.removeCallbacks(percent);
				continueDownload.setText(R.string.DownloadFailed);
				break;
			case DOWNLOADING_WAIT:
				Toast.makeText(AppInfo.this, R.string.downloading_wait,
						Toast.LENGTH_SHORT).show();
				break;
			case CHECK_ERROR:
				ErrdownNum++;
				downloading = false;
				handler.removeCallbacks(percent);
				if (ErrdownNum >= 3) {
					String path2 = (String) msg.obj;
					File file = new File(path2);
					file.delete();
					findViewById(R.id.errdown).setVisibility(View.GONE);
					continueDownload.setText(R.string.errdownloadnumlimite);
					continueDownload.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							AppInfo.this.finish();
						}
					});
				} else {

					reDownload();
					findViewById(R.id.errdown).setVisibility(View.VISIBLE);

				}
				break;
			default:
				break;
			}
		}
	};

	class DownloadThread extends Thread {
		String url;

		public DownloadThread(String apkUrl) {
			this.url = apkUrl;
		}

		@Override
		public void run() {
			String path = downloadUtil.download2Disk(url, pkgName);
			if (path != null) {
				if (path.endsWith("cfg")) {
					path = path.substring(0, path.lastIndexOf("."));
				}
				File file = new File(path);
				if (checkPkg(AppInfo.this, path, pkgName)
						&& FileMd5(AppInfo.this, file, md5)) {
					Message message = new Message();
					message.obj = path;
					message.what = DOWNLOAD_SUCCESS;
					handler.sendMessage(message);
					continueDownload.setClickable(false);
					// 安装方式
//					Intent intent = new Intent(Intent.ACTION_VIEW);
//					intent.setDataAndType(Uri.fromFile(file),
//							"application/vnd.android.package-archive");
//					startActivity(intent);

				} else {
					Message message = new Message();
					message.obj = path;
					message.what = CHECK_ERROR;
					handler.sendMessage(message);
				}

			} else {
				handler.sendEmptyMessage(DOWNLOAD_FAILED);
			}
		}
	}

	BroadcastReceiver packageReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "package installed");
			String data = intent.getDataString();
			final String packgename = data.substring(data.indexOf(":") + 1,
					data.length());
			if (packgename.equals(AppInfo.this.pkgName)) {
				downloading = false;
				Intent resultIntent = new Intent();
				if(isCustom){
					resultIntent.putExtra("tag", tag);
				}
				resultIntent.putExtra("package", packgename);
				AppInfo.this.setResult(RESULT_OK, resultIntent);

				AppInfo.this.finish();
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (downloading) {
				if (isDownloadComplete){
					Toast.makeText(AppInfo.this,"正在安装！",Toast.LENGTH_LONG).show();
				}else {
					exitDialog();
				}

				return true;
			}
		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void exitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.Downloading_exit_warning);
		builder.setNegativeButton(R.string.Cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.cancel();
					}
				});
		builder.setPositiveButton(R.string.Exit,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (isDownloadComplete){
							Toast.makeText(AppInfo.this,"正在安装！",Toast.LENGTH_LONG).show();
							return;
						}
						downloadUtil.stopDownload();
						AppInfo.this.finish();
					}
				});
		builder.create().show();
	}

	public boolean checkPkg(Context context, String path, String packageName) {
		PackageInfo info=null;
		if (packageName == null || packageName.equals("")) {
			return false;
		}
		Log.i(TAG, path + "--------" + packageName + "-------");
		PackageManager pm = context.getPackageManager();
		try {
			 info= pm.getPackageArchiveInfo(path,
					PackageManager.GET_ACTIVITIES);
			 System.out.println("infopackname" + info.packageName);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
		
		return packageName.equals(info.packageName);
	}

	@SuppressWarnings("resource")
	private boolean FileMd5(AppInfo appInfo, File file, String md5) {
		// TODO Auto-generated method stub
		if (md5 == null || md5.equals("")) {
			return false;
		}
		if (!file.isFile()) {
			return false;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		String intss = bigInt.toString(16);
		System.out.println("--------------intss" + intss + "-------md5====" + md5);
		return intss.contains(md5);
				//md5.equals(intss);
	}

}
