package com.ider.overlauncher.speedTest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ider.overlauncher.R;
import com.ider.overlauncher.utils.ActivityAnimationTool;
import com.ider.overlauncher.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class SpeedActivity extends Activity {
	private String url = "http://szider.net/upload/files/201707170859536011.apk";

	byte[] imageData = null;
	Button b;
	NetWorkSpeedInfo netWorkSpeedInfo = null;
	private final int UPDATE_SPEED = 1;// 进行中
	private final int UPDATE_DNOE = 0;// 完成下载
	private ImageView imageView;
	private long begin = 0;
	private Button startButton;
	private TextView connectionType, nowSpeed, avageSpeed;
	long tem = 0;
	long falg = 0;
	long numberTotal = 0;
	List<Long> list = new ArrayList<Long>();

	Thread thread1,thread2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.equipment);
		imageView = (ImageView) findViewById(R.id.iv_needle);
		startButton = (Button) findViewById(R.id.start_button);
		connectionType = (TextView) findViewById(R.id.connection_type);
		nowSpeed = (TextView) findViewById(R.id.now_speed);
		avageSpeed = (TextView) findViewById(R.id.average_speed);
		netWorkSpeedInfo = new NetWorkSpeedInfo();
		ActivityAnimationTool.prepareAnimation(this);
		ActivityAnimationTool.animate(this, 1000);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				list.clear();
				tem = 0;
				falg = 0;
				numberTotal = 0;
				if(NetUtil.isNetworkAvailable(SpeedActivity.this)) {
					startButton.setClickable(false);
					thread1 = new Thread() {
						@Override
						public void run() {
							imageData = ReadFile.getFileFromUrl(url, netWorkSpeedInfo);
						}
					};
					thread1.start();

					thread2 = new Thread() {
						@Override
						public void run() {
							while (netWorkSpeedInfo.hadFinishedBytes < netWorkSpeedInfo.totalBytes) {
								try {
									sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								handler.sendEmptyMessage(UPDATE_SPEED);
							}
							if (netWorkSpeedInfo.hadFinishedBytes >= netWorkSpeedInfo.totalBytes) {
								handler.sendEmptyMessage(UPDATE_DNOE);
								netWorkSpeedInfo.hadFinishedBytes = 0;
							}

						}
					};
					thread2.start();
				}
			}
		});
		if(NetUtil.isNetworkAvailable(this)) {
			if (NetUtil.isEthernetConnect(this)) {
				connectionType.setText(getResources().getString(R.string.ethconn));
			} else {
				connectionType.setText(getResources().getString(R.string.wificonn));
			}
		}else{
			connectionType.setText(getResources().getString(R.string.noconn));
		}

	}

	protected void startAnimation(double d) {
		AnimationSet animationSet = new AnimationSet(true);
		/**
		 * 前两个参数定义旋转的起始和结束的度数，后两个参数定义圆心的位置
		 */
		// Random random = new Random();
		int end = getDuShu(d);
		RotateAnimation rotateAnimation = new RotateAnimation(begin, end, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
		rotateAnimation.setDuration(1000);
		animationSet.addAnimation(rotateAnimation);
		imageView.startAnimation(animationSet);
		begin = end;
	}

	public int getDuShu(double number) {
		double a = 0;
		if (number >= 0 && number <= 512) {
			a = number / 128 * 15;
		} else if (number > 521 && number <= 1024) {
			a = number / 256 * 15 + 30;
		} else if (number > 1024 && number <= 10 * 1024) {
			a = number / 512 * 5 + 80;
		} else {
			a = 180;
		}
		return (int) a;
	}

	private Handler handler = new Handler() {
		long tem = 0;
		long falg = 0;
		long numberTotal = 0;
		List<Long> list = new ArrayList<Long>();

		@Override
		public void handleMessage(Message msg) {
			int value = msg.what;
			switch (value) {
			case UPDATE_SPEED:
				tem = netWorkSpeedInfo.speed / 1024;
				list.add(tem);
				for (Long numberLong : list) {
					numberTotal += numberLong;
				}
				falg = numberTotal / list.size();
				numberTotal = 0;
				nowSpeed.setText(tem + "kb/s");
				avageSpeed.setText(falg + "kb/s");
				startButton.setText(getString(R.string.testing));
				startAnimation(Double.parseDouble(tem+""));
				break;
			case UPDATE_DNOE:
				//speed.setText("完成");
				Log.i("zzz", "handleMessage: UPDATE_DNOE");
				list.clear();
				Toast.makeText(SpeedActivity.this,String.format(getString(R.string.netspeedTip),falg),Toast.LENGTH_SHORT).show();
				tem = 0;
				falg = 0;
				numberTotal = 0;
				startButton.setText(getString(R.string.test));
				startButton.setClickable(true);
				if(thread1!=null) {
					handler.removeCallbacks(thread1);
				}
				if(thread2!=null) {
					handler.removeCallbacks(thread2);
				}
				AnimationSet animationSet = new AnimationSet(true);
				RotateAnimation rotateAnimation = new RotateAnimation(begin, 0, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
				rotateAnimation.setDuration(500);
				animationSet.addAnimation(rotateAnimation);
				imageView.startAnimation(animationSet);
				begin=0;
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		if(thread1!=null) {
			handler.removeCallbacks(thread1);
		}
		if(thread2!=null) {
			handler.removeCallbacks(thread2);
		}
		super.onDestroy();
	}
}
