package com.ider.overlauncher.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;


import com.ider.overlauncher.R;

import java.util.Calendar;

@SuppressWarnings("deprecation")
public class MyDigitalClock extends android.widget.DigitalClock {
	Calendar mCalendar;
	// private final static String mFormat =
	// "EEEE,MMMM-dd-yyyy hh:mm aa";//h:mm:ss aa
	private String mFormat;
	private FormatChangeObserver mFormatChangeObserver;
	private Runnable mTicker;
	private Handler mHandler;
	private boolean mTickerStopped = false;
	private String m24 = "k:mm";
	private String m12 = "h:mm aa";
	private Context context;

	public MyDigitalClock(Context context) {
		super(context);
		initClock(context);
		this.context = context;
	}
	public MyDigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.MyDigitalClock);
        this.mFormat=(String)typedArray.getText(R.styleable.MyDigitalClock_format);
//        Typeface typeFace =Typeface.createFromAsset(context.getAssets(),"right.otf");
//        setTypeface(typeFace);
//        typedArray.recycle();
		this.context = context;
		initClock(context);
	}

	public  void initClock(Context context) {
		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}
		mFormatChangeObserver = new FormatChangeObserver();
		getContext().getContentResolver().registerContentObserver(
				Settings.System.CONTENT_URI, true, mFormatChangeObserver);
	}
	/**
	 */
	@Override
	protected void onAttachedToWindow() {
		mTickerStopped = false;
		super.onAttachedToWindow();

		mHandler = new Handler();

		mTicker = new Runnable() {
			@Override
			public void run() {
				if (mTickerStopped) {
					return;
				}
				mCalendar.setTimeInMillis(System.currentTimeMillis());
				setText(DateFormat.format(m24, mCalendar));
				invalidate();
				long now = SystemClock.uptimeMillis();
				long next = now + (1000 - now % 1000);
				mHandler.postAtTime(mTicker, next);
			}
		};
		mTicker.run();
}

	private class FormatChangeObserver extends ContentObserver {

		public FormatChangeObserver() {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange) {
			//initClock(context);
		}
	}
	
	// ����ϵͳʱ���ʽ������ʽ
	public String setFormat() {
		if(get24HourMode()) {
			mFormat = m24;
		} else {
			mFormat = m12;
		}
		return  mFormat;
	}
	
	/**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return DateFormat.is24HourFormat(getContext());
    }
}

