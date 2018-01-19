//package com.ider.overlauncher.view;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.ider.overlauncher.GalleryScrollListener;
//import com.ider.overlauncher.R;
//import com.ider.overlauncher.utils.GlideRoundTransform;
//import com.ider.overlauncher.view.CompareImageView;
//
//import oversettings.ider.com.deskcust.PageEntry;
//import oversettings.ider.com.deskcust.TileEntry;
//import oversettings.ider.com.deskcust.TileEntryItem;
//
//public class MyGallery extends ViewGroup {
//	List<CompareImageView> childs;
//	boolean autoScroll = true; // 标记是否自动滚动,默认自动滚动
//	Handler handler = new Handler();
//	GalleryScrollListener listener;
//	private Context context;
//	boolean isUpdate = false;
//	private int xpiex = 10;
//
//	public void setOnGalleryScrollListener(GalleryScrollListener listener) {
//		this.listener = listener;
//	}
//
//	public MyGallery(final Context context, AttributeSet attrs) {
//		super(context, attrs);
//	//	handler.postDelayed(scroll, 5000);
//		this.context= context;
//		WindowManager wm = (WindowManager) getContext()
//				.getSystemService(Context.WINDOW_SERVICE);
//		int width = wm.getDefaultDisplay().getWidth();
//		int height = wm.getDefaultDisplay().getHeight();
//		if(height>720){
//			xpiex=15;
//		}else{
//			xpiex=10;
//		}
//		setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (childs != null) {
//					//String tag = (String) childs.get(3).getTag();
//					CompareImageView view = childs.get(4);
////					for (int i =0 ; i <childs.size();i++){
////						Log.i("zzz", "onClick:**********"+i+"**"+childs.get(i).getTitle());
////					}
//					String url = view.getUrl();
////					Log.i("zzz", "onClick: uri="+url);
////					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
////					intent.setPackage("com.yidian.calender");
////					intent.setData(Uri.parse(url));
////					Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.yidian.calendar");
////					Log.i("zz", "onClick: intent=="+intent);
////					intent.setData(Uri.parse(url));
////					context.startActivity(intent);
//					if(url!=null) {
//						openWithHomePageUri(url);
//					}
//				}
//			}
//		});
//	}
//
//	public void autoScroll(boolean autoScroll) {
//		this.autoScroll = autoScroll;
//	}
//
//	Runnable scroll = new Runnable() {
//		@Override
//		public void run() {
//			if (autoScroll)
//				scrollLeft();
//			handler.postDelayed(scroll, 5000);
//
//		}
//	};
//
//	public String getSummary(int index) {
//		return ((CompareImageView) (childs.get(index))).getSummary();
//	}
//
//	public void changedScroll(boolean isScroll){
//		if(isScroll){
//			handler.postDelayed(scroll, 5000);
//		}else{
//			handler.removeCallbacks(scroll);
//		}
//	}
//	public void cancleScroll(){
//		handler.removeCallbacks(scroll);
//		handler.postDelayed(scroll, 5000);
//	}
//	/**
//	 * 返回tag值对应的title
//	 *
//	 * @param tag
//	 * @return
//	 */
//	public String getTitleByTag(String tag) {
//		for (int i = 0; i < childs.size(); i++) {
//			if (tag.equals((String) childs.get(i).getTag())) {
//				return childs.get(i).getTitle();
//			}
//		}
//		return null;
//	}
//
//	public void setChilds(List<CompareImageView> childs) {
//		this.childs = childs;
//		addViews();
//	}
//
//	/**
//	 * 隐藏标题
//	 *
//	 * public void hideTiles() { for (int i = 0; i < childs.size(); i++) {
//	 * childs.get(i).hideTitle(); } }
//	 */
//
//	// 根据顺序add
//	public void addViews() {
//		removeAllViews();
//		addView(childs.get(0));
//		addView(childs.get(8));
//		addView(childs.get(1));
//		addView(childs.get(7));
//		addView(childs.get(2));
//		addView(childs.get(6));
//		addView(childs.get(3));
//		addView(childs.get(5));
//		addView(childs.get(4));
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//		int count = getChildCount();
//		for (int i = 0; i < count; i++) {
//			switch (i) {
//				case 0:
//					getChildAt(0).measure(
//							MeasureSpec.makeMeasureSpec(124*xpiex/10, MeasureSpec.EXACTLY),
//							MeasureSpec.makeMeasureSpec(185*xpiex/10, MeasureSpec.EXACTLY));
//					break;
//				case 1:
//					getChildAt(1).measure(
//							MeasureSpec.makeMeasureSpec(124*xpiex/10, MeasureSpec.EXACTLY),
//							MeasureSpec.makeMeasureSpec(185*xpiex/10, MeasureSpec.EXACTLY));
//					break;
//				case 2:
//					getChildAt(2).measure(
//							MeasureSpec.makeMeasureSpec(163*xpiex/10, MeasureSpec.EXACTLY),
//							MeasureSpec.makeMeasureSpec(219*xpiex/10, MeasureSpec.EXACTLY));
//					break;
//				case 3:
//					getChildAt(3).measure(
//							MeasureSpec.makeMeasureSpec(163*xpiex/10, MeasureSpec.EXACTLY),
//							MeasureSpec.makeMeasureSpec(219*xpiex/10, MeasureSpec.EXACTLY));
//					break;
//				case 4:
//					getChildAt(4).measure(
//							MeasureSpec.makeMeasureSpec(188*xpiex/10, MeasureSpec.EXACTLY),
//							MeasureSpec.makeMeasureSpec(253*xpiex/10, MeasureSpec.EXACTLY));
//					break;
//				case 5:
//					getChildAt(5).measure(
//							MeasureSpec.makeMeasureSpec(188*xpiex/10, MeasureSpec.EXACTLY),
//							MeasureSpec.makeMeasureSpec(253*xpiex/10, MeasureSpec.EXACTLY));
//					break;
//				case 6:
//					getChildAt(6).measure(
//							MeasureSpec.makeMeasureSpec(213*xpiex/10, MeasureSpec.EXACTLY),
//							MeasureSpec.makeMeasureSpec(286*xpiex/10, MeasureSpec.EXACTLY));
//					break;
//				case 7:
//					getChildAt(7).measure(
//							MeasureSpec.makeMeasureSpec(213*xpiex/10, MeasureSpec.EXACTLY),
//							MeasureSpec.makeMeasureSpec(286*xpiex/10, MeasureSpec.EXACTLY));
//					break;
//				case 8:
//					getChildAt(8).measure(
//							MeasureSpec.makeMeasureSpec(249*xpiex/10, MeasureSpec.EXACTLY),
//							MeasureSpec.makeMeasureSpec(353*xpiex/10, MeasureSpec.EXACTLY));
//					break;
//				default:
//					break;
//			}
//		}
//
//	}
//
//	/**
//	 * 遍历所有的childView，根据childView的宽和高以及margin，然后分别将各个位置的childView依次设置到相应的位置
//	 */
//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//
//		int childWidth = 0;
//		int childHeight = 0;
//		/**
//		 * 遍历所有childView，根据其宽高进行布局
//		 */
//		if (childs != null) {
//			for (int i = 0; i < childs.size(); i++) {
//				// 在调用getMeasuredWidth之前一定要先调用这一句，否则得到的宽高都是0
//				// childs.get(i).measure(l - r, b - t);
//				int cl = 0, ct = 0, cr = 0, cb = 0;
//				switch (i) {
//					case 0: // 第一张
//						childWidth = 124*xpiex/10;
//						childHeight = 185*xpiex/10;
//						cl = 4*xpiex/10;
//						ct = 87*xpiex/10;
//						break;
//					case 1: // 第二张
//						childWidth = 163*xpiex/10;
//						childHeight = 219*xpiex/10;
//						cl = 95*xpiex/10;
//						ct = 67*xpiex/10;
//						break;
//					case 2: // 第三张
//						childWidth = 188*xpiex/10;
//						childHeight = 253*xpiex/10;
//						cl = 197*xpiex/10;
//						ct = 50*xpiex/10;
//						break;
//					case 3: // 第四张
//						childWidth = 213*xpiex/10;
//						childHeight = 286*xpiex/10;
//						cl = 299*xpiex/10;
//						ct = 29*xpiex/10;
//						break;
//					case 4: // 中间一张
//						childWidth = 249*xpiex/10;
//						childHeight = 353*xpiex/10;
//						cl = 427*xpiex/10;
//						ct = 0;
//						break;
//					case 5: // 第五张
//						childWidth = 213*xpiex/10;
//						childHeight = 286*xpiex/10;
//						cl = 590*xpiex/10;
//						ct = 29*xpiex/10;
//						break;
//
//					case 6: // 倒数第三张
//						childWidth = 188*xpiex/10;
//						childHeight = 253*xpiex/10;
//						cl = 709*xpiex/10;
//						ct = 50*xpiex/10;
//						break;
//
//					case 7:
//						childWidth = 163*xpiex/10;
//						childHeight = 219*xpiex/10;
//						cl = 842*xpiex/10;
//						ct = 67*xpiex/10;
//						break;
//					case 8:
//						childWidth = 124*xpiex/10;
//						childHeight = 185*xpiex/10;
//						cl = 971*xpiex/10;
//						ct = 87*xpiex/10;
//						break;
//					default:
//						break;
//				}
//				cr = cl + childWidth;
//				cb = ct + childHeight;
//
//				childs.get(i).layout(cl, ct, cr, cb);
//			}
//		}
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//			scrollLeft();
//			cancleScroll();
//			return true;
//		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//			scrollRight();
//			cancleScroll();
//			return true;
//		}
//		cancleScroll();
//		return super.onKeyDown(keyCode, event);
//	}
//
//	/**
//	 * 向左滚动
//	 */
//	public void scrollLeft() {
//		for (int i = 0; i < childs.size(); i++) {
//			int orderNum = childs.get(i).getOrderNum();
//			if (orderNum != 0) {
//				orderNum = orderNum - 1;
//			} else {
//				orderNum = 8;
//			}
//			childs.get(i).setOrderNum(orderNum);
//		}
//		Collections.sort(childs, new ImageComparator());
//		addViews();
//		if(listener!=null) {
//			listener.onScroll();
//		}
//	}
//
//	/**
//	 * 向右滚动
//	 */
//	public void scrollRight() {
//		for (int i = 0; i < childs.size(); i++) {
//			int orderNum = childs.get(i).getOrderNum();
//			if (orderNum != 8) {
//				orderNum = orderNum + 1;
//			} else {
//				orderNum = 0;
//			}
//			childs.get(i).setOrderNum(orderNum);
//		}
//		Collections.sort(childs, new ImageComparator());
//		addViews();
//
//		listener.onScroll();
//	}
//
//	public CompareImageView getCurrentView(){
//
//		return childs.get(4);
//	}
//	/**
//	 * 更新summary
//	 *
//	 * @param tag
//	 *            要更新的image的tag值
//	 */
//	public void updateSummary(String tag, String summary) {
//		CompareImageView view;
//		if (tag == null || summary == null) {
//			return;
//		}
//		view = (CompareImageView) findViewWithTag(tag);
//		if(view == null) {
//			return;
//		}
//
//		view.setSummary(summary);
//	}
//
//
//	public void updateTitle(String tag, String title) {
//		if(tag == null || title == null) {
//			return;
//		}
//		CompareImageView view = (CompareImageView) findViewWithTag(tag);
//		if(view == null) {
//			return;
//		}
//		view.setTitle(title);
//	}
//
//	/**
//	 * 更新图片
//	 *
//	 * @param tag
//	 *            要更新image的tag值
//	 * @param url
//	 */
//	public void updateBlockBitmap(final String tag, String url) {
//		CompareImageView view = (CompareImageView) findViewWithTag(tag);
//		Glide.with(getContext()).load(url).into(view.image);
//	}
//
//	public void updateGallery(PageEntry page){
//		TileEntry[] tileEntrys = page.tileEntrys;
//		if(!isUpdate) {
//			for (int i = 0; i < tileEntrys.length; i++) {
//				CompareImageView view = (CompareImageView) getChildAt(i);
//				if (view != null) {
//					Log.i("zzz", "updateGallery: i=="+i+"******"+tileEntrys[i].items[0].pic_470x630+tileEntrys[i].items[0].pic_496x722+"***"+tileEntrys[i].items[0].title);
//					if (tileEntrys[i].items[0].pic_470x630 != null) {
//						Glide.with(getContext()).load(tileEntrys[i].items[0].pic_470x630).transform(new GlideRoundTransform(context)).crossFade().into(view.image);
//					} else {
//						Glide.with(getContext()).load(tileEntrys[i].items[0].pic_496x722).transform(new GlideRoundTransform(context)).crossFade().into(view.image);
//					}
//					view.setTitle(tileEntrys[i].items[0].title);
//					view.setSummary(tileEntrys[i].items[0].episode_updated);
//					view.setUrl(tileEntrys[i].items[0].uri);
//				}
//			}
//			isUpdate = true;
//		}
//	}
//
//	class ImageComparator implements Comparator<Object> {
//
//		@Override
//		public int compare(Object lhs, Object rhs) {
//			CompareImageView image1 = (CompareImageView) lhs;
//			CompareImageView image2 = (CompareImageView) rhs;
//			int flag = image1.getOrderNum().compareTo(image2.getOrderNum());
//
//			return flag;
//		}
//	}
//
//	public List<String> getChildTags() {
//		List<String> tags = new ArrayList<String>();
//		for (int i = 0; i < childs.size(); i++) {
//			tags.add((String) childs.get(i).getTag());
//		}
//		return tags;
//	}
//
//
//
//	private void handleIntent(Intent intent) {
//		// 隐式调用的方式startActivity
//		//intent.setAction("com.tencent.qqlivetv.open");
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setPackage("com.yidian.calendar");//设置视频包名，要先确认包名
//		PackageManager packageManager = context.getPackageManager();
//		List<ResolveInfo> activities = packageManager
//				.queryIntentActivities(intent, 0);
//		boolean isIntentSafe = activities.size() > 0;
//		if (isIntentSafe) {
//			context.startActivity(intent);
//		} else {
//			Toast.makeText(context, "未安装腾讯视频 ， 无法跳转", Toast.LENGTH_SHORT).show();
//		}
//	}
//
//		private void openWithHomePageUri(String url) {
//			Intent intent = new Intent();
//			intent.setData(Uri.parse(url));
//			handleIntent(intent);
//		}
//}
